<?php

namespace App\Services;

use App\Models\User;

class UserLogoutService
{
    /**
     * Logout the authenticated API user.
     */
    public function logout(User $user): void
    {
        // Delete the current Sanctum access token only.
        $user->currentAccessToken()->delete();
    }
}