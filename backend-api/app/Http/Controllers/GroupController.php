<?php

namespace App\Http\Controllers;

use App\Models\Group;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use App\Models\GroupMember;
use Illuminate\Http\JsonResponse;

class GroupController extends Controller
{
    /**
     * Display a listing of the resource.
     */


   // Fetch only the groups the logged-in student belongs to using GroupMember model
   public function index(Request $request)
   {
       //Get the authenticated student's ID
       $userId = $request->user()->user_id;

       //Fetch memberships for this user along with their related group details
       $memberships = GroupMember::with('group')
           ->where('user_id', $userId)
           ->get();

       //Extract just the group objects out of the memberships collection
       $myGroups = $memberships->map(function ($membership) {
           return $membership->group;
       })->filter(); // filter() ensures no null values if a group was missing

       //Return the clean JSON response
       return response()->json([
           'success' => true,
           'data'    => array_values($myGroups->toArray())
       ], 200);
   }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
       //Validate the group creation data
          $validated = $request->validate([
          'group_name' => 'required|string|max:255',
          'description'=> 'nullable|string',
              ]);

        // Get the authenticated user creating the group
           $creatorId = $request->user()->id;

        //Insert the group details
           $groupId = DB::table('groups')->insertGetId([
            'group_name'  => $validated['group_name'],
            'description' => $validated['description'] ?? null,
            'created_by'  => $creatorId, // Tracks who owns the group
            'created_at'  => now(),
            'updated_at'  => now(),
                ]);

             //AUTOMATIC ASSIGNMENT: Link the creator to the group immediately and make him admin
               DB::table('group_members')->insert([
                 'group_id' => $groupId,
                 'user_id'  => $creatorId,
                 'role'     => 'admin' ,
                 ]);

                 return response()->json([
                     'status'  => 'Success',
                     'message' => 'Group created and creator automatically joined successfully!',
                     'group'   => [
                     'group_id'   => $groupId,
                     'group_name' => $validated['group_name'],
                     'description' => $validated['description'] ?? null,
                     'created_by' => $creatorId,
                     'created_at' => now()
                     ]
                 ], 201);
             }

    //ADD ANOTHER STUDENT TO GROUP USING EMAIL
        public function addMember(Request $request, $groupId)
        {
            $validated = $request->validate([
                'email' => 'required|email'
            ]);
             $groupIdClean = is_object($groupId) ? $groupId->id : $groupId;
            // Find the user by email
            $userToAdd = DB::table('users')->where('email', $validated['email'])->first();

            if (!$userToAdd) {
                return response()->json([
                    'status'  => 'Error',
                    'message' => 'No student found with that email address.'
                ], 404);
            }
        $authenticatedUserId = $request->user()->id;
        // check if person adding member is an admin
            $isAdmin = DB::table('group_members')
                ->where('group_id', $groupIdClean)
                ->where('user_id', $authenticatedUserId)
                ->where('role', 'admin')
                ->exists();

            if (!$isAdmin) {
                return response()->json([
                    'status'  => 'Error',
                    'message' => 'Unauthorized. Only group administrators can add new members.'
                ], 403);
            }

            // Check if they are already in the group
            $alreadyMember = DB::table('group_members')
                ->where('group_id', $groupIdClean)
                ->where('user_id', $userToAdd->id)
                ->exists();

            if ($alreadyMember) {
                return response()->json([
                    'status'  => 'Error',
                    'message' => 'This student is already a registered member of this group.'
                ], 422);
            }

            // Add them to the group
            DB::table('group_members')->insert([
                'group_id' => $groupIdClean,
                'user_id'  => $userToAdd->id
            ]);

            return response()->json([
                'status'  => 'Success',
                'message' => "Successfully added {$userToAdd->name} to the group!"
            ], 200);
        }


             // Get a list of all verified members in a group.
            public function getMembers(Group $group): JsonResponse
            {
               return response()->json([
                       'group_id' => $group->group_id,
                       'group_name' => $group->group_name, // If you have a name column
                       'members' => $group->members()->select('users.user_id', 'users.user_name')->get()
                   ]);
            }
    /**
     * Display the specified resource.
     */
    public function show(string $id)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, string $id)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(string $id)
    {
        //
    }
//Allows  a group admin to promote a regular member to an admin or demote them back down
public function changeMemberRole(Request $request, $groupId)
{
    // Validate the incoming request fields
    $validated = $request->validate([
        'user_id' => 'required|integer',
        'role'    => 'required|string|in:admin,member' // Validates against enum options
    ]);

    $groupIdClean = is_object($groupId) ? $groupId->id : $groupId;
    $authenticatedUserId = $request->user()->id;

    // Security Check: Is the logged-in user an ADMIN of this specific group
    $isAdmin = DB::table('group_members')
        ->where('group_id', $groupIdClean)
        ->where('user_id', $authenticatedUserId)
        ->where('role', 'admin')
        ->exists();

    if (!$isAdmin) {
        return response()->json([
            'status'  => 'Error',
            'message' => 'Unauthorized. Only group administrators can manage member roles.'
        ], 403); // 403 Forbidden
    }

    //Validation Check: Is the user we want to change actually a member of this group?
    $isTargetMember = DB::table('group_members')
        ->where('group_id', $groupIdClean)
        ->where('user_id', $validated['user_id'])
        ->exists();

    if (!$isTargetMember) {
        return response()->json([
            'status'  => 'Error',
            'message' => 'The specified student is not a member of this group.'
        ], 422); // 422 Unprocessable Entity
    }

    // Everything is okay, update the role in the pivot table
    DB::table('group_members')
        ->where('group_id', $groupIdClean)
        ->where('user_id', $validated['user_id'])
        ->update([
            'role' => $validated['role']
        ]);

    return response()->json([
        'status'  => 'Success',
        'message' => "Member role updated to {$validated['role']} successfully!"
    ], 200);
}

  //REMOVING A MEMBER FROM THE GROUP
public function removeMember(Request $request, $groupId)
{
    // Validate the incoming request
    $validated = $request->validate([
        'user_id' => 'required|integer'
    ]);

    $groupIdClean = is_object($groupId) ? $groupId->id : $groupId;
    $authenticatedUserId = $request->user()->id;

    //Check if the logged-in user an admin of this group
    $isAdmin = DB::table('group_members')
        ->where('group_id', $groupIdClean)
        ->where('user_id', $authenticatedUserId)
        ->where('role', 'admin')
        ->exists();

    if (!$isAdmin) {
        return response()->json([
            'status'  => 'Error',
            'message' => 'Unauthorized. Only group administrators can remove members.'
        ], 403);
    }

    //Prevent an admin from accidentally kicking themselves out
    if ($validated['user_id'] == $authenticatedUserId) {
        return response()->json([
            'status'  => 'Error',
            'message' => 'You cannot remove yourself from the group. Pass management to another admin first.'
        ], 422);
    }

    // Delete the user's record from the pivot table
    $deleted = DB::table('group_members')
        ->where('group_id', $groupIdClean)
        ->where('user_id', $validated['user_id'])
        ->delete();

    if (!$deleted) {
        return response()->json([
            'status'  => 'Error',
            'message' => 'The specified student is not a member of this group.'
        ], 422);
    }

    return response()->json([
        'status'  => 'Success',
        'message' => 'Student removed from the academic group successfully.'
    ], 200);
}
}
