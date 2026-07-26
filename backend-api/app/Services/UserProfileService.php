<?php

namespace App\Services;

use App\Models\User;

class UserProfileService
{
    /**
     * Get authenticated user's profile information.
     */
    public function getProfile(User $user)
    {
        return [
            'user_id' => $user->id,
            'name' => $user->name,
            'email' => $user->email,
            'role' => $user->role,
            'created_at' => $user->created_at,
        ];
    }


    /**
     * Update user profile information.
     */
    public function updateProfile(User $user, array $data)
    {
        $user->update([
            'name' => $data['name'],
            'email' => $data['email'],
        ]);

        return $user;
    }
}