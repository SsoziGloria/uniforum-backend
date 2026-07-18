@extends('layouts.student')

@section('title', 'Participation Results - UniForum')

@section('page-title', 'Participation Results')

@section('content')

<div class="space-y-6">


    <!-- Header -->

    <div class="bg-white rounded-2xl border border-slate-200 p-6">

        <a href="/student/groups/show"
           class="text-sm text-blue-600 hover:underline">

            ← Back to Group

        </a>


        <h2 class="mt-4 text-2xl font-bold text-slate-800">
            Participation Results
        </h2>


        <p class="text-slate-500 mt-2">
            View your contribution and participation performance in this group.
        </p>

    </div>





    <!-- Overall Score -->

    <div class="grid md:grid-cols-3 gap-6">


        <div class="bg-white rounded-2xl border border-slate-200 p-6">

            <p class="text-sm text-slate-500">
                Total Participation Score
            </p>

            {{-- Backend:
                 Retrieve calculated participation score
            --}}

            <h3 class="text-3xl font-bold text-blue-600 mt-2">
                --
            </h3>

        </div>




        <div class="bg-white rounded-2xl border border-slate-200 p-6">

            <p class="text-sm text-slate-500">
                Current Rank
            </p>

            {{-- Backend:
                 Retrieve student's rank within group
            --}}

            <h3 class="text-3xl font-bold text-blue-600 mt-2">
                --
            </h3>

        </div>




        <div class="bg-white rounded-2xl border border-slate-200 p-6">

            <p class="text-sm text-slate-500">
                Group
            </p>

            {{-- Backend:
                 Display group name
            --}}

            <h3 class="text-xl font-bold text-slate-800 mt-3">
                BSSE Year II
            </h3>

        </div>


    </div>







    <!-- Criteria Breakdown -->

    <div class="bg-white rounded-2xl border border-slate-200">


        <div class="p-6 border-b border-slate-200">

            <h3 class="text-lg font-semibold text-slate-800">
                Participation Breakdown
            </h3>

        </div>



        {{-- Backend:
             Retrieve criteria configured by lecturer.

             Examples:
             - Asking questions
             - Answering questions
             - Helpful replies
             - Discussion engagement
             - Resource sharing
        --}}



        <div class="divide-y divide-slate-100">


            <div class="p-6 flex justify-between">

                <span class="text-slate-600">
                    Questions Asked
                </span>


                <span class="font-semibold">
                    --
                </span>

            </div>




            <div class="p-6 flex justify-between">

                <span class="text-slate-600">
                    Questions Answered
                </span>


                <span class="font-semibold">
                    --
                </span>

            </div>





            <div class="p-6 flex justify-between">

                <span class="text-slate-600">
                    Helpful Contributions
                </span>


                <span class="font-semibold">
                    --
                </span>

            </div>




            <div class="p-6 flex justify-between">

                <span class="text-slate-600">
                    Resources Shared
                </span>


                <span class="font-semibold">
                    --
                </span>

            </div>


        </div>


    </div>






    <!-- Lecturer Feedback -->

    <div class="bg-white rounded-2xl border border-slate-200 p-6">


        <h3 class="text-lg font-semibold text-slate-800">
            Feedback
        </h3>


        {{-- Backend:
             Display lecturer comments if available.
        --}}


        <p class="mt-3 text-slate-500">
            No feedback available yet.
        </p>


    </div>



</div>


@endsection