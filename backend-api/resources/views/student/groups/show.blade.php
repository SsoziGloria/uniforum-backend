@extends('layouts.student')

@section('title', 'Group Details - UniForum')

@section('page-title', 'Group Details')

@section('content')

<div>

    <!-- Group Banner -->
    <div class="bg-gradient-to-r from-blue-600 to-blue-800">
        <div class="max-w-7xl mx-auto px-6 py-12 text-white">
            <a href="/student/groups" class="text-sm text-white-600 hover:underline">
                ← Back to Groups
            </a>

            <h1 class="mt-4 text-4xl font-bold">
                {{ $group->group_name }}
            </h1>

            <p class="mt-3 text-blue-100 max-w-3xl">
                {{ $group->description }}
            </p>

            <div class="mt-8 flex flex-wrap gap-8">
                <div>
                  <p class="text-blue-200 text-sm">Members</p>
                  <p class="text-2xl font-bold">{{ $group->members_count }}</p>
                </div>

                <div>
                  <p class="text-blue-200 text-sm">Group Role</p>
                    <p class="text-2xl font-bold">
                      {{ ucfirst($userRole ?? 'Not a Member') }}
                    </p>
                </div>
            </div>
        </div>
    </div>

    <div class="max-w-7xl mx-auto px-6 py-10">
        <div class="grid lg:grid-cols-3 gap-8">

            <!-- Main Content -->
            <div class="lg:col-span-2 space-y-8">

                <!-- Quick Actions -->
                <div class="bg-white rounded-2xl border border-slate-200 p-6">
                    <h2 class="text-lg font-semibold mb-5">
                        Quick Actions
                    </h2>

                    <div class="grid sm:grid-cols-3 gap-4">
                    @if($isMember)
                       <a href="{{ route('student.discussions.index', $group->group_id) }}"
                           class="flex items-center justify-center rounded-xl bg-blue-600 text-white py-3 hover:bg-blue-700">
                            Discussions
                        </a>

                        <a href="{{ route('student.discussions.create', $group->group_id) }}"
                           class="flex items-center justify-center rounded-xl bg-blue-600 text-white py-3 hover:bg-blue-700">
                             New Discussion
                        </a>
                        
                        <a href="{{ route('student.groups.chat', $group->group_id) }}"
                           class="flex items-center justify-center rounded-xl bg-blue-600 text-white py-3 hover:bg-blue-700">
                            Group Chat
                        </a>

                        <!-- Quizzes Button -->
                        <a href="{{ route('student.groups.quizzes.index', $group->group_id) }}"
                        class="flex items-center justify-center rounded-xl bg-blue-600 text-white py-3 hover:bg-blue-700 font-medium">
                            Quizzes
                        </a>
                    @else
                        <a href="{{ route('student.groups.join',$group->group_id) }}"
                           class="flex items-center justify-center rounded-xl bg-blue-600 text-white py-3 hover:bg-blue-700">
                              Join Group
                        </a>
                    @endif
                    </div>
                </div>

                <!-- Recent Discussions -->
                <div class="bg-white rounded-2xl border border-slate-200">
                    <div class="px-6 py-5 border-b border-slate-200">
                        <h2 class="font-semibold text-lg">
                            Recent Discussions
                        </h2>
                    </div>

                    <div class="divide-y divide-slate-100">
                    @forelse($topics as $topic)
                        <a href="{{ route('student.discussions.show', [$group->group_id, $topic->topic_id]) }}"
                           class="block p-6 hover:bg-slate-50 transition">
                            <h3 class="font-semibold text-slate-900">
                                {{ $topic->title }}
                            </h3>
                            <p class="text-sm text-slate-500 mt-2">
                                {{ $topic->messages_count }} replies . {{ $topic->created_at->diffForHumans() }}
                            </p>
                        </a>
                    @empty
                        <div class="p-6 text-slate-500">
                        No discussions have been created yet.
                        </div>
                    @endforelse
                    </div>
                </div>

                <!-- Active Members -->
                <div class="bg-white rounded-2xl border border-slate-200 p-6">
                    <h2 class="font-semibold text-lg mb-5">
                        Active Members
                    </h2>

                    <div class="space-y-4">
                    @forelse($members as $member)
                        <div class="flex items-center gap-3">
                            <div class="w-10 h-10 rounded-full bg-blue-600 text-white flex items-center justify-center font-semibold">
                                     {{ strtoupper(substr($member->name,0,1)) }}
                            </div>
                            <div>
                                <p class="font-medium">
                                    {{ $member->name }}
                                </p>
                                <p class="text-xs text-slate-500">
                                    {{ ucfirst($member->pivot->role) }}
                                </p>
                            </div>
                        </div>
                    @empty
                        <p class="text-slate-500">
                            No members yet.
                        </p>
                    @endforelse
                    </div>
                </div>
            </div>

            <!-- Sidebar -->
            <div class="space-y-8">

                <!-- Group Information -->
                <div class="bg-white rounded-2xl border border-slate-200 p-6">
                    <h2 class="font-semibold text-lg mb-5">
                        Group Information
                    </h2>

                    <div class="space-y-4 text-sm">
                        <div class="flex justify-between">
                            <span class="text-slate-500">Created</span>
                            <span>{{ $group->created_at->format('M Y') }}</span>
                        </div>

                        <div class="flex justify-between">
                            <span class="text-slate-500">Group Creator</span>
                            <span>{{ $group->creator->name }}</span>
                        </div>
                    </div>
                </div>

                <!-- Group Management & Danger Zone -->
                @if($group->created_by == auth()->id() || $isAdmin)
                <div class="bg-white rounded-2xl border border-slate-200 p-6 space-y-4">
                    <h2 class="font-semibold text-lg">Admin controls</h2>

                    <div class="space-y-3">
                        <a href="{{ route('student.groups.members', $group->group_id) }}"
                           class="block w-full text-left px-4 py-3 rounded-xl bg-blue-50 text-blue-700 hover:bg-blue-100 font-medium">
                               Manage Members
                        </a>

                        <a href="{{ route('student.groups.statistics',$group->group_id) }}"
                            class="block w-full text-left px-4 py-3 rounded-xl bg-blue-50 text-blue-700 hover:bg-blue-100 font-medium">
                              View Statistics
                        </a>
                    </div>

                    <!-- Delete Group (Creator Only) -->
                    @if($group->created_by == auth()->id())
                    <div class="pt-4 border-t border-slate-100">
                        <form method="POST" action="{{ route('student.groups.destroy', $group->group_id) }}" onsubmit="return confirm('Are you sure you want to delete this group? All discussion topics, messages, and member records will be permanently removed.');">
                            @csrf
                            @method('DELETE')
                            <button type="submit" class="w-full text-center px-4 py-3 rounded-xl bg-red-50 text-red-600 hover:bg-red-100 font-semibold text-sm transition">
                                Delete Group
                            </button>
                        </form>
                    </div>
                    @endif
                </div>
                @endif

                <!-- Leave Group Button -->
                @if($isMember && $group->created_by != auth()->id())
                <div class="bg-white rounded-2xl border border-slate-200 p-6">
                    <form method="POST" action="{{ route('student.groups.leave', $group->group_id) }}" onsubmit="return confirm('Are you sure you want to leave this group?');">
                        @csrf
                        @method('DELETE')
                        <button type="submit" class="w-full text-center px-4 py-3 rounded-xl bg-amber-50 text-amber-700 hover:bg-amber-100 font-semibold text-sm transition">
                            Leave Group
                        </button>
                    </form>
                </div>
                @endif

                <!-- Participation -->
                @if($isMember)
                <div class="bg-white rounded-2xl border border-slate-200 p-6">
                    <h2 class="font-semibold text-lg mb-5">
                         My Participation
                    </h2>
                    <p class="text-sm text-slate-500 mb-4">
                         View your participation performance in this group.
                    </p>
                    <a href="{{ route('student.groups.participation.results', $group->group_id) }}"
                        class="block w-full px-4 py-3 rounded-xl bg-blue-50 text-blue-700 hover:bg-blue-100 text-center font-medium">
                         View My Results
                    </a>
                </div>
                @endif

            </div>
        </div>
    </div>
</div>

@endsection