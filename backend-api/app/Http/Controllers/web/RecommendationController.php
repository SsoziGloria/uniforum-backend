<?php

namespace App\Http\Controllers\web;

use App\Http\Controllers\Controller;
use App\Models\Topic;
use App\Models\TopicEngagement;
use App\Services\RecommendationService;
use Illuminate\Support\Facades\Auth;

class RecommendationController extends Controller
{
    protected RecommendationService $recommendationService;

    public function __construct(RecommendationService $recommendationService)
    {
        $this->recommendationService = $recommendationService;
    }

    public function index()
    {
        $result = $this->recommendationService->getRecommendationsForUser(Auth::id(), 10);

        // Stats for the summary cards at the top of the page.
        // Note: "Topics Followed" is approximated as the number of distinct
        // categories among the current recommendations, since there's no
        // explicit "follow a topic" feature yet — flagging this as an
        // approximation, not a real follow-count.
        $recommendedToday = $result['items']->count();
        $topicsClassified = Topic::whereNotNull('ml_category')->distinct('ml_category')->count('ml_category');
        $discussionsRead  = TopicEngagement::where('user_id', Auth::id())->count();
        $topicsFollowed   = $result['items']->pluck('ml_category')->filter()->unique()->count();

        $suggestedCategories = Topic::whereNotNull('ml_category')
            ->distinct()
            ->orderBy('ml_category')
            ->pluck('ml_category');

        return view('student.recommendations.index', [
            'recommendations'     => $result['items'],
            'fallback'            => $result['fallback'],
            'recommendedToday'    => $recommendedToday,
            'topicsClassified'    => $topicsClassified,
            'discussionsRead'     => $discussionsRead,
            'topicsFollowed'      => $topicsFollowed,
            'suggestedCategories' => $suggestedCategories,
        ]);
    }
}
