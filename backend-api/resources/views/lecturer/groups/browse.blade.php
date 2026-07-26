@extends('layouts.lecturer')

@section('title', 'Browse Groups - UniForum')

@section('page-title', 'Browse Groups')

@section('content')

<div class="min-h-screen bg-slate-50">

    <!-- Header -->
    <div class="bg-white border-b border-slate-200">
        <div class="max-w-7xl mx-auto px-6 py-8">
            <a href="{{ route('lecturer.groups.index') }}"
               class="text-sm text-blue-600 hover:underline flex items-center gap-2">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7"/>
                </svg>
                Back to Groups
            </a>

            <h1 class="text-3xl font-bold text-slate-900 mt-2">
                Browse Groups
            </h1>

            <p class="mt-2 text-slate-500">
                Discover academic groups created by students and lecturers across the system.
            </p>
        </div>
    </div>

    <div class="max-w-7xl mx-auto px-6 py-10">

        <!-- Search & Actions -->
        <div class="flex flex-col md:flex-row gap-4 justify-between items-center mb-8">
            <form method="GET" action="{{ route('lecturer.groups.browse') }}" class="w-full md:w-2/3 flex gap-2">
                <div class="relative flex-1">
                    <input
                        type="text"
                        name="search"
                        value="{{ request('search') }}"
                        placeholder="Search groups by name or description..."
                        class="w-full rounded-xl border-slate-200 pr-10 focus:ring-blue-500 focus:border-blue-500">

                    @if(request('search'))
                        <a href="{{ route('lecturer.groups.browse') }}" 
                           class="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600 text-sm font-semibold">
                            ✕
                        </a>
                    @endif
                </div>

                <button type="submit" 
                        class="bg-slate-800 text-white px-5 py-2.5 rounded-xl hover:bg-slate-900 transition font-medium">
                    Search
                </button>
            </form>

            <a href="{{ route('lecturer.groups.create') }}"
               class="bg-blue-600 text-white px-5 py-3 rounded-xl hover:bg-blue-700 transition font-medium whitespace-nowrap">
                + Create Group
            </a>
        </div>

        <!-- Available Groups Grid -->
        <div class="grid md:grid-cols-2 xl:grid-cols-3 gap-8">
            @forelse($groups as $group)
                @php
                    $groupId = $group->group_id ?? $group->id;
                    $isMember = in_array($groupId, $joinedGroupIds ?? []);
                @endphp

                <div class="bg-white rounded-2xl border border-slate-200 p-6 flex flex-col justify-between">
                    <div>
                        <span class="px-3 py-1 rounded-full bg-blue-100 text-blue-700 text-xs font-semibold">
                            Academic Group
                        </span>

                        <h2 class="mt-5 text-xl font-bold text-slate-900">
                            {{ $group->group_name }}
                        </h2>

                        <p class="mt-3 text-slate-600 text-sm line-clamp-3">
                            {{ $group->description ?? 'No description provided.' }}
                        </p>
                    </div>

                    <div class="mt-6">
                        <div class="flex justify-between items-center mb-4">
                            <span class="text-sm text-slate-500">
                               {{ $group->members_count ?? $group->members()->count() }} Members
                            </span>
                        </div>

                        <div class="flex gap-3">
                            <a href="{{ route('lecturer.groups.show', $groupId) }}"
                               class="flex-1 text-center border border-slate-300 rounded-lg py-2 text-sm font-medium hover:bg-slate-50 transition">
                                 View Details
                            </a>

                            @if($isMember)
                                <a href="{{ route('lecturer.groups.show', $groupId) }}"
                                   class="flex-1 text-center bg-emerald-50 text-emerald-700 border border-emerald-200 rounded-lg py-2 text-sm font-semibold flex items-center justify-center gap-1">
                                    <span>✓ Joined</span>
                                </a>
                            @else
                                <a href="{{ route('lecturer.groups.join', $groupId) }}"
                                   class="flex-1 text-center bg-blue-600 text-white rounded-lg py-2 text-sm font-semibold hover:bg-blue-700 transition">
                                    Join Group
                                </a>
                            @endif
                        </div>
                    </div>
                </div>
            @empty
                <div class="col-span-full text-center text-slate-500 py-12 bg-white rounded-2xl border border-slate-200">
                    @if(request('search'))
                        No discussion groups found matching "<strong>{{ request('search') }}</strong>".
                    @else
                        No discussion groups found.
                    @endif
                </div>
            @endforelse
        </div>

    </div>

</div>

@endsection