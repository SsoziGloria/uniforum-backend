@extends('layouts.lecturer')

@section('title', 'Join Group - UniForum')

@section('page-title', 'Join Group')

@section('content')

<div class="min-h-screen bg-slate-50">

    <!-- Header -->
    <div class="bg-white border-b border-slate-200">
        <div class="max-w-4xl mx-auto px-6 py-8">
            <a href="{{ route('lecturer.groups.browse') }}"
               class="text-sm text-blue-600 hover:underline flex items-center gap-2">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7"/>
                </svg>
                Back to Browse Groups
            </a>

            <h1 class="mt-5 text-3xl font-bold text-slate-900">
                Join Academic Discussion Group
            </h1>

            <p class="mt-2 text-slate-500">
                Please review the group details and lecturer guidelines before joining.
            </p>
        </div>
    </div>

    <div class="max-w-4xl mx-auto px-6 py-10">
        <div class="bg-white rounded-2xl border border-slate-200 p-8">

            <!-- Dynamic Group Summary -->
            <div>
                <span class="inline-block px-3 py-1 rounded-full bg-blue-100 text-blue-700 text-xs font-semibold">
                    Academic Group
                </span>

                <h2 class="mt-4 text-2xl font-bold text-slate-900">
                    {{ $group->group_name }}
                </h2>

                <p class="mt-3 text-slate-600">
                    {{ $group->description ?? 'No description provided for this group.' }}
                </p>
            </div>

            <!-- Lecturer Guidelines & Responsibilities -->
            <div class="mt-10">
                <h3 class="text-xl font-semibold text-slate-900">
                    Lecturer & Moderation Guidelines
                </h3>

                <div class="mt-5 rounded-xl bg-slate-50 border border-slate-200 p-6">
                    <ul class="list-disc ml-6 space-y-3 text-slate-600">
                        <li>
                            Provide constructive academic guidance and supervise student discussions.
                        </li>
                        <li>
                            Ensure group activities align with university course objectives and policy.
                        </li>
                        <li>
                            Moderate discussions and address inappropriate or off-topic content promptly.
                        </li>
                        <li>
                            Maintain professional boundaries and foster an inclusive learning environment.
                        </li>
                    </ul>
                </div>
            </div>

            <!-- Agreement Form -->
            <form method="POST" action="{{ route('lecturer.groups.join.store', $group->group_id ?? $group->id) }}">
                @csrf

                <div class="mt-8">
                    <label class="flex items-start gap-3 cursor-pointer">
                        <input
                            type="checkbox"
                            name="rules_accepted"
                            value="1"
                            class="mt-1 rounded border-slate-300 text-blue-600 focus:ring-blue-500">

                        <span class="text-slate-700 text-sm">
                            I have read and agree to adhere to the academic guidelines and moderation responsibilities for this group.
                        </span>
                    </label>

                    @error('rules_accepted')
                        <p class="text-red-500 text-sm mt-2">
                            {{ $message }}
                        </p>
                    @enderror
                </div>

                <!-- Action Buttons -->
                <div class="mt-10 flex justify-end gap-4">
                    <a href="{{ route('lecturer.groups.browse') }}"
                       class="px-6 py-3 rounded-xl bg-slate-100 text-slate-700 hover:bg-slate-200 font-medium transition">
                        Cancel
                    </a>

                    <button
                        type="submit"
                        class="px-6 py-3 rounded-xl bg-blue-600 text-white hover:bg-blue-700 font-medium transition">
                        Join as Lecturer
                    </button>
                </div>
            </form>

        </div>
    </div>

</div>

@endsection