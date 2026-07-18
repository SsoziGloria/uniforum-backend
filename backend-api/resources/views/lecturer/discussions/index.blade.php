@extends('layouts.lecturer')

@section('title', 'Discussions - UniForum')

@section('page-title', 'Discussions')

@section('content')

<div>

    <!-- Header -->
    <div class="bg-white border-b border-slate-200">
        <div class="max-w-7xl mx-auto px-6 py-8">

            <div class="flex flex-col md:flex-row md:items-center md:justify-between gap-5">

                <div>
                    <h1 class="text-3xl font-bold text-slate-900">
                        Discussions
                    </h1>

                    <p class="mt-2 text-slate-500">
                        Ask questions, share knowledge, and connect with fellow members.
                    </p>
                </div>


                <a href="/lecturer/discussions/create"
                   class="inline-flex items-center justify-center gap-2 bg-blue-600 text-white px-5 py-3 rounded-xl hover:bg-blue-700 transition">

                    <svg class="w-5 h-5"
                         fill="none"
                         stroke="currentColor"
                         stroke-width="2"
                         viewBox="0 0 24 24">

                        <path stroke-linecap="round"
                              stroke-linejoin="round"
                              d="M12 4v16m8-8H4"/>
                    </svg>

                    Create Discussion

                </a>

            </div>


            <!-- Search -->
            <div class="mt-8">

                <div class="relative">

                    <input type="text"
                           placeholder="Search discussions..."
                           class="w-full rounded-xl border-slate-200 pl-12 py-3 focus:ring-blue-500 focus:border-blue-500">

                    <svg class="absolute left-4 top-3.5 w-5 h-5 text-slate-400"
                         fill="none"
                         stroke="currentColor"
                         stroke-width="2"
                         viewBox="0 0 24 24">

                        <path stroke-linecap="round"
                              stroke-linejoin="round"
                              d="m21 21-4.35-4.35m1.35-5.65a7 7 0 11-14 0 7 7 0 0114 0z"/>

                    </svg>

                </div>

            </div>

        </div>
    </div>



    <!-- Main Content -->

    <div class="max-w-7xl mx-auto px-6 py-10">

        <div class="grid lg:grid-cols-3 gap-8">


            <!-- Discussions -->

            <div class="lg:col-span-2 space-y-5">


                <!-- Discussion Card -->

                <div class="bg-white rounded-2xl border border-slate-200 p-6 hover:shadow-md transition">


                    <div class="flex justify-between items-start">

                        <div>

                            <span class="text-xs font-semibold bg-blue-100 text-blue-700 px-3 py-1 rounded-full">
                                Artificial Intelligence
                            </span>


                            <h2 class="mt-4 text-xl font-semibold text-slate-900">
                                How can machine learning improve university systems?
                            </h2>


                            <p class="mt-2 text-slate-500">
                                I would like to understand practical applications of ML in education platforms.
                            </p>

                        </div>


                        <span class="text-sm text-slate-400">
                            2h ago
                        </span>


                    </div>


                    <div class="mt-5 flex items-center justify-between text-sm text-slate-500">

                        <div>
                            12 replies · 45 views
                        </div>


                        <a href="/lecturer/discussions/show"
                           class="text-blue-600 hover:underline">

                            View Discussion

                        </a>

                    </div>


                </div>




                <div class="bg-white rounded-2xl border border-slate-200 p-6 hover:shadow-md transition">


                    <span class="text-xs font-semibold bg-green-100 text-green-700 px-3 py-1 rounded-full">
                        Software Engineering
                    </span>


                    <h2 class="mt-4 text-xl font-semibold text-slate-900">
                        Best practices when using Git in group projects
                    </h2>


                    <p class="mt-2 text-slate-500">
                        What workflow should beginners use when collaborating on GitHub?
                    </p>



                    <div class="mt-5 flex justify-between text-sm text-slate-500">

                        <span>
                            8 replies · 31 views
                        </span>

                        <a href="/lecturer/discussions/show"
                           class="text-blue-600 hover:underline">

                            View Discussion

                        </a>

                    </div>


                </div>




                <div class="bg-white rounded-2xl border border-slate-200 p-6 hover:shadow-md transition">


                    <span class="text-xs font-semibold bg-purple-100 text-purple-700 px-3 py-1 rounded-full">
                        Database Systems
                    </span>


                    <h2 class="mt-4 text-xl font-semibold text-slate-900">
                        Difference between SQL and NoSQL databases
                    </h2>


                    <p class="mt-2 text-slate-500">
                        Can someone explain when each database type should be used?
                    </p>



                    <div class="mt-5 flex justify-between text-sm text-slate-500">

                        <span>
                            20 replies · 90 views
                        </span>

                        <a href="/lecturer/discussions/show"
                           class="text-blue-600 hover:underline">

                            View Discussion

                        </a>

                    </div>


                </div>


            </div>




            <!-- Sidebar -->

            <div class="space-y-6">


                <!-- Categories -->

                <div class="bg-white rounded-2xl border border-slate-200 p-6">

                    <h3 class="font-semibold text-lg text-slate-900">
                        Popular Topics
                    </h3>


                    <div class="mt-4 space-y-3">

                        <div class="flex justify-between">
                            <span class="text-slate-600">
                                Programming
                            </span>
                            <span class="text-blue-600">
                                120
                            </span>
                        </div>


                        <div class="flex justify-between">
                            <span class="text-slate-600">
                                AI & Data Science
                            </span>
                            <span class="text-blue-600">
                                85
                            </span>
                        </div>


                        <div class="flex justify-between">
                            <span class="text-slate-600">
                                Web Development
                            </span>
                            <span class="text-blue-600">
                                64
                            </span>
                        </div>

                    </div>

                </div>




            </div>


        </div>

    </div>


</div>


@endsection