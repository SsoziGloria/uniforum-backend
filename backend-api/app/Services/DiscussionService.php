<?php

namespace App\Services;

use App\Models\Group;
use App\Models\GroupMember;
use App\Models\Topic;

class DiscussionService
{
    protected RecommendationService $recommendationService;

    public function __construct(RecommendationService $recommendationService)
    {
        $this->recommendationService = $recommendationService;
    }

    /**
     * Retrieve discussions for a group.
     */
    public function index(int $groupId, int $userId)
    {
        $group = Group::findOrFail($groupId);

        $membership = GroupMember::where('group_id', $groupId)
            ->where('user_id', $userId)
            ->first();

        $search = request('search');

        $topicsQuery = Topic::with([
                'group',
                'creator'
            ])
            ->withCount('messages')
            ->where('group_id', $groupId);

        if ($search) {
            $topicsQuery->where(function ($query) use ($search) {
                $query->where('title', 'LIKE', "%{$search}%")
                    ->orWhere('description', 'LIKE', "%{$search}%")
                    ->orWhere('ml_category', 'LIKE', "%{$search}%");
            });
        }

        $topics = $topicsQuery->latest('created_at')->get();

        $popularCategories = Topic::where('group_id', $groupId)
            ->whereNotNull('ml_category')
            ->selectRaw('ml_category, COUNT(*) as total')
            ->groupBy('ml_category')
            ->orderByDesc('total')
            ->get();

        // AI-recommended discussions for the "Recommended for you" strip
        // at the top of the Discussions page. Best-effort: never throws,
        // falls back to latest discussions if ml-service is unreachable
        // or the user has no engagement history yet.
        $recommendedDiscussions = $this->recommendationService
            ->getRecommendationsForUser($userId, 4)['items'];

        return [
            'group'                  => $group,
            'topics'                 => $topics,
            'popularCategories'      => $popularCategories,
            'isMember'               => $membership !== null,
            'userRole'               => $membership?->role,
            'recommendedDiscussions' => $recommendedDiscussions,
        ];
    }

    /**
     * Determine whether a user can create a discussion.
     */
    public function canCreateDiscussion(int $groupId, int $userId)
    {
        return GroupMember::where('group_id', $groupId)
            ->where('user_id', $userId)
            ->exists();
    }

    /**
     * Store a new discussion.
     */
    public function store(array $data, int $groupId, int $userId)
    {
        if (!$this->canCreateDiscussion($groupId, $userId)) {
            return [
                'success' => false,
                'message' => 'Only group members can create discussions.'
            ];
        }

        $topic = Topic::create([
            'group_id'    => $groupId,
            'title'       => $data['title'],
            'description' => $data['description'] ?? null,
            'ml_category' => $data['ml_category'] ?? null,
            'created_by'  => $userId,
            'created_at'  => now(),
        ]);

        // If ml_category wasn't explicitly provided by the caller, ask
        // ml-service to classify it. Either way, index the discussion so
        // it becomes recommendable. Both calls are best-effort — they
        // never throw, so discussion creation always succeeds even if
        // ml-service happens to be down.
        if (empty($data['ml_category'])) {
            $predictedCategory = $this->recommendationService->classifyAndIndex(
                $topic->topic_id,
                $topic->title,
                $topic->description ?? ''
            );
            if ($predictedCategory) {
                $topic->update(['ml_category' => $predictedCategory]);
            }
        } else {
            $this->recommendationService->classifyAndIndex(
                $topic->topic_id,
                $topic->title,
                $topic->description ?? ''
            );
        }

        return [
            'success' => true,
            'message' => 'Discussion created successfully.',
            'topic'   => $topic,
        ];
    }

    /**
     * Retrieve one discussion.
     */
    public function show(int $groupId, int $topicId, int $userId)
    {
        $topic = Topic::with([
                'creator',
                'acceptedAnswer'
            ])
            ->withCount('messages')
            ->where('group_id', $groupId)
            ->findOrFail($topicId);

        $membership = GroupMember::where('group_id', $groupId)
            ->where('user_id', $userId)
            ->first();

        $messages = $topic->messages()
            ->whereNull('parent_msg_id')
            ->with([
                'sender',
                'replies' => function ($query) {
                    $query->with(['sender', 'replies']);
                }
            ])
            ->withCount([
                'votes',
                'replies'
            ])
            ->orderBy('posted_at')
            ->get();

        // Log this view — feeds the "seen discussions" profile that
        // powers this user's future recommendations. Best-effort, never
        // throws.
        $this->recommendationService->logEngagement($userId, $topicId);

        return [
            'topic'    => $topic,
            'group'    => $topic->group,
            'messages' => $messages,
            'isMember' => $membership !== null,
            'userRole' => $membership?->role,
        ];
    }

    /**
     * Delete a discussion topic (Creator or Admin).
     */
    public function deleteDiscussion(int $groupId, int $topicId, int $userId): array
    {
        $topic = Topic::where('group_id', $groupId)
            ->where('topic_id', $topicId)
            ->firstOrFail();

        $membership = GroupMember::where('group_id', $groupId)
            ->where('user_id', $userId)
            ->first();

        $isAdmin = $membership && $membership->role === 'admin';

        if ($topic->created_by !== $userId && !$isAdmin) {
            return [
                'success' => false,
                'message' => 'Only the discussion creator or an admin can delete this discussion.'
            ];
        }

        // Remove from the recommendation index before deleting, so it
        // stops being suggested to anyone.
        $this->recommendationService->removeFromIndex($topicId);

        // Cascade delete using boot observer in Topic model
        $topic->delete();

        return [
            'success' => true,
            'message' => 'Discussion topic deleted successfully.'
        ];
    }
}
