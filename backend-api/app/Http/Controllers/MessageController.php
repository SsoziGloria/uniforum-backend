<?php

namespace App\Http\Controllers;

use App\Models\Message;
use App\Events\MessageSent;
use Illuminate\Http\Request;

use App\Services\MessageService;

class MessageController extends Controller
{
    protected MessageService $messageService;

    public function __construct(MessageService $messageService)
    {
        $this->messageService = $messageService;
    }

    public function getMessages(Request $request, $group)
    {
        $validated = $request->validate([
            'topic_id' => 'nullable|integer',
        ]);

        $authenticatedUser = $request->user();
        $userId = $authenticatedUser->user_id ?? $authenticatedUser->id;

        $groupId = is_object($group) ? ($group->group_id ?? $group->id) : (int) $group;

        $messages = $this->messageService->getGroupMessages(
            $groupId,
            $userId,
            $validated['topic_id'] ?? null
        );

        return response()->json([
            'status' => 'Messages retrieved successfully',
            'count'  => $messages->count(),
            'data'   => $messages
        ], 200);
    }

    public function store(Request $request, $group)
    {
        $validated = $request->validate([
            'topic_id'           => 'nullable|integer|exists:topics,topic_id',
            'msg_txt'            => 'required|string',
            'is_restricted'      => 'nullable|boolean',
            'excluded_user_ids'  => 'nullable|array',
            'excluded_user_ids.*'=> 'integer|exists:users,id'
        ]);

        $authenticatedUser = $request->user();
        $userId = $authenticatedUser->user_id ?? $authenticatedUser->id;

        $groupId = is_object($group) ? ($group->group_id ?? $group->id) : (int) $group;

        $result = $this->messageService->sendMessage(
            $groupId,
            $userId,
            $validated
        );

        if ($result['error']) {
            return response()->json([
                'status'  => 'Error',
                'message' => $result['message']
            ], 403);
        }

        $message = $result['message'];
        $message->load('sender:id,name');

        event(new MessageSent($message));

        return response()->json([
            'status'  => 'Success',
            'message' => 'Message stored and broadcasted successfully!',
            'data'    => $message
        ], 201);
    }

    public function sync(Request $request)
    {
        $validated = $request->validate([
            'group_id'       => 'required|integer',
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

    public function reply(Request $request, $group, $topic, $message)
    {
        $validated = $request->validate([
            'msg_txt' => 'required|string'
        ]);

        $userId = $request->user()->user_id ?? $request->user()->id;

        $reply = $this->messageService->replyToDiscussion(
            (int) $group,
            (int) $topic,
            (int) $message,
            $userId,
            $validated['msg_txt']
        );

        return response()->json([
            'status'  => 'Success',
            'message' => 'Reply posted successfully.',
            'data'    => $reply
        ], 201);
    }

    public function upvote(Request $request, $messageId)
    {
        $userId = $request->user()->user_id ?? $request->user()->id;

        $result = $this->messageService->toggleUpvote(
            (int) $messageId,
            $userId
        );

        return response()->json([
            'status'  => 'Success',
            'upvoted' => $result['upvoted']
        ], 200);
    }

    public function markAnswer(Request $request, $topicId, $messageId)
    {
        $userId = $request->user()->user_id ?? $request->user()->id;

        $result = $this->messageService->markAnswer(
            (int) $topicId,
            (int) $messageId,
            $userId
        );

        if (!$result['success']) {
            return response()->json([
                'status'  => 'Error',
                'message' => $result['message']
            ], 403);
        }

        return response()->json([
            'status'  => 'Success',
            'message' => 'Answer marked successfully.'
        ], 200);
    }

    public function destroy(Request $request, $group, $topic = null, $message = null)
    {
        $userId = $request->user()->user_id ?? $request->user()->id;

        // Handles both 3-param (group, topic, message) and 2-param (group, message) route signatures
        $targetMessageId = $message ?? $topic;

        $result = $this->messageService->deleteMessage(
            (int) $group,
            (int) $targetMessageId,
            $userId
        );

        if (!$result['success']) {
            return response()->json([
                'status'  => 'Error',
                'message' => $result['message']
            ], 403);
        }

        return response()->json([
            'status'  => 'Success',
            'message' => $result['message']
        ], 200);
    }
}