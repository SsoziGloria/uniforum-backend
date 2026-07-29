<?php

namespace App\Http\Controllers\web;

use App\Http\Controllers\Controller;
use App\Models\Message;
use App\Models\Quiz;
use App\Models\Topic;
use App\Services\RecommendationService;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Schema;

class StudentDashboardController extends Controller
{
    public function index(Request $request, RecommendationService $recommendationService)
    {
        $user = $request->user();

        // 1. Get user's group IDs safely
        $userGroupIds = method_exists($user, 'groups') 
            ? $user->groups()->pluck('groups.group_id') 
            : collect();

        // 2. Questions / Topics asked by this student
        $questionsAskedCount = Topic::where('created_by', $user->id)->count();

        // 3. Topics available across student's enrolled groups
        $topicsFollowingCount = Topic::whereIn('group_id', $userGroupIds)->count();

        // 4. Pending Quizzes in student groups
        $pendingQuizzesCount = Quiz::whereIn('group_id', $userGroupIds)
            ->where('quiz_date', '>=', now()->toDateString())
            ->count();

        // 5. Compute participation score
        $totalUserMessages = Message::where('sender_id', $user->id)->count();
        $participationScore = min(100, max(10, ($totalUserMessages * 5) + ($questionsAskedCount * 10)));

        // 6. Inspect table columns dynamically to prevent missing column SQL errors
        $msgColumns = Schema::getColumnListing('messages');
        $timeColumn = in_array('created_at', $msgColumns) ? 'created_at' : (in_array('sent_at', $msgColumns) ? 'sent_at' : null);
        $orderColumn = $timeColumn ?? (in_array('msg_id', $msgColumns) ? 'msg_id' : 'id');

        // Fetch recent messages using detected column
        $recentMessages = Message::where('sender_id', $user->id)
            ->orderBy($orderColumn, 'desc')
            ->take(5)
            ->get()
            ->map(function ($msg) use ($timeColumn) {
                $rawTime = $timeColumn ? $msg->{$timeColumn} : null;
                $formattedTime = $rawTime ? \Carbon\Carbon::parse($rawTime)->diffForHumans() : 'Recently';

                return [
                    'title' => 'Replied: "' . \Str::limit($msg->msg_txt ?? $msg->message ?? 'Message', 35) . '"',
                    'time'  => $formattedTime,
                    'icon'  => '💬',
                ];
            });

        // Fetch recent topics
        $topicColumns = Schema::getColumnListing('topics');
        $topicOrder = in_array('created_at', $topicColumns) ? 'created_at' : (in_array('topic_id', $topicColumns) ? 'topic_id' : 'id');

        $recentTopics = Topic::where('created_by', $user->id)
            ->orderBy($topicOrder, 'desc')
            ->take(3)
            ->get()
            ->map(function ($topic) {
                $formattedTime = isset($topic->created_at) 
                    ? \Carbon\Carbon::parse($topic->created_at)->diffForHumans() 
                    : 'Recently';

                return [
                    'title' => 'Asked: "' . \Str::limit($topic->title, 35) . '"',
                    'time'  => $formattedTime,
                    'icon'  => '❓',
                ];
            });

        $recentActivity = $recentMessages->concat($recentTopics)
            ->take(5);

        // 7. AI-recommended topics for the "Recommended Topics" card.
        // Best-effort: getRecommendationsForUser() never throws even if
        // ml-service is down — it just falls back to latest topics.
        $recommendedTopics = $recommendationService
            ->getRecommendationsForUser($user->id, 3)['items'];

        return view('student.dashboard', compact(
            'questionsAskedCount',
            'participationScore',
            'topicsFollowingCount',
            'pendingQuizzesCount',
            'recentActivity',
            'recommendedTopics'
        ));
    }
}
