# ml-service — Recommendation & Topic Classification API

The Python microservice that Laravel talks to for machine learning. Laravel
never touches the model directly — it only calls this API.

## 1. Setup

```bash
cd ml-service
python -m venv venv
source venv/bin/activate        # Windows: venv\Scripts\activate
pip install -r requirements.txt
```

Copy the two files your training script saved into `ml-service/models/`:
```
models/topic_classifier.joblib
models/tfidf_vectorizer.joblib
```

## 2. Run it

```bash
uvicorn api.app:app --reload --port 8000
```

Visit `http://localhost:8000/docs` — FastAPI auto-generates an interactive
Swagger page where you can try every endpoint from the browser, no Postman
needed.

## 3. Endpoints

### `GET /health`
Quick check that the service is up and how many discussions are indexed.

### `POST /predict-topic`
Classify a discussion's topic. Call this whenever a discussion is created,
before you index it.
```bash
curl -X POST http://localhost:8000/predict-topic \
  -H "Content-Type: application/json" \
  -d '{"title": "How do I connect to MySQL using SQLAlchemy?", "body": "Getting connection refused."}'
```
```json
{ "topic": "Database" }
```

### `POST /index-discussion`
Adds a discussion to the recommendation index. **Call this right after
saving any new discussion in Laravel** — this is how the recommender
learns about your real forum content over time instead of relying on the
Stack Overflow training data.
```bash
curl -X POST http://localhost:8000/index-discussion \
  -H "Content-Type: application/json" \
  -d '{"id": 1, "title": "Middleware in Laravel", "body": "How does middleware work?"}'
```
```json
{ "id": 1, "topic": "Web Development", "indexed": true }
```

### `DELETE /index-discussion/{id}`
Remove a discussion from the index (e.g. when it's deleted in Laravel).

### `POST /recommendations`
Given the discussion IDs a user has already engaged with, return the most
similar discussions they haven't seen yet.
```bash
curl -X POST http://localhost:8000/recommendations \
  -H "Content-Type: application/json" \
  -d '{"seen_ids": [1, 3, 7], "top_n": 5}'
```
```json
[
  { "id": 12, "title": "Eloquent Relationships Explained", "topic": "Web Development", "similarity": 0.41 }
]
```

**Cold-start note:** if `seen_ids` is empty, or none of them are indexed
yet, this returns `[]`. Laravel should treat an empty response as "show
popular/latest discussions instead" — see the fallback logic discussed
earlier. Once a user has a few logged interactions, this starts returning
real personalized results.

## 4. Calling it from Laravel

In `app/Services/Recommendation/RecommendationClient.php`:

```php
<?php

namespace App\Services\Recommendation;

use Illuminate\Support\Facades\Http;

class RecommendationClient
{
    protected string $baseUrl;
    protected ?string $apiKey;

    public function __construct()
    {
        $this->baseUrl = config('ml_service.base_url'); // e.g. http://localhost:8000
        $this->apiKey  = config('ml_service.api_key');  // null in local dev
    }

    protected function client()
    {
        return Http::baseUrl($this->baseUrl)
            ->when($this->apiKey, fn ($http) => $http->withHeaders(['X-API-Key' => $this->apiKey]))
            ->timeout(5);
    }

    public function predictTopic(string $title, string $body = ''): string
    {
        $response = $this->client()->post('/predict-topic', [
            'title' => $title,
            'body'  => $body,
        ]);

        return $response->json('topic');
    }

    public function indexDiscussion(int $id, string $title, string $body = ''): array
    {
        return $this->client()->post('/index-discussion', [
            'id'    => $id,
            'title' => $title,
            'body'  => $body,
        ])->json();
    }

    public function recommendationsFor(array $seenIds, int $topN = 5): array
    {
        return $this->client()->post('/recommendations', [
            'seen_ids' => $seenIds,
            'top_n'    => $topN,
        ])->json() ?? [];
    }
}
```

And `config/ml_service.php`:
```php
<?php

return [
    'base_url' => env('ML_SERVICE_URL', 'http://localhost:8000'),
    'api_key'  => env('ML_SERVICE_API_KEY'),
];
```

Add to `.env`:
```
ML_SERVICE_URL=http://localhost:8000
ML_SERVICE_API_KEY=
```

## 5. Where to hook these calls into Laravel

- **On discussion creation** (`DiscussionService::createPost()`): call
  `predictTopic()` to tag it, then `indexDiscussion()` so it becomes
  recommendable immediately.
- **On discussion deletion**: call `DELETE /index-discussion/{id}`.
- **On the "Recommended for you" widget**: pull the user's recently viewed
  discussion IDs from your engagement log table, call
  `recommendationsFor($seenIds)`, and fall back to "latest/popular in your
  topics" if the response is empty.

## 6. Production notes (not needed yet, just so you know it's coming)

- Set `ML_SERVICE_API_KEY` to a real secret once this is deployed anywhere
  reachable outside localhost.
- `index_store.joblib` is a flat file — fine for a university-scale forum,
  but if this ever needs to run across multiple server instances, move the
  index into a real database table instead.
