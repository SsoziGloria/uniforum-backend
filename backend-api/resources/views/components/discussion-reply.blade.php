@props(['reply', 'question', 'group', 'topic', 'isMember', 'level' => 1])

<div class="mt-4 p-4 rounded-xl border border-slate-200 bg-slate-50/50" style="margin-left: {{ min($level * 1.25, 5) }}rem;">
    <div class="flex justify-between items-center">
        <div>
            @if($reply->parent_msg_id == $question->msg_id)
                <span class="text-xs font-semibold text-green-700 uppercase">
                    Answer
                </span>
            @else
                <span class="text-xs font-semibold text-orange-600 uppercase">
                    Reply
                </span>
            @endif

            <h4 class="font-semibold text-slate-900 mt-1">
                {{ $reply->sender->name }}
            </h4>
        </div>

        <span class="text-xs text-slate-400">
            {{ $reply->posted_at->diffForHumans() }}
        </span>
    </div>

    <p class="mt-3 text-slate-700 leading-relaxed">
        {{ $reply->msg_txt }}
    </p>

    <div class="mt-3 flex items-center gap-3">
        <!-- Upvote -->
        <form method="POST" action="{{ route('student.groups.discussions.messages.upvote', [$group->group_id, $reply->msg_id]) }}">
            @csrf
            <button type="submit" class="px-3 py-1.5 rounded-lg bg-blue-50 text-blue-700 text-xs font-medium hover:bg-blue-100">
                👍 {{ $reply->votes_count ?? 0 }}
            </button>
        </form>

        <!-- Reply Button -->
        @if($isMember)
            <button type="button" 
                    onclick="document.getElementById('reply-form-{{ $reply->msg_id }}').classList.toggle('hidden')"
                    class="px-3 py-1.5 rounded-lg bg-slate-100 text-slate-600 text-xs font-medium hover:bg-slate-200">
                Reply
            </button>
        @endif

        <!-- Mark Answer Button (Topic Creator Only) -->
        @if(auth()->id() == $topic->created_by)
            @if($topic->accepted_msg_id == $reply->msg_id)
                <span class="px-3 py-1.5 rounded-lg bg-green-100 text-green-700 text-xs font-semibold border border-green-300">
                    ✓ Accepted Answer
                </span>
            @else
                <form method="POST" action="{{ route('student.groups.discussions.messages.answer', [$group->group_id, $topic->topic_id, $reply->msg_id]) }}">
                    @csrf
                    <button type="submit" class="px-3 py-1.5 rounded-lg bg-green-50 text-green-600 text-xs font-medium hover:bg-green-100 border border-green-200">
                        Mark Answer
                    </button>
                </form>
            @endif
        @endif

        <!-- Delete Button -->
        @if(auth()->id() == $reply->sender_id)
            <form method="POST" action="{{ route('student.groups.discussions.messages.destroy', [$group->group_id, $topic->topic_id, $reply->msg_id]) }}">
                @csrf
                @method('DELETE')
                <button type="submit" onclick="return confirm('Delete this reply?')" class="text-red-600 text-xs font-medium hover:underline">
                    Delete
                </button>
            </form>
        @endif
    </div>

    <!-- Hidden Reply Form -->
    @if($isMember)
        <form id="reply-form-{{ $reply->msg_id }}"
              class="hidden mt-3"
              method="POST"
              action="{{ route('student.groups.discussions.messages.reply', [$group->group_id, $topic->topic_id, $reply->msg_id]) }}">
            @csrf
            <textarea name="msg_txt" rows="2" class="w-full text-sm rounded-xl border-slate-200 focus:ring-blue-500 focus:border-blue-500" placeholder="Write a reply..."></textarea>
            <button type="submit" class="mt-2 px-3 py-1.5 bg-blue-600 text-white text-xs font-medium rounded-lg hover:bg-blue-700">
                Post Reply
            </button>
        </form>
    @endif

    <!-- Recursive Call for Nested Child Replies -->
    @if($reply->replies && $reply->replies->count() > 0)
        <div class="space-y-3 mt-3">
            @foreach($reply->replies as $childReply)
                <x-discussion-reply 
                    :reply="$childReply" 
                    :question="$question" 
                    :group="$group" 
                    :topic="$topic" 
                    :isMember="$isMember" 
                    :level="$level + 1" 
                />
            @endforeach
        </div>
    @endif
</div>