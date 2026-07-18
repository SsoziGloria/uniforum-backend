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

            <a href="/lecturer/groups/browse"
               class="bg-blue-600 hover:bg-blue-700 text-white px-6 py-3 rounded-xl">

                 Browse Groups

            </a>
            <a href="/lecturer/groups/create"
               class="bg-blue-600 hover:bg-blue-700 text-white px-6 py-3 rounded-xl">

             + Create Group

            </a>

        </div>

        <!-- Group Cards -->

        <div class="grid md:grid-cols-2 xl:grid-cols-3 gap-8">

            <!-- Card 1 -->

            <div class="bg-white rounded-2xl border border-slate-200 shadow-sm hover:shadow-lg transition">

                <div class="p-6">

                    <div class="flex items-center justify-between">

                        <span class="px-3 py-1 rounded-full bg-blue-100 text-blue-700 text-xs">
                            Software Engineering
                        </span>

                        <span class="text-sm text-slate-500">
                            -- Members
                        </span>

                    </div>

                    <h2 class="mt-5 text-xl font-bold text-slate-900">
                         Year 2 Group
                    </h2>

                    <p class="mt-3 text-slate-600">
                        General discussions, assignments, and class collaboration.
                    </p>

                    <div class="mt-6 flex justify-between items-center">

                        <span class="text-sm text-green-600">
                            ● Active today
                        </span>

                        <a href="/lecturer/groups/show"
                           class="text-blue-600 font-medium hover:underline">
                             Open →
                        </a>

                    </div>

                </div>

            </div>

            <!-- Card 2 -->

            <div class="bg-white rounded-2xl border border-slate-200 shadow-sm hover:shadow-lg transition">

                <div class="p-6">

                    <div class="flex items-center justify-between">

                        <span class="px-3 py-1 rounded-full bg-purple-100 text-purple-700 text-xs">
                            Artificial Intelligence
                        </span>

                        <span class="text-sm text-slate-500">
                            -- Members
                        </span>

                    </div>

                    <h2 class="mt-5 text-xl font-bold text-slate-900">
                        AI Course Forum
                    </h2>

                    <p class="mt-3 text-slate-600">
                        Machine learning discussions, tutorials and project help.
                    </p>

                    <div class="mt-6 flex justify-between items-center">

                        <span class="text-sm text-green-600">
                            ● 15 New Posts
                        </span>

                        <a href="/lecturer/groups/show"
                           class="text-blue-600 font-medium hover:underline">
                            Open →
                        </a>

                    </div>

                </div>

            </div>

            <!-- Card 3 -->

            <div class="bg-white rounded-2xl border border-slate-200 shadow-sm hover:shadow-lg transition">

                <div class="p-6">

                    <div class="flex items-center justify-between">

                        <span class="px-3 py-1 rounded-full bg-green-100 text-green-700 text-xs">
                            Databases
                        </span>

                        <span class="text-sm text-slate-500">
                            -- Members
                        </span>

                    </div>

                    <h2 class="mt-5 text-xl font-bold text-slate-900">
                        Database Systems
                    </h2>

                    <p class="mt-3 text-slate-600">
                        SQL discussions, assignments and revision materials.
                    </p>

                    <div class="mt-6 flex justify-between items-center">

                        <span class="text-sm text-orange-500">
                            ● 8 New Questions
                        </span>

                        <a href="/lecturer/groups/show"
                           class="text-blue-600 font-medium hover:underline">
                            Open →
                        </a>

                    </div>

                </div>

            </div>

        </div>

    </div>

</div>

@endsection