<?php

namespace App\Http\Controllers;

use App\Models\Message;
use App\Models\MessageExclusion;
use App\Events\MessageSent;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

class MessageController extends Controller
{
       /**
        * Fetch all messages belonging to a specific topic.
         */
        public function getTopicMessages($group_id, $topic_id)
        {
           $userId = Auth::id();

            //Look up all messages in the DB that match the requested topic_id
            $messages = Message::where('topic_id', $topic_id)
                               ->with('sender:user_id,user_name')
                               ->where(function ($query) use ($userId) {
                            //Show if the message is completely open to everyone
                              $query->where('is_restricted', false)
                            //OR show if the current user is the author (sender always sees their own text)
                              ->orWhere('sender_id', $userId)
                            //OR show if it IS restricted, but the user is NOT listed in the exclusions table
                               ->orWhere(function ($subQuery) use ($userId) {
                                  $subQuery->where('is_restricted', true)
                                    ->whereDoesntHave('exclusions', function ($exclusionCheck) use ($userId) {
                                         $exclusionCheck->where('ex_user_id', $userId); // Matches your model column!

                                          });
                                    });
                               })
                               ->orderBy('posted_at', 'asc')
                               ->get();

            //Return the list of messages as a clean JSON response

            return response()->json([
                'status' => 'Messages retrieved successfully',
                'data' => $messages
            ], 200);
        }
/**
   * Store a new message an trigger real-time broadcast
 */
    public function store(Request $request, $group_id, $topic_id)
    {

        //Validate the incoming request data
        $validated = $request->validate([
            'msg_txt'      => 'required|string',
            'is_restricted'      => 'required|boolean',
            'excluded_user_ids'  => 'nullable|array',
            'excluded_user_ids.*'=> 'integer|exists:users,user_id'
        ]);

         $sender_id = $request->user()->user_id;
         $sender_id = $request->user()->user_id;

        // Save the message into the database
         $message = Message::create([
                 'topic_id'   => $topic_id,
                 'sender_id'  => Auth::id(),
                 'msg_txt'    => $validated['msg_txt'],
                 'is_synced'  => true,
                 'is_restricted' => $request->is_restricted,
                 'posted_at'  => now(),
             ]);
             if ($request->is_restricted && $request->has('excluded_user_ids')) {
                     foreach ($request->excluded_user_ids as $excludedId) {
                       MessageExclusion::create([
                             'msg_id'     => $message->msg_id,
                             'ex_user_id' => $excludedId // Maps precisely to your ex_user_id column
                             ]);
                         }
                     }
              //so that broadcaster has user_name attached
             $message->load('sender:user_id,user_name');

        //Fire the Event, triggers Laravel Reverb to broadcast it in real-time

         event(new MessageSent($message));

           return response()->json([
               'status' => 'Success',
               'message' => 'Message stored and broadcasted successfully!',
               'data'    => $message->load('sender:user_id,user_name')
           ], 201);
    }

}
