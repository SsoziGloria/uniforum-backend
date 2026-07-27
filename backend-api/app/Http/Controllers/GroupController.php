<?php

namespace App\Http\Controllers;

use App\Models\Group;
use App\Models\GroupMember;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;
use App\Services\GroupService;
use App\Services\GroupStatisticsService;

class GroupController extends Controller
{
    protected GroupService $groupService;
    protected GroupStatisticsService $groupStatisticsService;


    public function __construct(
        GroupService $groupService,
        GroupStatisticsService $groupStatisticsService)
    {
        $this->groupService = $groupService;
        $this->groupStatisticsService = $groupStatisticsService;
    }
    /**
     * Display a listing of the resource.
     * Fetch only the groups the logged-in user belongs to.
     */
    public function index(Request $request)
    {
        $groups = $this->groupService
        ->getUserGroups($request->user()->id);


        return response()->json([
        'success' => true,
        'data' => array_values($groups->toArray())
        ],200);
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        $validated = $request->validate([
            'group_name' => 'required|string|max:255',
            'description' => 'nullable|string',
        ]);

        $group = $this->groupService->createGroup(
            $validated,
            $request->user()
        );


        return response()->json([
            'status'  => 'Success',
            'message' => 'Group created successfully!',
            'group'   => $group,
        ], 201);
    }
        /**
         * Browse and search all groups in the system.
         */
        public function search(Request $request)
        {
            $groups = $this->groupService->browseGroups(
            $request->query('search')
            );

            return response()->json([
               'success' => true,
               'data' => $groups
            ]);
        }

    /**
     * JOINING A GROUP
     * Join a group (with mandatory rules enforcement check)
     */
    public function join(Request $request, $groupId)
   {
        $validated = $request->validate([

            'rules_accepted' => 'required|accepted'

        ]);


        $group = Group::findOrFail($groupId);


        $result = $this->groupService->joinGroup(

            $group,

            $request->user()

        );


        if(!$result['success']){

            return response()->json([
               'status'=>'Error',
               'message'=>$result['message']

            ],400);

        }


        return response()->json([

            'status'=>'Success',

            'message'=>$result['message']

        ],200);
    }

    /**
     * Get a list of all verified members in a group.
     */
    public function getGroupMembers(Group $group): JsonResponse
    {
        return response()->json([
            'group_id'   => $group->group_id ?? $group->id,
            'group_name' => $group->group_name,
            'members'    => $group->members()->select('users.id', 'users.name')->get()
        ]);
    }

    public function show(Request $request, string $id)
    {
        $group = $this->groupService->getGroupDetails(
            (int) $id,
            $request->user()->id
        );

        return response()->json([
            'success' => true,
            'data' => $group,
        ]);
    }
    public function statistics($groupId)
    {
        $statistics = $this->groupStatisticsService
            ->getStatistics($groupId);

        return response()->json([
            'success' => true,
            'data' => $statistics
        ]);
    }
    /**
     * Delete an entire group (Creator only).
     */
    public function destroy(Request $request, string $id): JsonResponse
    {
        $group = Group::findOrFail($id);

        if ($group->created_by !== $request->user()->id) {
            return response()->json([
                'success' => false,
                'message' => 'Unauthorized. Only the group creator can delete this group.'
            ], 403);
        }

        $group->delete();

        return response()->json([
            'success' => true,
            'message' => 'Group deleted successfully.'
        ], 200);
    }

    /**
     * Leave a group (Member only).
     */
    public function leave(Request $request, string $id): JsonResponse
    {
        $userId = $request->user()->id;
        $group = Group::findOrFail($id);

        if ($group->created_by === $userId) {
            return response()->json([
                'success' => false,
                'message' => 'Group creators cannot leave their group. You must delete the group instead.'
            ], 400);
        }

        GroupMember::where('group_id', $id)
            ->where('user_id', $userId)
            ->delete();

        return response()->json([
            'success' => true,
            'message' => 'You have left the group.'
        ], 200);
    }
    public function update(Request $request, string $id) {}
    

}

