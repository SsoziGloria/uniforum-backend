<?php

namespace App\Notifications;

use App\Models\Message;
use App\Models\Topic;
use Illuminate\Bus\Queueable;
use Illuminate\Notifications\Notification;

class DiscussionReplyNotification extends Notification
{
    use Queueable;

    public function __construct(
        public Topic $topic,
        public Message $reply,
        public string $replierName
    ) {}

    public function via(object $notifiable): array
    {
        return ['database'];
    }

    public function toArray(object $notifiable): array
    {
        return [
            'type'        => 'discussion_reply',
            'icon'        => '💬',
            'title'       => 'New reply to discussion',
            'message'     => "{$this->replierName} replied to discussion: \"{$this->topic->title}\"",
            'link'        => route('student.discussions.show', [$this->topic->group_id, $this->topic->topic_id]),
            'group_id'    => $this->topic->group_id,
            'topic_id'    => $this->topic->topic_id,
        ];
    }
}