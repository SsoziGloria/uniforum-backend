<?php

namespace App\Http\Middleware;

use Closure;
use Illuminate\Http\Request;
use App\Models\GroupMember;
use Symfony\Component\HttpFoundation\Response;

class EnsureUserIsLecturer
{
    /**
     * Handle an incoming request.
     */
    public function handle(Request $request, Closure $next): Response
    {
        $group = $request->route('group');
        $groupId = is_object($group) ? ($group->group_id ?? $group->id) : (int)$group;

        if (!$request->user()) {
            return response()->json([
                'status' => 'Error',
                'message' => 'Unauthenticated.'
            ], 401);
        }

        $userId = $request->user()->id;

        // GLOBAL BYPASS: If the user's account role is already 'lecturer',
        // grant immediate access without checking group pivot tables.
        if (isset($userId->role) && $userId->role === 'lecturer') {
                    return $next($request);
                }

        //Otherwise, Query the group_members table using your Eloquent Pivot model
        $membership = GroupMember::where('group_id', $groupId)
            ->where('user_id', $userId)
            ->first();

        //Only allow access if they have 'lecturer' role
        if (!$membership || $membership->role !== 'lecturer') {
            return response()->json([
                'status' => 'Error',
                'message' => 'Access Denied: Only designated group lecturers can manage quizzes.'
            ], 403);
        }

        return $next($request);
    }
}
