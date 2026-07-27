@extends('layouts.student')

@section('title', 'Discussion Details - UniForum')

@section('page-title', 'Discussion Details')

@section('content')

<div>

    <!-- Header -->
    <div class="bg-white border-b border-slate-200">
        <div class="max-w-5xl mx-auto px-6 py-8">

            <a href="{{ route('student.discussions.index', $topic->group_id) }}" class="text-sm text-blue-600 hover:underline">
                ← Back to Discussions
            </a>

            <div class="mt-5">
                <span class="text-xs font-semibold bg-blue-100 text-blue-700 px-3 py-1 rounded-full">
                    {{ $topic->ml_category ?? 'General' }}
                </span>

                <h1 class="mt-4 text-3xl font-bold text-slate-900">
                    {{ $topic->title }}
                </h1>

                <div class="mt-4 flex gap-4 text-sm text-slate-500">
                    <span>Started by {{ $topic->creator->name }}</span>
                    <span>{{ $topic->created_at->diffForHumans() }}</span>
                </div>

                <div class="mt-5 flex items-center justify-between gap-3">
                    <div class="flex gap-3">
                        <div class="relative inline-block text-left" x-data="{ open: false }">
                            <button @click="open = !open" type="button" class="px-4 py-2 rounded-lg bg-slate-100 hover:bg-slate-200 text-slate-700 text-sm font-medium">
                                🔗 Share
                            </button>

                            <div x-show="open" @click.away="open = false" class="absolute left-0 mt-2 w-48 rounded-xl bg-white shadow-lg border border-slate-200 z-50 p-2 space-y-1">
                                <button onclick="navigator.clipboard.writeText(window.location.href); alert('Link copied to clipboard!');" 
                                        class="w-full text-left px-3 py-2 text-xs font-medium text-slate-700 hover:bg-slate-100 rounded-lg">
                                    📋 Copy Link
                                </button>
                                
                                <a :href="'https://wa.me/?text=' + encodeURIComponent('Check out this discussion on UniForum: ' + window.location.href)" 
                                target="_blank" class="block w-full text-left px-3 py-2 text-xs font-medium text-green-700 hover:bg-green-50 rounded-lg">
                                    💬 Share to WhatsApp
                                </a>

                                <a :href="'https://twitter.com/intent/tweet?text=' + encodeURIComponent('Check out this discussion on UniForum: ') + '&url=' + encodeURIComponent(window.location.href)" 
                                target="_blank" class="block w-full text-left px-3 py-2 text-xs font-medium text-blue-500 hover:bg-blue-50 rounded-lg">
                                    🐦 Share to X / Twitter
                                </a>
                            </div>
                        </div>

                        <a href="{{ route('student.discussions.export', [$group->group_id, $topic->topic_id]) }}"
                           class="px-4 py-2 rounded-lg bg-blue-50 text-blue-600 text-sm font-medium">
                            Export PDF
                        </a>
                    </div>

                    <!-- Delete Discussion Topic (Visible only to Topic Creator) -->
                    @if($topic->created_by == auth()->id())
                        <form method="POST" action="{{ route('student.discussions.destroy', [$group->group_id, $topic->topic_id]) }}" onsubmit="return confirm('Delete this entire discussion topic along with all questions and answers?');">
                            @csrf
                            @method('DELETE')
                            <button type="submit" class="px-4 py-2 rounded-lg bg-red-50 text-red-600 hover:bg-red-100 text-sm font-medium transition">
                                Delete Discussion
                            </button>
                        </form>
                    @endif
                </div>
            </div>
        </div>
    </div>

    <div class="max-w-5xl mx-auto px-6 py-10 space-y-8">

        <!-- Topic Description -->
        <div class="bg-white rounded-2xl border border-slate-200 p-8">
            <h2 class="text-lg font-semibold text-slate-900">
                Discussion Description
            </h2>
            <p class="mt-4 text-slate-600 leading-relaxed">
                {{ $topic->description }}
            </p>
        </div>

        <!-- Questions and Answers -->
        <div class="bg-white rounded-2xl border border-slate-200 p-8">
            <h2 class="text-xl font-semibold text-slate-900">
                Questions & Answers
            </h2>

            <div class="mt-6 space-y-8">
            @forelse($messages as $question)
                <div class="border-b border-slate-100 pb-6">

                    <!-- Question -->
                    <div class="bg-white border border-slate-200 rounded-xl p-5">
                        <div class="flex justify-between items-center">
                            <div>
                                <span class="text-xs font-semibold text-blue-700 uppercase">
                                    Question
                                </span>
                                <h3 class="font-semibold text-slate-900 mt-1">
                                    {{ $question->sender->name }}
                                </h3>
                            </div>
                            <span class="text-xs text-slate-400">
                                {{ $question->posted_at->diffForHumans() }}
                            </span>
                        </div>

                        <p class="mt-4 text-slate-700 leading-relaxed">
                            {{ $question->msg_txt }}
                        </p>
                    </div>

                    <div class="mt-4 flex gap-3">
                        <form method="POST" action="{{ route('student.groups.discussions.messages.upvote', [$group->group_id, $question->msg_id]) }}">
                            @csrf
                            <button class="px-3 py-2 rounded-lg bg-blue-50 text-blue-600 text-sm">
                                👍 {{ $question->votes_count }}
                            </button>
                        </form>

                        @if(auth()->id() == $question->sender_id)
                        <form method="POST" action="{{ route('student.groups.discussions.messages.destroy', [$group->group_id, $topic->topic_id, $question->msg_id]) }}">
                            @csrf
                            @method('DELETE')
                            <button onclick="return confirm('Delete this question?')" class="px-3 py-2 rounded-lg bg-red-50 text-red-600 text-sm">
                                Delete
                            </button>
                        </form>
                        @endif
                    </div>

                    <!-- Replies -->
                    @foreach($question->replies as $reply)
                        <x-discussion-reply 
                            :reply="$reply" 
                            :question="$question" 
                            :group="$group" 
                            :topic="$topic" 
                            :isMember="$isMember" 
                        />
                    @endforeach

                    <!-- Reply To Question -->
                    @if($isMember)
                    <form method="POST" action="{{ route('student.groups.discussions.messages.reply', [$group->group_id, $topic->topic_id, $question->msg_id]) }}">
                        @csrf
                        <textarea name="msg_txt" rows="3" placeholder="Write an answer..." class="mt-5 w-full rounded-xl border-slate-200"></textarea>
                        <button class="mt-3 px-5 py-2 bg-blue-600 text-white rounded-lg">
                            Post Answer
                        </button>
                    </form>
                    @endif
                </div>
            @empty
                <p class="text-slate-500">
                    No questions have been asked yet.
                </p>
            @endforelse
            </div>
        </div>

        <!-- Ask Question -->
        @if($isMember)
        <div class="bg-white rounded-2xl border border-slate-200 p-8">
            <h2 class="font-semibold text-lg">
                Ask a Question
            </h2>

            <form method="POST" action="{{ route('student.groups.discussions.messages.store', [$group->group_id, $topic->topic_id]) }}">
                @csrf
                <textarea name="msg_txt" rows="5" placeholder="Ask something about this discussion..." class="mt-4 w-full rounded-xl border-slate-200"></textarea>
                <button class="mt-4 px-6 py-3 bg-blue-600 text-white rounded-xl">
                    Post Question
                </button>
            </form>
        </div>
        @else
        <div class="bg-yellow-50 border border-yellow-200 rounded-xl p-6">
            <h2 class="font-semibold text-yellow-800">
                Join this group to participate
            </h2>
            <p class="mt-2 text-yellow-700">
                You can view questions and answers, but you must join the group before asking questions or replying.
            </p>
            <a href="{{ route('student.groups.join', $group->group_id) }}" class="inline-block mt-4 px-5 py-3 bg-blue-600 text-white rounded-xl">
                Join Group
            </a>
        </div>
        @endif

    </div>
</div>

@endsection