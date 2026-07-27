@extends('layouts.lecturer')

@section('title', 'Group Participation - UniForum')

@section('page-title', 'Group Participation')

@section('content')

<div class="space-y-6">

    <!-- Header -->
    <div class="bg-white rounded-2xl border border-slate-200 p-6 flex flex-col md:flex-row justify-between md:items-center gap-4">
        <div>
            <a href="{{ route('lecturer.groups.show', $group->group_id ?? $group->id) }}"
               class="text-sm text-blue-600 hover:underline flex items-center gap-2">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7"/>
                </svg>
                Back to Group
            </a>

            <h2 class="mt-4 text-2xl font-bold text-slate-800">
                {{ $group->group_name }} Participation
            </h2>

            <p class="text-slate-500 mt-1 text-sm">
                View student participation performance based on active system criteria.
            </p>
        </div>

        <a href="{{ route('lecturer.groups.participation.settings', $group->group_id ?? $group->id) }}"
           class="px-4 py-2.5 rounded-xl bg-blue-50 text-blue-600 hover:bg-blue-100 font-medium text-sm transition self-start md:self-auto">
            ⚙️ Participation Settings
        </a>
    </div>

    <!-- Active Criteria Summary -->
    <div class="bg-white rounded-2xl border border-slate-200 p-6">
        <h3 class="text-lg font-semibold text-slate-800 mb-4">
            Active Participation Weightage
        </h3>

        <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
            @forelse($criteria as $criterion)
                <div class="bg-blue-50/60 border border-blue-100 rounded-xl p-4">
                    <p class="text-xs uppercase font-semibold text-slate-500 tracking-wider">
                        {{ ucfirst($criterion->activity_type) }} Points
                    </p>
                    <p class="text-2xl font-bold text-blue-600 mt-1">
                        {{ $criterion->points }} pts / unit
                    </p>
                </div>
            @empty
                <p class="text-slate-500 text-sm col-span-4">No participation criteria configured yet.</p>
            @endforelse
        </div>
    </div>

    <!-- Student Scores Roster -->
    <div class="bg-white rounded-2xl border border-slate-200 overflow-hidden">
        <div class="p-6 border-b border-slate-200 flex justify-between items-center">
            <h3 class="text-lg font-semibold text-slate-800">
                Student Participation Scores
            </h3>
            <span class="text-xs font-semibold px-2.5 py-1 rounded-full bg-slate-100 text-slate-600">
                {{ count($roster) }} Students
            </span>
        </div>

        <div class="overflow-x-auto">
            <table class="w-full text-left">
                <thead class="bg-slate-50 border-b border-slate-100 text-xs font-semibold text-slate-500 uppercase">
                    <tr>
                        <th class="px-6 py-4">Rank</th>
                        <th class="px-6 py-4">Student</th>
                        <th class="px-6 py-4">Discussions Created</th>
                        <th class="px-6 py-4">Messages Sent</th>
                        <th class="px-6 py-4">Total Score</th>
                    </tr>
                </thead>

                <tbody class="divide-y divide-slate-100 text-sm">
                    @forelse($roster as $student)
                        <tr class="hover:bg-slate-50/50 transition">
                            <td class="px-6 py-4 font-bold text-slate-400">
                                #{{ $student['rank'] }}
                            </td>
                            <td class="px-6 py-4">
                                <div class="font-medium text-slate-800">{{ $student['name'] }}</div>
                                <div class="text-xs text-slate-400">{{ $student['email'] }}</div>
                            </td>
                            <td class="px-6 py-4 text-slate-600">
                                {{ $student['topics_created'] }}
                            </td>
                            <td class="px-6 py-4 text-slate-600">
                                {{ $student['messages_sent'] }}
                            </td>
                            <td class="px-6 py-4 font-bold text-blue-600">
                                {{ $student['total_score'] }} pts
                            </td>
                        </tr>
                    @empty
                        <tr>
                            <td colspan="5" class="px-6 py-8 text-center text-slate-500">
                                No students enrolled in this group yet.
                            </td>
                        </tr>
                    @endforelse
                </tbody>
            </table>
        </div>
    </div>

</div>

@endsection