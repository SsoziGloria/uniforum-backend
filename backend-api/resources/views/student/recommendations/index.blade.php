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
                <p class="text-sm text-slate-500">Recommended Today</p>
                <h2 class="mt-3 text-3xl font-bold text-blue-600">{{ $recommendedToday }}</h2>
            </div>

            <div class="bg-white rounded-2xl border border-slate-200 p-6">
                <p class="text-sm text-slate-500">Topics Classified</p>
                <h2 class="mt-3 text-3xl font-bold text-green-600">{{ $topicsClassified }}</h2>
            </div>

            <div class="bg-white rounded-2xl border border-slate-200 p-6">
                <p class="text-sm text-slate-500">Discussions Read</p>
                <h2 class="mt-3 text-3xl font-bold text-purple-600">{{ $discussionsRead }}</h2>
            </div>

            <div class="bg-white rounded-2xl border border-slate-200 p-6">
                <p class="text-sm text-slate-500">Topics Followed</p>
                <h2 class="mt-3 text-3xl font-bold text-orange-500">{{ $topicsFollowed }}</h2>
            </div>

        </div>


        <!-- AI Explanation -->

        <div class="bg-blue-50 border border-blue-200 rounded-2xl p-6 mb-10">

            <h2 class="text-xl font-semibold text-blue-900">
                Why am I seeing these recommendations?
            </h2>

            <p class="mt-3 text-blue-700 leading-relaxed">
                @if($fallback)
                    We don't have enough activity from you yet to personalize these — so here are the
                    latest discussions across UniForum instead. Once you read and engage with a few
                    discussions, recommendations here will start matching your interests.
                @else
                    Our recommendation engine analyzes your previous discussions,
                    topics you interact with, and engagement patterns to suggest
                    relevant discussions.
                @endif
            </p>

        </div>


        <!-- Recommended Topics -->

        <h2 class="text-2xl font-bold text-slate-900 mb-6">
            Recommended Discussions
        </h2>

        <div class="grid lg:grid-cols-2 gap-6">

            @forelse($recommendations as $topic)

                <div class="bg-white rounded-2xl border border-slate-200 p-6 hover:shadow-lg transition">

                    <div class="flex justify-between items-start">

                        <span class="px-3 py-1 rounded-full bg-blue-100 text-blue-700 text-xs">
                            {{ $fallback ? 'Latest' : 'Recommended for you' }}
                        </span>

                        <span class="text-sm text-slate-400">
                            {{ $topic->ml_category ?? 'General' }}
                        </span>

                    </div>

                    <h3 class="mt-5 text-xl font-semibold">
                        {{ $topic->title }}
                    </h3>

                    <p class="mt-3 text-slate-600">
                        @if($fallback)
                            One of the latest discussions on UniForum.
                        @else
                            Recommended because it's similar to discussions you've engaged with
                            ({{ round($topic->similarity * 100) }}% match).
                        @endif
                    </p>

                    <a href="{{ route('student.discussions.show', [$topic->group_id, $topic->topic_id]) }}"
                       class="mt-6 inline-block bg-blue-600 text-white px-5 py-2 rounded-lg hover:bg-blue-700">
                        View Discussion
                    </a>

                </div>

            @empty

                <div class="lg:col-span-2 bg-white rounded-2xl border border-slate-200 p-6 text-center text-slate-500">
                    No discussions to recommend yet — join a group and start participating.
                </div>

            @endforelse

        </div>


        <!-- Recommended Categories -->

        @if($suggestedCategories->isNotEmpty())
        <div class="mt-12">

            <h2 class="text-2xl font-bold mb-6">
                Topics You May Like
            </h2>

            <div class="flex flex-wrap gap-4">
                @php
                    $chipColors = [
                        'bg-blue-100 text-blue-700',
                        'bg-green-100 text-green-700',
                        'bg-purple-100 text-purple-700',
                        'bg-orange-100 text-orange-700',
                        'bg-pink-100 text-pink-700',
                    ];
                @endphp

                @foreach($suggestedCategories as $index => $category)
                    <span class="px-5 py-3 rounded-full {{ $chipColors[$index % count($chipColors)] }}">
                        {{ $category }}
                    </span>
                @endforeach
            </div>

        </div>
        @endif

    </div>

</div>

@endsection
