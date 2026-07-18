@extends('layouts.student')

@section('title', 'Browse Groups - UniForum')

@section('page-title', 'Browse Groups')

@section('content')

<div class="min-h-screen bg-slate-50">


    <!-- Header -->

    <div class="bg-white border-b border-slate-200">

        <div class="max-w-7xl mx-auto px-6 py-8">
            <a href="/student/groups"
               class="text-sm text-blue-600 hover:underline flex items-center gap-2">

                <svg class="w-4 h-4"
                     fill="none"
                     stroke="currentColor"
                     stroke-width="2"
                     viewBox="0 0 24 24">

                    <path stroke-linecap="round"
                          stroke-linejoin="round"
                          d="M15 19l-7-7 7-7"/>

                </svg>

                Back to Groups

            </a>

        

            <h1 class="text-3xl font-bold text-slate-900">
                Browse Groups
            </h1>


            <p class="mt-2 text-slate-500">
                Discover academic groups and join discussions with other members.
            </p>


        </div>

    </div>




    <div class="max-w-7xl mx-auto px-6 py-10">


        <!-- Search -->


        <div class="mb-8">

            <input
                type="text"
                placeholder="Search available groups..."
                class="w-full rounded-xl border-slate-200 focus:ring-blue-500 focus:border-blue-500">

        </div>
        <div class="mb-8">
            <a href="/lecturer/groups/create"
               class="bg-blue-600 text-white px-5 py-3 rounded-xl">
                + Create Group
            </a>


        </div>





        <!-- Available Groups -->


        <div class="grid md:grid-cols-2 xl:grid-cols-3 gap-8">



            <!-- Group Card -->


            <div class="bg-white rounded-2xl border border-slate-200 p-6">


                <span class="px-3 py-1 rounded-full bg-blue-100 text-blue-700 text-xs">
                    Software Engineering
                </span>


                <h2 class="mt-5 text-xl font-bold text-slate-900">
                    BSSE Year II
                </h2>


                <p class="mt-3 text-slate-600">
                    Discuss assignments, programming concepts and course activities.
                </p>


                <div class="mt-5 flex justify-between items-center">

                    <span class="text-sm text-slate-500">
                       -- Members
                    </span>

                </div>

                <div class="mt-4 flex gap-3">

                    <a href="/student/groups/show"
                       class="flex-1 text-center border border-slate-300 rounded-lg py-2 hover:bg-slate-50">

                        View Details

                    </a>

                    <a href="/student/groups/join"
                       class="flex-1 text-center bg-blue-600 text-white rounded-lg py-2 hover:bg-blue-700">

                           Join

                    </a>

                </div>


            </div>






            <div class="bg-white rounded-2xl border border-slate-200 p-6">


                <span class="px-3 py-1 rounded-full bg-purple-100 text-purple-700 text-xs">
                    Artificial Intelligence
                </span>


                <h2 class="mt-5 text-xl font-bold text-slate-900">
                    AI Research Group
                </h2>


                <p class="mt-3 text-slate-600">
                    Machine learning discussions, projects and AI resources.
                </p>


                <div class="mt-5 flex justify-between items-center">

                    <span class="text-sm text-slate-500">
                       -- Members
                    </span>

                </div>

                <div class="mt-4 flex gap-3">

                    <a href="/student/groups/show"
                       class="flex-1 text-center border border-slate-300 rounded-lg py-2 hover:bg-slate-50">

                        View Details

                    </a>

                    <a href="/student/groups/join"
                       class="flex-1 text-center bg-blue-600 text-white rounded-lg py-2 hover:bg-blue-700">

                           Join

                    </a>

                </div>


                </div>


            </div>



        </div>


    </div>


</div>


@endsection