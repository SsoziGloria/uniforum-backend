@extends('layouts.lecturer')

@section('title', 'Create Quiz - UniForum')

@section('page-title', 'Create Quiz')

@section('content')

<div class="min-h-screen bg-slate-50">


    <!-- Header -->

    <div class="bg-white border-b border-slate-200">

        <div class="max-w-5xl mx-auto px-6 py-8">


            <a href="/lecturer/quizzes"
               class="text-sm text-blue-600 hover:underline flex items-center gap-2">

                <svg class="w-4 h-4"
                     fill="none"
                     stroke="currentColor"
                     stroke-width="2"
                     viewBox="0 0 24 24">

                    <path stroke-linecap="round"
                          stroke-linejoin="round"
                          d="M15 19l-7-7 7-7"/>

                </svg>

                Back to Quizzes

            </a>



            <h1 class="mt-5 text-3xl font-bold text-slate-900">
                Create New Quiz
            </h1>


            <p class="mt-2 text-slate-500">
                Configure your assessment before publishing it to students.
            </p>


        </div>


    </div>







    <div class="max-w-5xl mx-auto px-6 py-10">


        <div class="bg-white rounded-2xl border border-slate-200 p-8">


            <form method="POST" action="#">

    {{-- Backend: quiz creation endpoint will be connected here --}}



                <!-- Basic Information -->


                <h2 class="text-lg font-semibold text-slate-900">
                    Quiz Information
                </h2>


                {{-- Quiz basic information from lecturer input --}}

                <div class="mt-6 grid md:grid-cols-2 gap-6">


                    <div>

                        <label class="block text-sm font-medium text-slate-700 mb-2">
                            Quiz Title
                        </label>


                        <input type="text"
                               placeholder="Enter quiz title"
                               class="w-full rounded-xl border-slate-200">

                    </div>




                    <div>

                        <label class="block text-sm font-medium text-slate-700 mb-2">
                            Category
                        </label>


                        <select class="w-full rounded-xl border-slate-200">


                            <option>
                                Select category
                            </option>


                            <option>
                                Software Engineering
                            </option>


                            <option>
                                Artificial Intelligence
                            </option>


                            <option>
                                Database Systems
                            </option>


                        </select>

                    </div>



                </div>







                <!-- Target Students -->


                <div class="mt-8">


                    <h2 class="text-lg font-semibold text-slate-900">
                        Student Access
                    </h2>


                    <p class="text-sm text-slate-500 mt-1">
                        Choose which students should attempt this quiz.
                    </p>


                    {{-- Student categories retrieved from backend --}}
                    <div class="mt-4 grid md:grid-cols-3 gap-4">


                        <label class="border rounded-xl p-4 cursor-pointer hover:border-blue-500">


                            <input type="checkbox"
                                   class="text-blue-600">

                            <span class="ml-2 text-sm">
                                Year 1 Students
                            </span>


                        </label>




                        <label class="border rounded-xl p-4 cursor-pointer hover:border-blue-500">


                            <input type="checkbox"
                                   class="text-blue-600">


                            <span class="ml-2 text-sm">
                                Year 2 Students
                            </span>


                        </label>





                        <label class="border rounded-xl p-4 cursor-pointer hover:border-blue-500">


                            <input type="checkbox"
                                   class="text-blue-600">


                            <span class="ml-2 text-sm">
                                Year 3 Students
                            </span>


                        </label>



                    </div>


                </div>








                <!-- Schedule -->


                <div class="mt-8">


                    <h2 class="text-lg font-semibold text-slate-900">
                        Schedule Configuration
                    </h2>


                   {{-- Quiz scheduling configuration --}}
                    <div class="mt-5 grid md:grid-cols-2 gap-6">



                        <div>


                            <label class="block text-sm font-medium text-slate-700 mb-2">
                                Start Date
                            </label>


                            <input type="date"
                                   class="w-full rounded-xl border-slate-200">


                        </div>





                        <div>


                            <label class="block text-sm font-medium text-slate-700 mb-2">
                                Start Time
                            </label>


                            <input type="time"
                                   class="w-full rounded-xl border-slate-200">


                        </div>





                        <div>


                            <label class="block text-sm font-medium text-slate-700 mb-2">
                                Duration
                            </label>


                            <select class="w-full rounded-xl border-slate-200">


                                <option>
                                    15 Minutes
                                </option>


                                <option>
                                    30 Minutes
                                </option>


                                <option>
                                    45 Minutes
                                </option>


                                <option>
                                    1 Hour
                                </option>


                            </select>


                        </div>




                        <div>


                            <label class="block text-sm font-medium text-slate-700 mb-2">
                                Number of Questions
                            </label>


                            <input type="number"
                                   placeholder="20"
                                   class="w-full rounded-xl border-slate-200">


                        </div>


                    </div>


                </div>








                <!-- Rules -->


                <div class="mt-8">


                    <h2 class="text-lg font-semibold text-slate-900">
                        Quiz Rules
                    </h2>



                    <div class="mt-4 space-y-4">


                        <label class="flex items-center gap-3">

                            <input type="checkbox"
                                   checked
                                   class="text-blue-600">


                            <span class="text-sm text-slate-600">
                                Automatically submit when time expires
                            </span>


                        </label>




                        <label class="flex items-center gap-3">


                            <input type="checkbox"
                                   class="text-blue-600">


                            <span class="text-sm text-slate-600">
                                Prevent students from leaving quiz interface
                            </span>


                        </label>





                        <label class="flex items-center gap-3">


                            <input type="checkbox"
                                   class="text-blue-600">


                            <span class="text-sm text-slate-600">
                                Publish performance report after completion
                            </span>


                        </label>



                    </div>



                </div>








                <!-- Actions -->


                <div class="mt-10 flex justify-end gap-4">


                    <button type="button"
                            class="px-6 py-3 rounded-xl bg-slate-100 text-slate-700">

                           {{-- Backend: save quiz as draft --}}

                            Save Draft

                    </button>


                    
                    <button type="submit"
                            class="px-6 py-3 rounded-xl bg-blue-600 text-white hover:bg-blue-700">
                        {{-- Backend: validate configuration then publish quiz --}}
                        Publish Quiz

                    </button>



                </div>




            </form>


        </div>


    </div>



</div>


@endsection