@extends('layouts.lecturer')

@section('title', 'Participation Settings - UniForum')

@section('page-title', 'Participation Settings')

@section('content')

<div class="space-y-6">


    <!-- Header -->

    <div class="bg-white rounded-2xl border border-slate-200 p-6">


        <a href="/lecturer/groups/show"
           class="text-sm text-blue-600 hover:underline">

            ← Back to Group

        </a>


        <h2 class="mt-4 text-2xl font-bold text-slate-800">

            Software Engineering Year 2 Participation Settings

        </h2>


        <p class="text-slate-500 mt-2">

            Configure how students earn participation marks based on their activity.

        </p>


    </div>







    <!-- Criteria Management -->


    <div class="bg-white rounded-2xl border border-slate-200">


        <div class="p-6 border-b border-slate-200 flex justify-between items-center">


            <div>

                <h3 class="text-lg font-semibold text-slate-800">

                    Participation Criteria

                </h3>


                <p class="text-sm text-slate-500 mt-1">

                    Define activities that contribute towards participation marks.

                </p>

            </div>



            <button
                class="px-4 py-2 rounded-xl bg-blue-600 text-white hover:bg-blue-700">

                + Add Criterion

            </button>


        </div>








        <!-- Criteria Table -->


        <div class="overflow-x-auto">


            <table class="w-full">


                <thead class="bg-slate-100">


                    <tr>


                        <th class="text-left px-6 py-4">
                            Activity
                        </th>


                        <th class="text-left px-6 py-4">
                            Description
                        </th>


                        <th class="text-left px-6 py-4">
                            Marks
                        </th>


                        <th class="text-left px-6 py-4">
                            Actions
                        </th>


                    </tr>


                </thead>






                <tbody>


                    <!-- Example criterion -->


                    <tr class="border-t">


                        <td class="px-6 py-5 font-medium">

                            Creating Discussion

                        </td>


                        <td class="px-6 py-5 text-slate-500">

                            Marks awarded when a student starts a discussion.

                        </td>


                        <td class="px-6 py-5">


                            <input
                                type="number"
                                value="5"
                                class="w-24 rounded-lg border-slate-200">


                        </td>


                        <td class="px-6 py-5">


                            <button class="text-blue-600 hover:underline">

                                Edit

                            </button>


                            <button class="text-red-600 hover:underline ml-4">

                                Delete

                            </button>


                        </td>


                    </tr>








                    <tr class="border-t">


                        <td class="px-6 py-5 font-medium">

                            Answering Questions

                        </td>


                        <td class="px-6 py-5 text-slate-500">

                            Marks awarded for helping other members.

                        </td>


                        <td class="px-6 py-5">


                            <input
                                type="number"
                                value="3"
                                class="w-24 rounded-lg border-slate-200">


                        </td>


                        <td class="px-6 py-5">


                            <button class="text-blue-600 hover:underline">

                                Edit

                            </button>


                            <button class="text-red-600 hover:underline ml-4">

                                Delete

                            </button>


                        </td>


                    </tr>








                    <tr class="border-t">


                        <td class="px-6 py-5 font-medium">

                            Replying to Discussions

                        </td>


                        <td class="px-6 py-5 text-slate-500">

                            Marks awarded for meaningful participation.

                        </td>


                        <td class="px-6 py-5">


                            <input
                                type="number"
                                value="1"
                                class="w-24 rounded-lg border-slate-200">


                        </td>


                        <td class="px-6 py-5">


                            <button class="text-blue-600 hover:underline">

                                Edit

                            </button>


                            <button class="text-red-600 hover:underline ml-4">

                                Delete

                            </button>


                        </td>


                    </tr>



                </tbody>


            </table>


        </div>



    </div>







    <!-- Save -->


    <div class="flex justify-end">


        <button
            class="px-6 py-3 rounded-xl bg-green-600 text-white hover:bg-green-700">

            Save Criteria

        </button>


    </div>






</div>


@endsection