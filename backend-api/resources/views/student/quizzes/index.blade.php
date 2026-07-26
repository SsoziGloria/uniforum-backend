@extends('layouts.student')

@section('title', 'My Quizzes - ' . $group->group_name)
@section('page-title', 'Group Quizzes')

@section('content')
<div>
    <!-- Header -->
    <div class="bg-white border-b border-slate-200">
        <div class="max-w-7xl mx-auto px-6 py-8">
            <a href="{{ route('student.groups.show', $group->group_id) }}" class="text-sm text-blue-600 hover:underline">
                ← Back to {{ $group->group_name }}
            </a>
            <h1 class="mt-2 text-3xl font-bold text-slate-900">
                Quizzes for {{ $group->group_name }}
            </h1>
            <p class="mt-2 text-slate-500">
                View active, scheduled, and completed assessments.
            </p>
        </div>
    </div>

    <div class="max-w-7xl mx-auto px-6 py-10">
        @if(session('info'))
            <div class="mb-6 p-4 bg-blue-50 border border-blue-200 text-blue-700 rounded-xl">
                {{ session('info') }}
            </div>
        @endif

        @if($errors->has('quiz'))
            <div class="mb-6 p-4 bg-red-50 border border-red-200 text-red-700 rounded-xl">
                {{ $errors->first('quiz') }}
            </div>
        @endif

        <!-- Summary Cards -->
        <div class="grid md:grid-cols-3 gap-6 mb-10">
            <div class="bg-white rounded-2xl border border-slate-200 p-6">
                <p class="text-sm text-slate-500">Active Quizzes</p>
                <h2 class="mt-3 text-3xl font-bold text-blue-600">
                    {{ $stats['available_count'] }}
                </h2>
            </div>
            <div class="bg-white rounded-2xl border border-slate-200 p-6">
                <p class="text-sm text-slate-500">Completed</p>
                <h2 class="mt-3 text-3xl font-bold text-green-600">
                    {{ $stats['completed_count'] }}
                </h2>
            </div>
            <div class="bg-white rounded-2xl border border-slate-200 p-6">
                <p class="text-sm text-slate-500">Average Score</p>
                <h2 class="mt-3 text-3xl font-bold text-purple-600">
                    {{ $stats['average_score'] }}%
                </h2>
            </div>
        </div>

        <!-- Quiz Lists -->
        <div class="space-y-6">
            <!-- ACTIVE QUIZZES -->
            @forelse($activeQuizzes as $quiz)
                <div class="bg-white rounded-2xl border border-blue-200 p-6 shadow-sm">
                    <div class="flex flex-col md:flex-row md:justify-between md:items-center gap-6">
                        <div>
                            <span class="inline-block px-3 py-1 rounded-full bg-blue-100 text-blue-700 text-xs font-semibold">
                                ACTIVE NOW
                            </span>
                            <h2 class="mt-4 text-2xl font-bold">
                                {{ $quiz->quiz_title }}
                            </h2>
                            <p class="mt-2 text-slate-500">
                                {{ $quiz->questions->count() }} Questions • {{ $quiz->duration_minutes }} Minutes
                            </p>
                        </div>
                        <a href="{{ route('student.groups.quizzes.take', ['group' => $group->group_id, 'quiz' => $quiz->quiz_id]) }}"
                           class="inline-flex items-center justify-center bg-blue-600 text-white px-6 py-3 rounded-xl hover:bg-blue-700 font-medium">
                            Start / Resume Quiz
                        </a>
                    </div>
                </div>
            @empty
            @endforelse

            <!-- UPCOMING QUIZZES -->
            @forelse($upcomingQuizzes as $quiz)
                <div class="bg-white rounded-2xl border border-slate-200 p-6">
                    <div class="flex justify-between items-center">
                        <div>
                            <h2 class="font-semibold text-xl">
                                {{ $quiz->quiz_title }}
                            </h2>
                            <p class="mt-2 text-slate-500">
                                Scheduled: {{ \Carbon\Carbon::parse($quiz->quiz_date . ' ' . $quiz->start_time)->format('M d, Y @ h:i A') }} • {{ $quiz->duration_minutes }} Mins
                            </p>
                        </div>
                        <span class="px-4 py-2 rounded-full bg-yellow-100 text-yellow-700 text-sm font-medium">
                            Upcoming
                        </span>
                    </div>
                </div>
            @empty
            @endforelse

            <!-- COMPLETED QUIZZES -->
            @forelse($completedQuizzes as $quiz)
                <div class="bg-white rounded-2xl border border-slate-200 p-6">
                    <div class="flex justify-between items-center">
                        <div>
                            <h2 class="font-semibold text-xl">
                                {{ $quiz->quiz_title }}
                            </h2>
                            <p class="mt-2 text-slate-500">
                                Assessment finished
                            </p>
                        </div>
                        <div class="text-right">
                            <p class="text-green-600 font-bold text-2xl">
                                {{ $quiz->user_score_percentage }}%
                            </p>
                            <a href="{{ route('student.groups.quizzes.report', ['group' => $group->group_id, 'quiz' => $quiz->quiz_id]) }}"
                               class="text-sm text-blue-600 hover:underline">
                                View Report
                            </a>
                        </div>
                    </div>
                </div>
            @empty
            @endforelse

            @if(count($activeQuizzes) === 0 && count($upcomingQuizzes) === 0 && count($completedQuizzes) === 0)
                <div class="bg-white rounded-2xl border border-slate-200 p-12 text-center text-slate-500">
                    No quizzes published in this group yet.
                </div>
            @endif
        </div>
    </div>
</div>
@endsection