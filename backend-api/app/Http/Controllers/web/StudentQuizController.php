<?php

namespace App\Http\Controllers\web;

use App\Http\Controllers\Controller;
use App\Services\QuizService;
use App\Models\Group;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

class StudentQuizController extends Controller
{
    protected QuizService $quizService;

    public function __construct(QuizService $quizService)
    {
        $this->quizService = $quizService;
    }

    /**
     * Quiz Dashboard for a specific group.
     */
    public function index($groupId)
    {
        $group = Group::findOrFail($groupId);
        $data = $this->quizService->getGroupQuizzes((int) $groupId, Auth::id());

        return view('student.quizzes.index', array_merge(['group' => $group], $data));
    }

    /**
     * Start/Resume Quiz view (Locks interface and sets strict time limits).
     */
    public function take($groupId, $quizId)
    {
        $group = Group::findOrFail($groupId);
        $result = $this->quizService->startOrResumeAttempt((int) $groupId, (int) $quizId, Auth::id());

        if (!$result['success']) {
            return redirect()
                ->route('student.groups.quizzes.index', $groupId)
                ->withErrors(['quiz' => $result['message']]);
        }

        return view('student.quizzes.take', [
            'group'            => $group,
            'quiz'             => $result['quiz'],
            'submission'       => $result['submission'],
            'remainingSeconds' => $result['remainingSeconds'],
            'questions'        => $result['questions']
        ]);
    }

    /**
     * Handle Web Quiz Submission.
     */
    public function submit(Request $request, $groupId, $quizId)
    {
        $answers = $request->input('answers', []);
        $result = $this->quizService->submitQuizAttempt((int) $quizId, Auth::id(), $answers);

        return redirect()
            ->route('student.groups.quizzes.report', ['group' => $groupId, 'quiz' => $quizId])
            ->with('success', $result['message']);
    }

    /**
     * Performance Report view for finished quizzes.
     */
    public function report($groupId, $quizId)
    {
        $group = Group::findOrFail($groupId);
        $reportData = $this->quizService->getQuizReport((int) $groupId, (int) $quizId, Auth::id());

        if (!$reportData['ready']) {
            return redirect()
                ->route('student.groups.quizzes.index', $groupId)
                ->with('info', $reportData['message']);
        }

        return view('student.quizzes.report', array_merge(['group' => $group], $reportData));
    }
}