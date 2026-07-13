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

Route::get('/student-dashboard', function () {
    return view('dashboard.student');
})->name('student.dashboard');


Route::get('/lecturer-dashboard', function () {
    return view('dashboard.lecturer');
})->name('lecturer.dashboard');


Route::get('/admin-dashboard', function () {
    return view('dashboard.admin');
})->name('admin.dashboard');