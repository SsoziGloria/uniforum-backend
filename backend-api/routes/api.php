<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\GroupController;
use App\Http\Controllers\TopicController;
use App\Http\Controllers\MessageController;
use App\Http\Controllers\AuthController;
use App\Http\Controllers\QuizController;
/*
|--------------------------------------------------------------------------
| Public Authentication Routes
|--------------------------------------------------------------------------
*/
Route::post('/login', [AuthController::class, 'login']);
Route::post('/register', [AuthController::class, 'register']);


/*
|--------------------------------------------------------------------------
| Protected Forum Routes (Requires Auth )
|--------------------------------------------------------------------------
*/
Route::middleware(['auth:sanctum'])->group(function () {

  // This allows authenticated users to securely authorize their private channels.
      Route::post('/broadcasting/auth', function (\Illuminate\Http\Request $request) {
              // Force the broadcaster to use the user authenticated by Sanctum
              $request->setUserResolver(fn () => auth('sanctum')->user());

              return Broadcast::auth($request);
          });

  // OFFLINE SYNC ROUTE:
      Route::get('/messages/sync', [MessageController::class, 'sync']);

    // --- GROUPS MANAGEMENT ---
    // Get all groups the student belongs to
    Route::get('/groups', [GroupController::class, 'index']);
    // Create a new academic group (if allowed)
    Route::post('/groups', [GroupController::class, 'store']);


/*
      ----------------------------------------------------
      Requires Auth &  Group Membership Verification
      ----------------------------------------------------
*/
    // Add member endpoint (only current group members can add others)

    Route::middleware(['group.member'])->group(function () {
      Route::post('/groups/{group}/members', [GroupController::class, 'addMember']);
      //The route to handle group role changes
      Route::put('/groups/{group}/members/role', [GroupController::class, 'changeMemberRole']);
      //Route to handle removing of members
      Route::delete('/groups/{group}/remove-member', [GroupController::class, 'removeMember']);

      // --- TOPICS MANAGEMENT ---
      // Fetch topics inside a specific group - only accessible if you are in that group
      Route::get('/groups/{group}/topics', [TopicController::class, 'index']);
      // Create a new topic inside a specific group
      Route::post('/groups/{group}/topics', [TopicController::class, 'store']);
      // --- UNIFIED MESSAGES ---
       Route::get('/groups/{group}/messages', [MessageController::class, 'getMessages']);
       Route::post('/groups/{group}/messages', [MessageController::class, 'store']);


      // --- GROUP MEMBER ---
      // Fetch all members belonging to a specific group
      Route::get('/groups/{group}/members', [GroupController::class, 'getMembers']);
        });

      // --- QUIZZES MANAGEMENT ---
      Route::get('/groups/{group}/quizzes', [QuizController::class, 'index']);
      Route::post('/groups/{group}/quizzes', [QuizController::class, 'store'])->middleware('lecturer'); // Configure/Publish Quiz (Lecturers)

      // --- QUIZ ATTEMPTS ---
      Route::post('/groups/{group}/quizzes/{quiz}/start', [QuizController::class, 'startAttempt']); // Initialize submission & get questions
      Route::post('/groups/{group}/quizzes/{quiz}/submit', [QuizController::class, 'submitAttempt']); // Process & score responses
      Route::post('/groups/{group}/quizzes/{quiz}/launch', [QuizController::class, 'launchQuiz']);
      // --- PERFORMANCE REPORTS ---
      Route::get('/groups/{group}/quizzes/{quiz}/results', [QuizController::class, 'resultsReport']); // Shared reports

});


