@extends('layouts.student')

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
                Browse your academic groups and continue collaborating with classmates.
            </p>

        </div>
    </div>

    <div class="max-w-7xl mx-auto px-6 py-10">

        <!-- Search -->

        <div class="flex flex-col md:flex-row gap-4 mb-8">

            <input
                type="text"
                placeholder="Search groups..."
                class="flex-1 rounded-xl border-slate-200 focus:border-blue-500 focus:ring-blue-500">

            <a href="/student/groups/browse"
               class="bg-blue-600 hover:bg-blue-700 text-white px-6 py-3 rounded-xl">

                 Browse Groups

            </a>
            <a href="/student/groups/create"
               class="bg-blue-600 hover:bg-blue-700 text-white px-6 py-3 rounded-xl">

             + Create Group

            </a>

        </div>

        <!-- Group Cards -->

           
           
            <div class="bg-white rounded-2xl border border-slate-200 shadow-sm hover:shadow-lg transition">
                @forelse($groups as $group)

                <div class="p-6">

                    <div class="flex items-center justify-between">

                        <span class="px-3 py-1 rounded-full bg-green-100 text-green-700 text-xs">
                             Academic Group
                        </span>

                        <span class="text-sm text-slate-500">
                            {{ $group->members()->count() }} Members
                        </span>

                    </div>

                    <h2 class="mt-5 text-xl font-bold text-slate-900">
                        {{ $group->group_name }}
                    </h2>

                    <p class="mt-3 text-slate-600">
                        {{ $group->description }}
                    </p>

                    <div class="mt-6 flex justify-between items-center">

                        <span class="text-sm text-orange-500">
                            ● Active
                        </span>

                        <a href="{{ route('student.groups.show', $group->group_id) }}"
                           class="text-blue-600 font-medium hover:underline">
                            Open →
                        </a>

                    </div>

                </div>

            </div>
            @empty

            <p class="text-slate-500">
               You have not joined any groups yet.
            </p>

            @endforelse

        </div>

        

</div>

@endsection