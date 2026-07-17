<?php

namespace App\Http\Controllers;

use App\Models\Message;
use App\Models\MessageExclusion;
use App\Events\MessageSent;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use \App\Models\GroupMember;

class MessageController extends Controller
{
    /**
     * Fetch all messages belonging to a specific group (General or Topic stream).
     * Protected by group.member middleware.
     */
    public function getMessages(Request $request, $group)
    {
        // validate optional topic_id
        $validated = $request->validate([
            'topic_id' => 'nullable|integer',
        ]);

        $authenticatedUser = $request->user();
        $userId = $authenticatedUser->user_id ?? $authenticatedUser->id;

        // Extract raw group ID integer safely if Route Model Binding or plain ID is used
        $groupId = is_object($group) ? ($group->group_id ?? $group->id) : (int)$group;

        //Build the query focused on the verified group boundary
        $query = Message::where('group_id', $groupId);

        //Branch logic based on whether the user clicked a topic or general chat
        if (!empty($validated['topic_id'])) {
            $query->where('topic_id', (int) $validated['topic_id']);
        } else {
            // If no topic_id is passed, it's general group chat
            $query->whereNull('topic_id');
        }

        //privacy gates
        $messages = $query->with('sender:id,name')
            ->where(function ($query) use ($userId) {
                $query->where('is_restricted', false)
                      ->orWhere('sender_id', $userId)
                      ->orWhere(function ($subQuery) use ($userId) {
                          $subQuery->where('is_restricted', true)
                                   ->whereDoesntHave('exclusions', function ($exclusionCheck) use ($userId) {
                                       $exclusionCheck->where('ex_user_id', $userId);
                                   });
                      });
            })
            ->orderBy('posted_at', 'asc')
            ->get();

        return response()->json([
            'status' => 'Messages retrieved successfully',
            'count'  => $messages->count(),
            'data'   => $messages
        ], 200);
    }

    /**
     * Store a new message and trigger real-time broadcast.
     * Protected by group.member middleware.
     */
    public function store(Request $request, $group)
    {
        // Validate the incoming request text and topic pointers
        $validated = $request->validate([
            'topic_id'           => 'nullable|integer|exists:topics,topic_id',
            'msg_txt'            => 'required|string',
            'is_restricted'      => 'required|boolean',
            'excluded_user_ids'  => 'nullable|array',
            'excluded_user_ids.*'=> 'integer|exists:users,id'
        ]);

        $authenticatedUser = $request->user();
        $userId = $authenticatedUser->user_id ?? $authenticatedUser->id;

        // Extract raw group ID integer safely from route middleware boundary
        $groupId = is_object($group) ? ($group->group_id ?? $group->id) : (int)$group;

        //CHECK IF THE USER IS CURRENTLY BLACKLISTED ===
         $membership = GroupMember::where('user_id', $userId)
                    ->where('group_id', $groupId)
                    ->first();

         if ($membership && $membership->blacklisted_until && Carbon::parse($membership->blacklisted_until)->isFuture()) {
           return response()->json([
            'status'  => 'Error',
             'message' => 'You are temporarily blacklisted from this group due to inactivity and cannot send messages until ' . $membership->blacklisted_until . '.'
                  ], 403);
              }

        // Save the message directly inside the validated group boundary
        $message = Message::create([
            'group_id'      => $groupId,
            'topic_id'      => $validated['topic_id'] ?? null,
            'sender_id'     => $userId,
            'msg_txt'       => $validated['msg_txt'],
            'is_synced'     => true,
            'is_restricted' => $validated['is_restricted'],
            'posted_at'     => now(),
        ]);

        //LAST ACTIVITY UPDATE
        \App\Models\GroupMember::where('user_id', $userId)
                    ->where('group_id', $groupId)
                    ->update([
                        'last_activity' => now(),
                    ]);

        // Process exclusions if message is set to restricted
        if ($validated['is_restricted'] && $request->has('excluded_user_ids')) {
            foreach ($request->excluded_user_ids as $excludedId) {
                MessageExclusion::create([
                    'msg_id'     => $message->msg_id,
                    'ex_user_id' => $excludedId
                ]);
            }
        }

        // Load sender relation so the WebSocket broadcaster has the name attached
        $message->load('sender:id,name');

        // Fire the Event, triggers Laravel Reverb to broadcast it in real-time
        event(new MessageSent($message));

        return response()->json([
            'status'  => 'Success',
            'message' => 'Message stored and broadcasted successfully!',
            'data'    => $message
        ], 201);
    }

    /**
     * Offline sync engine optimizing desktop storage cache streams.
     * (Kept self-contained since Java app handles sync boundary verification loops).
     */
    public function sync(Request $request)
    {
        $validated = $request->validate([
            'group_id' => 'required|integer',
            'last_sync_time' => 'required|date_format:Y-m-d H:i:s',
        ]);

        $authenticatedUser = $request->user();
        $targetGroupId = (int) $validated['group_id'];

        $userGroupIds = $authenticatedUser->groups()->pluck('groups.group_id')->toArray();
        if (!in_array($targetGroupId, $userGroupIds)) {
            return response()->json([
                'status'  => 'Error',
                'message' => 'Unauthorized. You do not belong to this academic group.'
            ], 403);
        }

        $currentUserId = $authenticatedUser->id;

        $messages = Message::where('group_id', $targetGroupId)
            ->where('posted_at', '>', $validated['last_sync_time'])
            ->whereDoesntHave('exclusions', function ($query) use ($currentUserId) {
                $query->where('ex_user_id', $currentUserId);
            })
            ->with(['sender:id,name,email', 'topic:topic_id,title'])
            ->orderBy('posted_at', 'asc')
            ->get();

        return response()->json([
            'status'   => 'Success',
            'message'  => 'Sync completed successfully.',
            'count'    => $messages->count(),
            'messages' => $messages
        ], 200);
    }
}
