<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\GroupMember;
use App\Services\GroupMemberService;

class GroupMemberController extends Controller
{
    protected GroupMemberService $groupMemberService;

    public function __construct(GroupMemberService $groupMemberService)
    {
        $this->groupMemberService = $groupMemberService;
    }
    /**
     * Update a group member's role.
     * Accessible by both 'admin' and 'lecturer' roles.
     */
    public function updateRole(Request $request, $group, $user)
    {
        $validated = $request->validate([
            'role' => 'required|string|in:admin,lecturer,member'
        ]);

        $result = $this->groupMemberService->changeMemberRole(
            $group,
            $request->user()->id,
            $user,
            $validated['role']
        );

        if (!$result['success']) {
            return response()->json([
                'status' => 'Error',
                'message' => $result['message']
            ], 403);
        }

        return response()->json([
            'status' => 'Success',
            'message' => $result['message']
        ]);
    }

    /**
     * Issue warning to member
     */
    public function issueWarning($group,$user)
    {

        $result = $this->groupMemberService
            ->issueWarning(
                $group,
                $user
            );


        return response()->json($result);

    }

    /**
     * Blacklist member
     */
    public function blacklist($group,$user)
    {

        $result = $this->groupMemberService
            ->blacklistMember(
                $group,
                $user
            );


        return response()->json($result);

    }

    /**
     * Reinstate member
     */
    public function reinstate($group,$user)
    {

        $result = $this->groupMemberService
            ->reinstateMember(
                $group,
                $user
            );


        return response()->json($result);

    }
    /**
    * Promote a group member to administrator.
    */
    public function promoteMember($group, $user)
   {

        $result = $this->groupMemberService
           ->changeMemberRole(
               $group,
               request()->user()->id,
               $user,
               'admin'
            );


        if(!$result['success']){

            return response()->json([
               'success'=>false,
               'message'=>$result['message']
            ],403);

        }


        return response()->json([
            'success'=>true,
            'message'=>'Member promoted to group administrator successfully.'
        ]);

    }

    /**
     * Demote a group administrator to standard member status.
     */
    public function demoteMember(Request $request, $group, $user)
    {
        $result = $this->groupMemberService->changeMemberRole(
            (int) $group,
            $request->user()->id,
            (int) $user,
            'member'
        );

        if (!$result['success']) {
            return response()->json([
                'success' => false,
                'message' => $result['message']
            ], 403);
        }

        return response()->json([
            'success' => true,
            'message' => $result['message']
        ]);
    }



}
