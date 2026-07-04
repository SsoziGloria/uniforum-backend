<?php

namespace App\Http\Controllers;

use App\Models\Message;
use App\Events\MessageSent;
use Illuminate\Http\Request;

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
        //temporarily bypass foreign keys for testing
     //\Illuminate\Support\Facades\DB::statement('SET FOREIGN_KEY_CHECKS=0;');

        // Save the message into the database
        $message = Message::create($validated);

        //turn on
     //\Illuminate\Support\Facades\DB::statement('SET FOREIGN_KEY_CHECKS=1;');

        //Fire the Event, triggers Laravel Reverb to broadcast it in real-time

   event(new MessageSent($message->topic_id, $message->sender_id, $message->msg_txt));
        // Return a response to the user who sent it
        return response()->json([
            'status' => 'Message sent successfully!',
            'data' => $message
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
