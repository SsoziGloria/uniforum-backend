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
        });

});

/*
-------------------------------------------
TEMPORARY TEST ROUTES
-------------------------------------------
*/
// Route to see all registered users
     Route::get('/test-users', function() {
         return response()->json(DB::table('users')->get());
     });

     // Route to see all academic groups
     Route::get('/test-groups', function() {
         return response()->json(DB::table('groups')->get());
     });

     // Route to see all group memberships (the pivot table records)
     Route::get('/test-members', function() {
         return response()->json(DB::table('group_members')->get());
     });
// Route to manually assign a user to a group for testing
Route::get('/test-assign/{groupId}/{userId}', function($groupId, $userId) {
    DB::table('group_members')->insert([
        'group_id'   => $groupId,
        'user_id'    => $userId,

    ]);

    return response()->json([
        'status'  => 'Success',
        'message' => "User {$userId} successfully assigned to Group {$groupId}!"
    ]);
});

