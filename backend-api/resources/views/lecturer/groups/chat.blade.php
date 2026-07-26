@extends('layouts.lecturer')

@section('title', 'Group Chat - UniForum')

@section('page-title', 'Group Chat')

@section('content')

<div class="min-h-screen bg-slate-50">

    <div class="max-w-5xl mx-auto px-6 py-8 space-y-6">

        <!-- Chat Header -->
        <div class="bg-white rounded-2xl border border-slate-200 p-6 flex items-center justify-between">
            <div>
                <a href="{{ route('lecturer.groups.show', $group->group_id ?? $group->id) }}" class="text-sm text-blue-600 hover:underline flex items-center gap-2">
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7"/>
                    </svg>
                    Back to Group
                </a>
                <h1 class="mt-2 text-2xl font-bold text-slate-900">
                    {{ $group->group_name }} Discussion Group Chat
                </h1>
                <p class="text-slate-500 text-sm mt-1">
                    Communicate with members of this group.
                </p>
            </div>
        </div>

        <!-- Messages Area -->
        <div class="bg-white rounded-2xl border border-slate-200 p-6 h-[500px] overflow-y-auto">

            <!-- Empty Chat State -->
            @if($messages->isEmpty())
                <div class="text-center py-20">
                    <div class="text-4xl mb-4">💬</div>
                    <h3 class="text-lg font-semibold text-slate-800">No messages yet</h3>
                    <p class="text-slate-500 mt-2">Start the conversation with your group members.</p>
                </div>
            @endif

            <!-- Messages List -->
            @foreach($messages as $message)
                @if($message->sender_id == auth()->id())
                    <!-- User's own message (right side) -->
                    <div class="mb-6 flex justify-end group">
                        <div class="max-w-xl">
                            <div class="bg-blue-600 text-white rounded-xl p-4 relative shadow-sm">
                                {{ $message->msg_txt }}
                            </div>
                            <div class="flex items-center justify-end gap-3 mt-1">
                                <p class="text-xs text-slate-500">
                                   {{ optional($message->posted_at)->diffForHumans() }}
                                </p>
                                <!-- Delete Button -->
                                <form method="POST" action="{{ route('lecturer.groups.chat.messages.destroy', ['group' => $group->group_id ?? $group->id, 'message' => $message->msg_id ?? $message->id]) }}" onsubmit="return confirm('Delete this message?');">
                                    @csrf
                                    @method('DELETE')
                                    <button type="submit" class="text-xs text-red-500 hover:underline opacity-0 group-hover:opacity-100 transition-opacity">
                                        Delete
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>
                @else
                    <!-- Other members' messages (left side) -->
                    <div class="mb-6">
                        <div class="flex items-center gap-3">
                            <div class="w-10 h-10 rounded-full bg-blue-600 text-white flex items-center justify-center font-semibold text-sm">
                               {{ strtoupper(substr(optional($message->sender)->name ?? 'U', 0, 1)) }}
                            </div>
                            <div>
                                <p class="font-medium text-slate-800 text-sm">
                                    {{ optional($message->sender)->name ?? 'User' }}
                                </p>
                                <p class="text-xs text-slate-500">
                                    {{ optional($message->posted_at)->diffForHumans() }}
                                </p>
                            </div>
                        </div>
                        <div class="mt-3 bg-slate-100 text-slate-800 rounded-xl p-4 max-w-xl shadow-sm text-sm">
                            {{ $message->msg_txt }}
                        </div>
                    </div>
                @endif
            @endforeach

        </div>

        <!-- Message Input -->
        <div class="bg-white rounded-2xl border border-slate-200 p-6">
            <form method="POST" action="{{ route('lecturer.groups.chat.store', $group->group_id ?? $group->id) }}">
                @csrf

                <textarea
                    name="msg_txt"
                    rows="3"
                    placeholder="Write a message..."
                    class="w-full rounded-xl border-slate-200 focus:ring-blue-500 focus:border-blue-500 text-sm" required></textarea>

                <!-- Message Visibility + Exclude Members Wrapper -->
                <div x-data="{ restricted: false }">
                    <div class="mt-5">
                        <h3 class="font-semibold text-slate-800">Message Visibility</h3>
                        <p class="text-sm text-slate-500 mt-1">Choose who can view this message.</p>

                        <div class="mt-4 space-y-3 text-sm">
                            <label class="flex items-center gap-3 cursor-pointer">
                                <input type="radio" name="is_restricted" value="0" checked x-on:change="restricted = false" class="text-blue-600 focus:ring-blue-500">
                                <span class="text-slate-700">All group members</span>
                            </label>

                            <label class="flex items-center gap-3 cursor-pointer">
                                <input type="radio" name="is_restricted" value="1" x-on:change="restricted = true" class="text-blue-600 focus:ring-blue-500">
                                <span class="text-slate-700">Select members who should NOT see this message</span>
                            </label>
                        </div>
                    </div>

                    <!-- Exclude Members -->
                    <div x-show="restricted" x-transition class="mt-6 bg-slate-50 rounded-xl p-5 border border-slate-200">
                        <h3 class="font-semibold text-slate-800">Exclude Members</h3>
                        <p class="text-sm text-slate-500 mt-1">Select members who should not receive this message.</p>

                        <div class="mt-4 space-y-3 text-sm">
                            @foreach($members as $member)
                                @if(($member->user_id ?? $member->id) != auth()->id())
                                    <label class="flex items-center gap-3 cursor-pointer">
                                        <input type="checkbox" name="excluded_user_ids[]" value="{{ $member->user_id ?? $member->id }}" class="rounded border-slate-300 text-blue-600 focus:ring-blue-500">
                                        <span class="text-slate-700">
                                            {{ optional($member->user)->name ?? $member->name }}
                                            <span class="text-xs text-slate-500">({{ ucfirst($member->role ?? optional($member->pivot)->role ?? 'Member') }})</span>
                                        </span>
                                    </label>
                                @endif
                            @endforeach
                        </div>
                    </div>
                </div>

                <!-- Send Button -->
                <div class="mt-6 flex justify-end">
                    <button type="submit" class="px-6 py-3 bg-blue-600 text-white rounded-xl hover:bg-blue-700 font-medium text-sm transition">
                        Send Message
                    </button>
                </div>
            </form>
        </div>

    </div>

</div>

@endsection