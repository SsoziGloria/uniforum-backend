@extends('layouts.lecturer')

@section('title', 'Students - UniForum')

@section('page-title', 'Students')

@section('content')

<div class="min-h-screen bg-slate-50">


    <!-- Header -->

    <div class="bg-white border-b border-slate-200">

        <div class="max-w-7xl mx-auto px-6 py-8">


            <h1 class="text-3xl font-bold text-slate-900">
                Student Performance
            </h1>


            <p class="mt-2 text-slate-500">
                Monitor participation, engagement, and academic activity.
            </p>


        </div>


    </div>







    <div class="max-w-7xl mx-auto px-6 py-10">


        <!-- Overview Cards -->


        <div class="grid md:grid-cols-4 gap-6 mb-10">


            <div class="bg-white border border-slate-200 rounded-2xl p-6">

                <p class="text-sm text-slate-500">
                    Total Students
                </p>

                <h2 class="mt-3 text-3xl font-bold">
                   {{-- Total students count from backend --}} 
                  --
                </h2>

            </div>





            <div class="bg-white border border-slate-200 rounded-2xl p-6">

                <p class="text-sm text-slate-500">
                    Active This Week
                </p>

                <h2 class="mt-3 text-3xl font-bold text-green-600">
                    --
                </h2>

            </div>





            <div class="bg-white border border-slate-200 rounded-2xl p-6">

                <p class="text-sm text-slate-500">
                    Average Participation
                </p>

                <h2 class="mt-3 text-3xl font-bold text-blue-600">
                    --
                </h2>

            </div>





            <div class="bg-white border border-slate-200 rounded-2xl p-6">

                <p class="text-sm text-slate-500">
                    Average Quiz Score
                </p>

                <h2 class="mt-3 text-3xl font-bold text-purple-600">
                    --
                </h2>

            </div>



        </div>







        <!-- Students Table -->


        <div class="bg-white rounded-2xl border border-slate-200 overflow-hidden">


            <div class="px-6 py-5 border-b border-slate-200">

                <h2 class="font-semibold text-lg text-slate-900">
                    Student Activity
                </h2>

            </div>





            <div class="overflow-x-auto">


                <table class="w-full text-left">


                    <thead class="bg-slate-50 text-sm text-slate-500">


                        <tr>


                            <th class="px-6 py-4">
                                Student
                            </th>


                            <th class="px-6 py-4">
                                Discussions
                            </th>


                            <th class="px-6 py-4">
                                Answers
                            </th>


                            <th class="px-6 py-4">
                                Quiz Average
                            </th>


                            <th class="px-6 py-4">
                                Participation Mark
                            </th>


                            <th class="px-6 py-4">
                                Status
                            </th>


                        </tr>


                    </thead>





                    <tbody class="divide-y divide-slate-100">

                    {{-- Student participation records will be loaded from backend --}}



                        <tr>


                            <td class="px-6 py-5">


                                <div>

                                    <p class="font-medium text-slate-900">
                                        Sarah Namukasa
                                    </p>


                                    <p class="text-sm text-slate-500">
                                        Year 2 Software Engineering
                                    </p>

                                </div>


                            </td>





                            <td class="px-6 py-5">
                                24
                            </td>




                            <td class="px-6 py-5">
                                36
                            </td>





                            <td class="px-6 py-5">
                                85%
                            </td>





                            <td class="px-6 py-5">


                                <span class="px-3 py-1 rounded-full text-xs bg-green-100 text-green-700">

                                    18/20

                                </span>


                            </td>





                            <td class="px-6 py-5">


                                <span class="px-3 py-1 rounded-full text-xs bg-green-100 text-green-700">

                                    Active

                                </span>


                            </td>



                        </tr>








                        <tr>


                            <td class="px-6 py-5">


                                <div>

                                    <p class="font-medium text-slate-900">
                                        John Okello
                                    </p>


                                    <p class="text-sm text-slate-500">
                                        Year 2 Software Engineering
                                    </p>

                                </div>


                            </td>





                            <td class="px-6 py-5">
                                8
                            </td>




                            <td class="px-6 py-5">
                                5
                            </td>





                            <td class="px-6 py-5">
                                62%
                            </td>





                            <td class="px-6 py-5">


                                <span class="px-3 py-1 rounded-full text-xs bg-yellow-100 text-yellow-700">

                                    11/20

                                </span>


                            </td>





                            <td class="px-6 py-5">


                                <span class="px-3 py-1 rounded-full text-xs bg-orange-100 text-orange-700">

                                    Low Activity

                                </span>


                            </td>



                        </tr>




                    </tbody>



                </table>


            </div>


        </div>




    </div>



</div>


@endsection