@extends('layouts.student')

@section('title', 'Group Statistics - UniForum')

@section('page-title', 'Group Statistics')

@section('content')

<div class="space-y-6">


    <!-- Header -->

    <div class="bg-white rounded-2xl border border-slate-200 p-6">


        <a href="{{ route('student.groups.show', $group->group_id) }}"
           class="text-sm text-blue-600 hover:underline">

            ← Back to Group

        </a>


        <h2 class="mt-4 text-2xl font-bold text-slate-800">

            {{ $group->group_name }} Group Statistics

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

                <h4 class="text-3xl font-bold text-blue-600 mt-2">
                    {{ $totalMembers }}
                </h4>

            </div>





            <div class="bg-white rounded-2xl border border-slate-200 p-6">

                <p class="text-sm text-slate-500">
                    Discussions
                </p>


                <h4 class="text-3xl font-bold text-blue-600 mt-2">
                    {{ $discussionCount }}
                </h4>

            </div>





            <div class="bg-white rounded-2xl border border-slate-200 p-6">

                <p class="text-sm text-slate-500">
                    Total Messages
                </p>


                <h4 class="text-3xl font-bold text-blue-600 mt-2">
                    {{ $messageCount }}
                </h4>

            </div>





            <div class="bg-white rounded-2xl border border-slate-200 p-6">

                <p class="text-sm text-slate-500">
                    Active Today
                </p>

                <h4 class="text-3xl font-bold text-blue-600 mt-2">
                    {{ $activeToday }}
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


                <p class="text-2xl font-bold text-blue-600 mt-2">
                    {{ $participationRate }}%
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



        <div class="divide-y divide-slate-100">

        @forelse( $mostActiveMembers as $member)


            <div class="p-6 flex justify-between">

                <span>
                   {{ $member->sender->name }}
                </span>

                <span class="text-slate-500">
                     {{ $member->posts }} posts
                </span>

            </div>
            @empty

                <div class="p-6 text-slate-500">

                   No activity yet.

                </div>

            @endforelse


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


                <p class="text-3xl font-bold text-orange-500 mt-2">
                    {{ $warningsIssued }}
                </p>

            </div>





            <div>

                <p class="text-sm text-slate-500">
                    Currently Blacklisted
                </p>


                <p class="text-3xl font-bold text-red-600 mt-2">
                    {{ $currentlyBlacklisted }}
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


        <div class="p-6 space-y-4">
        @forelse($popularDiscussions as $discussion)


            <div class="bg-slate-50 rounded-xl p-4 flex justify-between">

                <span>

                   {{ $discussion->title }}

                </span>

                <span class="text-slate-500">

                     {{ $discussion->messages_count }} messages

                </span>

            </div>
        @empty

            <div class="text-slate-500">

                No discussions yet.

            </div>

        @endforelse

        </div>


    </div>



</div>


@endsection