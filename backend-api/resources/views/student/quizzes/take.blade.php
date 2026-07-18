@extends('layouts.student')

@section('title', 'Take Quiz - UniForum')

@section('page-title', 'Take Quiz')

@section('content')

<div>

    <!-- Quiz Header -->

    <div class="bg-white border-b border-slate-200 sticky top-0 z-20">

        <div class="max-w-7xl mx-auto px-6 py-5 flex flex-col lg:flex-row justify-between items-center gap-4">

            <div>

                <h1 class="text-2xl font-bold text-slate-900">
                    Software Engineering Midterm
                </h1>

                <p class="text-slate-500 mt-1">
                    Question 1 of 20
                </p>

            </div>

            <div class="flex items-center gap-4">

                <div class="bg-red-100 text-red-700 px-5 py-3 rounded-xl font-semibold">
                    ⏱ 00:58:32
                </div>

                <button class="bg-blue-600 hover:bg-blue-700 text-white px-6 py-3 rounded-xl">
                    Finish Quiz
                </button>

            </div>

        </div>

    </div>





    <div class="max-w-7xl mx-auto px-6 py-10">

        <div class="grid lg:grid-cols-4 gap-8">

            <!-- Questions -->

            <div class="lg:col-span-3">

                <div class="bg-white rounded-2xl border border-slate-200 p-8">

                    <span class="text-sm text-blue-600 font-medium">
                        Multiple Choice
                    </span>

                    <h2 class="mt-4 text-2xl font-bold text-slate-900">
                        Which software development methodology emphasizes short iterative development cycles?
                    </h2>

                    <div class="mt-8 space-y-4">

                        <label class="flex items-center gap-4 border rounded-xl p-4 hover:border-blue-500 cursor-pointer">

                            <input type="radio" name="q1">

                            <span>Waterfall Model</span>

                        </label>

                        <label class="flex items-center gap-4 border rounded-xl p-4 hover:border-blue-500 cursor-pointer">

                            <input type="radio" name="q1">

                            <span>Agile Methodology</span>

                        </label>

                        <label class="flex items-center gap-4 border rounded-xl p-4 hover:border-blue-500 cursor-pointer">

                            <input type="radio" name="q1">

                            <span>V-Model</span>

                        </label>

                        <label class="flex items-center gap-4 border rounded-xl p-4 hover:border-blue-500 cursor-pointer">

                            <input type="radio" name="q1">

                            <span>Spiral Model</span>

                        </label>

                    </div>

                    <div class="mt-10 flex justify-between">

                        <button class="px-6 py-3 rounded-xl bg-slate-100 text-slate-400 cursor-not-allowed">
                            Previous
                        </button>

                        <button class="px-6 py-3 rounded-xl bg-blue-600 hover:bg-blue-700 text-white">
                            Save & Next
                        </button>

                    </div>

                </div>

            </div>





            <!-- Sidebar -->

            <div class="space-y-6">

                <!-- Progress -->

                <div class="bg-white rounded-2xl border border-slate-200 p-6">

                    <h2 class="font-semibold text-lg">
                        Progress
                    </h2>

                    <div class="mt-5">

                        <div class="flex justify-between text-sm">

                            <span>Answered</span>

                            <span>1/ 20</span>

                        </div>

                        <div class="mt-3 h-3 bg-slate-200 rounded-full overflow-hidden">

                            <div class="bg-blue-600 h-full w-1/4 rounded-full"></div>

                        </div>

                    </div>

                </div>





                <!-- Question Navigator -->

                <div class="bg-white rounded-2xl border border-slate-200 p-6">

                    <h2 class="font-semibold text-lg mb-5">
                        Questions
                    </h2>

                    <div class="grid grid-cols-5 gap-3">

                        <button class="bg-blue-600 text-white rounded-lg h-10">
                            1
                        </button>

                        <button class="bg-green-100 text-green-700 rounded-lg h-10">
                            2
                        </button>

                        <button class="bg-green-100 text-green-700 rounded-lg h-10">
                            3
                        </button>

                        <button class="bg-slate-100 rounded-lg h-10">
                            4
                        </button>

                        <button class="bg-slate-100 rounded-lg h-10">
                            5
                        </button>

                        <button class="bg-slate-100 rounded-lg h-10">
                            6
                        </button>

                        <button class="bg-slate-100 rounded-lg h-10">
                            7
                        </button>

                        <button class="bg-slate-100 rounded-lg h-10">
                            8
                        </button>

                        <button class="bg-slate-100 rounded-lg h-10">
                            9
                        </button>

                        <button class="bg-slate-100 rounded-lg h-10">
                            10
                        </button>

                        <button class="bg-slate-100 rounded-lg h-10">
                            11
                        </button>

                        <button class="bg-slate-100 rounded-lg h-10">
                            12
                        </button>

                        <button class="bg-slate-100 rounded-lg h-10">
                            13
                        </button>

                        <button class="bg-slate-100 rounded-lg h-10">
                            14
                        </button>

                        <button class="bg-slate-100 rounded-lg h-10">
                            15
                        </button>

                        <button class="bg-slate-100 rounded-lg h-10">
                            16
                        </button>

                        <button class="bg-slate-100 rounded-lg h-10">
                            17
                        </button>

                        <button class="bg-slate-100 rounded-lg h-10">
                            18
                        </button>

                        <button class="bg-slate-100 rounded-lg h-10">
                            19
                        </button>

                        <button class="bg-slate-100 rounded-lg h-10">
                            20
                        </button>

                    </div>

                </div>





                <!-- Instructions -->

                <div class="bg-yellow-50 border border-yellow-200 rounded-2xl p-6">

                    <h2 class="font-semibold text-yellow-800">
                        Quiz Rules
                    </h2>

                    <ul class="mt-4 space-y-2 text-sm text-yellow-700 list-disc list-inside">

                        <li>Each question carries equal marks.</li>

                        <li>Your quiz will be submitted automatically when time expires.</li>

                        <li>Do not refresh the page during the assessment.</li>

                        <li>Review your answers before submitting.</li>

                    </ul>

                </div>

            </div>

        </div>

    </div>

</div>

@endsection