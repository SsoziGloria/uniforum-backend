<?php

namespace App\Http\Controllers;

use Illuminate\Support\Facades\Auth;

class DashboardController extends Controller
{
    public function index()
    {
        $user = Auth::user();

        if ($user->role === 'student') {
            return redirect()->route('student.dashboard');
        }

        if ($user->role === 'lecturer') {
            return redirect()->route('lecturer.dashboard');
        }

        abort(403);
    }
}