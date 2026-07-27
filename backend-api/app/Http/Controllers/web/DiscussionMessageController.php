<?php

namespace App\Http\Controllers\Web;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use App\Services\MessageService;
use App\Notifications\DiscussionReplyNotification;

class DiscussionMessageController extends Controller
{
    protected MessageService $messageService;

    public function __construct(MessageService $messageService)
    {
        $this->messageService = $messageService;
    }

    public function store(Request $request, $group, $topic)
    {
        $validated = $request->validate([
            'msg_txt' => 'required|string'
        ]);

        $this->messageService->sendMessage(
            $group,
            Auth::id(),
            [
                'topic_id'      => $topic,
                'msg_txt'       => $validated['msg_txt'],
                'is_restricted' => false
            ]
        );

        return back()
            ->with('success', 'Question posted successfully.');
    }

    public function reply(Request $request, $group, $topic, $message)
    {
        $validated = $request->validate([
            'msg_txt' => 'required|string'
        ]);

        $this->messageService->replyToDiscussion(
            (int) $group,
            (int) $topic,
            (int) $message,
            Auth::id(),
            $validated['msg_txt']
        );

        if ($topic->created_by !== auth()->id()) {
            $topicCreator = User::find($topic->created_by);
            $topicCreator?->notify(new DiscussionReplyNotification($topic, $message, auth()->user()->name));
        }


        return back()
            ->with('success', 'Reply posted successfully.');
    }

    public function upvote($group, $message)
    {
        $this->messageService->toggleUpvote(
            (int) $message,
            Auth::id()
        );

        return back()->with('success', 'Vote updated.');
    }

    public function markAnswer($group, $topic, $message)
    {
        $result = $this->messageService->markAnswer(
            (int) $topic,
            (int) $message,
            Auth::id()
        );

        if (!$result['success']) {
            return back()->withErrors($result['message']);
        }

        return back()->with('success', 'Answer marked.');
    }

    public function destroy($group, $topic, $message)
    {
        $result = $this->messageService->deleteMessage(
            (int) $group,
            (int) $message,
            Auth::id()
        );

        if (!$result['success']) {
            abort(403, $result['message']);
        }

        return back()->with('success', $result['message']);
    }
}