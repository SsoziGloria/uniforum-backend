@extends('layouts.student')

@section('title', 'Student Dashboard')

@section('page-title', 'Dashboard')

@section('content')

<!-- Welcome Banner -->
<div class="bg-blue-600 rounded-3xl p-8 text-white mb-8 shadow-md">
    <h1 class="text-3xl font-bold">
        Welcome back, {{ auth()->user()->name ?? 'Student' }} 👋
    </h1>

    <p class="mt-3 text-blue-100 max-w-xl">
        Stay connected with your university discussions, discover recommended topics, and track your group participation score.
    </p>

    <a href="{{ route('student.groups.index') }}"
       class="inline-block mt-6 bg-white text-blue-600 px-5 py-3 rounded-xl font-semibold hover:bg-blue-50 transition">
        Explore Groups &rarr;
    </a>
</div>

<!-- Statistics Cards -->
<div class="grid md:grid-cols-4 gap-6">

    <div class="bg-white rounded-2xl border border-slate-200 p-6 hover:shadow-md transition">
        <div class="text-3xl mb-3">💬</div>
        <p class="text-sm text-slate-500 font-medium">Questions Asked</p>
        <h3 class="text-3xl font-bold mt-2 text-slate-900">{{ $questionsAskedCount }}</h3>
    </div>

    <div class="bg-white rounded-2xl border border-slate-200 p-6 hover:shadow-md transition">
        <div class="text-3xl mb-3">⭐</div>
        <p class="text-sm text-slate-500 font-medium">Participation Score</p>
        <div class="flex items-baseline gap-2 mt-2">
            <h3 class="text-3xl font-bold text-slate-900">{{ $participationScore }}%</h3>
            <span class="text-xs font-semibold text-emerald-600 bg-emerald-50 px-2 py-0.5 rounded-full">Active</span>
        </div>
    </div>

    <div class="bg-white rounded-2xl border border-slate-200 p-6 hover:shadow-md transition">
        <div class="text-3xl mb-3">📚</div>
        <p class="text-sm text-slate-500 font-medium">Topics Available</p>
        <h3 class="text-3xl font-bold mt-2 text-slate-900">{{ $topicsFollowingCount }}</h3>
    </div>

    <div class="bg-white rounded-2xl border border-slate-200 p-6 hover:shadow-md transition">
        <div class="text-3xl mb-3">📝</div>
        <p class="text-sm text-slate-500 font-medium">Pending Quizzes</p>
        <h3 class="text-3xl font-bold mt-2 text-slate-900">{{ $pendingQuizzesCount }}</h3>
    </div>

</div>

<!-- Main Content Grid -->
<div class="grid lg:grid-cols-3 gap-6 mt-8">

    <!-- Recommended Topics (AI Powered, now live) -->
    <div class="lg:col-span-2 bg-white rounded-2xl border border-slate-200 p-6">
        <div class="flex justify-between items-center mb-5">
            <h2 class="text-xl font-bold text-slate-900">
                Recommended Topics
            </h2>
            <span class="text-xs font-bold uppercase tracking-wider text-blue-600 bg-blue-50 px-3 py-1 rounded-full">
                AI Powered
            </span>
        </div>

        <div class="space-y-4">
            @forelse($recommendedTopics as $topic)
                <div class="p-4 bg-slate-50 rounded-xl flex items-center justify-between border border-slate-100">
                    <div>
                        <h3 class="font-semibold text-slate-800">{{ $topic->title }}</h3>
                        <p class="text-sm text-slate-500">{{ $topic->ml_category ?? 'General' }}</p>
                    </div>
                    <a href="{{ route('student.discussions.show', [$topic->group_id, $topic->topic_id]) }}"
                       class="text-blue-600 font-semibold text-sm hover:underline">
                        View
                    </a>
                </div>
            @empty
                <div class="text-center py-6">
                    <p class="text-slate-400 text-sm">No recommendations yet.</p>
                    <a href="{{ route('student.groups.index') }}" class="text-xs text-blue-600 hover:underline mt-1 inline-block">
                        Join a discussion
                    </a>
                </div>
            @endforelse
        </div>
    </div>

    <!-- Recent Activity -->
    <div class="bg-white rounded-2xl border border-slate-200 p-6">
        <h2 class="text-xl font-bold text-slate-900 mb-5">
            Recent Activity
        </h2>

        <div class="space-y-4">
            @forelse($recentActivity as $activity)
                <div class="flex items-start justify-between gap-3 text-sm pb-3 border-b border-slate-100 last:border-0 last:pb-0">
                    <div class="flex items-center gap-2">
                        <span>{{ $activity['icon'] }}</span>
                        <span class="text-slate-700 font-medium">{{ $activity['title'] }}</span>
                    </div>
                    <span class="text-xs text-slate-400 whitespace-nowrap">{{ $activity['time'] }}</span>
                </div>
            @empty
                <div class="text-center py-6">
                    <p class="text-slate-400 text-sm">No recent activity found.</p>
                    <a href="{{ route('student.groups.index') }}" class="text-xs text-blue-600 hover:underline mt-1 inline-block">
                        Join a discussion
                    </a>
                </div>
            @endforelse
        </div>
    </div>

</div>

@endsection
