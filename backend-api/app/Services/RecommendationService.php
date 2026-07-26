<?php

namespace App\Services;

use App\Models\Topic;
use App\Models\TopicEngagement;
use App\Services\Recommendation\RecommendationClient;
use Illuminate\Support\Collection;

class RecommendationService
{
    public function __construct(protected RecommendationClient $ml)
    {
    }

    /**
     * Get recommended topics for a user. Returns:
     *   [
     *     'items'    => Collection<Topic>  (each with a ->similarity attribute attached)
     *     'fallback' => bool               (true = "latest discussions" cold-start fallback)
     *   ]
     */
    public function getRecommendationsForUser(int $userId, int $limit = 10): array
    {
        $seenIds = TopicEngagement::where('user_id', $userId)
            ->pluck('topic_id')
            ->unique()
            ->values()
            ->all();

        $recommendations = $seenIds ? $this->ml->recommendationsFor($seenIds, $limit) : [];

        if (empty($recommendations)) {
            $fallback = Topic::orderBy('created_at', 'desc')->take($limit)->get();
            $fallback->each(fn ($topic) => $topic->similarity = 0);

            return ['items' => $fallback, 'fallback' => true];
        }

        // ml-service only returns id/title/topic/similarity — hydrate full
        // Topic rows so the view gets group_id, description, ml_category, etc.,
        // while keeping the similarity-ranked order ml-service returned.
        $topicIds = collect($recommendations)->pluck('id');
        $topicsById = Topic::whereIn('topic_id', $topicIds)->get()->keyBy('topic_id');

        $ordered = collect($recommendations)
            ->map(function ($rec) use ($topicsById) {
                $topic = $topicsById->get($rec['id']);
                if (!$topic) {
                    return null; // e.g. topic was deleted after being indexed
                }
                $topic->similarity = $rec['similarity'];
                return $topic;
            })
            ->filter()
            ->values();

        return ['items' => $ordered, 'fallback' => false];
    }

    /**
     * Record that a user engaged with (viewed) a topic — feeds the
     * "seen topics" profile used by getRecommendationsForUser().
     */
    public function logEngagement(int $userId, int $topicId): void
    {
        TopicEngagement::create([
            'user_id'  => $userId,
            'topic_id' => $topicId,
        ]);
    }

    /**
     * Classify + index a newly created discussion. Best-effort — never
     * throws, since a discussion must always save successfully even if
     * ml-service happens to be unreachable at that moment.
     *
     * Returns the predicted category string, or null if classification
     * wasn't available.
     */
    public function classifyAndIndex(int $topicId, string $title, string $description = ''): ?string
    {
        $category = $this->ml->predictTopic($title, $description);
        $this->ml->indexDiscussion($topicId, $title, $description);
        return $category;
    }

    public function removeFromIndex(int $topicId): void
    {
        $this->ml->removeFromIndex($topicId);
    }
}
