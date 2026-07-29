<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Broadcast;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\AuthController;
use App\Http\Controllers\GroupController;
use App\Http\Controllers\GroupMemberController;
use App\Http\Controllers\TopicController;
use App\Http\Controllers\MessageController;
use App\Http\Controllers\QuizController;
use App\Http\Controllers\ParticipationController;
use App\Http\Controllers\StudentPerformanceController;
use App\Http\Controllers\RecommendationController;

/*
|--------------------------------------------------------------------------
| Public Auth Routes
|--------------------------------------------------------------------------
*/
Route::post('/login', [AuthController::class, 'login']);
Route::post('/register', [AuthController::class, 'register']);


/*
|--------------------------------------------------------------------------
| Protected Routes (Sanctum Auth Required)
|--------------------------------------------------------------------------
*/
Route::middleware(['auth:sanctum'])->group(function () {

    // --- Authentication & Account Management ---
    Route::post('/logout', [AuthController::class, 'logout']);
    Route::get('/profile', [AuthController::class, 'profile']);
    Route::put('/profile', [AuthController::class, 'updateProfile']);

    // --- Private Channel Broadcasting Auth ---
    Route::post('/broadcasting/auth', function (Request $request) {
        $request->setUserResolver(fn () => auth('sanctum')->user());
        return Broadcast::auth($request);
    });

    // --- Offline Data Syncing & Analytics ---
    Route::get('/messages/sync', [MessageController::class, 'sync']);
    Route::get('/recommendations', [RecommendationController::class, 'index']);

    // --- Lecturer Overall Performance Dashboard ---
    Route::get('/lecturer/students', [StudentPerformanceController::class, 'index'])
        ->middleware('lecturer')
        ->name('api.lecturer.students.index');


    /*
    |----------------------------------------------------------------------
    | Groups Management (Global Search, Create, Join/Leave)
    |----------------------------------------------------------------------
    */
    // Note: 'search' MUST come before '{group}' to prevent dynamic binding conflicts
    Route::get('/groups/search', [GroupController::class, 'search']);
    Route::get('/groups', [GroupController::class, 'index']);
    Route::post('/groups', [GroupController::class, 'store']);
    Route::get('/groups/{group}', [GroupController::class, 'show']);
    Route::delete('/groups/{group}', [GroupController::class, 'destroy']);
    Route::post('/groups/{id}/join', [GroupController::class, 'join']);
    Route::delete('/groups/{group}/leave', [GroupController::class, 'leave']);


    /*
    |----------------------------------------------------------------------
    | Group-Scoped Operations (Requires Group Membership)
    |----------------------------------------------------------------------
    */
    Route::middleware(['group.member'])->prefix('groups/{group}')->group(function () {

        // --- Group Analytics & Statistics ---
        Route::get('/statistics', [GroupController::class, 'statistics']);


        // --- Participation Marks & Grading Criteria ---
        Route::prefix('participation')->group(function () {
            Route::get('/results', [ParticipationController::class, 'results']);
            Route::get('/roster', [ParticipationController::class, 'groupRoster']);

            // Added: Lecturer Participation Criteria Configuration
            Route::get('/settings', [ParticipationController::class, 'settings'])->middleware('lecturer');
            Route::post('/settings', [ParticipationController::class, 'storeCriterion'])->middleware('lecturer');
            Route::put('/settings', [ParticipationController::class, 'updateCriteria'])->middleware('lecturer');
            Route::delete('/settings/{criterion}', [ParticipationController::class, 'destroyCriterion'])->middleware('lecturer');
        });


        // --- Group Members & Moderation ---
        Route::get('/members', [GroupController::class, 'getGroupMembers']);
        Route::put('/members/{user}/role', [GroupMemberController::class, 'updateRole']);
        Route::post('/members/{user}/promote', [GroupMemberController::class, 'promoteMember']);
        Route::post('/members/{user}/demote', [GroupMemberController::class, 'demoteMember']);
        Route::post('/members/{user}/warning', [GroupMemberController::class, 'issueWarning']);
        Route::post('/members/{user}/blacklist', [GroupMemberController::class, 'blacklist']);
        Route::post('/members/{user}/reinstate', [GroupMemberController::class, 'reinstate']);


        // --- General Group Chat Messages ---
        Route::get('/messages', [MessageController::class, 'getMessages']);
        Route::post('/messages', [MessageController::class, 'store']);
        Route::delete('/messages/{message}', [MessageController::class, 'destroyChatMessage']); // Added: Delete general message


        // --- Topics / Forum Threads ---
        Route::prefix('topics')->group(function () {
            Route::get('/', [TopicController::class, 'index']);
            Route::post('/', [TopicController::class, 'store']);
            Route::get('/{topic}', [TopicController::class, 'show']);
            Route::delete('/{topic}', [MessageController::class, 'destroyTopic']);
            Route::get('/{id}/export', [TopicController::class, 'exportPdf']);

            // --- Topic Discussion Messages & Answers ---
            Route::prefix('{topic}/messages')->group(function () {
                Route::post('/', [MessageController::class, 'store']);
                Route::post('/{message}/reply', [MessageController::class, 'reply']);
                Route::post('/{message}/answer', [MessageController::class, 'markAnswer']);
                Route::post('/{message}/upvote', [MessageController::class, 'upvote']);
                Route::delete('/{message}', [MessageController::class, 'destroy']);
            });
        });


        // --- Quizzes & Assessments ---
        Route::prefix('quizzes')->group(function () {
            Route::get('/', [QuizController::class, 'index']);
            Route::post('/', [QuizController::class, 'store'])->middleware('lecturer');
            
            // Added Lecturer & Student Quiz Management Routes
            Route::get('/{quiz}', [QuizController::class, 'show'])->middleware('lecturer'); // View individual quiz details
            Route::post('/{quiz}/launch', [QuizController::class, 'launchQuiz'])->middleware('lecturer');
            
            // Results & Reports
            Route::get('/{quiz}/results', [QuizController::class, 'resultsReport']); // Handles both or can be restricted
            
            // Student Attempt Routes
            Route::post('/{quiz}/start', [QuizController::class, 'startAttempt']);
            Route::post('/{quiz}/submit', [QuizController::class, 'submitAttempt']);
        });
    });

});
