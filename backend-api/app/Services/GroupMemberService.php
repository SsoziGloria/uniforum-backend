<?php

namespace App\Services;

use App\Models\GroupMember;

class GroupMemberService
{
    /**
     * Retrieve all members belonging to a group.
     */
    public function getGroupMembers($groupId)
    {
        return GroupMember::where('group_id', $groupId)
            ->with('user:id,name,role')
            ->get();
    }
    
    /**
     * Change a member's role within a group (e.g. promote to admin or demote to member).
     */
    public function changeMemberRole(
        int $groupId,
        int $currentUserId,
        int $targetUserId,
        string $role
    ) {
        $currentMembership = GroupMember::with('user')
            ->where('group_id', $groupId)
            ->where('user_id', $currentUserId)
            ->first();

        if (!$currentMembership) {
            return [
                'success' => false,
                'message' => 'You are not a member of this group.'
            ];
        }

        // Global lecturer bypass
        if (
            $currentMembership->user->role !== 'lecturer' &&
            !in_array($currentMembership->role, ['admin', 'lecturer'])
        ) {
            return [
                'success' => false,
                'message' => 'Access denied.'
            ];
        }

        $targetMembership = GroupMember::where('group_id', $groupId)
            ->where('user_id', $targetUserId)
            ->first();

        if (!$targetMembership) {
            return [
                'success' => false,
                'message' => 'Target member not found.'
            ];
        }

        $group = \App\Models\Group::find($groupId);

        // Protection: Creator cannot be demoted from admin
        if ($group && $group->created_by === $targetUserId && $role !== 'admin') {
            return [
                'success' => false,
                'message' => 'The group creator cannot be demoted from administrator status.'
            ];
        }

        // Protection: Ensure at least one admin remains in the group
        if ($targetMembership->role === 'admin' && $role !== 'admin') {
            $adminCount = GroupMember::where('group_id', $groupId)
                ->where('role', 'admin')
                ->count();

            if ($adminCount <= 1) {
                return [
                    'success' => false,
                    'message' => 'Cannot demote the last administrator in the group.'
                ];
            }
        }

        $targetMembership->update([
            'role' => $role
        ]);

        return [
            'success' => true,
            'message' => "Member role updated to '{$role}' successfully."
        ];
    }
    /**
    * Manually issue warning
    */
    public function issueWarning($groupId, $userId)
    {
        $member = GroupMember::where('group_id',$groupId)
           ->where('user_id',$userId)
           ->firstOrFail();


        if($member->warning_count < 2){

          $member->increment('warning_count');

        }


        return [
            'success'=>true,
            'message'=>"Warning issued successfully."
        ];
    }




    /**
    * Blacklist member
    */
    public function blacklistMember($groupId,$userId)
    {
        $member = GroupMember::where('group_id',$groupId)
           ->where('user_id',$userId)
           ->firstOrFail();


        $member->update([

           'blacklisted_until'=>now()->addDays(7)

        ]);


        return [
           'success'=>true,
           'message'=>"Member blacklisted for 7 days."
        ];
    }





    /**
    * Reinstate member
     */
    public function reinstateMember($groupId,$userId)
    {
        $member = GroupMember::where('group_id',$groupId)
            ->where('user_id',$userId)
            ->firstOrFail();


        $member->update([

            'blacklisted_until'=>null,
            'warning_count'=>0

        ]);


        return [
            'success'=>true,
            'message'=>"Member reinstated."
        ];
    }

    public function checkInactiveMembers()
    {

        $members = GroupMember::whereNull('blacklisted_until')
           ->get();


        foreach($members as $member){


            if(!$member->last_activity){
                continue;
            }


            $inactiveDays = now()
                ->diffInDays($member->last_activity);



        /*
        First warning after 14 days
        */

            if($inactiveDays >= 14 
               && $member->warning_count == 0)
            {

                $member->update([
                   'warning_count'=>1
                ]);

            }



        /*
        Second warning after 21 days
        */

            elseif($inactiveDays >=21 
                && $member->warning_count ==1)
            {

                $member->update([
                    'warning_count'=>2
                ]);

            }



        /*
        Blacklist after 30 days
        */

            elseif($inactiveDays >=30
                && $member->warning_count >=2)
            {

                $member->update([

                    'blacklisted_until'=>now()->addDays(7)

                ]);

            }

            }

    }
}