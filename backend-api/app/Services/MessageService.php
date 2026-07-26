<?php

namespace App\Services;

use App\Models\Message;
use App\Models\MessageExclusion;
use App\Models\GroupMember;
use Carbon\Carbon;
use App\Models\MessageVote;
use App\Models\Topic;

class MessageService
{
    /**
     * Retrieve messages visible to a specific user in a group.
     */
    public function getGroupMessages($groupId, $userId, $topicId = null)
    {
        $query = Message::where('group_id', $groupId);

        if ($topicId) {
            $query->where('topic_id', $topicId);
        } else {
            $query->whereNull('topic_id');
        }

        return $query
            ->with('sender:id,name')
            ->where(function ($query) use ($userId) {
                $query->where('is_restricted', false)
                    ->orWhere('sender_id', $userId)
                    ->orWhere(function ($subQuery) use ($userId) {
                        $subQuery->where('is_restricted', true)
                            ->whereDoesntHave('exclusions', function ($check) use ($userId) {
                                $check->where('ex_user_id', $userId);
                            });
                    });
            })
            ->orderBy('posted_at', 'asc')
            ->get();
    }

    /**
     * Store a new message.
     */
    public function sendMessage($groupId, $userId, array $data)
    {
        $membership = GroupMember::where('group_id', $groupId)
            ->where('user_id', $userId)
            ->first();

        if (
            $membership &&
            $membership->blacklisted_until &&
            Carbon::parse($membership->blacklisted_until)->isFuture()
        ) {
            return [
                'error'   => true,
                'message' => 'You are temporarily blacklisted from this group.'
            ];
        }

        $message = Message::create([
            'group_id'      => $groupId,
            'topic_id'      => $data['topic_id'] ?? null,
            'sender_id'     => $userId,
            'msg_txt'       => $data['msg_txt'],
            'is_synced'     => true,
            'is_restricted' => $data['is_restricted'] ?? false,
            'posted_at'     => now(),
        ]);

        GroupMember::where('group_id', $groupId)
            ->where('user_id', $userId)
            ->update([
                'last_activity' => now()
            ]);

        if (
            !empty($data['is_restricted']) &&
            isset($data['excluded_user_ids'])
        ) {
            foreach ($data['excluded_user_ids'] as $excludedId) {
                MessageExclusion::create([
                    'msg_id'     => $message->msg_id,
                    'ex_user_id' => $excludedId
                ]);
            }
        }

        return [
            'error'   => false,
            'message' => $message
        ];
    }

    public function replyToDiscussion(
        int $groupId,
        int $topicId,
        ?int $parentMessageId,
        int $userId,
        string $message
    ) {
        if ($parentMessageId) {
            Message::where('group_id', $groupId)
                ->where('topic_id', $topicId)
                ->findOrFail($parentMessageId);
        }

        return Message::create([
            'group_id'      => $groupId,
            'topic_id'      => $topicId,
            'parent_msg_id' => $parentMessageId,
            'sender_id'     => $userId,
            'msg_txt'       => $message,
            'is_synced'     => true,
            'is_restricted' => false,
            'posted_at'     => now(),
        ]);
    }

    public function toggleUpvote(int $messageId, int $userId)
    {
        $vote = MessageVote::where('msg_id', $messageId)
            ->where('user_id', $userId)
            ->first();

        if ($vote) {
            $vote->delete();

            return [
                'success' => true,
                'upvoted' => false
            ];
        }

        MessageVote::create([
            'msg_id'  => $messageId,
            'user_id' => $userId
        ]);

        return [
            'success' => true,
            'upvoted' => true
        ];
    }

    public function markAnswer(int $topicId, int $messageId, int $userId)
    {
        $topic = Topic::findOrFail($topicId);

        if ($topic->created_by != $userId) {
            return [
                'success' => false,
                'message' => 'Only the discussion creator can mark an answer.'
            ];
        }

        $message = Message::where('topic_id', $topicId)
            ->findOrFail($messageId);

        $topic->accepted_msg_id = $message->msg_id;
        $topic->save();

        return [
            'success' => true,
            'message' => 'Answer marked successfully.'
        ];
    }

    public function getUnansweredDiscussions(int $groupId)
    {
        return Topic::where('group_id', $groupId)
            ->whereNull('accepted_msg_id')
            ->withCount('messages')
            ->latest('created_at')
            ->get();
    }

    /**
     * Delete a single message.
     */
    public function deleteMessage(int $groupId, int $messageId, int $userId): array
    {
        $message = Message::findOrFail($messageId);

        $membership = GroupMember::where('group_id', $groupId)
            ->where('user_id', $userId)
            ->first();

        $isAdmin = $membership && $membership->role === 'admin';

        if ($message->sender_id !== $userId && !$isAdmin) {
            return [
                'success' => false,
                'message' => 'Unauthorized. You can only delete your own messages.'
            ];
        }

        // Deletion cascade triggered by Model boot observer
        $message->delete();

        return [
            'success' => true,
            'message' => 'Message deleted successfully.'
        ];
    }
}