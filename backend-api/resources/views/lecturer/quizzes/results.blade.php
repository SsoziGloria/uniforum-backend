@extends('layouts.lecturer')

@section('title', 'Quiz Results - ' . $quiz->quiz_title)
@section('page-title', 'Quiz Results')

@section('content')
<div class="space-y-6 max-w-6xl mx-auto py-8 px-6">
    <div class="bg-white rounded-2xl border border-slate-200 p-6">
        <div>
            <a href="{{ route('lecturer.groups.quizzes.show', ['group' => $group->group_id ?? $group->id, 'quiz' => $quiz->quiz_id]) }}"
               class="text-sm text-blue-600 hover:underline">
                ← Back to Quiz Details
            </a>
            <h2 class="text-2xl font-bold text-slate-800 mt-2">{{ $quiz->quiz_title }} Results</h2>
            <p class="text-slate-500 mt-1">Review student scores and attempt details.</p>
        </div>
    </div>

    <!-- Summary Stats -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-6">
        <div class="bg-white border border-slate-200 rounded-2xl p-6">
            <p class="text-sm text-slate-500">Total Students</p>
            <h3 class="text-3xl font-bold text-blue-600 mt-2">{{ $totalStudents }}</h3>
        </div>
        <div class="bg-white border border-slate-200 rounded-2xl p-6">
            <p class="text-sm text-slate-500">Submitted</p>
            <h3 class="text-3xl font-bold text-green-600 mt-2">{{ $submittedCount }}</h3>
        </div>
        <div class="bg-white border border-slate-200 rounded-2xl p-6">
            <p class="text-sm text-slate-500">Average Score</p>
            <h3 class="text-3xl font-bold text-purple-600 mt-2">{{ $avgScore }}%</h3>
        </div>
        <div class="bg-white border border-slate-200 rounded-2xl p-6">
            <p class="text-sm text-slate-500">Completion Rate</p>
            <h3 class="text-3xl font-bold text-orange-600 mt-2">{{ $completionRate }}%</h3>
        </div>
    </div>

    <!-- Table -->
    <div class="bg-white rounded-2xl border border-slate-200 overflow-hidden">
        <div class="p-6 border-b border-slate-200">
            <h3 class="text-lg font-semibold text-slate-800">Student Performance Overview</h3>
        </div>
        <div class="overflow-x-auto">
            <table class="w-full text-left">
                <thead class="bg-slate-50 text-sm text-slate-500">
                    <tr>
                        <th class="px-6 py-4">Student</th>
                        <th class="px-6 py-4">Score</th>
                        <th class="px-6 py-4">Status</th>
                        <th class="px-6 py-4">Submitted At</th>
                    </tr>
                </thead>
                <tbody class="divide-y divide-slate-100">
                    @forelse($students as $student)
                        @php $sub = $submissions->get($student->id); @endphp
                        <tr>
                            <td class="px-6 py-5">
                                <p class="font-medium text-slate-800">{{ $student->name }}</p>
                                <p class="text-xs text-slate-400">{{ $student->email }}</p>
                            </td>
                            <td class="px-6 py-5 text-slate-600 font-bold">
                                @if($sub && in_array($sub->status, ['submitted', 'auto-submitted']))
                                    @php $pct = $totalPossible > 0 ? round(($sub->total_score / $totalPossible) * 100) : 0; @endphp
                                    {{ $pct }}% <span class="text-xs font-normal text-slate-400">({{ $sub->total_score }}/{{ $totalPossible }})</span>
                                @else
                                    --
                                @endif
                            </td>
                            <td class="px-6 py-5">
                                @if($sub)
                                    @if(in_array($sub->status, ['submitted', 'auto-submitted']))
                                        <span class="px-3 py-1 rounded-full text-xs bg-green-100 text-green-700 capitalize">{{ $sub->status }}</span>
                                    @else
                                        <span class="px-3 py-1 rounded-full text-xs bg-blue-100 text-blue-700 capitalize">In Progress</span>
                                    @endif
                                @else
                                    <span class="px-3 py-1 rounded-full text-xs bg-slate-100 text-slate-500">Pending</span>
                                @endif
                            </td>
                            <td class="px-6 py-5 text-slate-500 text-sm">
                                {{ $sub && $sub->submitted_at ? $sub->submitted_at->format('d M Y, h:i A') : 'N/A' }}
                            </td>
                        </tr>
                    @empty
                        <tr>
                            <td colspan="4" class="px-6 py-8 text-center text-slate-500">No student enrollment records found for this group.</td>
                        </tr>
                    @endforelse
                </tbody>
            </table>
        </div>
    </div>
</div>
@endsection