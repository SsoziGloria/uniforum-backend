@extends('layouts.student')

@section('title', 'Quiz Report - ' . $quiz->quiz_title)
@section('page-title', 'Quiz Performance Report')

@section('content')
<div class="min-h-screen bg-slate-50">
    <!-- Header -->
    <div class="bg-white border-b border-slate-200">
        <div class="max-w-6xl mx-auto px-6 py-8">
            <a href="{{ route('student.groups.quizzes.index', $group->group_id) }}"
               class="text-sm text-blue-600 hover:underline font-medium">
                ← Back to {{ $group->group_name }} Quizzes
            </a>

            <h1 class="mt-5 text-3xl font-bold text-slate-900">
                {{ $quiz->quiz_title }} Report
            </h1>

            <p class="mt-2 text-slate-500">
                Summary of your results and group score calculations.
            </p>
        </div>
    </div>

    <div class="max-w-6xl mx-auto px-6 py-10">
        <!-- Score Summary -->
        <div class="grid md:grid-cols-4 gap-6 mb-10">
            <div class="bg-white rounded-2xl border border-slate-200 p-6">
                <p class="text-sm text-slate-500">Score</p>
                <h2 class="mt-3 text-4xl font-bold text-blue-600">
                    {{ $scorePercentage }}%
                </h2>
            </div>

            <div class="bg-white rounded-2xl border border-slate-200 p-6">
                <p class="text-sm text-slate-500">Questions</p>
                <h2 class="mt-3 text-3xl font-bold text-slate-800">
                    {{ $totalQuestions }}
                </h2>
            </div>

            <div class="bg-white rounded-2xl border border-slate-200 p-6">
                <p class="text-sm text-slate-500">Correct Answers</p>
                <h2 class="mt-3 text-3xl font-bold text-green-600">
                    {{ $correctAnswersCount }}
                </h2>
            </div>

            <div class="bg-white rounded-2xl border border-slate-200 p-6">
                <p class="text-sm text-slate-500">Time Taken</p>
                <h2 class="mt-3 text-3xl font-bold text-slate-800">
                    {{ $timeTakenMinutes }} min
                </h2>
            </div>
        </div>

        <!-- Performance Overview Bar -->
        <div class="bg-white rounded-2xl border border-slate-200 p-8 mb-8">
            <h2 class="text-xl font-semibold text-slate-900">
                Performance Overview
            </h2>

            <div class="mt-6">
                <div class="flex justify-between text-sm mb-2 font-medium">
                    <span>Overall Accuracy</span>
                    <span class="text-blue-600">{{ $scorePercentage }}%</span>
                </div>

                <div class="h-4 bg-slate-100 rounded-full overflow-hidden">
                    <div class="bg-blue-600 h-full rounded-full transition-all" style="width: {{ $scorePercentage }}%;"></div>
                </div>
            </div>
        </div>

        <!-- Question Breakdown -->
        <div class="bg-white rounded-2xl border border-slate-200 p-8">
            <h2 class="text-xl font-semibold text-slate-900 mb-6">
                Question Breakdown
            </h2>

            <div class="space-y-4">
                <div class="flex justify-between items-center p-4 bg-green-50 rounded-xl">
                    <span class="font-medium text-green-900">Correct Answers</span>
                    <span class="text-green-600 font-bold text-lg">{{ $correctAnswersCount }}</span>
                </div>

                <div class="flex justify-between items-center p-4 bg-red-50 rounded-xl">
                    <span class="font-medium text-red-900">Incorrect Answers</span>
                    <span class="text-red-600 font-bold text-lg">{{ $incorrectAnswersCount }}</span>
                </div>

                <div class="flex justify-between items-center p-4 bg-yellow-50 rounded-xl">
                    <span class="font-medium text-yellow-900">Unanswered</span>
                    <span class="text-yellow-600 font-bold text-lg">{{ $unansweredCount }}</span>
                </div>
            </div>
        </div>
    </div>
</div>
@endsection