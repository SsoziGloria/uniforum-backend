@extends('layouts.lecturer')

@section('title', 'Discussions - UniForum')

@section('page-title', 'Discussions')

@section('content')

<div>
    <!-- Header -->
    <div class="bg-white border-b border-slate-200">
        <div class="max-w-7xl mx-auto px-6 py-8">
            <div class="mb-4">
                <a href="{{ route('lecturer.groups.show', $group->group_id ?? $group->id) }}"
                   class="text-sm text-blue-600 hover:underline">
                    ← Back to Group Overview
                </a>
            </div>

            <div class="flex flex-col md:flex-row md:items-center md:justify-between gap-5">
                <div>
                    <h1 class="text-3xl font-bold text-slate-900">
                        {{ $group->group_name }} Discussions
                    </h1>
                    <p class="mt-2 text-slate-500">
                        Moderate discussions, answer questions, and connect with students.
                    </p>
                </div>

                <a href="{{ route('lecturer.groups.discussions.create', $group->group_id ?? $group->id) }}"
                   class="inline-flex items-center justify-center gap-2 bg-blue-600 text-white px-5 py-3 rounded-xl hover:bg-blue-700 transition">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m8-8H4"/>
                    </svg>
                    Create Discussion
                </a>
            </div>

            <!-- Search Form -->
            <div class="mt-8">
                <form method="GET" action="{{ route('lecturer.groups.discussions.index', $group->group_id ?? $group->id) }}">
                    <div class="relative">
                        <input type="text"
                               name="search"
                               value="{{ request('search') }}"
                               placeholder="Search discussions by title, description, or category..."
                               class="w-full rounded-xl border-slate-200 pl-12 pr-24 py-3 focus:ring-blue-500 focus:border-blue-500">

                        <svg class="absolute left-4 top-3.5 w-5 h-5 text-slate-400" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" d="m21 21-4.35-4.35m1.35-5.65a7 7 0 11-14 0 7 7 0 0114 0z"/>
                        </svg>

                        @if(request('search'))
                            <a href="{{ route('lecturer.groups.discussions.index', $group->group_id ?? $group->id) }}" 
                               class="absolute right-20 top-3 text-xs text-slate-400 hover:text-slate-600 bg-slate-100 px-2 py-1.5 rounded-lg">
                                Clear
                            </a>
                        @endif

                        <button type="submit" class="absolute right-2.5 top-2 bg-blue-600 text-white text-xs px-4 py-2 rounded-lg hover:bg-blue-700">
                            Search
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Alert Messages -->
    @if(session('success'))
        <div class="max-w-7xl mx-auto px-6 mt-6">
            <div class="p-4 rounded-xl bg-emerald-50 border border-emerald-200 text-emerald-700 text-sm">
                {{ session('success') }}
            </div>
        </div>
    @endif

    <!-- Main Content -->
    <div class="max-w-7xl mx-auto px-6 py-10">
        <div class="grid lg:grid-cols-3 gap-8">

            <!-- Discussion Topics -->
            <div class="lg:col-span-2 space-y-5">
                @forelse($topics as $topic)
                    <div class="bg-white rounded-2xl border border-slate-200 p-6 hover:shadow-md transition">
                        <div class="flex justify-between items-start">
                            <div>
                                <span class="text-xs font-semibold bg-blue-100 text-blue-700 px-3 py-1 rounded-full">
                                    {{ $topic->ml_category ?? 'General' }}
                                </span>

                                <h2 class="mt-4 text-xl font-semibold text-slate-900">
                                    {{ $topic->title }}
                                </h2>

                                @if($topic->description)
                                    <p class="mt-2 text-slate-500 text-sm line-clamp-2">
                                        {{ $topic->description }}
                                    </p>
                                @endif
                            </div>

                            <span class="text-xs text-slate-400 shrink-0 ml-4">
                                {{ $topic->created_at->diffForHumans() }}
                            </span>
                        </div>

                        <div class="mt-5 flex items-center justify-between text-sm text-slate-500">
                            <div>
                                <span>{{ $topic->messages_count }} replies</span>
                                <span class="mx-2">•</span>
                                <span>Posted by <strong class="text-slate-700">{{ $topic->creator->name ?? 'Unknown' }}</strong></span>
                            </div>

                            <div class="flex items-center gap-4">
                                <a href="{{ route('lecturer.groups.discussions.show', [$group->group_id ?? $group->id, $topic->topic_id]) }}"
                                   class="text-blue-600 font-medium hover:underline">
                                    View Discussion
                                </a>

                                @if($topic->created_by === auth()->id())
                                    <form action="{{ route('lecturer.groups.discussions.destroy', ['group' => $group->group_id ?? $group->id, 'topic' => $topic->topic_id]) }}" 
                                        method="POST" 
                                        onsubmit="return confirm('Are you sure you want to delete this topic?');">
                                        @csrf
                                        @method('DELETE')
                                        <button type="submit" class="text-xs text-red-600 hover:text-red-800 font-medium">
                                            Delete
                                        </button>
                                    </form>
                                @endif
                            </div>
                        </div>
                    </div>
                @empty
                    <div class="bg-white rounded-2xl border border-slate-200 p-8 text-slate-500 text-center">
                        @if(request('search'))
                            No discussions found matching "<strong>{{ request('search') }}</strong>".
                        @else
                            No discussions yet in this group.
                        @endif
                    </div>
                @endforelse
            </div>

            <!-- Sidebar -->
            <div class="space-y-6">
                <!-- Categories -->
                <div class="bg-white rounded-2xl border border-slate-200 p-6">
                    <h3 class="font-semibold text-lg text-slate-900">
                        Popular Topics
                    </h3>

                    <div class="mt-4 space-y-3">
                        @forelse($popularCategories as $category)
                            <div class="flex justify-between items-center text-sm">
                                <span class="text-slate-600 font-medium">
                                    {{ $category->ml_category }}
                                </span>
                                <span class="bg-blue-50 text-blue-600 font-semibold px-2.5 py-1 rounded-full text-xs">
                                    {{ $category->total }}
                                </span>
                            </div>
                        @empty
                            <p class="text-sm text-slate-500">
                                No categorized discussions yet.
                            </p>
                        @endforelse
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>

@endsection