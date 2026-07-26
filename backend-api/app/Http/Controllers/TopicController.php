<?php

namespace App\Http\Controllers;

use App\Models\Topic;
use Illuminate\Http\Request;
use Barryvdh\DomPDF\Facade\Pdf;
use App\Services\DiscussionService;

class TopicController extends Controller
{
    protected DiscussionService $discussionService;

    public function __construct(DiscussionService $discussionService)
    {
        $this->discussionService = $discussionService;
    } 

    /**
     * Display a listing of the resource.
     */
    public function index($groupId)
    {
        $statistics = $this->discussionService
            ->index(
                $groupId,
                auth()->id()
            );

        return response()->json([
            'success' => true,
            'data'    => $statistics
        ], 200);
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request, $groupId)
    {
        $validated = $request->validate([
            'title'       => 'required|string|max:255',
            'description' => 'nullable|string|max:1000',
            'ml_category' => 'nullable|string|max:100',
        ]);

        $result = $this->discussionService->store(
            $validated,
            $groupId,
            $request->user()->id
        );

        if (!$result['success']) {
            return response()->json([
                'success' => false,
                'message' => $result['message']
            ], 403);
        }

        return response()->json([
            'success' => true,
            'message' => $result['message'],
            'data'    => $result['topic']
        ], 201);
    }

    public function exportPdf($group, $id)
    {
        $topic = Topic::where('group_id', $group)->where('topic_id', $id)->firstOrFail();
        
        $messages = $topic->messages()
            ->whereNull('parent_msg_id')
            ->with([
                'sender',
                'replies' => function ($query) {
                    $query->with(['sender', 'replies']);
                }
            ])
            ->orderBy('posted_at')
            ->get();

        $pdf = Pdf::loadView('pdf.topic', compact('topic', 'messages'));

        return $pdf->download("Discussion-{$id}-messages.pdf");
    }

    /**
     * Display the specified resource.
     */
    public function show($groupId, $topicId)
    {
        $discussion = $this->discussionService
            ->show(
                $groupId,
                $topicId,
                auth()->id()
            );

        return response()->json([
            'success' => true,
            'data'    => $discussion
        ]);
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
    public function destroy($groupId, $topicId)
    {
        $userId = auth()->id() ?? request()->user()?->id;

        $result = $this->discussionService->deleteDiscussion(
            (int) $groupId,
            (int) $topicId,
            (int) $userId
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
        ], 200);
    }
}