@extends('layouts.lecturer')

@section('title', 'Participation Settings - UniForum')

@section('page-title', 'Participation Settings')

@section('content')

<div class="space-y-6" x-data="{ openAddModal: false }">

    <!-- Header -->
    <div class="bg-white rounded-2xl border border-slate-200 p-6 flex flex-col md:flex-row justify-between md:items-center gap-4">
        <div>
            <a href="{{ route('lecturer.groups.participation', $group->group_id ?? $group->id) }}"
               class="text-sm text-blue-600 hover:underline inline-flex items-center gap-1.5">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7"/>
                </svg>
                Back to Participation Overview
            </a>

            <h2 class="mt-4 text-2xl font-bold text-slate-800">
                {{ $group->group_name }} Participation Settings
            </h2>

            <p class="text-slate-500 mt-1 text-sm">
                Configure how students earn participation marks based on their activity in this group.
            </p>
        </div>

        <button @click="openAddModal = true"
                class="px-4 py-2.5 rounded-xl bg-blue-600 text-white hover:bg-blue-700 font-medium text-sm transition self-start md:self-auto flex items-center gap-2">
            <span>+</span> Add Criterion
        </button>
    </div>

    <!-- Alert Messages -->
    @if(session('success'))
        <div class="p-4 rounded-xl bg-emerald-50 border border-emerald-200 text-emerald-700 text-sm">
            {{ session('success') }}
        </div>
    @endif

    <!-- Criteria Management Table -->
    <div class="bg-white rounded-2xl border border-slate-200 overflow-hidden">
        <div class="p-6 border-b border-slate-200">
            <h3 class="text-lg font-semibold text-slate-800">
                Participation Criteria Rules
            </h3>
            <p class="text-sm text-slate-500 mt-1">
                Define the points awarded per activity. Changes apply automatically to performance calculations.
            </p>
        </div>

        <!-- MAIN UPDATE FORM -->
        <form action="{{ route('lecturer.groups.participation.settings.update', $group->group_id ?? $group->id) }}" method="POST" id="update-criteria-form">
            @csrf
            @method('PUT')

            <div class="overflow-x-auto">
                <table class="w-full text-left">
                    <thead class="bg-slate-50 border-b border-slate-100 text-xs font-semibold text-slate-500 uppercase">
                        <tr>
                            <th class="px-6 py-4">Criterion Name</th>
                            <th class="px-6 py-4">Activity Type</th>
                            <th class="px-6 py-4">Points Awarded</th>
                            <th class="px-6 py-4 text-right">Actions</th>
                        </tr>
                    </thead>

                    <tbody class="divide-y divide-slate-100 text-sm">
                        @forelse($criteria as $criterion)
                            @php
                                $id = is_object($criterion) 
                                    ? ($criterion->criterion_id ?? $criterion->id) 
                                    : ($criterion['criterion_id'] ?? $criterion['id'] ?? null);
                            @endphp
                            <tr class="hover:bg-slate-50/50 transition">
                                <td class="px-6 py-5 font-semibold text-slate-800">
                                    {{ is_object($criterion) ? ($criterion->criterion_name ?? ucfirst($criterion->activity_type)) : ($criterion['criterion_name'] ?? ucfirst($criterion['activity_type'])) }}
                                </td>
                                <td class="px-6 py-5 text-slate-500 capitalize">
                                    {{ is_object($criterion) ? $criterion->activity_type : $criterion['activity_type'] }}
                                </td>
                                <td class="px-6 py-5">
                                    <div class="flex items-center gap-2">
                                        <input type="number" 
                                               name="criteria[{{ $id }}][points]" 
                                               value="{{ is_object($criterion) ? $criterion->points : $criterion['points'] }}" 
                                               min="0" 
                                               step="1"
                                               class="w-24 rounded-xl border-slate-200 focus:border-blue-500 focus:ring-blue-500 text-sm font-semibold text-slate-800">
                                        <span class="text-xs text-slate-400">pts / unit</span>
                                    </div>
                                </td>
                                
                                <td class="px-6 py-5 text-right">
                                    <!-- Button targets external standalone delete form by ID -->
                                    <button type="submit" 
                                            form="delete-form-{{ $id }}" 
                                            onsubmit="return confirm('Are you sure you want to delete this criterion?');"
                                            class="text-red-600 hover:text-red-800 text-xs font-semibold">
                                        Delete
                                    </button>
                                </td>
                            </tr>
                        @empty
                            <tr>
                                <td colspan="4" class="px-6 py-8 text-center text-slate-500">
                                    No criteria configured yet. Click <strong>+ Add Criterion</strong> above to create one.
                                </td>
                            </tr>
                        @endforelse
                    </tbody>
                </table>
            </div>

            @if(count($criteria) > 0)
                <div class="p-6 bg-slate-50 border-t border-slate-100 flex justify-end">
                    <button type="submit" 
                            class="px-6 py-2.5 rounded-xl bg-emerald-600 text-white hover:bg-emerald-700 font-medium text-sm transition">
                        Save Changes
                    </button>
                </div>
            @endif
        </form>
    </div>

    <!-- STANDALONE DELETE FORMS (OUTSIDE MAIN FORM) -->
    @foreach($criteria as $criterion)
        @php
            $deleteId = is_object($criterion) 
                ? ($criterion->criterion_id ?? $criterion->id) 
                : ($criterion['criterion_id'] ?? $criterion['id'] ?? null);
        @endphp
        @if($deleteId)
            <form id="delete-form-{{ $deleteId }}" 
                  action="{{ route('lecturer.groups.participation.settings.destroy', ['group' => $group->group_id ?? $group->id, 'criterion' => $deleteId]) }}" 
                  method="POST" 
                  class="hidden"
                  onsubmit="return confirm('Are you sure you want to delete this criterion?');">
                @csrf
                @method('DELETE')
            </form>
        @endif
    @endforeach

    <!-- Modal for Adding New Criterion -->
    <div x-show="openAddModal" 
         class="fixed inset-0 z-50 flex items-center justify-center bg-slate-900/50 p-4" 
         x-cloak>
        <div class="bg-white rounded-2xl max-w-md w-full p-6 space-y-4 shadow-xl" @click.away="openAddModal = false">
            <h3 class="text-lg font-bold text-slate-800">Add Participation Criterion</h3>

            <form action="{{ route('lecturer.groups.participation.settings.store', $group->group_id ?? $group->id) }}" method="POST" class="space-y-4">
                @csrf
                
                <div>
                    <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Criterion Name</label>
                    <input type="text" 
                           name="criterion_name" 
                           placeholder="e.g. Asking Questions, Helping Others" 
                           required 
                           class="w-full rounded-xl border-slate-200 text-sm focus:border-blue-500 focus:ring-blue-500">
                </div>

                <div>
                    <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Activity Type</label>
                    <select name="activity_type" required class="w-full rounded-xl border-slate-200 text-sm focus:border-blue-500 focus:ring-blue-500">
                        <option value="topic">Create Discussion Topic</option>
                        <option value="message">Send Message / Post</option>
                        <option value="answer">Answer Question</option>
                        <option value="resource">Share Resource</option>
                    </select>
                </div>

                <div>
                    <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Points Awarded</label>
                    <input type="number" name="points" value="1" min="1" required class="w-full rounded-xl border-slate-200 text-sm focus:border-blue-500 focus:ring-blue-500">
                </div>

                <div class="flex justify-end gap-3 pt-2">
                    <button type="button" @click="openAddModal = false" class="px-4 py-2 rounded-xl text-slate-600 hover:bg-slate-100 text-sm font-medium">Cancel</button>
                    <button type="submit" class="px-4 py-2 rounded-xl bg-blue-600 text-white hover:bg-blue-700 text-sm font-medium">Save Criterion</button>
                </div>
            </form>
        </div>
    </div>

</div>

@endsection