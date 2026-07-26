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
| Protected Routes (Sanctum Auth)
|--------------------------------------------------------------------------
*/
Route::middleware(['auth:sanctum'])->group(function () {

    // --- Authentication & Account ---
    Route::post('/logout', [AuthController::class, 'logout']);
    Route::get('/profile', [AuthController::class, 'profile']);
    Route::put('/profile', [AuthController::class, 'updateProfile']);

    // --- Private Channel Broadcasting Auth ---
    Route::post('/broadcasting/auth', function (Request $request) {
        $request->setUserResolver(fn () => auth('sanctum')->user());
        return Broadcast::auth($request);
    });

    // --- General Utilities ---
    Route::get('/messages/sync', [MessageController::class, 'sync']);

    Route::get('/lecturer/students', [StudentPerformanceController::class, 'index'])
    ->middleware('lecturer')
    ->name('api.lecturer.students.index');

    Route::get('/recommendations', [RecommendationController::class, 'index']);


    /*
    |----------------------------------------------------------------------
    | Groups Management (Global / General)
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
    | Group Scope (Requires Group Membership)
    |----------------------------------------------------------------------
    */
    Route::middleware(['group.member'])->prefix('groups/{group}')->group(function () {

        // --- Group Overview & Stats ---
        Route::get('/statistics', [GroupController::class, 'statistics']);
        Route::get('/participation/results', [ParticipationController::class, 'results']);
        Route::get('/participation/roster', [ParticipationController::class, 'groupRoster']); // <-- ADDED HERE

        // --- Group Members Management ---
        Route::get('/members', [GroupController::class, 'getGroupMembers']);
        Route::put('/members/{user}/role', [GroupMemberController::class, 'updateRole']);
        Route::post('/members/{user}/warning', [GroupMemberController::class, 'issueWarning']);
        Route::post('/members/{user}/blacklist', [GroupMemberController::class, 'blacklist']);
        Route::post('/members/{user}/reinstate', [GroupMemberController::class, 'reinstate']);
        
    Route::post('/members/{user}/promote', [GroupMemberController::class, 'promoteMember']);
    Route::post('/members/{user}/demote', [GroupMemberController::class, 'demoteMember']);

        // --- General Group Chat Messages ---
        Route::get('/messages', [MessageController::class, 'getMessages']);
        Route::post('/messages', [MessageController::class, 'store']);

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

        // --- Quizzes ---
        Route::prefix('quizzes')->group(function () {
            Route::get('/', [QuizController::class, 'index']);
            Route::post('/', [QuizController::class, 'store'])->middleware('lecturer');
            Route::post('/{quiz}/launch', [QuizController::class, 'launchQuiz']);
            Route::post('/{quiz}/start', [QuizController::class, 'startAttempt']);
            Route::post('/{quiz}/submit', [QuizController::class, 'submitAttempt']);
            Route::get('/{quiz}/results', [QuizController::class, 'resultsReport']);
        });

    });

});