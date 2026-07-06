<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\GroupController;
use App\Http\Controllers\TopicController;
use App\Http\Controllers\MessageController;
use App\Http\Controllers\AuthController;

Route::get('/groups', [GroupController::class, 'index']);
Route::get('/topics', [TopicController::class, 'index']);
//Get messages belonging to a specific topic when clicked
Route::get('/topics/{topic}/messages', [MessageController::class, 'getTopicMessages']);
Route::get('/messages', [MessageController::class, 'index']);


//POST ROUTE
Route::post('/messages', [MessageController::class, 'store']);
Route::post('/groups', [GroupController::class, 'store']);
Route::post('/topics', [TopicController::class, 'store']);

//route for authentication
Route::post('/login', [AuthController::class, 'login']);
//Route for registering a new account
Route::post('/register', [AuthController::class, 'register']);
