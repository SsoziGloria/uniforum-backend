@extends('layouts.student')

@section('title', 'Group Chat - UniForum')

@section('page-title', 'Group Chat')

@section('content')

<div class="min-h-screen bg-slate-50">

    <div class="max-w-5xl mx-auto px-6 py-8 space-y-6">

        <!-- Chat Header -->
        <div class="bg-white rounded-2xl border border-slate-200 p-6 flex items-center justify-between">
            <div>
                <a href="{{ route('student.groups.show', $group->group_id) }}" class="text-sm text-blue-600 hover:underline">
                    ← Back to Group
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
                            <div class="bg-blue-600 text-white rounded-xl p-4 relative">
                                {{ $message->msg_txt }}
                            </div>
                            <div class="flex items-center justify-end gap-3 mt-1">
                                <p class="text-xs text-slate-500">
                                   {{ $message->posted_at->diffForHumans() }}
                                </p>
                                <!-- Delete Button -->
                                <form method="POST" action="{{ route('student.groups.chat.messages.destroy', ['group' => $group->group_id, 'message' => $message->msg_id]) }}" onsubmit="return confirm('Delete this message?');">
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
                            <div class="w-10 h-10 rounded-full bg-blue-600 text-white flex items-center justify-center font-semibold">
                               {{ strtoupper(substr($message->sender->name ?? 'U',0,1)) }}
                            </div>
                            <div>
                                <p class="font-medium text-slate-800">
                                    {{ $message->sender->name ?? 'User' }}
                                </p>
                                <p class="text-xs text-slate-500">
                                    {{ $message->posted_at->diffForHumans() }}
                                </p>
                            </div>
                        </div>
                        <div class="mt-3 bg-slate-100 rounded-xl p-4 max-w-xl">
                            {{ $message->msg_txt }}
                        </div>
                    </div>
                @endif
            @endforeach

        </div>

        <!-- Message Input -->
        <div class="bg-white rounded-2xl border border-slate-200 p-6">
            <form method="POST" action="{{ route('student.groups.chat.store', $group->group_id) }}">
                @csrf

                <textarea
                    name="msg_txt"
                    rows="3"
                    placeholder="Write a message..."
                    class="w-full rounded-xl border-slate-200 focus:ring-blue-500 focus:border-blue-500" required></textarea>

                <!-- Message Visibility + Exclude Members Wrapper -->
                <div x-data="{ restricted: false }">
                    <div class="mt-5">
                        <h3 class="font-semibold text-slate-800">Message Visibility</h3>
                        <p class="text-sm text-slate-500 mt-1">Choose who can view this message.</p>

                        <div class="mt-4 space-y-3">
                            <label class="flex items-center gap-3">
                                <input type="radio" name="is_restricted" value="0" checked x-on:change="restricted=false">
                                <span>All group members</span>
                            </label>

                            <label class="flex items-center gap-3">
                                <input type="radio" name="is_restricted" value="1" x-on:change="restricted=true">
                                <span>Select members who should NOT see this message</span>
                            </label>
                        </div>
                    </div>

                    <!-- Exclude Members -->
                    <div x-show="restricted" x-transition class="mt-6 bg-slate-50 rounded-xl p-5">
                        <h3 class="font-semibold text-slate-800">Exclude Members</h3>
                        <p class="text-sm text-slate-500 mt-1">Select members who should not receive this message.</p>

                        <div class="mt-4 space-y-3">
                            @foreach($members as $member)
                               @if($member->user_id != auth()->id())
                                <label class="flex items-center gap-3">
                                    <input type="checkbox" name="excluded_user_ids[]" value="{{ $member->user_id }}">
                                    <span>
                                       {{ $member->user->name }}
                                        <span class="text-sm text-slate-500">({{ ucfirst($member->role) }})</span>
                                    </span>
                                </label>
                                @endif
                            @endforeach
                        </div>
                    </div>
                </div>

                <!-- Send Button -->
                <div class="mt-6 flex justify-end">
                    <button type="submit" class="px-6 py-3 bg-blue-600 text-white rounded-xl hover:bg-blue-700 font-medium">
                        Send Message
                    </button>
                </div>
            </form>
        </div>

    </div>

</div>

@endsection