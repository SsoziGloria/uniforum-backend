<?php

use Illuminate\Support\Facades\Broadcast;
use App\Models\Topic;

Broadcast::channel('App.Models.User.{id}', function ($user, $id) {
    return (int) $user->id === (int) $id;
});
 // private topic channel mapping here
 // The channel name placeholder {topicId} maps directly to channel string
 Broadcast::channel('topics.{topicId}', function ($user, $topicId) {

// Find the topic along with its associated group
    $topic = Topic::with('group')->find($topicId);

    if (!$topic || !$topic->group) {
        return false; // Reject if the topic or group doesn't exist
    }

    // Check if the authenticated user belongs to this specific academic group
    return $topic->group->members()->where('user_id', $user->id)->exists();

 });
