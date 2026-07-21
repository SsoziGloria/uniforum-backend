<?php

namespace App\Services;

use App\Models\User;
use Illuminate\Support\Facades\Hash;
use Illuminate\Validation\ValidationException;

class UserLoginService
{
    /**
     * Authenticate a user.
     *
     * @param array $credentials
     * @return User
     *
     * @throws ValidationException
     */
    public function login(array $credentials): User
    {
        // Find the user by email
        $user = User::where('email', $credentials['email'])->first();

        // Check if the user exists and the password is correct
        if (!$user || !Hash::check($credentials['password'], $user->password)) {
            throw ValidationException::withMessages([
                'email' => ['Invalid email or password.'],
            ]);
        }

        return $user;
    }
}