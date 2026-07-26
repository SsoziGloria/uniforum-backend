@extends('layouts.lecturer')

@section('title', 'Lecturer Dashboard')

@section('page-title', 'Dashboard')

@section('content')

<!-- Welcome Banner -->
<div class="bg-blue-600 rounded-3xl p-8 text-white mb-8">
    <h1 class="text-3xl font-bold">
        Welcome back, {{ auth()->user()->name ?? 'Lecturer' }} 👋
    </h1>

    <p class="mt-3 text-blue-100 max-w-2xl">
        Manage discussions, monitor student participation, review quizzes, and guide academic conversations.
    </p>

    <a href="{{ route('lecturer.groups.index') }}"
       class="inline-block mt-6 bg-white text-blue-600 px-5 py-3 rounded-xl font-semibold hover:bg-blue-50 transition shadow-sm">
        Explore Groups
    </a>
</div>

<!-- Key Performance Statistics -->
<div class="grid md:grid-cols-4 gap-6">

    <div class="bg-white rounded-2xl border border-slate-200 p-6 shadow-sm">
        <div class="text-3xl mb-3">💬</div>
        <p class="text-sm font-medium text-slate-500">Active Discussions</p>
        <h3 class="text-3xl font-bold text-slate-800 mt-2">
            {{ number_format($activeDiscussions) }}
        </h3>
    </div>

    <div class="bg-white rounded-2xl border border-slate-200 p-6 shadow-sm">
        <div class="text-3xl mb-3">👨‍🎓</div>
        <p class="text-sm font-medium text-slate-500">Students Engaged</p>
        <h3 class="text-3xl font-bold text-slate-800 mt-2">
            {{ number_format($engagedStudents) }}
        </h3>
    </div>

    <div class="bg-white rounded-2xl border border-slate-200 p-6 shadow-sm">
        <div class="text-3xl mb-3">📝</div>
        <p class="text-sm font-medium text-slate-500">Active Quizzes</p>
        <h3 class="text-3xl font-bold text-slate-800 mt-2">
            {{ number_format($activeQuizzes) }}
        </h3>
    </div>

    <div class="bg-white rounded-2xl border border-slate-200 p-6 shadow-sm">
        <div class="text-3xl mb-3">⭐</div>
        <p class="text-sm font-medium text-slate-500">Average Participation</p>
        <h3 class="text-3xl font-bold text-slate-800 mt-2">
            {{ $avgParticipation }}%
        </h3>
    </div>

</div>

<!-- Content Grid -->
<div class="grid lg:grid-cols-3 gap-6 mt-8">

    <!-- Quiz Management Overview -->
    <div class="lg:col-span-2 bg-white rounded-2xl border border-slate-200 p-6 shadow-sm flex flex-col justify-between">
        <div>
            <div class="flex justify-between items-center mb-5">
                <div>
                    <h2 class="text-xl font-bold text-slate-800">Recent Quizzes</h2>
                    <p class="text-xs text-slate-500 mt-0.5">Quizzes managed within your assigned groups</p>
                </div>
            </div>

            <div class="space-y-4">
                @forelse($quizzes as $quiz)
                    <div class="p-4 bg-slate-50 rounded-xl flex items-center justify-between hover:bg-blue-50/50 transition">
                        <div>
                            <h3 class="font-semibold text-slate-800">{{ $quiz->quiz_title }}</h3>
                            <p class="text-sm text-slate-500 mt-0.5">
                                Group: {{ $quiz->group->group_name ?? 'N/A' }} 
                                @if($quiz->quiz_date)
                                    &bull; {{ \Carbon\Carbon::parse($quiz->quiz_date)->format('d M Y') }}
                                @endif
                            </p>
                        </div>

                        <span class="text-xs font-semibold px-3 py-1 rounded-full {{ $quiz->is_published ? 'bg-green-100 text-green-700' : 'bg-yellow-100 text-yellow-700' }}">
                            {{ $quiz->is_published ? 'Published' : 'Draft' }}
                        </span>
                    </div>
                @empty
                    <div class="p-8 text-center bg-slate-50 rounded-xl">
                        <p class="text-slate-500 text-sm">No quizzes found across your groups.</p>
                    </div>
                @endforelse
            </div>
        </div>
    </div>

    <!-- Student Participation Analytics -->
    <div class="bg-white rounded-2xl border border-slate-200 p-6 shadow-sm">
        <h2 class="text-xl font-bold text-slate-800 mb-5">
            Group Participation Overview
        </h2>

        <div class="space-y-5">
            @forelse($groupParticipation as $groupItem)
                <div>
                    <div class="flex justify-between text-sm font-medium mb-2">
                        <span class="text-slate-700 truncate max-w-[180px]">{{ $groupItem['group_name'] }}</span>
                        <span class="text-slate-600 font-semibold">{{ $groupItem['percentage'] }}%</span>
                    </div>

                    <div class="h-3 bg-slate-100 rounded-full overflow-hidden">
                        <div class="h-3 bg-blue-600 rounded-full transition-all duration-500" style="width: {{ $groupItem['percentage'] }}%;"></div>
                    </div>
                </div>
            @empty
                <div class="p-6 text-center text-slate-500 text-sm">
                    No active group data available.
                </div>
            @endforelse
        </div>
    </div>

</div>

@endsection