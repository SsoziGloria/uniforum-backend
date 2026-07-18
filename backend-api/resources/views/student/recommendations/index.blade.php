@extends('layouts.student')

@section('title', 'AI Recommendations - UniForum')

@section('page-title', 'AI Recommendations')

@section('content')

<div>

    <!-- Header -->

    <div class="bg-gradient-to-r from-blue-600 to-indigo-700 text-white">

        <div class="max-w-7xl mx-auto px-6 py-10">

            <span class="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-white/20 text-sm">
                🤖 AI Powered
            </span>

            <h1 class="mt-5 text-4xl font-bold">
                Personalized Recommendations
            </h1>

            <p class="mt-3 text-blue-100 max-w-2xl">
                Based on your previous discussions, quiz performance and topics you frequently engage with,
                UniForum recommends these discussions for you.
            </p>

        </div>

    </div>





    <div class="max-w-7xl mx-auto px-6 py-10">

        <!-- Recommendation Summary -->

        <div class="grid md:grid-cols-4 gap-6 mb-10">

            <div class="bg-white rounded-2xl border border-slate-200 p-6">

                <p class="text-sm text-slate-500">
                    Recommended Today
                </p>

                <h2 class="mt-3 text-3xl font-bold text-blue-600">
                    18
                </h2>

            </div>

            <div class="bg-white rounded-2xl border border-slate-200 p-6">

                <p class="text-sm text-slate-500">
                    Topics Classified
                </p>

                <h2 class="mt-3 text-3xl font-bold text-green-600">
                    7
                </h2>

            </div>

            <div class="bg-white rounded-2xl border border-slate-200 p-6">

                <p class="text-sm text-slate-500">
                    Discussions Read
                </p>

                <h2 class="mt-3 text-3xl font-bold text-purple-600">
                    46
                </h2>

            </div>

            <div class="bg-white rounded-2xl border border-slate-200 p-6">

                <p class="text-sm text-slate-500">
                    Topics Followed
                </p>

                <h2 class="mt-3 text-3xl font-bold text-orange-500">
                    12
                </h2>

            </div>

        </div>





        <!-- AI Explanation -->

        <div class="bg-blue-50 border border-blue-200 rounded-2xl p-6 mb-10">

            <h2 class="text-xl font-semibold text-blue-900">
                Why am I seeing these recommendations?
            </h2>

            <p class="mt-3 text-blue-700 leading-relaxed">

                Our recommendation engine analyzes your previous discussions,
                topics you interact with, and engagement patterns to suggest
                relevant discussions.  
            </p>

        </div>





        <!-- Recommended Topics -->

        <h2 class="text-2xl font-bold text-slate-900 mb-6">
            Recommended Discussions
        </h2>

        <div class="grid lg:grid-cols-2 gap-6">

            <!-- Card -->

            <div class="bg-white rounded-2xl border border-slate-200 p-6 hover:shadow-lg transition">

                <div class="flex justify-between items-start">

                    <span class="px-3 py-1 rounded-full bg-blue-100 text-blue-700 text-xs">

                        Recommended for you

                    </span>

                    <span class="text-sm text-slate-400">

                        Software Engineering

                    </span>

                </div>

                <h3 class="mt-5 text-xl font-semibold">

                    Agile vs Waterfall: Which methodology should startups use?

                </h3>

                <p class="mt-3 text-slate-600">

                    Recommended because you've recently interacted with
                    Software Engineering discussions.

                </p>

                <button class="mt-6 bg-blue-600 text-white px-5 py-2 rounded-lg hover:bg-blue-700">

                    View Discussion

                </button>

            </div>





            <div class="bg-white rounded-2xl border border-slate-200 p-6 hover:shadow-lg transition">

                <div class="flex justify-between">

                    <span class="px-3 py-1 rounded-full bg-green-100 text-green-700 text-xs">

                        Trending

                    </span>

                    <span class="text-sm text-slate-400">

                        Artificial Intelligence

                    </span>

                </div>

                <h3 class="mt-5 text-xl font-semibold">

                    Introduction to Recommendation Systems

                </h3>

                <p class="mt-3 text-slate-600">

                    Students with similar interests also viewed this discussion.

                </p>

                <button class="mt-6 bg-blue-600 text-white px-5 py-2 rounded-lg hover:bg-blue-700">

                    View Discussion

                </button>

            </div>





            <div class="bg-white rounded-2xl border border-slate-200 p-6 hover:shadow-lg transition">

                <div class="flex justify-between">

                    <span class="px-3 py-1 rounded-full bg-purple-100 text-purple-700 text-xs">

                        New

                    </span>

                    <span class="text-sm text-slate-400">

                        Programming

                    </span>

                </div>

                <h3 class="mt-5 text-xl font-semibold">

                    Laravel Authentication Best Practices

                </h3>

                <p class="mt-3 text-slate-600">

                    Matches your recent activity in Laravel discussions.

                </p>

                <button class="mt-6 bg-blue-600 text-white px-5 py-2 rounded-lg hover:bg-blue-700">

                    View Discussion

                </button>

            </div>





            <div class="bg-white rounded-2xl border border-slate-200 p-6 hover:shadow-lg transition">

                <div class="flex justify-between">

                    <span class="px-3 py-1 rounded-full bg-orange-100 text-orange-700 text-xs">

                        Popular

                    </span>

                    <span class="text-sm text-slate-400">

                        Database Systems

                    </span>

                </div>

                <h3 class="mt-5 text-xl font-semibold">

                    SQL Optimization Techniques

                </h3>

                <p class="mt-3 text-slate-600">

                    Highly rated by students with similar engagement history.

                </p>

                <button class="mt-6 bg-blue-600 text-white px-5 py-2 rounded-lg hover:bg-blue-700">

                    View Discussion

                </button>

            </div>

        </div>





        <!-- Recommended Categories -->

        <div class="mt-12">

            <h2 class="text-2xl font-bold mb-6">

                Topics You May Like

            </h2>

            <div class="flex flex-wrap gap-4">

                <span class="px-5 py-3 rounded-full bg-blue-100 text-blue-700">
                    Laravel
                </span>

                <span class="px-5 py-3 rounded-full bg-green-100 text-green-700">
                    Machine Learning
                </span>

                <span class="px-5 py-3 rounded-full bg-purple-100 text-purple-700">
                    Software Engineering
                </span>

                <span class="px-5 py-3 rounded-full bg-orange-100 text-orange-700">
                    Data Structures
                </span>

                <span class="px-5 py-3 rounded-full bg-pink-100 text-pink-700">
                    Artificial Intelligence
                </span>

            </div>

        </div>

    </div>

</div>

@endsection