<?php

namespace App\Services;

use App\Models\User;
use App\Models\GroupMember;
use App\Models\Topic;
use App\Models\Message;
use App\Models\StudentSubmission;
use Carbon\Carbon;

class StudentPerformanceService
{
    /**
     * Get performance metrics for students taught by a specific lecturer.
     */
    public function getLecturerStudentsPerformance(int $lecturerId): array
    {
        // 1. Find all group IDs where the lecturer is a member
        $lecturerGroupIds = GroupMember::where('user_id', $lecturerId)
            ->pluck('group_id');

        // 2. Retrieve student users who belong to any of those groups
        $students = User::where('role', 'student')
            ->whereHas('groups', function ($query) use ($lecturerGroupIds) {
                $query->whereIn('groups.group_id', $lecturerGroupIds);
            })
            ->with(['groups'])
            ->get();

        $totalStudents = $students->count();
        $oneWeekAgo = Carbon::now(config('app.timezone', 'UTC'))->subDays(7);

        $activeThisWeekCount = 0;
        $totalQuizPercentageSum = 0;
        $studentsWithQuizzesCount = 0;

        $studentsData = $students->map(function ($student) use ($oneWeekAgo, &$activeThisWeekCount, &$totalQuizPercentageSum, &$studentsWithQuizzesCount) {
            
            // Discussion topics created by this student (Topic model: created_by)
            $discussionsCount = Topic::where('created_by', $student->id)->count();

            // Discussion answers/messages posted by this student (Message model: sender_id)
            $answersCount = Message::where('sender_id', $student->id)->count();

            // Quiz submissions & scores
            $submissions = StudentSubmission::where('student_id', $student->id)
                ->whereIn('status', ['submitted', 'auto-submitted'])
                ->with('quiz.questions')
                ->get();

            $quizAvgPercentage = 0;
            if ($submissions->count() > 0) {
                $totalScored = 0;
                $totalPossible = 0;

                foreach ($submissions as $sub) {
                    if ($sub->quiz) {
                        $possible = $sub->quiz->questions->sum('marks_worth');
                        if ($possible > 0) {
                            $totalScored += $sub->total_score;
                            $totalPossible += $possible;
                        }
                    }
                }

                $quizAvgPercentage = $totalPossible > 0 ? round(($totalScored / $totalPossible) * 100) : 0;
                $totalQuizPercentageSum += $quizAvgPercentage;
                $studentsWithQuizzesCount++;
            }

            // Recent activity check using accurate timestamps (created_at for Topic, posted_at for Message)
            $hasRecentTopic = Topic::where('created_by', $student->id)
                ->where('created_at', '>=', $oneWeekAgo)
                ->exists();

            $hasRecentMsg = Message::where('sender_id', $student->id)
                ->where('posted_at', '>=', $oneWeekAgo)
                ->exists();

            $hasRecentQuiz = StudentSubmission::where('student_id', $student->id)
                ->where('submitted_at', '>=', $oneWeekAgo)
                ->exists();

            $isRecent = $hasRecentTopic || $hasRecentMsg || $hasRecentQuiz;
            if ($isRecent) {
                $activeThisWeekCount++;
            }

            $totalActivityCount = $discussionsCount + $answersCount + $submissions->count();

            if ($totalActivityCount >= 10 && $isRecent) {
                $status = 'Active';
                $statusClass = 'bg-green-100 text-green-700';
            } elseif ($totalActivityCount >= 3) {
                $status = 'Moderate';
                $statusClass = 'bg-yellow-100 text-yellow-700';
            } else {
                $status = 'Low Activity';
                $statusClass = 'bg-orange-100 text-orange-700';
            }

            // Calculate participation mark out of 20
            $participationMark = min(20, round(($discussionsCount * 0.5) + ($answersCount * 0.4) + ($quizAvgPercentage * 0.1)));

            return [
                'id'                  => $student->id,
                'name'                => $student->name,
                'email'               => $student->email,
                'discussions_count'   => $discussionsCount,
                'answers_count'       => $answersCount,
                'quiz_avg_percentage' => $quizAvgPercentage,
                'participation_mark'  => $participationMark,
                'status'              => $status,
                'status_class'        => $statusClass,
                'student'             => $student,
            ];
        });

        $avgQuizScore = $studentsWithQuizzesCount > 0 ? round($totalQuizPercentageSum / $studentsWithQuizzesCount) : 0;
        $avgParticipation = $totalStudents > 0 ? round($studentsData->avg('participation_mark')) : 0;

        return [
            'studentsData'        => $studentsData,
            'totalStudents'       => $totalStudents,
            'activeThisWeekCount' => $activeThisWeekCount,
            'avgParticipation'    => $avgParticipation,
            'avgQuizScore'        => $avgQuizScore,
        ];
    }
}