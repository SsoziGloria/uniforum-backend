@extends('layouts.lecturer')

@section('title', 'Create Discussion - UniForum')

@section('page-title', 'Create Discussion')

@section('content')

<div>

    <!-- Header -->
    <div class="bg-white border-b border-slate-200">

        <div class="max-w-4xl mx-auto px-6 py-8">

            <a href="/lecturer/discussions"
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

                Back to Discussions

            </a>


            <h1 class="mt-5 text-3xl font-bold text-slate-900">
                Start a New Discussion
            </h1>


            <p class="mt-2 text-slate-500">
                Ask questions, share ideas, and start conversations with your university community.
            </p>

        </div>

    </div>





    <!-- Form -->

    <div class="max-w-4xl mx-auto px-6 py-10">


        <div class="bg-white rounded-2xl border border-slate-200 p-8">


            <form method="POST" action="#">


                <!-- Title -->

                <div>

                    <label class="block text-sm font-medium text-slate-700 mb-2">
                        Discussion Title
                    </label>


                    <input type="text"
                           placeholder="Enter your discussion title"
                           class="w-full rounded-xl border-slate-200 focus:ring-blue-500 focus:border-blue-500">

                    <p class="mt-2 text-xs text-slate-400">
                        Use a clear title that describes your question.
                    </p>

                </div>





                <!-- Category -->

                <div class="mt-6">


                    <label class="block text-sm font-medium text-slate-700 mb-2">
                        Category
                    </label>


                    <select class="w-full rounded-xl border-slate-200 focus:ring-blue-500 focus:border-blue-500">


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


                        <option>
                            Web Development
                        </option>


                    </select>


                </div>







                <!-- Description -->

                <div class="mt-6">


                    <label class="block text-sm font-medium text-slate-700 mb-2">
                        Description
                    </label>


                    <textarea
                        rows="7"
                        placeholder="Explain your question or topic..."
                        class="w-full rounded-xl border-slate-200 focus:ring-blue-500 focus:border-blue-500"></textarea>


                </div>







                <!-- Attachment -->

                <div class="mt-6">


                    <label class="block text-sm font-medium text-slate-700 mb-2">
                        Attachment
                    </label>


                    <div class="border-2 border-dashed border-slate-200 rounded-xl p-8 text-center">


                        <svg class="mx-auto w-10 h-10 text-slate-400"
                             fill="none"
                             stroke="currentColor"
                             stroke-width="2"
                             viewBox="0 0 24 24">

                            <path stroke-linecap="round"
                                  stroke-linejoin="round"
                                  d="M12 4v16m8-8H4"/>

                        </svg>


                        <p class="mt-3 text-sm text-slate-500">
                            Upload supporting files (optional)
                        </p>


                        <button type="button"
                                class="mt-3 text-blue-600 text-sm font-medium">
                            Choose File
                        </button>


                    </div>


                </div>







                <!-- AI Notice -->

                <div class="mt-6 bg-blue-50 rounded-xl p-4 flex gap-3">


                    <svg class="w-6 h-6 text-blue-600 flex-shrink-0"
                         fill="none"
                         stroke="currentColor"
                         stroke-width="2"
                         viewBox="0 0 24 24">

                        <path stroke-linecap="round"
                              stroke-linejoin="round"
                              d="M13 10V3L4 14h7v7l9-11h-7z"/>

                    </svg>


                    <p class="text-sm text-blue-700">

                        UniForum AI will automatically classify your discussion 
                        and recommend it to members with similar interests based on their previous engagement.
                    </p>


                </div>







                <!-- Buttons -->

                <div class="mt-8 flex justify-end gap-4">


                    <a href="/lecturer/discussions"
                       class="px-6 py-3 rounded-xl bg-slate-100 text-slate-700 hover:bg-slate-200">

                       Cancel

                    </a>

                    <button type="submit"
                            class="px-6 py-3 rounded-xl bg-blue-600 text-white hover:bg-blue-700">

                        Post Discussion

                    </button>


                </div>



            </form>


        </div>


    </div>



</div>


@endsection