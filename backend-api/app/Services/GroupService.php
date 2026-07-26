<?php

namespace App\Services;

use App\Models\Group;
use App\Models\GroupMember;
use App\Models\User;

class GroupService
{
    /**
     * Get all groups belonging to a user.
     */
    public function getUserGroups($userId)
    {
        $memberships = GroupMember::with('group')
            ->where('user_id', $userId)
            ->get();

        return $memberships
            ->map(function ($membership) {
                return $membership->group;
            })
            ->filter();
    }


    /**
     * Search available groups.
     */
    public function searchGroups($searchTerm = null)
    {
        return Group::when($searchTerm, function ($query, $searchTerm) {

            return $query
                ->where('group_name', 'like', "%{$searchTerm}%")
                ->orWhere('description', 'like', "%{$searchTerm}%");

        })->get();
    }


    /**
     * Create a group and automatically add creator.
     */
    public function createGroup(array $data, User $user)
    {
        $role = 'admin';

        if ($user->role === 'lecturer') {
            $role = 'lecturer';
        }

        $group = Group::create([
            'group_name' => $data['group_name'],
            'description' => $data['description'] ?? null,
            'created_by' => $user->id,
        ]);


        GroupMember::create([
            'group_id' => $group->group_id,
            'user_id' => $user->id,
            'role' => $role,
        ]);


        return $group;
    }


    /**
     * Get group members.
     */
    public function getMembers(Group $group)
    {
        return $group->members()
            ->select('users.id', 'users.name')
            ->get();
    }
    /**
     * Get all groups or search groups.
    */
    public function browseGroups(?string $search = null)
    {
        return Group::withCount('members')
            ->when($search, function ($query, $search) {
                $query->where(function ($q) use ($search) {
                   $q->where('group_name', 'like', "%{$search}%")
                     ->orWhere('description', 'like', "%{$search}%");
            });
        })
        ->latest()
        ->get();
    }
    /**
     * Get group details
     */
    public function getGroupDetails(int $groupId, int $userId)
   {
        $group = Group::with([
            'creator',
            'topics' => function($query){
                $query->latest('created_at')
                      ->limit(5)
                      ->withCount('messages');
            },
            'members'
            ])
        ->withCount('members')
        ->findOrFail($groupId);

         $membership = GroupMember::where('group_id', $groupId)
           ->where('user_id', $userId)
           ->first();

        return [
           'group' => $group,
           'membership' => $membership,
           'isMember' => $membership !== null,
            'isAdmin' => $membership && $membership->role === 'admin',
            'userRole' => $membership?->role,
            'topics' => $group->topics,
            'members' => $group->members,

        ];
    }
    /**
    * Join a group.
    */
    public function joinGroup(Group $group, User $user)
    {
        // Check if already a member
        if ($group->members()
            ->where('user_id', $user->id)
            ->exists()) {

            return [
               'success' => false,
               'message' => 'You are already a member of this group.'
            ];
        }


        // Default role
        $role = 'member';


        // Lecturer users join as lecturers
        if ($user->role === 'lecturer') {
            $role = 'lecturer';
        }


        $group->members()->attach($user->id, [

            'role' => $role,

            'rules_accepted' => true,

            'joined_at' => now(),

            'last_activity' => now(),

        ]);


        return [
            'success' => true,
            'message' => "Successfully joined the group as {$role}."
        ];
    }

}