@extends('layouts.student')

@section('title', 'My Quizzes - UniForum')

@section('page-title', 'My Quizzes')

@section('content')

<div>

    <!-- Header -->

    <div class="bg-white border-b border-slate-200">

        <div class="max-w-7xl mx-auto px-6 py-8">

            <h1 class="text-3xl font-bold text-slate-900">
                My Quizzes
            </h1>

            <p class="mt-2 text-slate-500">
                View upcoming, active and completed quizzes.
            </p>

        </div>

    </div>



    <div class="max-w-7xl mx-auto px-6 py-10">

        <!-- Summary Cards -->

        <div class="grid md:grid-cols-3 gap-6 mb-10">

            <div class="bg-white rounded-2xl border border-slate-200 p-6">

                <p class="text-sm text-slate-500">
                    Available
                </p>

                <h2 class="mt-3 text-3xl font-bold text-blue-600">
                    3
                </h2>

            </div>

            <div class="bg-white rounded-2xl border border-slate-200 p-6">

                <p class="text-sm text-slate-500">
                    Completed
                </p>

                <h2 class="mt-3 text-3xl font-bold text-green-600">
                    12
                </h2>

            </div>

            <div class="bg-white rounded-2xl border border-slate-200 p-6">

                <p class="text-sm text-slate-500">
                    Average Score
                </p>

                <h2 class="mt-3 text-3xl font-bold text-purple-600">
                    81%
                </h2>

            </div>

        </div>



        <!-- Quiz Cards -->

        <div class="space-y-6">

            <!-- Active Quiz -->

            <div class="bg-white rounded-2xl border border-blue-200 p-6">

                <div class="flex flex-col md:flex-row md:justify-between md:items-center gap-6">

                    <div>

                        <span class="inline-block px-3 py-1 rounded-full bg-blue-100 text-blue-700 text-xs">
                            ACTIVE
                        </span>

                        <h2 class="mt-4 text-2xl font-bold">
                            Software Engineering Midterm
                        </h2>

                        <p class="mt-2 text-slate-500">
                            20 Questions • Opens today at 2:00 PM • 60 Minutes
                        </p>

                    </div>

                    <a href="/student/quizzes/take"
                       class="inline-flex items-center justify-center bg-blue-600 text-white px-6 py-3 rounded-xl hover:bg-blue-700">
                            Start Quiz
                    </a>

                </div>

            </div>



            <!-- Upcoming -->

            <div class="bg-white rounded-2xl border border-slate-200 p-6">

                <div class="flex justify-between items-center">

                    <div>

                        <h2 class="font-semibold text-xl">
                            Artificial Intelligence Quiz
                        </h2>

                        <p class="mt-2 text-slate-500">
                            Tomorrow • 10:00 AM • 30 Minutes
                        </p>

                    </div>

                    <span class="px-4 py-2 rounded-full bg-yellow-100 text-yellow-700">
                        Upcoming
                    </span>

                </div>

            </div>



            <!-- Completed -->

            <div class="bg-white rounded-2xl border border-slate-200 p-6">

                <div class="flex justify-between items-center">

                    <div>

                        <h2 class="font-semibold text-xl">
                            Database Systems Quiz
                        </h2>

                        <p class="mt-2 text-slate-500">
                            Completed last week
                        </p>

                    </div>

                    <div class="text-right">

                        <p class="text-green-600 font-bold text-2xl">
                            89%
                        </p>

                        <a href="/student/quizzes/report"
                         class="text-sm text-slate-500">
                            View Report
                        </a>

                    </div>

                </div>

            </div>

        </div>

    </div>

</div>

@endsection