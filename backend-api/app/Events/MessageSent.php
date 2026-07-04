<?php

namespace App\Events;

use Illuminate\Broadcasting\Channel;
use Illuminate\Broadcasting\InteractsWithSockets;
use Illuminate\Broadcasting\PrivateChannel;
use Illuminate\Contracts\Broadcasting\ShouldBroadcast;
use Illuminate\Foundation\Events\Dispatchable;
use Illuminate\Queue\SerializesModels;

class MessageSent implements ShouldBroadcast
{
    use Dispatchable, InteractsWithSockets, SerializesModels;

    // 1. You MUST declare these public properties at the top!
    public $topic_id;
    public $sender_id;
    public $msg_txt;

    /**
     * Create a new event instance.
     */
    public function __construct($topic_id, $sender_id, $msg_txt)
    {
        // 2. Assign the incoming variables to the public properties
        $this->topic_id = $topic_id;
        $this->sender_id = $sender_id;
        $this->msg_txt = $msg_txt;
    }

    /**
     * Get the channels the event should broadcast on.
     */
    public function broadcastOn(): array
    {
        // Using a public channel for testing convenience
        return [
            new Channel('topic.' . $this->topic_id)
        ];
    }

    /**
     * Data to broadcast.
     */
    public function broadcastWith(): array
    {
        return [
            'topic_id'  => $this->topic_id,
            'sender_id' => $this->sender_id,
            'msg_txt'   => $this->msg_txt,
        ];
    }
}
