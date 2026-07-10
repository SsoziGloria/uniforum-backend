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
           $creatorId = $request->user()->user_id;

        //Insert the group and get the new auto-incremented ID
           $groupId = DB::table('groups')->insertGetId([
            'group_name'  => $validated['group_name'],
            'description' => $validated['description'] ?? null,
            'created_by'  => $creatorId, // Tracks who owns the group
            'created_at'  => now(),
            'updated_at'  => now(),
                ]);

             //AUTOMATIC ASSIGNMENT: Link the creator to the group immediately
               DB::table('group_members')->insert([
                 'group_id' => $groupId,
                 'user_id'  => $creatorId,
                 ]);

                 return response()->json([
                     'status'  => 'Success',
                     'message' => 'Group created and creator automatically joined successfully!',
                     'group'   => [
                     'group_id'   => $groupId,
                     'group_name' => $validated['group_name'],
                     'description' => $validated['description'] ?? null,
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
             $groupIdClean = is_object($groupId) ? $groupId->group_id : $groupId;
            // Find the user by email
            $userToAdd = DB::table('users')->where('email', $validated['email'])->first();

            if (!$userToAdd) {
                return response()->json([
                    'status'  => 'Error',
                    'message' => 'No student found with that email address.'
                ], 404);
            }

            // Check if they are already in the group
            $alreadyMember = DB::table('group_members')
                ->where('group_id', $groupIdClean)
                ->where('user_id', $userToAdd->user_id)
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
                'user_id'  => $userToAdd->user_id
            ]);

            return response()->json([
                'status'  => 'Success',
                'message' => "Successfully added {$userToAdd->user_name} to the group!"
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
}
