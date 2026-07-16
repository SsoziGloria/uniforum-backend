<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\GroupMember;

class GroupMemberController extends Controller
{
    /**
     * Update a group member's role.
     * Accessible by both 'admin' and 'lecturer' roles.
     */
    public function updateRole(Request $request, $group, $user)
    {
        $validated = $request->validate([
            'role' => 'required|string|in:admin,lecturer,member',
        ]);

        $groupId = is_object($group) ? ($group->group_id ?? $group->id) : (int)$group;
        $targetUserId = is_object($user) ? ($user->id) : (int)$user;
        $currentUserId = $request->user()->id;

        // Ensure the logged-in user has administrative power (must be 'admin' or 'lecturer')
        $currentUserMembership = GroupMember::where('group_id', $groupId)
            ->where('user_id', $currentUserId)
            ->first();

        if (!$currentUserMembership || !in_array($currentUserMembership->role, ['admin', 'lecturer'])) {
            return response()->json([
                'status' => 'Error',
                'message' => 'Access Denied: Only group administrators or lecturers can modify roles.'
            ], 403);
        }

        // Fetch the target member to update
        $targetMembership = GroupMember::where('group_id', $groupId)
            ->where('user_id', $targetUserId)
            ->first();

        if (!$targetMembership) {
            return response()->json([
                'status' => 'Error',
                'message' => 'The target user is not a member of this group.'
            ], 404);
        }

        //Update their role
        $targetMembership->update([
            'role' => $validated['role']
        ]);

        return response()->json([
            'status' => 'Success',
            'message' => "User's role has been successfully updated to " . $validated['role']
        ], 200);
    }
}
