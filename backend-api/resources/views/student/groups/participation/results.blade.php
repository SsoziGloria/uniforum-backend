@extends('layouts.student')

@section('title', 'Participation Results - UniForum')

@section('page-title', 'Participation Results')

@section('content')

<div class="space-y-6">


    <!-- Header -->

    <div class="bg-white rounded-2xl border border-slate-200 p-6">

        <a href="{{ route('student.groups.show', $group->group_id) }}"
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

            <h3 class="text-3xl font-bold text-blue-600 mt-2">
                {{ $results['total_score'] }}
            </h3>

        </div>




        <div class="bg-white rounded-2xl border border-slate-200 p-6">

            <p class="text-sm text-slate-500">
                Current Rank
            </p>

            <h3 class="text-3xl font-bold text-blue-600 mt-2">
                #{{ $results['rank'] }}
            </h3>

        </div>




        <div class="bg-white rounded-2xl border border-slate-200 p-6">

            <p class="text-sm text-slate-500">
                Group
            </p>

            <h3 class="text-xl font-bold text-slate-800 mt-3">
                {{ $group->group_name }}
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


        <div class="divide-y divide-slate-100">


            <div class="p-6 flex justify-between">

                <span class="text-slate-600">
                    Discussions created
                </span>


                <span class="font-semibold">
                    {{ $results['breakdown']['topics_created'] }}
                </span>

            </div>




            <div class="p-6 flex justify-between">

                <span class="text-slate-600">
                    Messages sent
                </span>
          

                <span class="font-semibold">
                   {{ $results['breakdown']['messages_sent'] }}
                </span>

            </div>



        </div>


    </div>



</div>


@endsection