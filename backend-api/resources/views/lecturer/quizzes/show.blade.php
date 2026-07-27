@extends('layouts.lecturer')

@section('title', 'Quiz Details - ' . $quiz->quiz_title)
@section('page-title', 'Quiz Details')

@section('content')
<div class="space-y-6 max-w-6xl mx-auto py-8 px-6">
    <div class="bg-white rounded-2xl border border-slate-200 p-6">
        <div class="flex flex-col md:flex-row md:justify-between md:items-center gap-4">
            <div>
                <a href="{{ route('lecturer.groups.quizzes.index', $group->group_id ?? $group->id) }}" class="text-sm text-blue-600 hover:underline">
                    ← Back to Quizzes
                </a>
                <h2 class="text-2xl font-bold text-slate-800 mt-2">{{ $quiz->quiz_title }}</h2>
                <p class="text-slate-500 mt-1">{{ $quiz->student_category }}</p>
            </div>
            <span class="px-4 py-2 rounded-full text-sm font-semibold {{ $quiz->is_published ? 'bg-green-100 text-green-700' : 'bg-yellow-100 text-yellow-700' }}">
                {{ $quiz->is_published ? 'Published' : 'Draft' }}
            </span>
        </div>
    </div>

    <!-- Configurations -->
    <div class="bg-white rounded-2xl border border-slate-200 p-6">
        <h3 class="text-lg font-semibold text-slate-800 mb-6">Quiz Configuration</h3>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
                <p class="text-sm text-slate-500">Date & Time</p>
                <p class="font-medium text-slate-800 mt-1">
                    {{ \Carbon\Carbon::parse($quiz->quiz_date . ' ' . $quiz->start_time)->format('d M Y, h:i A') }}
                </p>
            </div>
            <div>
                <p class="text-sm text-slate-500">Duration</p>
                <p class="font-medium text-slate-800 mt-1">{{ $quiz->duration_minutes }} Minutes</p>
            </div>
            <div>
                <p class="text-sm text-slate-500">Student Category</p>
                <p class="font-medium text-slate-800 mt-1">{{ $quiz->student_category }}</p>
            </div>
            <div>
                <p class="text-sm text-slate-500">Number of Questions</p>
                <p class="font-medium text-slate-800 mt-1">{{ $quiz->questions->count() }} Questions</p>
            </div>
        </div>
    </div>

    <!-- Statistics Cards -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div class="bg-white border border-slate-200 rounded-2xl p-6">
            <p class="text-sm text-slate-500">Total Assigned Students</p>
            <h3 class="text-3xl font-bold text-blue-600 mt-2">{{ $totalStudents }}</h3>
        </div>
        <div class="bg-white border border-slate-200 rounded-2xl p-6">
            <p class="text-sm text-slate-500">Submissions</p>
            <h3 class="text-3xl font-bold text-green-600 mt-2">{{ $submittedCount }}</h3>
        </div>
        <div class="bg-white border border-slate-200 rounded-2xl p-6">
            <p class="text-sm text-slate-500">Average Score</p>
            <h3 class="text-3xl font-bold text-purple-600 mt-2">{{ $avgScore }}%</h3>
        </div>
    </div>

    <!-- Actions -->
    <div class="bg-white rounded-2xl border border-slate-200 p-6">
        <h3 class="text-lg font-semibold text-slate-800 mb-4">Actions</h3>
        <div class="flex flex-wrap gap-3">
            <a href="{{ route('lecturer.groups.quizzes.results', ['group' => $group->group_id ?? $group->id, 'quiz' => $quiz->quiz_id]) }}"
               class="px-5 py-3 rounded-xl bg-blue-600 text-white font-medium hover:bg-blue-700 transition">
                View Performance Report
            </a>
        </div>
    </div>
</div>
@endsection