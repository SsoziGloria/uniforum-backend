<?php

namespace App\Http\Controllers;

use App\Services\RecommendationService;
use Illuminate\Http\Request;

class RecommendationController extends Controller
{
    protected RecommendationService $recommendationService;

    public function __construct(RecommendationService $recommendationService)
    {
        $this->recommendationService = $recommendationService;
    }

    /**
     * GET /api/recommendations?limit=N
     * Used by the Java desktop app (and any other API consumer).
     * Same response contract as the Web version, just as JSON:
     *   { "success": true, "fallback": false, "data": [ {topic fields..., similarity}, ... ] }
     */
    public function index(Request $request)
    {
        $limit = (int) $request->query('limit', 10);

        $result = $this->recommendationService->getRecommendationsForUser(
            $request->user()->id,
            $limit
        );

        return response()->json([
            'success'  => true,
            'fallback' => $result['fallback'],
            'data'     => $result['items'],
        ], 200);
    }
}
