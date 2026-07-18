@extends('layouts.lecturer')

@section('title', 'Participation - UniForum')

@section('page-title', 'Participation')

@section('content')

<div class="space-y-6">


    <!-- Header -->

    <div class="bg-white rounded-2xl border border-slate-200 p-6">


        <a href="/lecturer/groups/show"
           class="text-sm text-blue-600 hover:underline">

            ← Back to Group

        </a>


        <h2 class="mt-4 text-2xl font-bold text-slate-800">

            Group Participation

        </h2>


        <p class="text-slate-500 mt-2">

            View student participation performance based on configured criteria.

        </p>


    </div>






    <!-- Criteria Summary -->

    <div class="bg-white rounded-2xl border border-slate-200 p-6">


        <h3 class="text-lg font-semibold text-slate-800 mb-5">

            Active Participation Criteria

        </h3>



        {{-- Backend:
             Retrieve criteria configured by lecturer.
             
             Example:
             Questions Asked - 20%
             Answers Provided - 30%
             Helpful Replies - 30%
             Resources Shared - 20%
        --}}



        <div class="grid md:grid-cols-4 gap-4">


            <div class="bg-blue-50 rounded-xl p-4">

                <p class="text-sm text-slate-600">
                    Questions Asked
                </p>

                <p class="text-xl font-bold text-blue-600 mt-2">
                    --
                </p>

            </div>



            <div class="bg-blue-50 rounded-xl p-4">

                <p class="text-sm text-slate-600">
                    Answers Given
                </p>

                <p class="text-xl font-bold text-blue-600 mt-2">
                    --
                </p>

            </div>




            <div class="bg-blue-50 rounded-xl p-4">

                <p class="text-sm text-slate-600">
                    Helpful Replies
                </p>

                <p class="text-xl font-bold text-blue-600 mt-2">
                    --
                </p>

            </div>




            <div class="bg-blue-50 rounded-xl p-4">

                <p class="text-sm text-slate-600">
                    Resources Shared
                </p>

                <p class="text-xl font-bold text-blue-600 mt-2">
                    --
                </p>

            </div>


        </div>


    </div>







    <!-- Student Scores -->

    <div class="bg-white rounded-2xl border border-slate-200 overflow-hidden">


        <div class="p-6 border-b border-slate-200">


            <h3 class="text-lg font-semibold text-slate-800">

                Student Participation Scores

            </h3>


        </div>



        {{-- Backend:
             Retrieve students and calculated participation marks.
        --}}



        <table class="w-full">


            <thead class="bg-slate-100">


                <tr>


                    <th class="text-left px-6 py-4">
                        Student
                    </th>


                    <th class="text-left px-6 py-4">
                        Questions
                    </th>


                    <th class="text-left px-6 py-4">
                        Answers
                    </th>


                    <th class="text-left px-6 py-4">
                        Contributions
                    </th>


                    <th class="text-left px-6 py-4">
                        Total Score
                    </th>


                </tr>


            </thead>





            <tbody>


                <tr class="border-t">


                    <td class="px-6 py-5">
                        Sarah Namukasa
                    </td>


                    <td class="px-6 py-5">
                        --
                    </td>


                    <td class="px-6 py-5">
                        --
                    </td>


                    <td class="px-6 py-5">
                        --
                    </td>


                    <td class="px-6 py-5 font-semibold text-blue-600">
                        -- / 100
                    </td>


                </tr>




                <tr class="border-t">


                    <td class="px-6 py-5">
                        Peter Okello
                    </td>


                    <td class="px-6 py-5">
                        --
                    </td>


                    <td class="px-6 py-5">
                        --
                    </td>


                    <td class="px-6 py-5">
                        --
                    </td>


                    <td class="px-6 py-5 font-semibold text-blue-600">
                        -- / 100
                    </td>


                </tr>


            </tbody>


        </table>


    </div>






    <!-- Participation Information -->

    <div class="bg-white rounded-2xl border border-slate-200 p-6">


        <h3 class="text-lg font-semibold text-slate-800">

            How Scores Are Calculated

        </h3>


        <p class="mt-3 text-slate-500">

            Participation scores are automatically generated by the system
            based on student engagement within the discussion group.

        </p>


    </div>



</div>


@endsection