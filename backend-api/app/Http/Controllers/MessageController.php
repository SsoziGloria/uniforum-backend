<?php

namespace App\Http\Controllers;

use App\Models\Message;
use App\Events\MessageSent;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class MessageController extends Controller
{

    public function store(Request $request)
    {

        //Validate the incoming request data
        $validated = $request->validate([
            'topic_id' => 'required|integer',
            'sender_id'   => 'required|integer',
            'msg_txt'      => 'required|string',
        ]);


        // Save the message into the database
         DB::table('messages')->insert([
                 'topic_id'   => $validated['topic_id'],
                 'sender_id'  => $validated['sender_id'],
                 'msg_txt'    => $validated['msg_txt'],
                 'is_synced'  => true,
                 'is_restricted' => false,
                 'posted_at'  => now(),
             ]);

        //Fire the Event, triggers Laravel Reverb to broadcast it in real-time

         event(new MessageSent(
               $validated['topic_id'],
               $validated['sender_id'],
               $validated['msg_txt']
           ));

           return response()->json([
               'status' => 'Success',
               'message' => 'Message stored and broadcasted successfully!'
           ], 201);
    }

/**
     * Fetch all messages belonging to a specific topic.
     */
    public function getTopicMessages($topic_id)
    {
        //Look up all messages in the DB that match the requested topic_id
        //latest() to get the newest messages first
        $messages = Message::where('topic_id', $topic_id)
                           ->orderBy('posted_at', 'desc')
                           ->get();

        //Return the list of messages as a clean JSON response

        return response()->json([
            'status' => 'Messages retrieved successfully',
            'data' => $messages
        ], 200);
    }
}
