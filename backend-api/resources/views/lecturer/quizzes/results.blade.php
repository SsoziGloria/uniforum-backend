@extends('layouts.lecturer')

@section('title', 'Quiz Results - UniForum')

@section('page-title', 'Quiz Results')

@section('content')

<div class="space-y-6">


    <!-- Header -->

    <div class="bg-white rounded-2xl border border-slate-200 p-6">


        <div>

            {{-- Quiz results data from backend --}}

            <h2 class="text-2xl font-bold text-slate-800">
                Object Oriented Programming Quiz Results
            </h2>


            <p class="text-slate-500 mt-2">
                Review student performance and quiz submissions.
            </p>


        </div>


    </div>





    <!-- Summary Statistics -->


    <div class="grid grid-cols-1 md:grid-cols-4 gap-6">


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


            {{-- Completed attempts from backend --}}

        </div>





        <div class="bg-white border border-slate-200 rounded-2xl p-6">

            <p class="text-sm text-slate-500">
                Average Score
            </p>


            <h3 class="text-3xl font-bold text-purple-600 mt-2">
                --
            </h3>


            {{-- Average score calculated by backend --}}

        </div>





        <div class="bg-white border border-slate-200 rounded-2xl p-6">

            <p class="text-sm text-slate-500">
                Completion Rate
            </p>


            <h3 class="text-3xl font-bold text-orange-600 mt-2">
                --
            </h3>


            {{-- Completion percentage from backend --}}

        </div>


    </div>







    <!-- Student Performance Table -->


    <div class="bg-white rounded-2xl border border-slate-200 overflow-hidden">


        <div class="p-6 border-b border-slate-200">

            <h3 class="text-lg font-semibold text-slate-800">
                Student Performance
            </h3>

        </div>





        <div class="overflow-x-auto">


            {{-- Student results populated from backend --}}


            <table class="w-full text-left">


                <thead class="bg-slate-50 text-sm text-slate-500">


                    <tr>

                        <th class="px-6 py-4">
                            Student
                        </th>


                        <th class="px-6 py-4">
                            Score
                        </th>


                        <th class="px-6 py-4">
                            Submission Status
                        </th>


                        <th class="px-6 py-4">
                            Submitted At
                        </th>


                    </tr>


                </thead>




                <tbody class="divide-y divide-slate-100">



                    <tr>


                        <td class="px-6 py-5">

                            <p class="font-medium text-slate-800">
                                John Okello
                            </p>


                        </td>




                        <td class="px-6 py-5 text-slate-600">
                            85%
                        </td>




                        <td class="px-6 py-5">

                            <span class="px-3 py-1 rounded-full text-xs bg-green-100 text-green-700">

                                Submitted

                            </span>

                        </td>




                        <td class="px-6 py-5 text-slate-500">
                            20 July 2026, 10:45 AM
                        </td>


                    </tr>






                    <tr>


                        <td class="px-6 py-5">

                            <p class="font-medium text-slate-800">
                                Sarah Namukasa
                            </p>


                        </td>




                        <td class="px-6 py-5 text-slate-600">
                            72%
                        </td>




                        <td class="px-6 py-5">

                            <span class="px-3 py-1 rounded-full text-xs bg-green-100 text-green-700">

                                Submitted

                            </span>

                        </td>




                        <td class="px-6 py-5 text-slate-500">
                            20 July 2026, 10:30 AM
                        </td>


                    </tr>



                </tbody>


            </table>


        </div>


    </div>





    <!-- Back Navigation -->


    <div>

        <a href="/lecturer/quizzes/show"
           class="text-blue-600 hover:underline text-sm">

            ← Back to Quiz Details

        </a>

    </div>



</div>


@endsection