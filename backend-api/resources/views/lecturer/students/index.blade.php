@extends('layouts.lecturer')

@section('title', 'Students - UniForum')
@section('page-title', 'Students')

@section('content')
<div class="min-h-screen bg-slate-50">

    <!-- Header -->
    <div class="bg-white border-b border-slate-200">
        <div class="max-w-7xl mx-auto px-6 py-8">
            <h1 class="text-3xl font-bold text-slate-900">
                Student Performance
            </h1>
            <p class="mt-2 text-slate-500">
                Monitor participation, engagement, and academic activity.
            </p>
        </div>
    </div>

    <div class="max-w-7xl mx-auto px-6 py-10">

        <!-- Overview Cards -->
        <div class="grid md:grid-cols-4 gap-6 mb-10">

            <div class="bg-white border border-slate-200 rounded-2xl p-6">
                <p class="text-sm text-slate-500">Total Students</p>
                <h2 class="mt-3 text-3xl font-bold text-slate-800">
                   {{ $totalStudents }}
                </h2>
            </div>

            <div class="bg-white border border-slate-200 rounded-2xl p-6">
                <p class="text-sm text-slate-500">Active This Week</p>
                <h2 class="mt-3 text-3xl font-bold text-green-600">
                    {{ $activeThisWeekCount }}
                </h2>
            </div>

            <div class="bg-white border border-slate-200 rounded-2xl p-6">
                <p class="text-sm text-slate-500">Average Participation</p>
                <h2 class="mt-3 text-3xl font-bold text-blue-600">
                    {{ $avgParticipation }}/20
                </h2>
            </div>

            <div class="bg-white border border-slate-200 rounded-2xl p-6">
                <p class="text-sm text-slate-500">Average Quiz Score</p>
                <h2 class="mt-3 text-3xl font-bold text-purple-600">
                    {{ $avgQuizScore }}%
                </h2>
            </div>

        </div>

        <!-- Students Table -->
        <div class="bg-white rounded-2xl border border-slate-200 overflow-hidden">

            <div class="px-6 py-5 border-b border-slate-200">
                <h2 class="font-semibold text-lg text-slate-900">
                    Student Activity
                </h2>
            </div>

            <div class="overflow-x-auto">
                <table class="w-full text-left">
                    <thead class="bg-slate-50 text-sm text-slate-500">
                        <tr>
                            <th class="px-6 py-4">Student</th>
                            <th class="px-6 py-4">Discussions</th>
                            <th class="px-6 py-4">Answers</th>
                            <th class="px-6 py-4">Quiz Average</th>
                            <th class="px-6 py-4">Participation Mark</th>
                            <th class="px-6 py-4">Status</th>
                        </tr>
                    </thead>

                    <tbody class="divide-y divide-slate-100">
                        @forelse($studentsData as $row)
                            <tr>
                                <td class="px-6 py-5">
                                    <div>
                                        <p class="font-medium text-slate-900">
                                            {{ $row['student']->name }}
                                        </p>
                                        <p class="text-sm text-slate-500">
                                            {{ $row['student']->email }}
                                        </p>
                                    </div>
                                </td>

                                <td class="px-6 py-5 text-slate-700">
                                    {{ $row['discussions_count'] }}
                                </td>

                                <td class="px-6 py-5 text-slate-700">
                                    {{ $row['answers_count'] }}
                                </td>

                                <td class="px-6 py-5 font-semibold text-slate-800">
                                    {{ $row['quiz_avg_percentage'] }}%
                                </td>

                                <td class="px-6 py-5">
                                    <span class="px-3 py-1 rounded-full text-xs font-semibold bg-blue-100 text-blue-700">
                                        {{ $row['participation_mark'] }}/20
                                    </span>
                                </td>

                                <td class="px-6 py-5">
                                    <span class="px-3 py-1 rounded-full text-xs font-medium {{ $row['status_class'] }}">
                                        {{ $row['status'] }}
                                    </span>
                                </td>
                            </tr>
                        @empty
                            <tr>
                                <td colspan="6" class="px-6 py-8 text-center text-slate-500">
                                    No students enrolled in your groups yet.
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