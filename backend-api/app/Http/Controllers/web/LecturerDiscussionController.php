<?php

namespace App\Http\Controllers\Web;

use App\Http\Controllers\Controller;
use App\Services\DiscussionService;
use App\Services\MessageService;
use App\Models\Group;
use App\Models\Topic;
use Illuminate\Http\Request;

class LecturerDiscussionController extends Controller
{
    protected DiscussionService $discussionService;
    protected MessageService $messageService;

    public function __construct(
        DiscussionService $discussionService,
        MessageService $messageService
    ) {
        $this->discussionService = $discussionService;
        $this->messageService = $messageService;
    }

    /**
     * List group discussions for lecturers.
     */
    public function index($groupId)
    {
        $data = $this->discussionService->index($groupId, auth()->id());

        return view('lecturer.groups.discussions.index', $data);
    }

    /**
     * Display form to create a new discussion topic.
     */
    public function create($groupId)
    {
        $group = Group::findOrFail($groupId);

        return view('lecturer.groups.discussions.create', compact('group'));
    }

    /**
     * Store a lecturer-created discussion topic.
     */
    public function store(Request $request, $groupId)
    {
        $validated = $request->validate([
            'title'       => 'required|string|max:255',
            'description' => 'nullable|string',
            'ml_category' => 'nullable|string|max:100',
        ]);

        // Override group membership check for lecturers if necessary
        $topic = Topic::create([
            'group_id'    => $groupId,
            'title'       => $validated['title'],
            'description' => $validated['description'] ?? null,
            'ml_category' => $validated['ml_category'] ?? null,
            'created_by'  => auth()->id(),
            'created_at'  => now(),
        ]);

        return redirect()
            ->route('lecturer.groups.discussions.index', $groupId)
            ->with('success', 'Discussion created successfully.');
    }

    /**
     * Show a discussion topic thread for lecturers.
     */
    public function show($groupId, $topicId)
    {
        $data = $this->discussionService->show((int)$groupId, (int)$topicId, auth()->id());

        return view('lecturer.groups.discussions.show', $data);
    }

    /**
     * Delete a discussion topic (Lecturers always have admin rights).
     */
    public function destroy($groupId, $topicId)
    {
        $topic = Topic::where('group_id', $groupId)
            ->where('topic_id', $topicId)
            ->firstOrFail();

        $topic->delete();

        return redirect()
            ->route('lecturer.groups.discussions.index', $groupId)
            ->with('success', 'Discussion topic deleted successfully.');
    }

    public function exportPdf($groupId, $topicId)
    {
        return app(\App\Http\Controllers\TopicController::class)
           ->exportPdf($groupId, $topicId);
    }
}