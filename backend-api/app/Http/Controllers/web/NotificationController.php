<?php

namespace App\Http\Controllers\web;

use App\Http\Controllers\Controller;
use App\Services\NotificationService;
use Illuminate\Http\Request;

class NotificationController extends Controller
{
    public function __construct(protected NotificationService $notificationService) {}

    public function index(Request $request)
    {
        $user = $request->user();
        $notifications = $this->notificationService->getUserNotifications($user);
        $unreadCount = $user->unreadNotifications()->count();

        return view('student.notifications.index', compact('notifications', 'unreadCount'));
    }

    public function markAsRead(Request $request, string $id)
    {
        $this->notificationService->markAsRead($request->user(), $id);
        return back()->with('status', 'Notification marked as read');
    }

    public function markAllAsRead(Request $request)
    {
        $this->notificationService->markAllAsRead($request->user());
        return back()->with('status', 'All notifications marked as read');
    }
}