<?php

namespace App\Services;

use App\Models\GroupMember;
use App\Models\Message;
use App\Models\Topic;

class GroupStatisticsService
{
    /**
     * Retrieve all statistics for a group.
     */
    public function getStatistics($groupId)
    {
        /*
        |--------------------------------------------------------------------------
        | Group Overview
        |--------------------------------------------------------------------------
        */

        $totalMembers = GroupMember::where('group_id', $groupId)
            ->count();

        $discussionCount = Topic::where('group_id', $groupId)
            ->count();

        $messageCount = Message::where('group_id', $groupId)
            ->count();

        $activeToday = GroupMember::where('group_id', $groupId)
            ->whereDate('last_activity', today())
            ->count();


        /*
        |--------------------------------------------------------------------------
        | Participation
        |--------------------------------------------------------------------------
        */

        $activeThisWeek = GroupMember::where('group_id', $groupId)
            ->where('last_activity', '>=', now()->subDays(7))
            ->count();

        $participationRate = $totalMembers > 0
            ? round(($activeThisWeek / $totalMembers) * 100)
            : 0;


        /*
        |--------------------------------------------------------------------------
        | Moderation
        |--------------------------------------------------------------------------
        */

        $warningsIssued = GroupMember::where('group_id', $groupId)
            ->sum('warning_count');

        $currentlyBlacklisted = GroupMember::where('group_id', $groupId)
            ->where('blacklisted_until', '>', now())
            ->count();


        /*
        |--------------------------------------------------------------------------
        | Most Active Members
        |--------------------------------------------------------------------------
        */

        $mostActiveMembers = Message::selectRaw('sender_id, COUNT(*) as posts')
            ->where('group_id', $groupId)
            ->groupBy('sender_id')
            ->with('sender:id,name')
            ->orderByDesc('posts')
            ->take(5)
            ->get();


        /*
        |--------------------------------------------------------------------------
        | Popular Discussions
        |--------------------------------------------------------------------------
        */

        $popularDiscussions = Topic::withCount('messages')
            ->where('group_id', $groupId)
            ->orderByDesc('messages_count')
            ->take(5)
            ->get();


        return [

            'totalMembers' => $totalMembers,

            'discussionCount' => $discussionCount,

            'messageCount' => $messageCount,

            'activeToday' => $activeToday,

            'participationRate' => $participationRate,

            'warningsIssued' => $warningsIssued,

            'currentlyBlacklisted' => $currentlyBlacklisted,

            'mostActiveMembers' => $mostActiveMembers,

            'popularDiscussions' => $popularDiscussions,

        ];
    }
}