<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\DashboardController;
use App\Http\Controllers\web\StudentGroupController;
use App\Http\Controllers\web\ProfileController;
use App\Http\Controllers\web\StudentGroupStatisticsController;
use App\Http\Controllers\web\TopicController;
use App\Http\Controllers\MessageController;
use App\Http\Controllers\web\DiscussionMessageController;
use App\Http\Controllers\web\StudentQuizController;
use App\Http\Controllers\web\NotificationController;
use App\Http\Controllers\web\StudentDashboardController;
use App\Http\Controllers\web\LecturerGroupController;
use App\Http\Controllers\web\LecturerDiscussionController;
use App\Http\Controllers\web\LecturerQuizController;
use App\Http\Controllers\web\LecturerStudentController;
use App\Http\Controllers\web\LecturerNotificationController;
use App\Http\Controllers\web\LecturerDashboardController;
use App\Http\Controllers\web\RecommendationController;



Route::get('/', function () {
    return view('welcome');
})->name('home');

Route::middleware([
    'auth:sanctum',
    config('jetstream.auth_session'),
    'verified',
])->group(function () {

    Route::get('/dashboard', [DashboardController::class, 'index'])
        ->name('dashboard');

});

Route::middleware('auth')->group(function () {

    // --- STUDENT ROUTES ---
    Route::get('/student/dashboard', [StudentDashboardController::class, 'index'])
        ->name('student.dashboard');

    Route::get('/student/groups', [StudentGroupController::class, 'index'])
        ->name('student.groups.index');

    Route::get('/student/groups/browse', [StudentGroupController::class, 'browse'])
        ->name('student.groups.browse');

    Route::get('/student/groups/create', [StudentGroupController::class, 'create'])
        ->name('student.groups.create');

    Route::get('/student/groups/{group}/join', [StudentGroupController::class, 'join'])
        ->name('student.groups.join');

    Route::post('/student/groups/{group}/join', [StudentGroupController::class, 'storeJoin'])
        ->name('student.groups.join.store');

    Route::get('/student/groups/{group}/chat', [StudentGroupController::class, 'chat'])
        ->name('student.groups.chat');

    Route::post('/student/groups/{group}/chat', [StudentGroupController::class, 'sendMessage'])
        ->name('student.groups.chat.store');

    Route::get('/student/groups/{group}/participation/results', [StudentGroupController::class, 'participationResults'])
        ->name('student.groups.participation.results');

    Route::get('/student/groups/{group}/members', [StudentGroupController::class, 'members'])
        ->name('student.groups.members');

    Route::get('/student/groups/{group}/statistics', [StudentGroupController::class, 'statistics'])
        ->name('student.groups.statistics');

    Route::prefix('student/groups/{group}/discussions')->group(function () {
        Route::get('/', [TopicController::class, 'index'])
            ->name('student.discussions.index');

        Route::get('/create', [TopicController::class, 'create'])
            ->name('student.discussions.create');

        Route::post('/', [TopicController::class, 'store'])
            ->name('student.discussions.store');

        Route::get('/{topic}', [TopicController::class, 'show'])
            ->name('student.discussions.show');

        Route::get('/{topic}/export', [TopicController::class, 'exportPdf'])
            ->name('student.discussions.export');

        Route::get('/{topic}/share', [TopicController::class, 'share'])
            ->name('student.discussions.share');
        
        Route::post('/{topic}/messages', [DiscussionMessageController::class, 'store'])
            ->name('student.groups.discussions.messages.store');

        Route::delete('/{topic}', [TopicController::class, 'destroy'])
            ->name('student.discussions.destroy');

        Route::post('/{topic}/messages/{message}/reply', [DiscussionMessageController::class, 'reply'])
            ->name('student.groups.discussions.messages.reply');

        Route::post('/messages/{message}/upvote', [DiscussionMessageController::class, 'upvote'])
            ->name('student.groups.discussions.messages.upvote');

        Route::post('/{topic}/messages/{message}/answer', [DiscussionMessageController::class, 'markAnswer'])
            ->name('student.groups.discussions.messages.answer');

        Route::delete('/{topic}/messages/{message}', [DiscussionMessageController::class, 'destroy'])
            ->name('student.groups.discussions.messages.destroy');
    });

    Route::post('/student/groups/{group}/members/{user}/warning', [StudentGroupController::class, 'issueWarning'])
        ->name('student.groups.members.warning');

    Route::post('/student/groups/{group}/members/{user}/blacklist', [StudentGroupController::class, 'blacklist'])
        ->name('student.groups.members.blacklist');

    Route::post('/student/groups/{group}/members/{user}/reinstate', [StudentGroupController::class, 'reinstate'])
        ->name('student.groups.members.reinstate');

    Route::post('/student/groups/{group}/members/{user}/promote', [StudentGroupController::class, 'promoteMember'])
        ->name('student.groups.members.promote');

    Route::post('/student/groups/{group}/members/{user}/demote', [StudentGroupController::class, 'demoteMember'])
       ->name('student.groups.members.demote');


    Route::delete('/student/groups/{group}/chat/messages/{message}', [StudentGroupController::class, 'destroyChatMessage'])
        ->name('student.groups.chat.messages.destroy');

    Route::get('/student/groups/{group}/quizzes', [StudentQuizController::class, 'index'])
        ->name('student.groups.quizzes.index');
    Route::get('/student/groups/{group}/quizzes/{quiz}/take', [StudentQuizController::class, 'take'])
        ->name('student.groups.quizzes.take');
    Route::post('/student/groups/{group}/quizzes/{quiz}/submit', [StudentQuizController::class, 'submit'])
        ->name('student.groups.quizzes.submit');
    Route::get('/student/groups/{group}/quizzes/{quiz}/report', [StudentQuizController::class, 'report'])
        ->name('student.groups.quizzes.report');
    
    Route::delete('/student/groups/{group}/leave', [StudentGroupController::class, 'leave'])
        ->name('student.groups.leave');

    Route::delete('/student/groups/{group}', [StudentGroupController::class, 'destroy'])
        ->name('student.groups.destroy');

    Route::get('/student/groups/{group}', [StudentGroupController::class, 'show'])
        ->name('student.groups.show');

    Route::post('/student/groups', [StudentGroupController::class, 'store'])
        ->name('student.groups.store');

    Route::get('/student/profile', [ProfileController::class, 'show'])
        ->name('student.profile.show');

    Route::get('/student/notifications', [NotificationController::class, 'index'])
        ->name('student.notifications.index');

    Route::post('/student/notifications/{id}/read', [NotificationController::class, 'markAsRead'])
        ->name('student.notifications.read');

    Route::post('/student/notifications/read-all', [NotificationController::class, 'markAllAsRead'])
        ->name('student.notifications.read-all');

    Route::get('/student/recommendations', [RecommendationController::class, 'index'])
    ->name('student.recommendations.index');



    // --- LECTURER ROUTES ---
    Route::get('/lecturer/dashboard', [LecturerDashboardController::class, 'index'])
        ->name('lecturer.dashboard');

    Route::get('/lecturer/profile', [ProfileController::class, 'show'])
        ->name('lecturer.profile.show');

    Route::get('/lecturer/students', [LecturerStudentController::class, 'index'])
        ->name('lecturer.students.index');

    Route::get('/lecturer/notifications', [LecturerNotificationController::class, 'index'])->name('lecturer.notifications.index');
    Route::post('/lecturer/notifications/{id}/read', [LecturerNotificationController::class, 'markAsRead'])->name('lecturer.notifications.read');
    Route::post('/lecturer/notifications/read-all', [LecturerNotificationController::class, 'markAllAsRead'])->name('lecturer.notifications.read-all');


    Route::prefix('lecturer/groups')->name('lecturer.groups.')->group(function () {
        Route::get('/', [LecturerGroupController::class, 'index'])->name('index');
        Route::get('/browse', [LecturerGroupController::class, 'browse'])->name('browse');
        Route::get('/create', [LecturerGroupController::class, 'create'])->name('create');
        Route::post('/', [LecturerGroupController::class, 'store'])->name('store');

        // Lecturer Join Routes
        Route::get('/{group}/join', [LecturerGroupController::class, 'join'])->name('join');
        Route::post('/{group}/join', [LecturerGroupController::class, 'storeJoin'])->name('join.store');

        Route::delete('/{group}/leave', [LecturerGroupController::class, 'leave'])->name('leave');
        Route::get('/{group}/chat', [LecturerGroupController::class, 'chat'])->name('chat');
        Route::post('/{group}/chat', [LecturerGroupController::class, 'sendMessage'])->name('chat.store');
        Route::delete('/{group}/chat/messages/{message}', [LecturerGroupController::class, 'destroyChatMessage'])->name('chat.messages.destroy');
        Route::get('/{group}/members', [LecturerGroupController::class, 'members'])->name('members');
        Route::post('/{group}/members/{user}/warning', [LecturerGroupController::class, 'issueWarning'])->name('members.warning');
        Route::post('/{group}/members/{user}/blacklist', [LecturerGroupController::class, 'blacklist'])->name('members.blacklist');
        Route::post('/{group}/members/{user}/reinstate', [LecturerGroupController::class, 'reinstate'])->name('members.reinstate');
        Route::post('/{group}/members/{user}/promote', [LecturerGroupController::class, 'promoteMember'])->name('members.promote');
        Route::post('/{group}/members/{user}/demote', [LecturerGroupController::class, 'demoteMember'])->name('members.demote');

        Route::get('/{group}/statistics', [LecturerGroupController::class, 'statistics'])->name('statistics');

        
        Route::prefix('{group}/discussions')->name('discussions.')->group(function () {
            Route::get('/', [LecturerDiscussionController::class, 'index'])->name('index');
            Route::get('/create', [LecturerDiscussionController::class, 'create'])->name('create');
            Route::post('/', [LecturerDiscussionController::class, 'store'])->name('store');
            Route::get('/{topic}', [LecturerDiscussionController::class, 'show'])->name('show');
            Route::delete('/{topic}', [LecturerDiscussionController::class, 'destroy'])->name('destroy');
            
            Route::get('/{topic}/export', [LecturerDiscussionController::class, 'exportPdf'])->name('export');
            // Message responses inside topic
            Route::post('/{topic}/messages', [DiscussionMessageController::class, 'store'])->name('messages.store');
            Route::post('/{topic}/messages/{message}/reply', [DiscussionMessageController::class, 'reply'])->name('messages.reply');
            Route::post('/{topic}/messages/{message}/upvote', [DiscussionMessageController::class, 'upvote'])->name('messages.upvote');
            Route::post('/{topic}/messages/{message}/mark-answer', [DiscussionMessageController::class, 'markAnswer'])->name('messages.markAnswer');
            Route::delete('/{topic}/messages/{message}', [DiscussionMessageController::class, 'destroy'])->name('messages.destroy');
           
        });
        Route::prefix('{group}/quizzes')->name('quizzes.')->group(function () {
            Route::get('/', [LecturerQuizController::class, 'index'])->name('index');
            Route::get('/create', [LecturerQuizController::class, 'create'])->name('create');
            Route::post('/', [LecturerQuizController::class, 'store'])->name('store');
            Route::get('/{quiz}', [LecturerQuizController::class, 'show'])->name('show');
            Route::get('/{quiz}/results', [LecturerQuizController::class, 'results'])->name('results');
        });

        Route::get('/{group}/participation', [LecturerGroupController::class, 'participation'])->name('participation');
        // Participation Settings & Criteria CRUD
        Route::get('/{group}/participation/settings', [LecturerGroupController::class, 'participationSettings'])->name('participation.settings');
        Route::post('/{group}/participation/settings', [LecturerGroupController::class, 'storeCriterion'])->name('participation.settings.store');
        Route::put('/{group}/participation/settings', [LecturerGroupController::class, 'updateCriteria'])->name('participation.settings.update');
        Route::delete('/{group}/participation/settings/{criterion}', [LecturerGroupController::class, 'destroyCriterion'])->name('participation.settings.destroy');

        Route::get('/{group}', [LecturerGroupController::class, 'show'])->name('show');
    });

});




