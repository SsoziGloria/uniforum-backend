<?php

namespace App\Http\Controllers\Web;

use App\Http\Controllers\Controller;
use App\Services\GroupService;
use App\Models\Group;
use Illuminate\Support\Facades\Auth;
use Illuminate\Http\Request;
use App\Services\ParticipationService;
use App\Services\MessageService;
use App\Services\GroupMemberService;
use App\Services\GroupStatisticsService;
use App\Models\ParticipationCriteria;

class LecturerGroupController extends Controller
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

    /**
     * Display all groups the lecturer belongs to or oversees.
     */
    public function index()
    {
        $groups = $this->groupService->getUserGroups(Auth::id());

        return view('lecturer.groups.index', compact('groups'));
    }

    /**
     * Search & browse all groups system-wide.
     */
    public function browse(Request $request)
    {
        $groups = $this->groupService->browseGroups($request->search);

        $joinedGroupIds = \App\Models\GroupMember::where('user_id', Auth::id())
            ->pluck('group_id')
            ->toArray();

        return view('lecturer.groups.browse', compact('groups', 'joinedGroupIds'));
    }

    /**
     * Form to create a new group.
     */
    public function create()
    {
        return view('lecturer.groups.create');
    }

    /**
     * Store newly created group.
     */
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
            ->route('lecturer.groups.index')
            ->with('success', 'Group created successfully!');
    }

    /**
     * Display a specific group detail page.
     */
    public function show($groupId)
    {
        $data = $this->groupService->getGroupDetails($groupId, Auth::id());

        return view('lecturer.groups.show', $data);
    }

    /**
     * Display join group confirmation page for lecturer.
     */
    public function join($groupId)
    {
        $group = Group::findOrFail($groupId);

        return view('lecturer.groups.join', compact('group'));
    }

    /**
     * Store lecturer membership join request.
     */
    public function storeJoin(Request $request, $groupId)
    {
        $request->validate([
            'rules_accepted' => 'required|accepted'
        ], [
            'rules_accepted.required' => 'You must accept the group guidelines before joining.',
            'rules_accepted.accepted' => 'You must accept the group guidelines before joining.'
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
            ->route('lecturer.groups.show', $groupId)
            ->with('success', $result['message']);
    }

    public function chat($groupId)
    {
        $group = Group::findOrFail($groupId);
        $messages = $this->messageService->getGroupMessages($groupId, Auth::id());
        $members = $this->groupMemberService->getGroupMembers($groupId);

        return view('lecturer.groups.chat', compact('group', 'messages', 'members'));
    }

    public function sendMessage(Request $request, $groupId)
    {
        $validated = $request->validate([
            'msg_txt'             => 'required|string',
            'is_restricted'       => 'required|boolean',
            'excluded_user_ids'   => 'nullable|array',
            'excluded_user_ids.*' => 'integer'
        ]);

        $this->messageService->sendMessage($groupId, Auth::id(), $validated);

        return redirect()
            ->route('lecturer.groups.chat', $groupId)
            ->with('success', 'Message sent successfully.');
    }

    public function destroyChatMessage($groupId, $messageId)
    {
        $result = $this->messageService->deleteMessage((int) $groupId, (int) $messageId, Auth::id());

        if (!$result['success']) {
            abort(403, $result['message']);
        }

        return back()->with('success', $result['message']);
    }
    public function members($groupId)
    {
        $group = Group::findOrFail($groupId);

        $membership = \App\Models\GroupMember::where('group_id', $groupId)
            ->where('user_id', Auth::id())
            ->first();

        if (!$membership || !in_array($membership->role, ['admin', 'lecturer'])) {
            abort(403, 'Only group administrators or lecturers can manage members.');
        }

        $members = $this->groupMemberService->getGroupMembers($groupId);

        return view('lecturer.groups.members', compact('group', 'members'));
    }

    public function issueWarning($groupId, $userId)
    {
        $this->verifyAdminOrLecturer($groupId);
        $this->groupMemberService->issueWarning($groupId, $userId);

        return back()->with('success', 'Warning issued.');
    }

    public function blacklist($groupId, $userId)
    {
        $this->verifyAdminOrLecturer($groupId);
        $this->groupMemberService->blacklistMember($groupId, $userId);

        return back()->with('success', 'Member blacklisted.');
    }

    public function reinstate($groupId, $userId)
    {
        $this->verifyAdminOrLecturer($groupId);
        $this->groupMemberService->reinstateMember($groupId, $userId);

        return back()->with('success', 'Member reinstated.');
    }

    public function promoteMember($groupId, $userId)
    {
        $this->verifyAdminOrLecturer($groupId);
        $this->groupMemberService->changeMemberRole(
            $groupId,
            Auth::id(),
            $userId,
            'admin'
        );

        return back()->with('success', 'Member promoted to group admin.');
    }

    private function verifyAdminOrLecturer($groupId)
    {
        $membership = \App\Models\GroupMember::where('group_id', $groupId)
            ->where('user_id', Auth::id())
            ->first();

        if (!$membership || !in_array($membership->role, ['admin', 'lecturer'])) {
            abort(403, 'Only group administrators or lecturers can perform this action.');
        }
    }

    public function statistics($groupId, GroupStatisticsService $statisticsService)
    {
        $group = Group::findOrFail($groupId);

        // Fetch statistics from the dedicated service
        $stats = $statisticsService->getStatistics($groupId);

        return view('lecturer.groups.statistics', array_merge([
            'group' => $group,
        ], $stats));
    }

    public function participation($groupId, ParticipationService $participationService)
    {
        $group = Group::findOrFail($groupId);

        $criteria = $participationService->getCriteriaSummary();
        $roster = $participationService->getGroupParticipationRoster($groupId);

        return view('lecturer.groups.participation.index', compact('group', 'criteria', 'roster'));
    }

    public function participationSettings($groupId, ParticipationService $participationService)
    {
        $group = Group::findOrFail($groupId);
        $criteria = $participationService->getCriteriaSummary();

        return view('lecturer.groups.participation.settings', compact('group', 'criteria'));
    }

    public function storeCriterion(Request $request, $groupId)
    {
        $validated = $request->validate([
            'criterion_name' => 'required|string|max:255',
            'activity_type' => 'required|string|max:50',
            'points'        => 'required|integer|min:1',

        ]);

        ParticipationCriteria::create([
            'criterion_name' => $validated['criterion_name'],
            'activity_type' => $validated['activity_type'],
            'points'        => $validated['points'],
        ]);

        return back()->with('success', 'New participation criterion added successfully.');
    }

    public function updateCriteria(Request $request, $groupId)
    {
        $validated = $request->validate([
            'criteria'          => 'required|array',
            'criteria.*.points' => 'required|integer|min:0',
        ]);

        foreach ($validated['criteria'] as $id => $data) {
            ParticipationCriteria::where('criterion_id', $id)->update([
                'points' => $data['points'],
            ]);
        }

        return back()->with('success', 'Participation criteria updated successfully.');
    }

    public function destroyCriterion($groupId, $criterionId)
    {
        ParticipationCriteria::destroy($criterionId);
        ParticipationCriteria::where('criterion_id', $criterionId)->delete();

        return back()->with('success', 'Criterion removed.');
    }

    public function demoteMember($groupId, $userId)
    {
        $this->verifyAdminOrLecturer($groupId);

        $result = $this->groupMemberService->changeMemberRole(
            (int) $groupId,
            Auth::id(),
            (int) $userId,
            'member'
        );

        if (!$result['success']) {
            return back()->withErrors(['group' => $result['message']]);
        }

        return back()->with('success', 'User demoted to standard member successfully.');
    }


}