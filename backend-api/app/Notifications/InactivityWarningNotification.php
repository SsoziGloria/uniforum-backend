<?php

namespace App\Notifications;

use Illuminate\Bus\Queueable;
use Illuminate\Notifications\Notification;

class InactivityWarningNotification extends Notification
{
    use Queueable;

    public function __construct(
        public string $groupName,
        public int $daysInactive
    ) {}

    public function via(object $notifiable): array
    {
        return ['database'];
    }

    public function toArray(object $notifiable): array
    {
        return [
            'type'     => 'warning',
            'icon'     => '⚠️',
            'title'    => 'Inactivity Warning',
            'message'  => "You have not participated in {$this->groupName} for the last {$this->daysInactive} days. Please contribute to stay active.",
            'link'     => '#',
        ];
    }
}