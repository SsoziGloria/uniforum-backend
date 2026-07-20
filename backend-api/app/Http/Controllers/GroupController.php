<?php

namespace App\Http\Controllers;

use App\Models\Group;
use App\Models\GroupMember;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;

class GroupController extends Controller
{
    /**
     * Display a listing of the resource.
     * Fetch only the groups the logged-in user belongs to.
     */
    public function index(Request $request)
    {
        // Get the authenticated user's ID
        $userId = $request->user()->id;

        // Fetch memberships along with related group details using Eloquent
        $memberships = GroupMember::with('group')
            ->where('user_id', $userId)
            ->get();

        // Extract just the group objects out of the memberships collection
        $myGroups = $memberships->map(function ($membership) {
            return $membership->group;
        })->filter();

        return response()->json([
            'success' => true,
            'data'    => array_values($myGroups->toArray())
        ], 200);
    }

    /**
     * Store a newly created resource in storage.
     * Protects the 'lecturer' group role using a secret passcode.
     */
    public function store(Request $request)
    {
        $validated = $request->validate([
            'group_name'        => 'required|string|max:255',
            'description'       => 'nullable|string',
            'creator_role'      => 'nullable|string|in:admin,lecturer',
            'lecturer_passcode' => 'nullable|string',
        ]);

        $creatorId = $request->user()->id;
        $role = 'admin';

        // If user is already a global lecturer, auto-assign lecturer role
        if (isset($currentUser->role) && $currentUser->role === 'lecturer') {
            $role = 'lecturer';
         }
        // Elevate the creator to 'lecturer' ONLY if they provide the correct passcode
        elseif (isset($validated['creator_role']) && $validated['creator_role'] === 'lecturer') {

            // Hardcoded secret code for verification during testing/presentation
            $secretStaffCode = 'MUK-STAFF-2026';

            if (($validated['lecturer_passcode'] ?? '') === $secretStaffCode) {
                $role = 'lecturer';
            } else {
                return response()->json([
                    'status'  => 'Error',
                    'message' => 'Invalid lecturer passcode. You cannot create a group as a lecturer.'
                ], 403);
            }
        }

        //Create Group using Eloquent Model
                $group = Group::create([
                    'group_name'  => $validated['group_name'],
                    'description' => $validated['description'] ?? null,
                    'created_by'  => $creatorId,
                ]);


                //AUTOMATIC ASSIGNMENT: Link creator
                GroupMember::create([
                    'group_id' => $group->group_id,
                    'user_id'  => $creatorId,
                    'role'     => $role,
                ]);

        return response()->json([
            'status'  => 'Success',
            'message' => "Group created successfully! You joined as group {$role}.",
            'group'   => $group
        ], 201);
    }

    /**
     * ADD ANOTHER USER TO GROUP USING EMAIL
     * Accessible by both 'admin' and 'lecturer' roles.
     */
    public function addMember(Request $request, $groupId)
    {
        $validated = $request->validate([
            'email' => 'required|email'
        ]);

        $groupIdClean = is_object($groupId) ? ($groupId->id ?? $groupId->group_id) : (int)$groupId;

        // Find the user by email using Eloquent Model
        $userToAdd = User::where('email', $validated['email'])->first();

        if (!$userToAdd) {
            return response()->json([
                'status'  => 'Error',
                'message' => 'No user found with that email address.'
            ], 404);
        }

        $currentUser= $request->user()->id;
        $isAuthorized = false;


        // Global Lecturer Bypass or Group Admin/Lecturer check
          if (isset($currentUser->role) && $currentUser->role === 'lecturer') {
                    $isAuthorized = true;
           } else {
                $isAuthorized = GroupMember::where('group_id', $groupIdClean)
                        ->where('user_id', $currentUser->id)
                        ->whereIn('role', ['admin', 'lecturer'])
                        ->exists();
            }

        if (!$isAuthorized) {
            return response()->json([
                'status'  => 'Error',
                'message' => 'Unauthorized. Only group administrators or lecturers can add new members.'
            ], 403);
        }

        // Check if they are already in the group
        $alreadyMember = GroupMember::where('group_id', $groupIdClean)
            ->where('user_id', $userToAdd->id)
            ->exists();

        if ($alreadyMember) {
            return response()->json([
                'status'  => 'Error',
                'message' => 'This user is already a registered member of this group.'
            ], 422);
        }

        // Add them to the group with default role 'member' (Student)
        GroupMember::create([
            'group_id' => $groupIdClean,
            'user_id'  => $userToAdd->id,
            'role'     => 'member'
        ]);

        return response()->json([
            'status'  => 'Success',
            'message' => "Successfully added {$userToAdd->name} to the group!"
        ], 200);
    }

    /**
     * Get a list of all verified members in a group.
     */
    public function getMembers(Group $group): JsonResponse
    {
        return response()->json([
            'group_id'   => $group->group_id ?? $group->id,
            'group_name' => $group->group_name,
            'members'    => $group->members()->select('users.id', 'users.name')->get()
        ]);
    }

    /**
     * Allows group administrators or lecturers to modify member roles (admin, lecturer, member)
     */
    public function changeMemberRole(Request $request, $groupId)
    {
        $validated = $request->validate([
            'user_id' => 'required|integer',
            'role'    => 'required|string|in:admin,lecturer,member'
        ]);

        $groupIdClean = is_object($groupId) ? ($groupId->id ?? $groupId->group_id) : (int)$groupId;
        $currentUser = $request->user()->id;
        $isAuthorized = false;

       if (isset($currentUser->role) && $currentUser->role === 'lecturer') {
                   $isAuthorized = true;
           } else {
              $isAuthorized = GroupMember::where('group_id', $groupIdClean)
                 ->where('user_id', $currentUser->id)
                 ->whereIn('role', ['admin', 'lecturer'])
                 ->exists();
               }

       if (!$isAuthorized) {
           return response()->json([
             'status'  => 'Error',
             'message' => 'Unauthorized. Only group administrators or lecturers can manage member roles.'
                  ], 403);
            }

        // Check if the target user actually belongs to the group
        $targetMembership = GroupMember::where('group_id', $groupIdClean)
            ->where('user_id', $validated['user_id'])
            ->first();

        if (!$targetMembership) {
            return response()->json([
                'status'  => 'Error',
                'message' => 'The specified user is not a member of this group.'
            ], 422);
        }

        // Update target user's role
        $targetMembership->update([
            'role' => $validated['role']
        ]);

        return response()->json([
            'status'  => 'Success',
            'message' => "Member role updated to {$validated['role']} successfully!"
        ], 200);
    }

    /**
     * REMOVING A MEMBER FROM THE GROUP
     * Accessible by both 'admin' and 'lecturer' roles.
     */
    public function removeMember(Request $request, $groupId)
    {
        $validated = $request->validate([
            'user_id' => 'required|integer'
        ]);

        $groupIdClean = is_object($groupId) ? ($groupId->id ?? $groupId->group_id) : (int)$groupId;
        $currentUser = $request->user()->id;
        $isAuthorized = false;

     if (isset($currentUser->role) && $currentUser->role === 'lecturer') {
            $isAuthorized = true;
        } else {
            $isAuthorized = GroupMember::where('group_id', $groupIdClean)
                ->where('user_id', $currentUser->id)
                ->whereIn('role', ['admin', 'lecturer'])
                ->exists();
        }

        if (!$isAuthorized) {
            return response()->json([
                'status'  => 'Error',
                'message' => 'Unauthorized. Only group administrators or lecturers can remove members.'
            ], 403);
        }

        if ($validated['user_id'] == $currentUser->id) {
            return response()->json([
                'status'  => 'Error',
                'message' => 'You cannot remove yourself from the group.'
            ], 422);
        }

        $membership = GroupMember::where('group_id', $groupIdClean)
            ->where('user_id', $validated['user_id'])
            ->first();

        if (!$membership) {
            return response()->json([
                'status'  => 'Error',
                'message' => 'The specified user is not a member of this group.'
            ], 422);
        }

        $membership->delete();

        return response()->json([
            'status'  => 'Success',
            'message' => 'User removed from the academic group successfully.'
        ], 200);
    }

    public function show(string $id) {}
    public function update(Request $request, string $id) {}
    public function destroy(string $id) {}
}
