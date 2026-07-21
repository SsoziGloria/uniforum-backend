<?php

namespace App\Services;

use App\Models\User;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Validator;
use Illuminate\Validation\ValidationException;

class UserRegistrationService
{
    /**
     * Register a new UniForum user.
     */
    public function register(array $input): User
    {
        Validator::make($input, [
    'name' => ['required', 'string', 'max:255'],
    'email' => ['required', 'email', 'max:255', 'unique:users,email'],
    'password' => ['required', 'string', 'min:8'],
    'role' => ['required', 'in:student,lecturer'],
         ])->validate();

    $role = 'student';

    if ($input['role'] === 'lecturer') {

      $secretStaffCode = 'MUK-STAFF-2026';

       if (($input['lecturer_passcode'] ?? '') !== $secretStaffCode) {

          throw ValidationException::withMessages([
             'lecturer_passcode' => [
                 'Invalid lecturer verification token.'
             ],
          ]);

     }

    $role = 'lecturer';
    }
    return User::create([
      'name' => $input['name'],
      'email' => $input['email'],
      'password' => Hash::make($input['password']),
      'role' => $role,
     ]);
   }
}