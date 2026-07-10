<?php

namespace App\Http\Middleware;

use Closure;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Symfony\Component\HttpFoundation\Response;

class CheckGroupMembership
{
    /**
     * Handle an incoming request.
     *
     * @param  Closure(Request): (Response)  $next
     */
    public function handle(Request $request, Closure $next): Response
    {
   /*
     dd([
             'URL_Group_ID' => $request->route('group')->group_id ?? $request->route('group'),
             'Auth_User_ID' => $request->user()->user_id
         ]);
         */
        //Get the authenticated student
        $user = $request->user();

        //Extract the group ID parameter from the API URL route
        $group = $request->route('group');
        $groupId = is_object($group) ? $group->group_id : $group;

        // Safety check to ensure a user is logged in and a group parameter exists
        if (!$user || !$groupId) {
            return response()->json(['error' => 'Unauthorized or invalid route execution.'], 401);
        }

        //Query the group_members table using custom user_id primary key
        $isMember = DB::table('group_members')
            ->where('group_id', $groupId)
            ->where('user_id', $user->user_id)
            ->exists();

        // If they are not a member of this specific group, deny access
        if (!$isMember) {
            return response()->json([
                'status' => 'Error',
                'error' => 'Access Denied. You are not a verified member of this group.'
            ], 403);
        }

        return $next($request);
    }
}
