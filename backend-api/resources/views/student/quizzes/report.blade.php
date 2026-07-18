@extends('layouts.student')

@section('title', 'Quiz Report - UniForum')

@section('page-title', 'Quiz Report')

@section('content')

<div class="min-h-screen bg-slate-50">


    <!-- Header -->

    <div class="bg-white border-b border-slate-200">

        <div class="max-w-6xl mx-auto px-6 py-8">


            <a href="/student/quizzes"
               class="text-sm text-blue-600 hover:underline">

                ← Back to Quizzes

            </a>


            <h1 class="mt-5 text-3xl font-bold text-slate-900">

                Software Engineering Midterm Report

            </h1>


            <p class="mt-2 text-slate-500">

                View your quiz performance and assessment results.

            </p>


        </div>

    </div>






    <div class="max-w-6xl mx-auto px-6 py-10">


        <!-- Score Summary -->


        <div class="grid md:grid-cols-4 gap-6 mb-10">


            <div class="bg-white rounded-2xl border p-6">

                <p class="text-sm text-slate-500">
                    Score
                </p>

                <h2 class="mt-3 text-4xl font-bold text-blue-600">
                    89%
                </h2>

            </div>



            <div class="bg-white rounded-2xl border p-6">

                <p class="text-sm text-slate-500">
                    Questions
                </p>

                <h2 class="mt-3 text-3xl font-bold">
                    20
                </h2>

            </div>




            <div class="bg-white rounded-2xl border p-6">

                <p class="text-sm text-slate-500">
                    Correct Answers
                </p>

                <h2 class="mt-3 text-3xl font-bold text-green-600">
                    18
                </h2>

            </div>




            <div class="bg-white rounded-2xl border p-6">

                <p class="text-sm text-slate-500">
                    Time Taken
                </p>

                <h2 class="mt-3 text-3xl font-bold">
                    45 min
                </h2>

            </div>


        </div>






        <!-- Performance Overview -->


        <div class="bg-white rounded-2xl border p-8 mb-8">


            <h2 class="text-xl font-semibold">

                Performance Overview

            </h2>



            <div class="mt-6">


                <div class="flex justify-between text-sm mb-2">

                    <span>
                        Overall Performance
                    </span>

                    <span>
                        Excellent
                    </span>

                </div>


                <div class="h-3 bg-slate-200 rounded-full overflow-hidden">

                    <div class="bg-blue-600 h-full w-[89%] rounded-full"></div>

                </div>


            </div>


        </div>






        <!-- Question Breakdown -->


        <div class="bg-white rounded-2xl border p-8">


            <h2 class="text-xl font-semibold mb-6">

                Question Breakdown

            </h2>



            <div class="space-y-4">


                <div class="flex justify-between p-4 bg-green-50 rounded-xl">

                    <span>
                        Correct Answers
                    </span>

                    <span class="text-green-600 font-bold">
                        18
                    </span>

                </div>



                <div class="flex justify-between p-4 bg-red-50 rounded-xl">

                    <span>
                        Incorrect Answers
                    </span>

                    <span class="text-red-600 font-bold">
                        2
                    </span>

                </div>



                <div class="flex justify-between p-4 bg-yellow-50 rounded-xl">

                    <span>
                        Unanswered
                    </span>

                    <span class="text-yellow-600 font-bold">
                        0
                    </span>

                </div>


            </div>


        </div>






        <!-- Lecturer Feedback -->


        <div class="mt-8 bg-blue-50 border border-blue-200 rounded-2xl p-6">


            <h2 class="font-semibold text-blue-900">

                Lecturer Feedback

            </h2>


            <p class="mt-3 text-blue-700">

                Your lecturer feedback will appear here after review.

            </p>


        </div>



    </div>


</div>


@endsection