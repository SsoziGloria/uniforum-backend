@extends('layouts.lecturer')

@section('title', 'Notifications - UniForum')

@section('page-title', 'Notifications')

@section('content')
<div class="space-y-6">


    <!-- Introduction -->

    <div>

        <h2 class="text-2xl font-bold text-slate-800">
            Notifications
        </h2>

        <p class="text-slate-500 mt-1">
            Stay updated with activities happening in your groups and discussions.
        </p>

    </div>



    <!-- Notifications List -->


    <div class="bg-white rounded-2xl border border-slate-200">


        <div class="p-6 border-b border-slate-200">

            <h3 class="text-lg font-semibold text-slate-800">
                Recent Notifications
            </h3>

        </div>



        {{-- Notifications retrieved from backend --}}

        <div class="divide-y divide-slate-100">


           {{-- Notification action route will be connected during backend integration --}}
            <div class="p-6 flex gap-4">


                <div class="w-10 h-10 rounded-full bg-blue-100 flex items-center justify-center">
                    💬
                </div>


                <div>

                    <p class="font-semibold text-slate-800">
                        A student replied to your discussion
                    </p>


                    <p class="text-sm text-slate-500 mt-1">
                        "Laravel authentication problem" received a new reply.
                    </p>


                    <span class="text-xs text-slate-400">
                        10 minutes ago
                    </span>


                </div>


            </div>






            <div class="p-6 flex gap-4">


                <div class="w-10 h-10 rounded-full bg-blue-100 flex items-center justify-center">
                    📝
                </div>


                <div>

                    <p class="font-semibold text-slate-800">
                        Quiz submissions available
                    </p>


                    <p class="text-sm text-slate-500 mt-1">
                        Students have submitted answers for Database Systems quiz.
                    </p>


                    <span class="text-xs text-slate-400">
                        Yesterday
                    </span>


                </div>


            </div>







            <div class="p-6 flex gap-4">


                <div class="w-10 h-10 rounded-full bg-blue-100 flex items-center justify-center">
                    👥
                </div>


                <div>

                    <p class="font-semibold text-slate-800">
                        New students joined your group
                    </p>


                    <p class="text-sm text-slate-500 mt-1">
                        5 students joined Software Engineering Year 2.
                    </p>


                    <span class="text-xs text-slate-400">
                        2 days ago
                    </span>


                </div>


            </div>




        </div>


    </div>



</div>
@endsection