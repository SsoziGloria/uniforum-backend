@extends('layouts.lecturer')

@section('title', 'Group Details - UniForum')

@section('page-title', 'Group Details')

@section('content')

<div>

    <!-- Group Banner -->
    <div class="bg-gradient-to-r from-blue-600 to-blue-800">
        <div class="max-w-7xl mx-auto px-6 py-12 text-white">
            <a href="{{ route('lecturer.groups.index') }}" class="text-sm text-blue-100 hover:underline flex items-center gap-2">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7"/>
                </svg>
                Back to Groups
            </a>

            <h1 class="mt-4 text-4xl font-bold">
                {{ $group->group_name }}
            </h1>

            <p class="mt-3 text-blue-100 max-w-3xl">
                {{ $group->description ?? 'No description provided for this group.' }}
            </p>

            <div class="mt-8 flex flex-wrap gap-8">
                <div>
                    <p class="text-blue-200 text-sm">Members</p>
                    <p class="text-2xl font-bold">{{ $group->members_count ?? $group->members()->count() }}</p>
                </div>

                <div>
                    <p class="text-blue-200 text-sm">Your Group Role</p>
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
                <h2 class="text-lg font-semibold mb-5 text-slate-900">
                    Quick Actions
                </h2>

                <div class="grid sm:grid-cols-3 gap-4">
                    @if($isMember)
                        <a href="{{ route('lecturer.groups.discussions.index', $group->group_id ?? $group->id) }}"
                        class="flex items-center justify-center rounded-xl bg-blue-600 text-white py-3 hover:bg-blue-700 transition font-medium text-sm">
                            Discussions
                        </a>

                        <a href="{{ route('lecturer.groups.discussions.create', $group->group_id ?? $group->id) }}"
                        class="flex items-center justify-center rounded-xl bg-blue-600 text-white py-3 hover:bg-blue-700 transition font-medium text-sm">
                            New Discussion
                        </a>
                        
                        <a href="{{ route('lecturer.groups.chat', $group->group_id ?? $group->id) }}"
                        class="flex items-center justify-center rounded-xl bg-blue-600 text-white py-3 hover:bg-blue-700 transition font-medium text-sm">
                            Group Chat
                        </a>

                        <a href="{{ route('lecturer.groups.quizzes.index', $group->group_id ?? $group->id) }}"
                          class="flex items-center justify-center rounded-xl bg-blue-600 text-white py-3 hover:bg-blue-700 font-medium transition text-sm">

                            Quizzes
                        </a>
                    @else
                        <a href="{{ route('lecturer.groups.join', $group->group_id ?? $group->id) }}"
                        class="col-span-full flex items-center justify-center rounded-xl bg-blue-600 text-white py-3 hover:bg-blue-700 transition font-medium text-sm">
                            Join Group
                        </a>
                    @endif
                </div>
            </div>

                <!-- Recent Discussions -->
                <div class="bg-white rounded-2xl border border-slate-200">
                    <div class="px-6 py-5 border-b border-slate-200">
                        <h2 class="font-semibold text-lg text-slate-900">
                            Recent Discussions
                        </h2>
                    </div>

                    <div class="divide-y divide-slate-100">
                        @forelse($topics ?? [] as $topic)
                            <a href="#"
                               class="block p-6 hover:bg-slate-50 transition">
                                <h3 class="font-semibold text-slate-900">
                                    {{ $topic->title }}
                                </h3>
                                <p class="text-sm text-slate-500 mt-2">
                                    {{ $topic->messages_count ?? 0 }} replies • {{ optional($topic->created_at)->diffForHumans() }}
                                </p>
                            </a>
                        @empty
                            <div class="p-6 text-slate-500 text-center text-sm">
                                No discussions have been created yet.
                            </div>
                        @endforelse
                    </div>
                </div>

                <!-- Active Members -->
                <div class="bg-white rounded-2xl border border-slate-200 p-6">
                    <h2 class="font-semibold text-lg mb-5 text-slate-900">
                        Active Members
                    </h2>

                    <div class="space-y-4">
                        @forelse($members ?? [] as $member)
                            <div class="flex items-center gap-3">
                                <div class="w-10 h-10 rounded-full bg-blue-600 text-white flex items-center justify-center font-semibold text-sm">
                                    {{ strtoupper(substr($member->name, 0, 1)) }}
                                </div>
                                <div>
                                    <p class="font-medium text-slate-900 text-sm">
                                        {{ $member->name }}
                                    </p>
                                    <p class="text-xs text-slate-500">
                                        {{ ucfirst(optional($member->pivot)->role ?? 'Member') }}
                                    </p>
                                </div>
                            </div>
                        @empty
                            <p class="text-slate-500 text-sm text-center">
                                No members in this group yet.
                            </p>
                        @endforelse
                    </div>
                </div>

            </div>

            <!-- Sidebar -->
            <div class="space-y-8">

                <!-- Group Information -->
                <div class="bg-white rounded-2xl border border-slate-200 p-6">
                    <h2 class="font-semibold text-lg mb-5 text-slate-900">
                        Group Information
                    </h2>

                    <div class="space-y-4 text-sm">
                        <div class="flex justify-between">
                            <span class="text-slate-500">Created</span>
                            <span class="font-medium text-slate-800">{{ optional($group->created_at)->format('M Y') }}</span>
                        </div>

                        <div class="flex justify-between">
                            <span class="text-slate-500">Group Creator</span>
                            <span class="font-medium text-slate-800">{{ optional($group->creator)->name ?? 'System' }}</span>
                        </div>
                    </div>
                </div>

                <!-- Admin & Lecturer Controls (Requires Group Membership) -->
                @if($isMember && ($group->created_by == auth()->id() || $isAdmin || auth()->user()->role === 'lecturer'))
                    <div class="bg-white rounded-2xl border border-slate-200 p-6 space-y-4">
                        <h2 class="font-semibold text-lg text-slate-900">Admin Controls</h2>

                        <div class="space-y-3">
                            <a href="{{ route('lecturer.groups.members', $group->group_id ?? $group->id) }}"
                               class="block w-full text-left px-4 py-3 rounded-xl bg-blue-50 text-blue-700 hover:bg-blue-100 font-medium text-sm transition">
                                Manage Members
                            </a>

                            <a href="{{ route('lecturer.groups.statistics', $group->group_id ?? $group->id) }}"
                               class="block w-full text-left px-4 py-3 rounded-xl bg-blue-50 text-blue-700 hover:bg-blue-100 font-medium text-sm transition">
                                View Statistics
                            </a>
                        </div>

                        <!-- Delete Group (Creator Only) -->
                        @if($group->created_by == auth()->id())
                            <div class="pt-4 border-t border-slate-100">
                                <form method="POST" action="#" onsubmit="return confirm('Are you sure you want to delete this group? All topics and data will be permanently removed.');">
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

                <!-- Leave Group Button (Same as student layout) -->
                @if($isMember && $group->created_by != auth()->id())
                    <div class="bg-white rounded-2xl border border-slate-200 p-6">
                        <form method="POST" action="{{ route('lecturer.groups.leave', $group->group_id ?? $group->id) }}" onsubmit="return confirm('Are you sure you want to leave this group?');">
                            @csrf
                            @method('DELETE')
                            <button type="submit" class="w-full text-center px-4 py-3 rounded-xl bg-amber-50 text-amber-700 hover:bg-amber-100 font-semibold text-sm transition">
                                Leave Group
                            </button>
                        </form>
                    </div>
                @endif

                <!-- Participation Management (Requires Group Membership) -->
                @if($isMember)
                    <div class="bg-white rounded-2xl border border-slate-200 p-6">
                        <h2 class="font-semibold text-lg mb-3 text-slate-900">
                            Participation
                        </h2>

                        <p class="text-sm text-slate-500 mb-4">
                            Configure participation criteria and monitor student performance.
                        </p>

                        <div class="space-y-3">
                            <a href="{{ route('lecturer.groups.participation.settings', $group->group_id ?? $group->id) }}"
                               class="block w-full text-center px-4 py-3 rounded-xl bg-blue-50 text-blue-700 hover:bg-blue-100 font-medium text-sm transition">
                                Participation Settings
                            </a>

                            <a href="{{ route('lecturer.groups.participation', $group->group_id ?? $group->id) }}"
                               class="block w-full text-center px-4 py-3 rounded-xl bg-blue-50 text-blue-700 hover:bg-blue-100 font-medium text-sm transition">
                                View Participation Scores
                            </a>
                        </div>
                    </div>
                @endif

            </div>
        </div>
    </div>
</div>

@endsection