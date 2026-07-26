<?php

namespace App\Services\Recommendation;

use Illuminate\Support\Facades\Http;
use Illuminate\Support\Facades\Log;

class RecommendationClient
{
    protected string $baseUrl;
    protected ?string $apiKey;
    protected int $timeout;

    public function __construct()
    {
        $this->baseUrl = config('ml_service.base_url');
        $this->apiKey  = config('ml_service.api_key');
        $this->timeout = config('ml_service.timeout', 5);
    }

    protected function client()
    {
        return Http::baseUrl($this->baseUrl)
            ->when($this->apiKey, fn ($http) => $http->withHeaders(['X-API-Key' => $this->apiKey]))
            ->timeout($this->timeout);
    }

    /**
     * Ask ml-service to classify a discussion's subject category.
     * Returns null (instead of throwing) if ml-service is unreachable —
     * callers should treat null as "no prediction available yet," not as
     * a fatal error. Discussion creation must never fail because of this.
     */
    public function predictTopic(string $title, string $body = ''): ?string
    {
        try {
            $response = $this->client()->post('/predict-topic', [
                'title' => $title,
                'body'  => $body,
            ]);

            return $response->successful() ? $response->json('topic') : null;
        } catch (\Throwable $e) {
            Log::warning('ml-service predictTopic failed: ' . $e->getMessage());
            return null;
        }
    }

    /**
     * Add a discussion to the recommendation index. Same "never throw"
     * policy as above — indexing is best-effort.
     */
    public function indexDiscussion(int $id, string $title, string $body = ''): bool
    {
        try {
            $response = $this->client()->post('/index-discussion', [
                'id'    => $id,
                'title' => $title,
                'body'  => $body,
            ]);

            return $response->successful();
        } catch (\Throwable $e) {
            Log::warning('ml-service indexDiscussion failed: ' . $e->getMessage());
            return false;
        }
    }

    /**
     * Remove a discussion from the recommendation index (e.g. on delete).
     */
    public function removeFromIndex(int $id): bool
    {
        try {
            $response = $this->client()->delete("/index-discussion/{$id}");
            return $response->successful();
        } catch (\Throwable $e) {
            Log::warning('ml-service removeFromIndex failed: ' . $e->getMessage());
            return false;
        }
    }

    /**
     * Get content-based recommendations for a user given the discussion IDs
     * they've already engaged with. Returns an empty array (never throws)
     * if ml-service is unreachable or nothing is recommendable yet —
     * callers should fall back to "latest/popular discussions" in that case.
     *
     * @return array<int, array{id:int, title:string, topic:string, similarity:float}>
     */
    public function recommendationsFor(array $seenIds, int $topN = 10): array
    {
        try {
            $response = $this->client()->post('/recommendations', [
                'seen_ids' => $seenIds,
                'top_n'    => $topN,
            ]);

            return $response->successful() ? ($response->json() ?? []) : [];
        } catch (\Throwable $e) {
            Log::warning('ml-service recommendationsFor failed: ' . $e->getMessage());
            return [];
        }
    }
}
