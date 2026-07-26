<?php

namespace App\Services;

use App\Models\Quiz;
use App\Models\QuizQuestion;
use App\Models\StudentSubmission;
use App\Models\StudentAnswer;
use Carbon\Carbon;
use Illuminate\Support\Facades\DB;

class QuizService
{
    /**
     * Helper to reliably parse quiz start and end datetimes in the active timezone.
     */
    private function getQuizTimeWindow(Quiz $quiz): array
    {
        $timezone = config('app.timezone', 'UTC');
        $startString = trim($quiz->quiz_date . ' ' . $quiz->start_time);
        
        $startTime = Carbon::parse($startString, $timezone);
        $endTime = $startTime->copy()->addMinutes((int) $quiz->duration_minutes);

        return [$startTime, $endTime];
    }

    /**
     * Get categorized quizzes (Active, Upcoming, Completed) for a group and user.
     */
    public function getGroupQuizzes(int $groupId, int $userId): array
    {
        $quizzes = Quiz::where('group_id', $groupId)
            ->where('is_published', true)
            ->with(['questions'])
            ->get();

        $now = Carbon::now(config('app.timezone', 'UTC'));

        $activeQuizzes = [];
        $upcomingQuizzes = [];
        $completedQuizzes = [];

        $totalScorePercentageSum = 0;
        $completedCount = 0;

        foreach ($quizzes as $quiz) {
            [$startTime, $endTime] = $this->getQuizTimeWindow($quiz);

            $submission = StudentSubmission::where('quiz_id', $quiz->quiz_id)
                ->where('student_id', $userId)
                ->first();

            $totalMarksPossible = $quiz->questions->sum('marks_worth');

            if ($submission && in_array($submission->status, ['submitted', 'auto-submitted'])) {
                $percentage = $totalMarksPossible > 0 ? round(($submission->total_score / $totalMarksPossible) * 100) : 0;
                $quiz->user_score_percentage = $percentage;
                $quiz->submission = $submission;
                $completedQuizzes[] = $quiz;

                $totalScorePercentageSum += $percentage;
                $completedCount++;
            } elseif ($now->between($startTime, $endTime) && (!$submission || $submission->status === 'in-progress')) {
                $quiz->end_timestamp = $endTime->timestamp;
                $quiz->remaining_seconds = max(0, $now->diffInSeconds($endTime, false));
                $quiz->submission = $submission;
                $activeQuizzes[] = $quiz;
            } elseif ($now->lt($startTime)) {
                $quiz->start_datetime = $startTime;
                $upcomingQuizzes[] = $quiz;
            } elseif ($now->gt($endTime) && (!$submission || $submission->status === 'in-progress')) {
                // Time passed and student never completed it
                $quiz->user_score_percentage = 0;
                $completedQuizzes[] = $quiz;
            }
        }

        $averageScore = $completedCount > 0 ? round($totalScorePercentageSum / $completedCount) : 0;

        return [
            'activeQuizzes'    => $activeQuizzes,
            'upcomingQuizzes'  => $upcomingQuizzes,
            'completedQuizzes' => $completedQuizzes,
            'stats'            => [
                'available_count' => count($activeQuizzes),
                'completed_count' => $completedCount,
                'average_score'   => $averageScore,
            ]
        ];
    }

    /**
     * Start or resume a quiz attempt. Enforces time window & late joiner strict rules.
     */
    public function startOrResumeAttempt(int $groupId, int $quizId, int $userId): array
    {
        $quiz = Quiz::where('group_id', $groupId)
            ->where('quiz_id', $quizId)
            ->with('questions')
            ->firstOrFail();

        $now = Carbon::now(config('app.timezone', 'UTC'));
        [$scheduledStartTime, $scheduledEndTime] = $this->getQuizTimeWindow($quiz);

        if ($now->lt($scheduledStartTime)) {
            return [
                'success' => false,
                'message' => 'This quiz has not started yet. Scheduled for: ' . $scheduledStartTime->toDayDateTimeString()
            ];
        }

        if ($now->gt($scheduledEndTime)) {
            return [
                'success' => false,
                'message' => 'The quiz duration has expired. Late entry is not allowed.'
            ];
        }

        $submission = StudentSubmission::firstOrCreate(
            ['quiz_id' => $quizId, 'student_id' => $userId],
            [
                'started_at'  => $now,
                'status'      => 'in-progress',
                'total_score' => null
            ]
        );

        if (in_array($submission->status, ['submitted', 'auto-submitted'])) {
            return [
                'success' => false,
                'message' => 'You have already finalized your submission for this quiz.'
            ];
        }

        // Calculate absolute remaining time (diffInSeconds between $now and $scheduledEndTime)
        $remainingSeconds = max(0, $now->diffInSeconds($scheduledEndTime, false));

        return [
            'success'          => true,
            'quiz'             => $quiz,
            'submission'       => $submission,
            'remainingSeconds' => (int) $remainingSeconds,
            'scheduledEndTime' => $scheduledEndTime,
            'questions'        => $quiz->questions
        ];
    }

    /**
     * Grade and submit student attempt.
     */
    public function submitQuizAttempt(int $quizId, int $userId, array $answers): array
    {
        $quiz = Quiz::with('questions')->findOrFail($quizId);
        $submission = StudentSubmission::where('quiz_id', $quizId)
            ->where('student_id', $userId)
            ->firstOrFail();

        if (in_array($submission->status, ['submitted', 'auto-submitted'])) {
            return [
                'success' => false,
                'message' => 'This quiz has already been submitted.'
            ];
        }

        $now = Carbon::now(config('app.timezone', 'UTC'));
        [, $scheduledEndTime] = $this->getQuizTimeWindow($quiz);

        $isLate = $now->gt($scheduledEndTime);
        $status = $isLate ? 'auto-submitted' : 'submitted';

        $totalScore = 0;

        DB::transaction(function () use ($answers, $quiz, $submission, &$totalScore, $status, $now) {
            foreach ($quiz->questions as $question) {
                $selectedOption = $answers[$question->quiz_qn_id] ?? null;
                $isCorrect = false;

                if ($selectedOption !== null) {
                    $isCorrect = (trim((string)$question->correct_option) === trim((string)$selectedOption));
                    if ($isCorrect) {
                        $totalScore += $question->marks_worth;
                    }
                }

                StudentAnswer::updateOrCreate(
                    [
                        'submission_id' => $submission->submission_id,
                        'quiz_qn_id'    => $question->quiz_qn_id,
                    ],
                    [
                        'selected_option' => $selectedOption ?? '',
                        'is_correct'      => $isCorrect,
                    ]
                );
            }

            $submission->update([
                'submitted_at' => $now,
                'status'       => $status,
                'total_score'  => $totalScore,
            ]);
        });

        return [
            'success'     => true,
            'message'     => $isLate ? 'Quiz time expired! Your choices were automatically submitted.' : 'Quiz submitted successfully!',
            'total_score' => $totalScore
        ];
    }

    /**
     * Get performance report for student after quiz duration expires.
     */
    public function getQuizReport(int $groupId, int $quizId, int $userId): array
    {
        $quiz = Quiz::where('group_id', $groupId)
            ->where('quiz_id', $quizId)
            ->with(['questions'])
            ->firstOrFail();

        $now = Carbon::now(config('app.timezone', 'UTC'));
        [, $scheduledEndTime] = $this->getQuizTimeWindow($quiz);

        if ($now->lt($scheduledEndTime)) {
            return [
                'ready'   => false,
                'message' => 'Results will be published when the official quiz duration ends at ' . $scheduledEndTime->format('h:i A') . '.'
            ];
        }

        $submission = StudentSubmission::where('quiz_id', $quizId)
            ->where('student_id', $userId)
            ->with('answers')
            ->first();

        $totalQuestions = $quiz->questions->count();
        $totalMarksPossible = $quiz->questions->sum('marks_worth');

        $userScore = $submission ? ($submission->total_score ?? 0) : 0;
        $scorePercentage = $totalMarksPossible > 0 ? round(($userScore / $totalMarksPossible) * 100) : 0;

        $correctAnswersCount = 0;
        $incorrectAnswersCount = 0;
        $unansweredCount = 0;

        if ($submission) {
            $answersByQn = $submission->answers->keyBy('quiz_qn_id');

            foreach ($quiz->questions as $question) {
                $ans = $answersByQn->get($question->quiz_qn_id);
                if (!$ans || $ans->selected_option === null || $ans->selected_option === '') {
                    $unansweredCount++;
                } elseif ($ans->is_correct) {
                    $correctAnswersCount++;
                } else {
                    $incorrectAnswersCount++;
                }
            }
        } else {
            $unansweredCount = $totalQuestions;
        }

        $startedAt = $submission && $submission->started_at ? Carbon::parse($submission->started_at) : null;
        $submittedAt = $submission && $submission->submitted_at ? Carbon::parse($submission->submitted_at) : null;

        $timeTakenMinutes = ($startedAt && $submittedAt)
            ? max(1, $startedAt->diffInMinutes($submittedAt))
            : 0;

        return [
            'ready'                 => true,
            'quiz'                  => $quiz,
            'scorePercentage'       => $scorePercentage,
            'totalQuestions'        => $totalQuestions,
            'correctAnswersCount'   => $correctAnswersCount,
            'incorrectAnswersCount' => $incorrectAnswersCount,
            'unansweredCount'       => $unansweredCount,
            'timeTakenMinutes'      => $timeTakenMinutes,
            'submission'            => $submission
        ];
    }
}