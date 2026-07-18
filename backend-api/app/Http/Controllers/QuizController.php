<?php

namespace App\Http\Controllers;

use App\Models\Quiz;
use App\Models\QuizQuestion;
use App\Models\StudentSubmission;
use App\Models\StudentAnswer;
use Illuminate\Http\Request;
use Carbon\Carbon;
use App\Events\QuizStarted;

class QuizController extends Controller
{
    /**
     * CREATE QUIZ (Lecturer only)
     * Configures the quiz title, timing, category, and questions.
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
        $lecturerId = $request->user()->id;

        // Save the Quiz Configuration
        $quiz = Quiz::create([
            'group_id'         => $groupId,
            'lecturer_id'      => $lecturerId,
            'quiz_title'       => $validated['quiz_title'],
            'quiz_date'        => $validated['quiz_date'],
            'start_time'       => $validated['start_time'],
            'duration_minutes' => $validated['duration_minutes'],
            'student_category' => $validated['student_category'],
            'is_published'     => true, // Triggers visibility/announcements automatically
        ]);

        // Save the associated Questions
        foreach ($validated['questions'] as $qnData) {
            QuizQuestion::create([
                'quiz_id'        => $quiz->quiz_id,
                'qn_text'        => $qnData['qn_text'],
                'options'        => $qnData['options'],
                'correct_option' => $qnData['correct_option'],
                'marks_worth'    => $qnData['marks_worth'],
            ]);
        }

        return response()->json([
            'status'  => 'Success',
            'message' => 'Quiz configured and published successfully as an announcement!',
            'data'    => $quiz->load('questions')
        ], 201);
    }

/**
 * LAUNCH QUIZ (Triggers the WebSocket lockdown announcement)
 */
public function launchQuiz(Request $request, $group, $quiz_id)
{
    $quiz = Quiz::findOrFail($quiz_id);

    // only the lecturer can launch it
    if ($quiz->lecturer_id !== $request->user()->id) {
        return response()->json([
            'status'  => 'Error',
            'message' => 'Unauthorized to launch this quiz.'
        ], 403);
    }

    //WebSocket broadcast event to force screen takeover on client apps
    broadcast(new QuizStarted($quiz));

    return response()->json([
        'status'  => 'Success',
        'message' => 'Quiz has been launched! Client screens are now locked down.',
        'data'    => $quiz
    ], 200);
}

    /**
     * LIST ACTIVE QUIZZES (Student/Lecturer)
     * Returns published quizzes for a group.
     */
    public function index(Request $request, $group)
    {
        $groupId = is_object($group) ? ($group->group_id ?? $group->id) : (int)$group;

        $quizzes = Quiz::where('group_id', $groupId)
            ->where('is_published', true)
            ->orderBy('quiz_date', 'asc')
            ->orderBy('start_time', 'asc')
            ->get();

        return response()->json([
            'status' => 'Success',
            'data'   => $quizzes
        ], 200);
    }

    /**
     * START ATTEMPT (Student opens the quiz)
     * Enforces start-time window checks and starts the timer.
     */
    public function startAttempt(Request $request, $group, $quiz_id)
    {
        $studentId = $request->user()->id;
        $quiz = Quiz::findOrFail($quiz_id);

        // Check if an attempt already exists
        $existingSubmission = StudentSubmission::where('quiz_id', $quiz_id)
            ->where('student_id', $studentId)
            ->first();

        if ($existingSubmission) {
            return response()->json([
                'status'  => 'Error',
                'message' => 'You have already started or submitted this quiz.'
            ], 400);
        }

        $now = Carbon::now();
        $quizStartString = $quiz->quiz_date . ' ' . $quiz->start_time;
        $scheduledStartTime = Carbon::createFromFormat('Y-m-d H:i:s', $quizStartString);
        $scheduledEndTime = $scheduledStartTime->copy()->addMinutes($quiz->duration_minutes);

        // Enforce quiz time boundary
        if ($now->lt($scheduledStartTime)) {
            return response()->json([
                'status'  => 'Error',
                'message' => 'This quiz has not started yet.'
            ], 400);
        }

        if ($now->gt($scheduledEndTime)) {
            return response()->json([
                'status'  => 'Error',
                'message' => 'The quiz time has already expired. Late joiners are not permitted.'
            ], 403);
        }

        // "in-progress" attempt
        $submission = StudentSubmission::create([
            'quiz_id'      => $quiz_id,
            'student_id'   => $studentId,
            'started_at'   => $now,
            'status'       => 'in-progress',
            'total_score'  => null
        ]);

        // Return questions without the correct answers to prevent cheating!
        $questions = QuizQuestion::where('quiz_id', $quiz_id)
            ->select('quiz_qn_id', 'qn_text', 'options', 'marks_worth')
            ->get();

        return response()->json([
            'status'            => 'Success',
            'message'           => 'Quiz started. Timer is now ticking.',
            'submission_id'     => $submission->submission_id,
            'started_at'        => $submission->started_at,
            'duration_minutes'  => $quiz->duration_minutes,
            'questions'         => $questions
        ], 200);
    }

    /**
     * SUBMIT ATTEMPT (Manual or Automated Timeout)
     * Handles student submissions, grading logic, and auto-submit flags.
     */
    public function submitAttempt(Request $request, $group, $quiz_id)
    {
        $validated = $request->validate([
            'submission_id' => 'required|integer|exists:student_submissions,submission_id',
            'answers'       => 'required|array',
            'answers.*.quiz_qn_id'     => 'required|integer|exists:quiz_questions,quiz_qn_id',
            'answers.*.selected_option' => 'required|string',
        ]);

        $submission = StudentSubmission::findOrFail($validated['submission_id']);
        $quiz = Quiz::findOrFail($quiz_id);

        if ($submission->status !== 'in-progress') {
            return response()->json([
                'status'  => 'Error',
                'message' => 'This submission has already been finalized.'
            ], 400);
        }

        $now = Carbon::now();
        $quizStartString = $quiz->quiz_date . ' ' . $quiz->start_time;
        $scheduledStartTime = Carbon::createFromFormat('Y-m-d H:i:s', $quizStartString);
        $scheduledEndTime = $scheduledStartTime->copy()->addMinutes($quiz->duration_minutes);

        // Determine if submission is late
        $isLate = $now->gt($scheduledEndTime);
        $status = $isLate ? 'auto-submitted' : 'submitted';

        $totalScore = 0;

        // Process each answer and grade it
        foreach ($validated['answers'] as $ansData) {
            $question = QuizQuestion::where('quiz_id', $quiz_id)
                ->where('quiz_qn_id', $ansData['quiz_qn_id'])
                ->first();

            if (!$question) continue;

            $isCorrect = (trim($question->correct_option) === trim($ansData['selected_option']));
            if ($isCorrect) {
                $totalScore += $question->marks_worth;
            }

            StudentAnswer::create([
                'submission_id'   => $submission->submission_id,
                'quiz_qn_id'      => $ansData['quiz_qn_id'],
                'selected_option' => $ansData['selected_option'],
                'is_correct'      => $isCorrect,
            ]);
        }

        // Save
        $submission->update([
            'submitted_at' => $now,
            'status'       => $status,
            'total_score'  => $totalScore
        ]);

        return response()->json([
            'status'      => 'Success',
            'message'     => $isLate ? 'Time expired. Your progress was automatically saved.' : 'Quiz submitted successfully!',
            'total_score' => $totalScore
        ], 200);
    }

    /**
     * VIEW RESULTS REPORT (Available to everyone once duration ends)
     */
    public function resultsReport(Request $request, $group, $quiz_id)
    {
        $quiz = Quiz::findOrFail($quiz_id);

        $quizStartString = $quiz->quiz_date . ' ' . $quiz->start_time;
        $scheduledStartTime = Carbon::createFromFormat('Y-m-d H:i:s', $quizStartString);
        $scheduledEndTime = $scheduledStartTime->copy()->addMinutes($quiz->duration_minutes);

        // Block viewing results early
        if (Carbon::now()->lt($scheduledEndTime)) {
            return response()->json([
                'status'  => 'Error',
                'message' => 'Results will be visible once the quiz duration officially ends.'
            ], 403);
        }

        // Retrieve student grades for the performance report
        $submissions = StudentSubmission::where('quiz_id', $quiz_id)
            ->with('student:id,name,email')
            ->orderBy('total_score', 'desc')
            ->get();

        return response()->json([
            'status'     => 'Success',
            'quiz_title' => $quiz->quiz_title,
            'results'    => $submissions
        ], 200);
    }
}
