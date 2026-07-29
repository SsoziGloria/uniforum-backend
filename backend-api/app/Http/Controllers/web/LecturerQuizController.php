<?php

namespace App\Http\Controllers\web;

use App\Http\Controllers\Controller;
use App\Models\Group;
use App\Models\Quiz;
use App\Models\QuizQuestion;
use App\Models\StudentSubmission;
use App\Services\QuizService;
use App\Notifications\QuizPublishedNotification;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Carbon\Carbon;

class LecturerQuizController extends Controller
{
    protected QuizService $quizService;

    public function __construct(QuizService $quizService)
    {
        $this->quizService = $quizService;
    }

    /**
     * List all quizzes in a group.
     */
    public function index($groupId)
    {
        $group = Group::findOrFail($groupId);

        $quizzes = Quiz::where('group_id', $groupId)
            ->withCount('questions')
            ->orderBy('created_at', 'desc')
            ->get();

        $publishedCount = $quizzes->where('is_published', true)->count();
        $upcomingCount  = $quizzes->filter(function ($q) {
            $start = Carbon::parse($q->quiz_date . ' ' . $q->start_time);
            return $start->isFuture();
        })->count();

        // Calculate group-wide average score
        $submissions = StudentSubmission::whereIn('quiz_id', $quizzes->pluck('quiz_id'))
            ->whereIn('status', ['submitted', 'auto-submitted'])
            ->get();

        $avgScore = 0;
        if ($submissions->count() > 0) {
            $totalPossible = 0;
            $totalScored = 0;

            foreach ($submissions as $sub) {
                $q = $quizzes->firstWhere('quiz_id', $sub->quiz_id);
                if ($q) {
                    $possible = $q->questions()->sum('marks_worth');
                    if ($possible > 0) {
                        $totalScored += $sub->total_score;
                        $totalPossible += $possible;
                    }
                }
            }

            $avgScore = $totalPossible > 0 ? round(($totalScored / $totalPossible) * 100) : 0;
        }

        return view('lecturer.quizzes.index', compact('group', 'quizzes', 'publishedCount', 'upcomingCount', 'avgScore'));
    }

    /**
     * Show quiz creation form.
     */
    public function create($groupId)
    {
        $group = Group::findOrFail($groupId);
        return view('lecturer.quizzes.create', compact('group'));
    }

    /**
     * Store new quiz and questions.
     */
    public function store(Request $request, $groupId)
    {
        $group = Group::findOrFail($groupId);

        $validated = $request->validate([
            'quiz_title'       => 'required|string|max:150',
            'quiz_date'        => 'required|date',
            'start_time'       => 'required',
            'duration_minutes' => 'required|integer|min:1',
            'student_category' => 'required|string',
            'questions'        => 'required|array|min:1',
            'questions.*.qn_text'        => 'required|string',
            'questions.*.options'        => 'required|array|min:2',
            'questions.*.correct_option' => 'required|string',
            'questions.*.marks_worth'    => 'required|integer|min:1',
        ]);

        $quiz = Quiz::create([
            'group_id'         => $group->group_id ?? $group->id,
            'lecturer_id'      => Auth::id(),
            'quiz_title'       => $validated['quiz_title'],
            'quiz_date'        => $validated['quiz_date'],
            'start_time'       => $validated['start_time'],
            'duration_minutes' => $validated['duration_minutes'],
            'student_category' => $validated['student_category'],
            'is_published'     => true,
        ]);

        foreach ($validated['questions'] as $qn) {
            QuizQuestion::create([
                'quiz_id'        => $quiz->quiz_id,
                'qn_text'        => $qn['qn_text'],
                'options'        => $qn['options'],
                'correct_option' => $qn['correct_option'],
                'marks_worth'    => $qn['marks_worth'],
            ]);
        }

        // Notify Group Members
        $members = User::whereHas('groups', function($q) use ($group) {
            $q->where('groups.group_id', $group->group_id ?? $group->id);
        })->where('id', '!=', Auth::id())->get();

        foreach ($members as $member) {
            $member->notify(new QuizPublishedNotification($quiz, $group->group_name));
        }

        return redirect()
            ->route('lecturer.groups.quizzes.index', $group->group_id ?? $group->id)
            ->with('success', 'Quiz created and published successfully!');
    }

    /**
     * Show detailed quiz info and stats.
     */
    public function show($groupId, $quizId)
    {
        $group = Group::findOrFail($groupId);
        $quiz = Quiz::where('group_id', $groupId)->with('questions')->findOrFail($quizId);

        $totalStudents = User::whereHas('groups', function($q) use ($groupId) {
            $q->where('groups.group_id', $groupId);
        })->where('role', 'student')->count();

        $submissions = StudentSubmission::where('quiz_id', $quizId)
            ->whereIn('status', ['submitted', 'auto-submitted'])
            ->get();

        $submittedCount = $submissions->count();
        $totalPossible = $quiz->questions->sum('marks_worth');

        $avgScore = 0;
        if ($submittedCount > 0 && $totalPossible > 0) {
            $avgScore = round(($submissions->avg('total_score') / $totalPossible) * 100);
        }

        return view('lecturer.quizzes.show', compact('group', 'quiz', 'totalStudents', 'submittedCount', 'avgScore'));
    }

    /**
     * Show class-wide performance results for a quiz.
     */
    public function results($groupId, $quizId)
    {
        $group = Group::findOrFail($groupId);
        $quiz = Quiz::where('group_id', $groupId)->with('questions')->findOrFail($quizId);

        // Filter only student members (exclude lecturers)
        $students = User::whereHas('groups', function($q) use ($groupId) {
            $q->where('groups.group_id', $groupId);
        })
        ->where('role', 'student') // OR ->where('role', '!=', 'lecturer') 
        ->get();

        $totalStudents = $students->count();

        // Pull submissions only for these student IDs
        $submissions = StudentSubmission::where('quiz_id', $quizId)
            ->whereIn('student_id', $students->pluck('id'))
            ->get()
            ->keyBy('student_id');

        $submittedCount = $submissions->whereIn('status', ['submitted', 'auto-submitted'])->count();
        $completionRate = $totalStudents > 0 ? round(($submittedCount / $totalStudents) * 100) : 0;

        $totalPossible = $quiz->questions->sum('marks_worth');
        $avgScore = 0;
        if ($submittedCount > 0 && $totalPossible > 0) {
            $avgScore = round(($submissions->whereIn('status', ['submitted', 'auto-submitted'])->avg('total_score') / $totalPossible) * 100);
        }

        return view('lecturer.quizzes.results', compact(
            'group', 'quiz', 'students', 'submissions', 
            'totalStudents', 'submittedCount', 'avgScore', 
            'completionRate', 'totalPossible'
        ));
    }
}