<?php

use Illuminate\Support\Facades\Route;

Route::get('/', function () {
    return view('welcome');
})->name('home');

Route::middleware([
    'auth:sanctum',
    config('jetstream.auth_session'),
    'verified',
])->group(function () {
    Route::get('/dashboard', function () {
        return view('dashboard');
    })->name('dashboard');
});

Route::get('/student/dashboard', function () {
    return view('student.dashboard');
})->name('student.dashboard');


Route::get('/lecturer/dashboard', function () {
    return view('lecturer.dashboard');
})->name('lecturer.dashboard');

Route::get('/student/discussions', function () {
    return view('student.discussions.index');
});

Route::get('/student/discussions/show', function () {
    return view('student.discussions.show');
});

Route::get('/student/discussions/create', function () {
    return view('student.discussions.create');
});

Route::get('/lecturer/discussions', function () {
    return view('lecturer.discussions.index');
});

Route::get('/lecturer/discussions/create', function () {
    return view('lecturer.discussions.create');
});
Route::get('/lecturer/discussions/show', function () {
    return view('lecturer.discussions.show');
});
Route::get('/lecturer/quizzes', function () {
    return view('lecturer.quizzes.index');
});

Route::get('/lecturer/quizzes/create', function () {
    return view('lecturer.quizzes.create');
});

Route::get('/lecturer/quizzes/show', function () {
    return view('lecturer.quizzes.show');
});

Route::get('/lecturer/quizzes/results', function () {
    return view('lecturer.quizzes.results');
});

Route::get('/lecturer/groups', function () {
    return view('lecturer.groups.index');
});
Route::get('/lecturer/groups/show', function () {
    return view('lecturer.groups.show');
});
Route::get('/lecturer/groups/statistics', function () {
    return view('lecturer.groups.statistics');
});

Route::get('/lecturer/notifications', function () {
    return view('lecturer.notifications.index');
});

Route::get('/lecturer/students', function () {
    return view('lecturer.students.index');
});

Route::get('/lecturer/groups/create', function () {
    return view('lecturer.groups.create');
});

Route::get('/lecturer/groups/browse', function () {
    return view('lecturer.groups.browse');
});
Route::get('/lecturer/groups/join', function () {
    return view('lecturer.groups.join');
});
Route::get('/lecturer/groups/members', function () {
    return view('lecturer.groups.members');
});

Route::get('/student/groups', function () {
    return view('student.groups.index');
});

Route::get('/student/groups/show', function () {
    return view('student.groups.show');
});

Route::get('/student/groups/statistics', function () {
    return view('student.groups.statistics');
});


Route::get('/student/groups/join', function () {
    return view('student.groups.join');
});

Route::get('/student/notifications', function () {
    return view('student.notifications.index');
});

Route::get('/student/quizzes', function () {
    return view('student.quizzes.index');
});

Route::get('/student/quizzes/take', function () {
    return view('student.quizzes.take');
});

Route::get('/student/recommendations', function () {
    return view('student.recommendations.index');
});


Route::get('/student/groups/members', function () {
        return view('student.groups.members');
    });

Route::get('/student/quizzes/report', function () {
    return view('student.quizzes.report');
    });

Route::get('/student/groups/browse', function () {
    return view('student.groups.browse');
});

Route::get('/student/groups/create', function () {
    return view('student.groups.create');
});

Route::get('/student/groups/chat', function () {
    return view('student.groups.chat');
});
Route::get('/student/groups/participation/results', function () {
    return view('student.groups.participation.results');
});
Route::get('/lecturer/groups/participation/settings', function () {
    return view('lecturer.groups.participation.settings');
});

Route::get('/lecturer/groups/participation', function () {
    return view('lecturer.groups.participation.index');
});

Route::get('/lecturer/groups/chat', function () {
    return view('lecturer.groups.chat');
});

Route::get('/profile_uniforum/settings', function () {
    return view('profile_uniforum.settings');
});
