<?php

namespace App\Notifications;

use App\Models\Quiz;
use Illuminate\Bus\Queueable;
use Illuminate\Notifications\Notification;

class QuizPublishedNotification extends Notification
{
    use Queueable;

    public function __construct(
        public Quiz $quiz,
        public string $groupName
    ) {}

    public function via(object $notifiable): array
    {
        return ['database'];
    }

    public function toArray(object $notifiable): array
    {
        $quizDate = \Carbon\Carbon::parse($this->quiz->quiz_date)->format('M d, Y');
        $startTime = \Carbon\Carbon::parse($this->quiz->start_time)->format('h:i A');

        return [
            'type'      => 'quiz_announcement',
            'icon'      => '📝',
            'title'     => 'New Quiz Available',
            'message'   => "{$this->quiz->quiz_title} in {$this->groupName} is scheduled for {$quizDate} at {$startTime}.",
            'link'      => route('student.groups.quizzes.index', $this->quiz->group_id),
            'group_id'  => $this->quiz->group_id,
            'quiz_id'   => $this->quiz->quiz_id,
        ];
    }
}