@extends('layouts.lecturer')

@section('title', 'Quiz Details - UniForum')

@section('page-title', 'Quiz Details')

@section('content')

<div class="space-y-6">


    <!-- Quiz Header -->

    <div class="bg-white rounded-2xl border border-slate-200 p-6">


        <div class="flex flex-col md:flex-row md:justify-between md:items-center gap-4">


            <div>

                {{-- Quiz details from backend --}}

                <h2 class="text-2xl font-bold text-slate-800">
                    Object Oriented Programming Quiz
                </h2>


                <p class="text-slate-500 mt-2">
                    Software Engineering
                </p>


            </div>


            <span class="px-4 py-2 rounded-full text-sm bg-green-100 text-green-700">

                Published

            </span>


        </div>


    </div>





    <!-- Quiz Configuration -->


    <div class="bg-white rounded-2xl border border-slate-200 p-6">


        <h3 class="text-lg font-semibold text-slate-800 mb-6">
            Quiz Configuration
        </h3>


        {{-- Configuration data from backend --}}


        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">


            <div>

                <p class="text-sm text-slate-500">
                    Date
                </p>

                <p class="font-medium text-slate-800 mt-1">
                    20 July 2026
                </p>

            </div>




            <div>

                <p class="text-sm text-slate-500">
                    Duration
                </p>

                <p class="font-medium text-slate-800 mt-1">
                    45 Minutes
                </p>

            </div>




            <div>

                <p class="text-sm text-slate-500">
                    Student Category
                </p>

                <p class="font-medium text-slate-800 mt-1">
                    Software Engineering Year 2
                </p>

            </div>




            <div>

                <p class="text-sm text-slate-500">
                    Number of Questions
                </p>

                <p class="font-medium text-slate-800 mt-1">
                    20 Questions
                </p>

            </div>


        </div>


    </div>







    <!-- Submission Statistics -->


    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">


        <div class="bg-white border border-slate-200 rounded-2xl p-6">

            <p class="text-sm text-slate-500">
                Total Students
            </p>


            <h3 class="text-3xl font-bold text-blue-600 mt-2">
                --
            </h3>


            {{-- Number of assigned students from backend --}}

        </div>




        <div class="bg-white border border-slate-200 rounded-2xl p-6">

            <p class="text-sm text-slate-500">
                Submitted
            </p>


            <h3 class="text-3xl font-bold text-green-600 mt-2">
                --
            </h3>


            {{-- Submitted attempts from backend --}}

        </div>





        <div class="bg-white border border-slate-200 rounded-2xl p-6">

            <p class="text-sm text-slate-500">
                Average Score
            </p>


            <h3 class="text-3xl font-bold text-purple-600 mt-2">
                --
            </h3>


            {{-- Quiz performance data from backend --}}

        </div>


    </div>








    <!-- Actions -->


    <div class="bg-white rounded-2xl border border-slate-200 p-6">


        <h3 class="text-lg font-semibold text-slate-800 mb-4">
            Actions
        </h3>



        <div class="flex flex-wrap gap-3">


            <a href="/lecturer/quizzes/results"
               class="px-5 py-3 rounded-xl bg-blue-600 text-white hover:bg-blue-700">

                  View Results

            </a>


        </div>


    </div>



</div>


@endsection