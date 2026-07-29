<?php

namespace App\Http\Controllers\web;

use App\Http\Controllers\Controller;
use App\Services\GroupService;
use App\Models\Group;
use Illuminate\Support\Facades\Auth;
use Illuminate\Http\Request;
use App\Services\ParticipationService;
use App\Services\MessageService;
use App\Services\GroupMemberService;
use App\Services\GroupStatisticsService;
use App\Notifications\GroupChatMessageNotification;

class StudentGroupController extends Controller
{
    protected GroupService $groupService;
    protected ParticipationService $participationService;
    protected MessageService $messageService;
    protected GroupMemberService $groupMemberService;
    protected GroupStatisticsService $groupStatisticsService;

    public function __construct(
        GroupService $groupService,
        ParticipationService $participationService,
        MessageService $messageService,
        GroupMemberService $groupMemberService,
        GroupStatisticsService $groupStatisticsService
    ) {
        $this->groupService = $groupService;
        $this->participationService = $participationService;
        $this->messageService = $messageService;
        $this->groupMemberService = $groupMemberService;
        $this->groupStatisticsService = $groupStatisticsService;
    }

    public function index()
    {
        $groups = $this->groupService->getUserGroups(Auth::id());

        return view('student.groups.index', compact('groups'));
    }

    public function browse(Request $request)
    {
        $groups = $this->groupService->browseGroups($request->search);

        // Fetch array of group_ids where current user is a member
        $joinedGroupIds = \App\Models\GroupMember::where('user_id', Auth::id())
            ->pluck('group_id')
            ->toArray();

        return view('student.groups.browse', compact('groups', 'joinedGroupIds'));
    }

    public function create()
    {
        return view('student.groups.create');
    }

    public function store(Request $request)
    {
        $validated = $request->validate([
            'group_name'  => 'required|string|max:255',
            'description' => 'nullable|string',
        ]);

        $this->groupService->createGroup(
            $validated,
            Auth::user()
        );

        return redirect()
            ->route('student.groups.index')
            ->with('success', 'Group created successfully!');
    }

    public function show($groupId)
    {
        $data = $this->groupService->getGroupDetails($groupId, Auth::id());

        return view('student.groups.show', $data);
    }

    public function join($groupId)
    {
        $group = Group::findOrFail($groupId);

        return view('student.groups.join', compact('group'));
    }

    public function storeJoin(Request $request, $groupId)
    {
        $request->validate([
            'rules_accepted' => 'required|accepted'
        ], [
            'rules_accepted.required' => 'You must accept the group rules before joining.',
            'rules_accepted.accepted' => 'You must accept the group rules before joining.'
        ]);

        $group = Group::findOrFail($groupId);

        $result = $this->groupService->joinGroup(
            $group,
            Auth::user()
        );

        if (!$result['success']) {
            return back()->withErrors(['group' => $result['message']]);
        }

        return redirect()
            ->route('student.groups.show', $groupId)
            ->with('success', $result['message']);
    }

    public function participationResults($groupId)
    {
        $results = $this->participationService->getParticipationResults(
            $groupId,
            Auth::id()
        );

        $group = Group::findOrFail($groupId);

        return view(
            'student.groups.participation.results',
            compact('results', 'group')
        );
    }

    public function chat($groupId)
    {
        $group = Group::findOrFail($groupId);

        $messages = $this->messageService->getGroupMessages(
            $groupId,
            Auth::id()
        );

        $members = $this->groupMemberService->getGroupMembers($groupId);

        return view(
            'student.groups.chat',
            compact('group', 'messages', 'members')
        );
    }

    public function sendMessage(Request $request, $groupId)
    {
        $validated = $request->validate([
            'msg_txt'             => 'required|string',
            'is_restricted'       => 'required|boolean',
            'excluded_user_ids'   => 'nullable|array',
            'excluded_user_ids.*' => 'integer'
        ]);

        $this->messageService->sendMessage(
            $groupId,
            Auth::id(),
            $validated
        );

        $otherMembers = $group->members()->where('users.id', '!=', auth()->id())->get();
        foreach ($otherMembers as $member) {
            $member->notify(new GroupChatMessageNotification($group, $chatMessage, auth()->user()->name));
        }

        return redirect()
            ->route('student.groups.chat', $groupId)
            ->with('success', 'Message sent successfully.');
    }

    public function members($groupId)
    {
        $group = Group::findOrFail($groupId);

        $membership = \App\Models\GroupMember::where('group_id', $groupId)
            ->where('user_id', Auth::id())
            ->first();

        if (!$membership || $membership->role !== 'admin') {
            abort(403, 'Only group administrators can manage members.');
        }

        $members = $this->groupMemberService->getGroupMembers($groupId);

        return view('student.groups.members', compact('group', 'members'));
    }

    public function issueWarning($groupId, $userId)
    {
        $this->verifyAdmin($groupId);
        $this->groupMemberService->issueWarning($groupId, $userId);

        return back()->with('success', 'Warning issued.');
    }

    public function blacklist($groupId, $userId)
    {
        $this->verifyAdmin($groupId);
        $this->groupMemberService->blacklistMember($groupId, $userId);

        return back()->with('success', 'Member blacklisted.');
    }

    public function reinstate($groupId, $userId)
    {
        $this->verifyAdmin($groupId);
        $this->groupMemberService->reinstateMember($groupId, $userId);

        return back()->with('success', 'Member reinstated.');
    }

    public function promoteMember($groupId, $userId)
    {
        $this->verifyAdmin($groupId);
        $this->groupMemberService->changeMemberRole(
            $groupId,
            Auth::id(),
            $userId,
            'admin'
        );

        return back()->with('success', 'Member promoted to group admin.');
    }

    private function verifyAdmin($groupId)
    {
        $membership = \App\Models\GroupMember::where('group_id', $groupId)
            ->where('user_id', Auth::id())
            ->first();

        if (!$membership || $membership->role !== 'admin') {
            abort(403, 'Only group administrators can perform this action.');
        }
    }

    public function statistics($groupId)
    {
        $group = Group::findOrFail($groupId);

        $statistics = $this->groupStatisticsService->getStatistics($groupId);

        return view(
            'student.groups.statistics',
            array_merge(['group' => $group], $statistics)
        );
    }

    public function leave($groupId)
    {
        $userId = Auth::id();

        $group = Group::findOrFail($groupId);

        if ($group->created_by === $userId) {
            return back()->withErrors(['group' => 'As the creator, you cannot leave the group. You must delete it instead.']);
        }

        \App\Models\GroupMember::where('group_id', $groupId)
            ->where('user_id', $userId)
            ->delete();

        return redirect()
            ->route('student.groups.index')
            ->with('success', 'You have left the group.');
    }

    public function destroy($groupId)
    {
        $group = Group::findOrFail($groupId);

        $membership = \App\Models\GroupMember::where('group_id', $groupId)
            ->where('user_id', Auth::id())
            ->first();

        if ($group->created_by !== Auth::id() && (!$membership || $membership->role !== 'admin')) {
            abort(403, 'Only the group owner or an admin can delete this group.');
        }

        $group->delete();

        return redirect()
            ->route('student.groups.index')
            ->with('success', 'Group deleted successfully.');
    }

    /**
     * Delete a chat message (Delegated to MessageService).
     */
    public function destroyChatMessage($groupId, $messageId)
    {
        $result = $this->messageService->deleteMessage(
            (int) $groupId,
            (int) $messageId,
            Auth::id()
        );

        if (!$result['success']) {
            abort(403, $result['message']);
        }

        return back()->with('success', $result['message']);
    }

    public function demoteMember($groupId, $userId)
    {
        $this->verifyAdmin($groupId);

        $result = $this->groupMemberService->changeMemberRole(
            (int) $groupId,
            Auth::id(),
            (int) $userId,
            'member'
        );

        if (!$result['success']) {
            return back()->withErrors(['group' => $result['message']]);
        }

        return back()->with('success', 'Admin status removed; user demoted to member.');
    }
}