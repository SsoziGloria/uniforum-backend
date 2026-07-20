@extends('layouts.lecturer')

@section('title', 'Create Group - UniForum')

@section('page-title', 'Create Group')

@section('content')

<div class="min-h-screen bg-slate-50">


    <!-- Header -->

    <div class="bg-white border-b border-slate-200">

        <div class="max-w-5xl mx-auto px-6 py-8">


            <a href="/lecturer/groups"
               class="text-sm text-blue-600 hover:underline">

                ← Back to Groups

            </a>


            <h1 class="mt-5 text-3xl font-bold text-slate-900">

                Create Academic Group

            </h1>


            <p class="mt-2 text-slate-500">

                Create an academic discussion group and manage student collaboration.

            </p>


        </div>

    </div>





    <div class="max-w-5xl mx-auto px-6 py-10">


        <div class="bg-white rounded-2xl border border-slate-200 p-8">


            <form method="POST" action="#">

                {{-- @csrf --}}

                {{-- Backend:
                     Replace action with group creation route.
                     After successful creation:
                     - Create group
                     - Add authenticated lecturer as group member
                     - Assign group admin privilege to creator
                --}}



                <!-- Group Information -->


                <h2 class="text-lg font-semibold text-slate-900">

                    Group Information

                </h2>



                <div class="mt-6 space-y-6">


                    <div>

                        <label class="block text-sm font-medium text-slate-700 mb-2">

                            Group Name

                        </label>


                        <input
                            type="text"
                            name="name"
                            placeholder="e.g. Artificial Intelligence Research Group"
                            class="w-full rounded-xl border-slate-200 focus:border-blue-500 focus:ring-blue-500">


                    </div>





                    <div>

                        <label class="block text-sm font-medium text-slate-700 mb-2">

                            Description

                        </label>


                        <textarea
                            name="description"
                            rows="5"
                            placeholder="Describe the purpose of this group..."
                            class="w-full rounded-xl border-slate-200 focus:border-blue-500 focus:ring-blue-500"></textarea>


                    </div>


                </div>







                <!-- Academic Details -->


                <div class="mt-10">


                    <h2 class="text-lg font-semibold text-slate-900">

                        Academic Details

                    </h2>




                    <div class="mt-6 grid md:grid-cols-2 gap-6">



                        <div>


                            <label class="block text-sm font-medium text-slate-700 mb-2">

                                Course / Programme

                            </label>



                            <select
                                name="course"
                                class="w-full rounded-xl border-slate-200">


                                <option>
                                    Select Course
                                </option>


                                <option>
                                    Software Engineering
                                </option>


                                <option>
                                    Computer Science
                                </option>


                                <option>
                                    Information Systems
                                </option>


                            </select>


                            {{-- Backend:
                                 Populate courses dynamically if required.
                            --}}


                        </div>






                        <div>


                            <label class="block text-sm font-medium text-slate-700 mb-2">

                                Academic Year

                            </label>



                            <select
                                name="year"
                                class="w-full rounded-xl border-slate-200">


                                <option>
                                    Select Year
                                </option>


                                <option>
                                    Year 1
                                </option>


                                <option>
                                    Year 2
                                </option>


                                <option>
                                    Year 3
                                </option>


                                <option>
                                    Year 4
                                </option>


                            </select>


                        </div>



                    </div>



                </div>


                <!-- Actions -->


                <div class="mt-10 flex justify-end gap-4">



                    <a href="/lecturer/groups"
                       class="px-6 py-3 rounded-xl bg-slate-100 text-slate-700 hover:bg-slate-200">


                        Cancel


                    </a>





                    <button
                        type="submit"
                        class="px-6 py-3 rounded-xl bg-blue-600 text-white hover:bg-blue-700">


                        Create Group


                    </button>



                </div>




            </form>


        </div>


    </div>


</div>


@endsection