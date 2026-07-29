<?php

namespace App\Http\Controllers;

use App\Models\Quiz;
use App\Models\QuizQuestion;
use App\Services\QuizService;
use Illuminate\Http\Request;
use App\Events\QuizStarted;
use App\Notifications\QuizPublishedNotification;
use App\Models\User;

class QuizController extends Controller
{
    protected QuizService $quizService;

    public function __construct(QuizService $quizService)
    {
        $this->quizService = $quizService;
    }

    /**
     * CREATE QUIZ (Lecturer only)
     */
    public function store(Request $request, $group)
    {
        $validated = $request->validate([
            'quiz_title'       => 'required|string|max:150',
            'quiz_date'        => 'required|date_format:Y-m-d',
            'start_time'       => 'required|date_format:H:i:s',
            'duration_minutes' => 'required|integer|min:1',
            'student_category' => 'required|string',
            'questions'        => 'required|array|min:1',
            'questions.*.qn_text'        => 'required|string',
            'questions.*.options'        => 'required|array|min:2',
            'questions.*.correct_option' => 'required|string|max:5',
            'questions.*.marks_worth'    => 'required|integer|min:1',
        ]);

        $groupId = is_object($group) ? ($group->group_id ?? $group->id) : (int)$group;

        $quiz = Quiz::create([
            'group_id'         => $groupId,
            'lecturer_id'      => $request->user()->id,
            'quiz_title'       => $validated['quiz_title'],
            'quiz_date'        => $validated['quiz_date'],
            'start_time'       => $validated['start_time'],
            'duration_minutes' => $validated['duration_minutes'],
            'student_category' => $validated['student_category'],
            'is_published'     => true,
        ]);

        foreach ($validated['questions'] as $qnData) {
            QuizQuestion::create([
                'quiz_id'        => $quiz->quiz_id,
                'qn_text'        => $qnData['qn_text'],
                'options'        => $qnData['options'],
                'correct_option' => $qnData['correct_option'],
                'marks_worth'    => $qnData['marks_worth'],
            ]);
        }

        $groupMembers = User::whereHas('groups', function($q) use ($groupId) {
            $q->where('groups.group_id', $groupId);
        })->where('id', '!=', $lecturerId)->get();

        foreach ($groupMembers as $member) {
            $member->notify(new QuizPublishedNotification($quiz, $quiz->group->group_name ?? 'your group'));
        }

        return response()->json([
            'status'  => 'Success',
            'message' => 'Quiz configured and published successfully!',
            'data'    => $quiz->load('questions')
        ], 201);
    }

    /**
     * LAUNCH QUIZ
     */
    public function launchQuiz(Request $request, $group, $quiz_id)
    {
        $quiz = Quiz::findOrFail($quiz_id);

        if ($quiz->lecturer_id !== $request->user()->id) {
            return response()->json([
                'status'  => 'Error',
                'message' => 'Unauthorized to launch this quiz.'
            ], 403);
        }

        broadcast(new QuizStarted($quiz));

        return response()->json([
            'status'  => 'Success',
            'message' => 'Quiz has been launched! Client screens are now locked down.',
            'data'    => $quiz
        ], 200);
    }

    /**
     * LIST ACTIVE QUIZZES (Uses QuizService)
     */
    public function index(Request $request, $group)
    {
        $groupId = is_object($group) ? ($group->group_id ?? $group->id) : (int)$group;
        $userId = $request->user()->id;

        $data = $this->quizService->getGroupQuizzes($groupId, $userId);

        return response()->json([
            'status' => 'Success',
            'data'   => $data
        ], 200);
    }

    /**
     * START ATTEMPT (Uses QuizService)
     */
    public function startAttempt(Request $request, $group, $quiz_id)
    {
        $groupId = is_object($group) ? ($group->group_id ?? $group->id) : (int)$group;
        $userId = $request->user()->id;

        $result = $this->quizService->startOrResumeAttempt($groupId, (int)$quiz_id, $userId);

        if (!$result['success']) {
            return response()->json([
                'status'  => 'Error',
                'message' => $result['message']
            ], 400);
        }

        // Hide correct options before delivering payload
        $questions = $result['questions']->map(function ($qn) {
            return [
                'quiz_qn_id'  => $qn->quiz_qn_id,
                'qn_text'     => $qn->qn_text,
                'options'     => $qn->options,
                'marks_worth' => $qn->marks_worth,
            ];
        });

        return response()->json([
            'status'            => 'Success',
            'message'           => 'Quiz started. Timer is now ticking.',
            'submission_id'     => $result['submission']->submission_id,
            'started_at'        => $result['submission']->started_at,
            'remaining_seconds' => $result['remainingSeconds'],
            'scheduled_end_time'=> $result['scheduledEndTime'],
            'questions'         => $questions
        ], 200);
    }

    /**
     * SUBMIT ATTEMPT (Uses QuizService)
     */
    public function submitAttempt(Request $request, $group, $quiz_id)
    {
        $validated = $request->validate([
            'answers'                   => 'required|array',
            'answers.*.quiz_qn_id'     => 'required|integer|exists:quiz_questions,quiz_qn_id',
            'answers.*.selected_option' => 'nullable|string',
        ]);

        $userId = $request->user()->id;

        // Reformat answers array from [{quiz_qn_id: 1, selected_option: "A"}] to [1 => "A"] for QuizService
        $formattedAnswers = [];
        foreach ($validated['answers'] as $ans) {
            $formattedAnswers[$ans['quiz_qn_id']] = $ans['selected_option'] ?? null;
        }

        $result = $this->quizService->submitQuizAttempt((int)$quiz_id, $userId, $formattedAnswers);

        if (!$result['success']) {
            return response()->json([
                'status'  => 'Error',
                'message' => $result['message']
            ], 400);
        }

        return response()->json([
            'status'      => 'Success',
            'message'     => $result['message'],
            'total_score' => $result['total_score']
        ], 200);
    }

    /**
     * VIEW RESULTS REPORT (Uses QuizService)
     */
    public function resultsReport(Request $request, $group, $quiz_id)
    {
        $groupId = is_object($group) ? ($group->group_id ?? $group->id) : (int)$group;
        $userId = $request->user()->id;

        $report = $this->quizService->getQuizReport($groupId, (int)$quiz_id, $userId);

        if (!$report['ready']) {
            return response()->json([
                'status'  => 'Error',
                'message' => $report['message']
            ], 403);
        }

        return response()->json([
            'status' => 'Success',
            'data'   => $report
        ], 200);
    }

    /**
     * SHOW QUIZ DETAILS (Lecturer overview with stats)
     */
    public function show(Request $request, $group, $quiz_id)
    {
        $groupId = is_object($group) ? ($group->group_id ?? $group->id) : (int)$groupId;

        $quiz = Quiz::where('group_id', $groupId)
            ->with('questions')
            ->findOrFail($quiz_id);

        $totalStudents = User::whereHas('groups', function($q) use ($groupId) {
            $q->where('groups.group_id', $groupId);
        })->where('role', 'student')->count();

        $submissions = \App\Models\StudentSubmission::where('quiz_id', $quiz_id)
            ->whereIn('status', ['submitted', 'auto-submitted'])
            ->get();

        $submittedCount = $submissions->count();
        $totalPossible = $quiz->questions->sum('marks_worth');

        $avgScore = 0;
        if ($submittedCount > 0 && $totalPossible > 0) {
            $avgScore = round(($submissions->avg('total_score') / $totalPossible) * 100);
        }

        return response()->json([
            'status' => 'Success',
            'data'   => [
                'quiz'            => $quiz,
                'total_students'  => $totalStudents,
                'submitted_count' => $submittedCount,
                'average_score'   => $avgScore
            ]
        ], 200);
    }
}