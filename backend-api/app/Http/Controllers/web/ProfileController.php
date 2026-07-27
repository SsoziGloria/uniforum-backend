<?php

namespace App\Http\Controllers\Web;

use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Auth;

class ProfileController extends Controller
{

    public function show()
    {
        $user = Auth::user();


        if($user->role === 'lecturer')
        {
            return view('lecturer.profile.show');
        }


        return view('student.profile.show');
    }

}