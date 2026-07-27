<?php

namespace App\Http\Controllers\Web;

use App\Services\DiscussionService;
use Illuminate\Http\Request;
use App\Http\Controllers\Controller;

class TopicController extends Controller
{
    protected DiscussionService $discussionService;

    public function __construct(DiscussionService $discussionService)
    {
        $this->discussionService = $discussionService;
    }

    public function index($groupId)
    {
        $data = $this->discussionService
            ->index(
                $groupId,
                auth()->id()
            );

        return view(
            'student.groups.discussions.index',
            $data
        );
    }

    public function create($groupId)
    {
        if (
            !$this->discussionService
                ->canCreateDiscussion(
                    $groupId,
                    auth()->id()
                )
        ) {
            abort(403);
        }

        return view(
            'student.groups.discussions.create',
            [
               'groupId' => $groupId
            ]
        );
    }

    public function store(
        Request $request,
        $groupId
    ) {
        $validated = $request->validate([
            'title'       => 'required|string|max:255',
            'description' => 'nullable|string',
            'ml_category' => 'nullable|string|max:100'
        ]);

        $result = $this->discussionService
            ->store(
                $validated,
                $groupId,
                auth()->id()
            );

        if (!$result['success']) {
            return back()
                ->withErrors($result['message'])
                ->withInput();
        }

        return redirect()
            ->route(
                'student.discussions.index',
                $groupId
            )
            ->with(
                'success',
                $result['message']
            );
    }

    public function show(
        $groupId,
        $topicId
    ) {
        $data = $this->discussionService
            ->show(
                $groupId,
                $topicId,
                auth()->id()
            );

        return view(
            'student.groups.discussions.show',
            $data
        );
    }

    public function exportPdf($groupId, $topicId)
    {
        return app(\App\Http\Controllers\TopicController::class)
           ->exportPdf($groupId, $topicId);
    }

    public function destroy($groupId, $topicId)
    {
        $result = $this->discussionService->deleteDiscussion(
            (int) $groupId,
            (int) $topicId,
            auth()->id()
        );

        if (!$result['success']) {
            abort(403, $result['message']);
        }

        return redirect()
            ->route('student.discussions.index', $groupId)
            ->with('success', $result['message']);
    }
}