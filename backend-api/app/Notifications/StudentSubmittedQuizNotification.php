<?php

namespace App\Notifications;

use App\Models\Quiz;
use App\Models\User;
use Illuminate\Bus\Queueable;
use Illuminate\Notifications\Notification;

class StudentSubmittedQuizNotification extends Notification
{
    use Queueable;

    public function __construct(
        public Quiz $quiz,
        public User $student
    ) {}

    public function via(object $notifiable): array
    {
        return ['database'];
    }

    public function toArray(object $notifiable): array
    {
        return [
            'type'     => 'quiz_submission',
            'icon'     => '📝',
            'title'    => 'New Quiz Submission',
            'message'  => "{$this->student->name} submitted an attempt for \"{$this->quiz->quiz_title}\".",
            'link'     => route('lecturer.quizzes.results', [$this->quiz->group_id, $this->quiz->quiz_id]),
            'quiz_id'  => $this->quiz->quiz_id,
        ];
    }
}