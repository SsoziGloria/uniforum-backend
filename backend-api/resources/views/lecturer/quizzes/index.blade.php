@extends('layouts.lecturer')

@section('title', 'Quiz Management - ' . $group->group_name)
@section('page-title', 'Quiz Management')

@section('content')
<div class="min-h-screen bg-slate-50">
    <div class="bg-white border-b border-slate-200">
        <div class="max-w-7xl mx-auto px-6 py-8">
            <div class="flex flex-col md:flex-row md:justify-between md:items-center gap-5">
                <div>
                    <a href="{{ route('lecturer.groups.show', $group->group_id ?? $group->id) }}" class="text-sm text-blue-600 hover:underline">
                        ← Back to {{ $group->group_name }}
                    </a>
                    <h1 class="text-3xl font-bold text-slate-900 mt-2">Quiz Management</h1>
                    <p class="mt-1 text-slate-500">Create, schedule, and monitor student assessments.</p>
                </div>
                <a href="{{ route('lecturer.groups.quizzes.create', $group->group_id ?? $group->id) }}"
                   class="px-5 py-3 bg-blue-600 text-white font-medium rounded-xl hover:bg-blue-700 transition">
                    + Create Quiz
                </a>
            </div>
        </div>
    </div>

    <div class="max-w-7xl mx-auto px-6 py-10">
        @if(session('success'))
            <div class="mb-6 p-4 bg-green-50 border border-green-200 text-green-700 rounded-xl">
                {{ session('success') }}
            </div>
        @endif

        <div class="grid md:grid-cols-4 gap-6 mb-10">
            <div class="bg-white border border-slate-200 rounded-2xl p-6">
                <p class="text-sm text-slate-500">Total Quizzes</p>
                <h2 class="text-3xl font-bold mt-3 text-slate-800">{{ $quizzes->count() }}</h2>
            </div>
            <div class="bg-white border border-slate-200 rounded-2xl p-6">
                <p class="text-sm text-slate-500">Published</p>
                <h2 class="text-3xl font-bold mt-3 text-green-600">{{ $publishedCount }}</h2>
            </div>
            <div class="bg-white border border-slate-200 rounded-2xl p-6">
                <p class="text-sm text-slate-500">Upcoming</p>
                <h2 class="text-3xl font-bold mt-3 text-blue-600">{{ $upcomingCount }}</h2>
            </div>
            <div class="bg-white border border-slate-200 rounded-2xl p-6">
                <p class="text-sm text-slate-500">Average Score</p>
                <h2 class="text-3xl font-bold mt-3 text-purple-600">{{ $avgScore }}%</h2>
            </div>
        </div>

        <div class="bg-white border border-slate-200 rounded-2xl overflow-hidden">
            <div class="px-6 py-5 border-b border-slate-200">
                <h2 class="text-lg font-semibold text-slate-900">Your Quizzes</h2>
            </div>
            <div class="overflow-x-auto">
                <table class="w-full text-left">
                    <thead class="bg-slate-50 text-sm text-slate-500">
                        <tr>
                            <th class="px-6 py-4">Quiz Title</th>
                            <th class="px-6 py-4">Category</th>
                            <th class="px-6 py-4">Date & Time</th>
                            <th class="px-6 py-4">Duration</th>
                            <th class="px-6 py-4">Status</th>
                            <th class="px-6 py-4">Action</th>
                        </tr>
                    </thead>
                    <tbody class="divide-y divide-slate-100">
                        @forelse($quizzes as $quiz)
                            <tr>
                                <td class="px-6 py-5">
                                    <p class="font-medium text-slate-900">{{ $quiz->quiz_title }}</p>
                                    <p class="text-sm text-slate-500">{{ $quiz->questions_count }} Questions</p>
                                </td>
                                <td class="px-6 py-5 text-slate-600">{{ $quiz->student_category }}</td>
                                <td class="px-6 py-5 text-slate-600">
                                    {{ \Carbon\Carbon::parse($quiz->quiz_date . ' ' . $quiz->start_time)->format('d M Y, h:i A') }}
                                </td>
                                <td class="px-6 py-5 text-slate-600">{{ $quiz->duration_minutes }} Mins</td>
                                <td class="px-6 py-5">
                                    @if($quiz->is_published)
                                        <span class="px-3 py-1 rounded-full text-xs bg-green-100 text-green-700">Published</span>
                                    @else
                                        <span class="px-3 py-1 rounded-full text-xs bg-yellow-100 text-yellow-700">Draft</span>
                                    @endif
                                </td>
                                <td class="px-6 py-5">
                                    <a href="{{ route('lecturer.groups.quizzes.show', ['group' => $group->group_id ?? $group->id, 'quiz' => $quiz->quiz_id]) }}"
                                       class="text-blue-600 font-medium hover:underline">Manage</a>
                                </td>
                            </tr>
                        @empty
                            <tr>
                                <td colspan="6" class="px-6 py-8 text-center text-slate-500">
                                    No quizzes created yet for this group.
                                </td>
                            </tr>
                        @endforelse
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
@endsection