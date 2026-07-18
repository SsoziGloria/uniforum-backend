@extends('layouts.lecturer')

@section('title', 'Discussion Review - UniForum')

@section('page-title', 'Discussion Review')

@section('content')

<div>

    <!-- Header -->
    <div class="bg-white border-b border-slate-200">

        <div class="max-w-5xl mx-auto px-6 py-8">

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
            
            {{-- Discussion details from backend --}}

            <div class="mt-5">
 
                <span class="text-xs font-semibold bg-blue-100 text-blue-700 px-3 py-1 rounded-full">
                    Artificial Intelligence
                </span>


                <h1 class="mt-4 text-3xl font-bold text-slate-900">
                    How can machine learning improve university systems?
                </h1>


                <div class="mt-4 flex flex-wrap gap-4 text-sm text-slate-500">

                    <span>
                        Posted by Gloria
                    </span>

                    <span>
                        •
                    </span>

                    <span>
                        2 hours ago
                    </span>

                    <span>
                        •
                    </span>

                    <span>
                        45 views
                    </span>

                    {{-- Discussion status from backend --}}
                    <span class="inline-flex items-center rounded-full bg-orange-100 px-3 py-1 text-xs font-semibold text-orange-700">
                       Needs Answer
                    </span>

                </div>

            </div>

        </div>

    </div>




    <div class="max-w-5xl mx-auto px-6 py-10 space-y-8">


        <!-- Question -->

        <div class="bg-white rounded-2xl border border-slate-200 p-8">


            <h2 class="text-lg font-semibold text-slate-900">
                Question
            </h2>


            <p class="mt-4 text-slate-600 leading-relaxed">

                I am interested in understanding how machine learning
                can be used to improve university systems such as
                recommendation systems, discussion forums, and learning
                platforms.

            </p>


            <div class="mt-6 flex justify-between items-center">


                <div class="flex gap-3">
                    
                    {{-- Mark discussion as answered (backend implementation) --}}
                    <button class="px-4 py-2 rounded-lg bg-blue-50 text-blue-600 text-sm">
                        ✓ Mark as Answered
                    </button>


                    {{-- Share discussion to supported social media platforms (backend implementation) --}}
                   <button class="px-4 py-2 rounded-lg bg-slate-100 text-slate-600 text-sm">

                      Share

                   </button>

                </div>



                <button class="flex items-center gap-2 text-sm text-slate-500 hover:text-blue-600">

                    <svg class="w-5 h-5"
                         fill="none"
                         stroke="currentColor"
                         stroke-width="2"
                         viewBox="0 0 24 24">

                        <path stroke-linecap="round"
                              stroke-linejoin="round"
                              d="M12 20h9"/>

                    </svg>
                {{-- Export discussion as PDF (backend implementation) --}}
                    Export PDF

                </button>


            </div>


        </div>


       {{-- Discussion replies from backend --}}

        <!-- Replies -->

        <div class="bg-white rounded-2xl border border-slate-200 p-8">


            <h2 class="text-xl font-semibold text-slate-900">
                Answers (12)
            </h2>



            <div class="mt-6 space-y-6">


                <!-- Reply -->

                <div class="border-b border-slate-100 pb-6">


                    <div class="flex items-center justify-between">


                        <div>

                            <h3 class="font-semibold text-slate-900">
                                Sarah Namukasa
                            </h3>

                            <p class="text-sm text-slate-400">
                                Computer Science Student
                            </p>

                        </div>


                        <span class="text-sm text-slate-400">
                            1 hour ago
                        </span>


                    </div>



                    <p class="mt-4 text-slate-600">

                        Machine learning can analyze student activities
                        and recommend relevant learning materials,
                        courses, and discussions.

                    </p>


                </div>

                



                <div>


                    <div class="flex items-center justify-between">


                        <div>

                            <h3 class="font-semibold text-slate-900">
                                John Okello
                            </h3>

                            <p class="text-sm text-slate-400">
                                Software Engineering Student
                            </p>

                        </div>


                        <span class="text-sm text-slate-400">
                            30 minutes ago
                        </span>


                    </div>



                    <p class="mt-4 text-slate-600">

                        Recommendation models can also reduce irrelevant
                        discussions by ranking topics according to user
                        interests.

                    </p>


                </div>


            </div>


        </div>


        <!-- Participation Summary -->

<div class="bg-white rounded-2xl border border-slate-200 p-8">

    <h2 class="text-lg font-semibold text-slate-900">

        Participation Summary

    </h2>

    {{-- Participation statistics from backend --}}

    <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mt-6">

        <div class="rounded-xl bg-slate-50 border border-slate-200 p-5">

            <p class="text-sm text-slate-500">

                Replies

            </p>

            <h3 class="mt-2 text-3xl font-bold text-blue-600">

                12

            </h3>

        </div>

        <div class="rounded-xl bg-slate-50 border border-slate-200 p-5">

            <p class="text-sm text-slate-500">

                Participants

            </p>

            <h3 class="mt-2 text-3xl font-bold text-green-600">

                8

            </h3>

        </div>

        <div class="rounded-xl bg-slate-50 border border-slate-200 p-5">

            <p class="text-sm text-slate-500">

                Views

            </p>

            <h3 class="mt-2 text-3xl font-bold text-orange-500">

                45

            </h3>

        </div>

    </div>

</div>


       {{-- Lecturer response submission (backend integration) --}}
        <!-- Reply Box -->

        <div class="bg-white rounded-2xl border border-slate-200 p-8">


            <h2 class="font-semibold text-lg text-slate-900">
                Provide Academic Response
            </h2>
        
            {{-- Lecturer response submission (backend integration) --}}
          <form method="POST" action="#">
            <textarea
                rows="5"
                placeholder="Provide guidance or answer the discussion......"
                class="mt-4 w-full rounded-xl border-slate-200 focus:ring-blue-500 focus:border-blue-500">
            </textarea>


            <div class="mt-4 flex justify-end">


                <button class="bg-blue-600 text-white px-6 py-3 rounded-xl hover:bg-blue-700 transition">

                   Publish Response

                </button>
          </form>

            </div>


        </div>



    </div>


</div>


@endsection