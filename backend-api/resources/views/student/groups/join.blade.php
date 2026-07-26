@extends('layouts.student')

@section('title', 'Join Group - UniForum')

@section('page-title', 'Join Group')

@section('content')

<div class="min-h-screen bg-slate-50">

    <!-- Header -->

    <div class="bg-white border-b border-slate-200">

        <div class="max-w-4xl mx-auto px-6 py-8">

            <a href="/student/groups/browse"
               class="text-sm text-blue-600 hover:underline">

                ← Back to Browse Groups

            </a>

            <h1 class="mt-5 text-3xl font-bold text-slate-900">
                Join Discussion Group
            </h1>

            <p class="mt-2 text-slate-500">
                Please review the group information and rules before joining.
            </p>

        </div>

    </div>





    <div class="max-w-4xl mx-auto px-6 py-10">

        <div class="bg-white rounded-2xl border border-slate-200 p-8">

            <div>

                <span class="inline-block px-3 py-1 rounded-full bg-blue-100 text-blue-700 text-xs">

                    Academic Group

                </span>

                <h2 class="mt-4 text-2xl font-bold text-slate-900">

                    {{ $group->group_name }}

                </h2>

                <p class="mt-3 text-slate-600">

                    {{ $group->description }}

                </p>

            </div>





            <!-- Group Rules -->

            <div class="mt-10">

                <h3 class="text-xl font-semibold text-slate-900">

                    Group Rules

                </h3>

                <div class="mt-5 rounded-xl bg-slate-50 border border-slate-200 p-6">

                    <ul class="list-disc ml-6 space-y-3 text-slate-600">

                        <li>
                            Respect all members of the discussion group.
                        </li>

                        <li>
                            Share only academic and relevant content.
                        </li>

                        <li>
                            Avoid offensive, abusive or inappropriate language.
                        </li>

                        <li>
                            Do not spam or flood discussions.
                        </li>

                        <li>
                            Follow all instructions provided by group administrators.
                        </li>

                    </ul>

                </div>

            </div>





            <!-- Agreement -->

            <form method="POST" action="{{ route('student.groups.join.store',$group->group_id) }}">

                 @csrf

                <div class="mt-8">

                    <label class="flex items-start gap-3">

                        <input
                            type="checkbox"
                            name="rules_accepted"
                            value="1"
                            class="mt-1">

                        <span class="text-slate-700">

                            I have read, understood and agree to follow the
                            rules of this discussion group.

                        </span>

                    </label>
                    @error('rules_accepted')

                    <p class="text-red-500 text-sm mt-2">

                    {{ $message }}

                    </p>

                    @enderror

                </div>





                <!-- Buttons -->

                <div class="mt-10 flex justify-end gap-4">

                    <a href="/student/groups/browse"
                       class="px-6 py-3 rounded-xl bg-slate-100 text-slate-700 hover:bg-slate-200">

                        Cancel

                    </a>

                    <button
                        type="submit"
                        class="px-6 py-3 rounded-xl bg-blue-600 text-white hover:bg-blue-700">

                        Join Group

                    </button>

                </div>

            </form>

        </div>

    </div>

</div>

@endsection