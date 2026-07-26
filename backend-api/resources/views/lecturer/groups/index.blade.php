@extends('layouts.lecturer')

@section('title', 'My Groups - UniForum')

@section('page-title', 'My Groups')

@section('content')

<div>

    <!-- Header -->
    <div class="bg-white border-b border-slate-200">
        <div class="max-w-7xl mx-auto px-6 py-8">

            <h1 class="text-3xl font-bold text-slate-900">
                 My Discussion Groups
            </h1>

            <p class="mt-2 text-slate-500">
                Browse your academic groups and manage course discussions with your students.
            </p>

        </div>
    </div>

    <div class="max-w-7xl mx-auto px-6 py-10">

        <!-- Search & Actions -->
        <div class="flex flex-col md:flex-row gap-4 mb-8">

            <form method="GET" action="{{ route('lecturer.groups.browse') }}" class="flex-1">
                <input
                    type="text"
                    name="search"
                    placeholder="Search groups..."
                    class="w-full rounded-xl border-slate-200 focus:border-blue-500 focus:ring-blue-500">
            </form>

            <a href="{{ route('lecturer.groups.browse') }}"
               class="bg-blue-600 hover:bg-blue-700 text-white px-6 py-3 rounded-xl font-medium text-center  transition">
                 Browse All Groups
            </a>

            <a href="{{ route('lecturer.groups.create') }}"
               class="bg-blue-600 hover:bg-blue-700 text-white px-6 py-3 rounded-xl font-medium text-center transition">
             + Create Group
            </a>

        </div>

        <!-- Group Cards Grid -->
        <div class="grid md:grid-cols-2 xl:grid-cols-3 gap-6">

            @forelse($groups as $group)
                @php
                    $groupId = $group->group_id ?? $group->id;
                @endphp

                <div class="bg-white rounded-2xl border border-slate-200 shadow-sm hover:shadow-lg transition p-6 flex flex-col justify-between">

                    <div>
                        <div class="flex items-center justify-between">
                            <span class="px-3 py-1 rounded-full bg-blue-100 text-blue-700 text-xs font-semibold">
                                 Academic Group
                            </span>

                            <span class="text-sm text-slate-500">
                                {{ $group->members_count ?? $group->members()->count() }} Members
                            </span>
                        </div>

                        <h2 class="mt-5 text-xl font-bold text-slate-900">
                            {{ $group->group_name }}
                        </h2>

                        <p class="mt-3 text-slate-600 text-sm line-clamp-2">
                            {{ $group->description ?? 'No description provided.' }}
                        </p>
                    </div>

                    <div class="mt-6 flex justify-between items-center border-t border-slate-100 pt-4">

                        <span class="text-xs font-medium text-emerald-600 flex items-center gap-1">
                            ● Active
                        </span>

                        <a href="{{ route('lecturer.groups.show', $groupId) }}"
                           class="text-blue-600 font-semibold text-sm hover:underline flex items-center gap-1">
                            Open →
                        </a>

                    </div>

                </div>
            @empty

                <div class="col-span-full bg-white rounded-2xl border border-slate-200 p-12 text-center">
                    <p class="text-slate-500 font-medium">
                       You have not joined or created any groups yet.
                    </p>
                    <a href="{{ route('lecturer.groups.create') }}" class="inline-block mt-4 text-sm text-blue-600 font-semibold hover:underline">
                        + Create your first group
                    </a>
                </div>

            @endforelse

        </div>

    </div>

</div>

@endsection