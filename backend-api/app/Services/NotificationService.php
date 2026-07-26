<?php

namespace App\Services;

use App\Models\User;

class NotificationService
{
    public function getUserNotifications(User $user, int $perPage = 15)
    {
        return $user->notifications()->paginate($perPage);
    }

    public function markAsRead(User $user, string $notificationId): bool
    {
        $notification = $user->notifications()->where('id', $notificationId)->first();
        if ($notification) {
            $notification->markAsRead();
            return true;
        }
        return false;
    }

    public function markAllAsRead(User $user): void
    {
        $user->unreadNotifications->markAsRead();
    }
}