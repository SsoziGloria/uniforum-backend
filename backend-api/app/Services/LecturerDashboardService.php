<?php

namespace App\Services;

use App\Models\Group;
use App\Models\GroupMember;
use App\Models\Message;
use App\Models\Quiz;
use App\Models\Topic;
use App\Models\User;

class LecturerDashboardService
{
    public function getDashboardMetrics(int $lecturerId): array
    {
        // 1. Get group IDs associated with this lecturer
        $lecturerGroupIds = GroupMember::where('user_id', $lecturerId)
            ->pluck('group_id');

        // 2. Active Discussions (Topics in lecturer's groups)
        $activeDiscussionsCount = Topic::whereIn('group_id', $lecturerGroupIds)->count();

        // 3. Unique Students Engaged in lecturer's groups
        $engagedStudentsCount = User::where('role', 'student')
            ->whereHas('groups', function ($query) use ($lecturerGroupIds) {
                $query->whereIn('groups.group_id', $lecturerGroupIds);
            })
            ->count();

        // 4. Published Quizzes in lecturer's groups (fixed is_published column)
        $activeQuizzesCount = Quiz::whereIn('group_id', $lecturerGroupIds)
            ->where('is_published', true)
            ->count();

        // 5. Recent Quizzes list (Limit 5)
        $quizzes = Quiz::whereIn('group_id', $lecturerGroupIds)
            ->with('group')
            ->latest('created_at')
            ->take(5)
            ->get();

        // 6. Group Participation Breakdown
        $groups = Group::whereIn('group_id', $lecturerGroupIds)->get();

        $groupParticipation = $groups->map(function ($group) {
            $studentCount = GroupMember::where('group_id', $group->group_id)
                ->whereHas('user', fn($q) => $q->where('role', 'student'))
                ->count();

            // Total topics & messages in group
            $topicsCount = Topic::where('group_id', $group->group_id)->count();
            $messagesCount = Message::where('group_id', $group->group_id)->count();

            $totalActivity = $topicsCount + $messagesCount;

            // Calculate percentage engagement benchmark (capped at 100%)
            $percentage = $studentCount > 0 ? min(100, round(($totalActivity / ($studentCount * 3)) * 100)) : 0;

            return [
                'group_id'   => $group->group_id,
                'group_name' => $group->group_name,
                'percentage' => $percentage,
            ];
        });

        // Overall Average Participation
        $avgParticipation = $groupParticipation->count() > 0 
            ? round($groupParticipation->avg('percentage')) 
            : 0;

        return [
            'activeDiscussions' => $activeDiscussionsCount,
            'engagedStudents'   => $engagedStudentsCount,
            'activeQuizzes'     => $activeQuizzesCount,
            'avgParticipation'  => $avgParticipation,
            'quizzes'           => $quizzes,
            'groupParticipation'=> $groupParticipation,
        ];
    }
}