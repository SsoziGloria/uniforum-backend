<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\GroupController;
use App\Http\Controllers\TopicController;
use App\Http\Controllers\MessageController;
use App\Http\Controllers\AuthController;

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
      Route::post('/groups/{group}/change-role', [GroupController::class, 'changeMemberRole']);
      //Route to handle removing of members
      Route::delete('/groups/{group}/remove-member', [GroupController::class, 'removeMember']);

      // --- TOPICS MANAGEMENT ---
      // Fetch topics inside a specific group - only accessible if you are in that group
      Route::get('/groups/{group}/topics', [TopicController::class, 'index']);
      // Create a new topic inside a specific group
      Route::post('/groups/{group}/topics', [TopicController::class, 'store']);

      // --- MESSAGES ---
      // Get past messages belonging to a specific topic inside a group
      Route::get('/groups/{group}/topics/{topic}/messages', [MessageController::class, 'getTopicMessages']);
      // Post a message inside a specific group topic (Triggers WebSocket broadcast)
      Route::post('/groups/{group}/topics/{topic}/messages', [MessageController::class, 'store']);

      // --- GROUP MEMBER ---
      // Fetch all members belonging to a specific group
      Route::get('/groups/{group}/members', [GroupController::class, 'getMembers']);
        });

});


