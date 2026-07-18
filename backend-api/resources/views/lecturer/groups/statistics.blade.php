@extends('layouts.lecturer')

@section('title', 'Group Statistics - UniForum')

@section('page-title', 'Group Statistics')

@section('content')

<div class="space-y-6">


    <!-- Header -->

    <div class="bg-white rounded-2xl border border-slate-200 p-6">


        <a href="/lecturer/groups/show"
           class="text-sm text-blue-600 hover:underline">

            ← Back to Group

        </a>


        <h2 class="mt-4 text-2xl font-bold text-slate-800">

            BSSE Year II Group Statistics

        </h2>


        <p class="text-slate-500 mt-2">

            Monitor group activity, participation, and moderation activities.

        </p>


    </div>





    <!-- Group Overview Statistics -->


    <div>

        <h3 class="text-lg font-semibold text-slate-800 mb-4">

            Group Overview

        </h3>


        <div class="grid grid-cols-1 md:grid-cols-4 gap-6">


            <div class="bg-white rounded-2xl border border-slate-200 p-6">

                <p class="text-sm text-slate-500">
                    Total Members
                </p>

                {{-- Backend: Count group members --}}

                <h4 class="text-3xl font-bold text-blue-600 mt-2">
                    --
                </h4>

            </div>





            <div class="bg-white rounded-2xl border border-slate-200 p-6">

                <p class="text-sm text-slate-500">
                    Discussions
                </p>

                {{-- Backend: Count discussions in this group --}}

                <h4 class="text-3xl font-bold text-blue-600 mt-2">
                    --
                </h4>

            </div>





            <div class="bg-white rounded-2xl border border-slate-200 p-6">

                <p class="text-sm text-slate-500">
                    Total Messages
                </p>

                {{-- Backend: Count messages/posts --}}

                <h4 class="text-3xl font-bold text-blue-600 mt-2">
                    --
                </h4>

            </div>





            <div class="bg-white rounded-2xl border border-slate-200 p-6">

                <p class="text-sm text-slate-500">
                    Active Today
                </p>

                {{-- Backend: Members active today --}}

                <h4 class="text-3xl font-bold text-blue-600 mt-2">
                    --
                </h4>

            </div>


        </div>

    </div>









    <!-- Participation Statistics -->


    <div class="bg-white rounded-2xl border border-slate-200">


        <div class="p-6 border-b border-slate-200">

            <h3 class="text-lg font-semibold text-slate-800">

                Participation Statistics

            </h3>

        </div>



        <div class="p-6 grid md:grid-cols-2 gap-6">


            <div>

                <p class="text-sm text-slate-500">
                    Participation Rate
                </p>


                {{-- Backend:
                     Calculate percentage of members
                     actively participating
                --}}

                <p class="text-2xl font-bold text-blue-600 mt-2">
                    -- %
                </p>


            </div>





            <div>

                <p class="text-sm text-slate-500">
                    Questions Answered
                </p>


                {{-- Backend:
                     Count questions with accepted answers/replies
                --}}

                <p class="text-2xl font-bold text-blue-600 mt-2">
                    --
                </p>


            </div>


        </div>



    </div>









    <!-- Most Active Members -->


    <div class="bg-white rounded-2xl border border-slate-200">


        <div class="p-6 border-b border-slate-200">

            <h3 class="text-lg font-semibold text-slate-800">

                Most Active Members

            </h3>

        </div>


        {{-- Backend:
             Sort members by participation/activity
        --}}


        <div class="divide-y divide-slate-100">


            <div class="p-6 flex justify-between">

                <span>
                    Sarah Namukasa
                </span>

                <span class="text-slate-500">
                    -- posts
                </span>

            </div>



            <div class="p-6 flex justify-between">

                <span>
                    Peter Okello
                </span>

                <span class="text-slate-500">
                    -- posts
                </span>

            </div>


        </div>


    </div>









    <!-- Moderation Statistics -->


    <div class="bg-white rounded-2xl border border-slate-200">


        <div class="p-6 border-b border-slate-200">


            <h3 class="text-lg font-semibold text-slate-800">

                Moderation Statistics

            </h3>


        </div>



        <div class="grid md:grid-cols-3 gap-6 p-6">


            <div>

                <p class="text-sm text-slate-500">
                    Warnings Issued
                </p>


                {{-- Backend:
                     Count warnings given to members
                --}}

                <p class="text-3xl font-bold text-orange-500 mt-2">
                    --
                </p>

            </div>





            <div>

                <p class="text-sm text-slate-500">
                    Currently Blacklisted
                </p>


                {{-- Backend:
                     Members currently restricted
                --}}

                <p class="text-3xl font-bold text-red-600 mt-2">
                    --
                </p>


            </div>





            <div>

                <p class="text-sm text-slate-500">
                    Moderation Actions
                </p>


                {{-- Backend:
                     Total moderation actions performed
                --}}

                <p class="text-3xl font-bold text-blue-600 mt-2">
                    --
                </p>


            </div>


        </div>



    </div>









    <!-- Trending Discussions -->


    <div class="bg-white rounded-2xl border border-slate-200">


        <div class="p-6 border-b border-slate-200">


            <h3 class="text-lg font-semibold text-slate-800">

                Popular Discussions

            </h3>


        </div>



        {{-- Backend:
             Retrieve popular discussions/topics
        --}}


        <div class="p-6 space-y-4">


            <div class="bg-slate-50 rounded-xl p-4">

                Laravel Authentication Problem

            </div>


            <div class="bg-slate-50 rounded-xl p-4">

                Database Design Questions

            </div>


            <div class="bg-slate-50 rounded-xl p-4">

                AI Project Ideas

            </div>


        </div>


    </div>



</div>


@endsection