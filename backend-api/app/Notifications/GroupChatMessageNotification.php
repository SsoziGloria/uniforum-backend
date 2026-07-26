<?php

namespace App\Notifications;

use App\Models\Group;
use App\Models\Message;
use Illuminate\Bus\Queueable;
use Illuminate\Notifications\Notification;

class GroupChatMessageNotification extends Notification
{
    use Queueable;

    public function __construct(
        public Group $group,
        public Message $chatMessage,
        public string $senderName
    ) {}

    public function via(object $notifiable): array
    {
        return ['database'];
    }

    public function toArray(object $notifiable): array
    {
        return [
            'type'      => 'group_chat',
            'icon'      => '💬',
            'title'     => 'New Chat Message',
            'message'   => "{$this->senderName} sent a message in {$this->group->group_name}: \"" . \Str::limit($this->chatMessage->msg_txt, 50) . "\"",
            'link'      => route('student.groups.chat', $this->group->group_id),
            'group_id'  => $this->group->group_id,
        ];
    }
}