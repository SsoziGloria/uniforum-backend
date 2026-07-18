@extends('layouts.lecturer')

@section('title', 'Quiz Management - UniForum')

@section('page-title', 'Quiz Management')

@section('content')

<div class="min-h-screen bg-slate-50">


    <!-- Header -->

    <div class="bg-white border-b border-slate-200">

        <div class="max-w-7xl mx-auto px-6 py-8">


            <div class="flex flex-col md:flex-row md:justify-between md:items-center gap-5">


                <div>

                    <h1 class="text-3xl font-bold text-slate-900">
                        Quiz Management
                    </h1>

                    <p class="mt-2 text-slate-500">
                        Create, schedule, and monitor student assessments.
                    </p>

                </div>



                <a href="/lecturer/quizzes/create"
                   class="px-5 py-3 bg-blue-600 text-white rounded-xl hover:bg-blue-700 transition">

                    + Create Quiz

                </a>


            </div>


        </div>


    </div>





    <div class="max-w-7xl mx-auto px-6 py-10">



        <!-- Statistics -->


        <div class="grid md:grid-cols-4 gap-6 mb-10">


            <div class="bg-white border border-slate-200 rounded-2xl p-6">

                <p class="text-sm text-slate-500">
                    Total Quizzes
                </p>

                {{-- Total quizzes created by lecturer from backend --}}
                <h2 class="text-3xl font-bold mt-3">
                    --
                </h2>

            </div>




            <div class="bg-white border border-slate-200 rounded-2xl p-6">

                <p class="text-sm text-slate-500">
                    Published
                </p>

                {{-- Published quizzes count from backend --}}
                <h2 class="text-3xl font-bold mt-3 text-green-600">
                    --
                </h2>

            </div>





            <div class="bg-white border border-slate-200 rounded-2xl p-6">

                <p class="text-sm text-slate-500">
                    Upcoming
                </p>

                {{-- Upcoming scheduled quizzes from backend --}}
                <h2 class="text-3xl font-bold mt-3 text-blue-600">
                    --
                </h2>

            </div>





            <div class="bg-white border border-slate-200 rounded-2xl p-6">

                <p class="text-sm text-slate-500">
                    Average Score
                </p>

                {{-- Average student performance from backend --}}
                <h2 class="text-3xl font-bold mt-3 text-purple-600">
                   --
                </h2>

            </div>



        </div>







        <!-- Quiz List -->


        <div class="bg-white border border-slate-200 rounded-2xl overflow-hidden">


            <div class="px-6 py-5 border-b border-slate-200">

                <h2 class="text-lg font-semibold text-slate-900">
                    Your Quizzes
                </h2>

            </div>




            <div class="overflow-x-auto">


                <table class="w-full text-left">


                    <thead class="bg-slate-50 text-sm text-slate-500">

                        <tr>

                            <th class="px-6 py-4">
                                Quiz Title
                            </th>


                            <th class="px-6 py-4">
                                Category
                            </th>


                            <th class="px-6 py-4">
                                Date
                            </th>


                            <th class="px-6 py-4">
                                Duration
                            </th>


                            <th class="px-6 py-4">
                                Status
                            </th>


                            <th class="px-6 py-4">
                                Action
                            </th>


                        </tr>


                    </thead>



                   {{-- Quiz records from backend --}}


                    <tbody class="divide-y divide-slate-100">



                        <tr>


                            <td class="px-6 py-5">


                                <p class="font-medium text-slate-900">
                                    Object Oriented Programming Quiz
                                </p>


                                <p class="text-sm text-slate-500">
                                    20 Questions
                                </p>


                            </td>




                            <td class="px-6 py-5 text-slate-600">
                                Software Engineering
                            </td>



                            <td class="px-6 py-5 text-slate-600">
                                20 July 2026
                            </td>




                            <td class="px-6 py-5 text-slate-600">
                                45 Minutes
                            </td>




                            <td class="px-6 py-5">


                                <span class="px-3 py-1 rounded-full text-xs bg-green-100 text-green-700">

                                    Published

                                </span>


                            </td>




                            <td class="px-6 py-5">


                                <a href="/lecturer/quizzes/show"
                                   class="text-blue-600 hover:underline">

                                   Manage

                                </a>

                            </td>


                        </tr>







                        <tr>


                            <td class="px-6 py-5">


                                <p class="font-medium text-slate-900">
                                    Database Normalization
                                </p>


                                <p class="text-sm text-slate-500">
                                    15 Questions
                                </p>


                            </td>




                            <td class="px-6 py-5 text-slate-600">
                                Database Systems
                            </td>



                            <td class="px-6 py-5 text-slate-600">
                                25 July 2026
                            </td>




                            <td class="px-6 py-5 text-slate-600">
                                30 Minutes
                            </td>




                            <td class="px-6 py-5">


                                <span class="px-3 py-1 rounded-full text-xs bg-yellow-100 text-yellow-700">

                                    Draft

                                </span>


                            </td>

                            <td class="px-6 py-5">


                                <a href="/lecturer/quizzes/show"
                                   class="text-blue-600 hover:underline">

                                     Manage

                                </a>


                            </td>


                        </tr>



                    </tbody>


                </table>


            </div>


        </div>




    </div>


</div>


@endsection