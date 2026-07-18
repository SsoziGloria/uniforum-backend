@extends('layouts.student')

@section('title', 'Notifications - UniForum')

@section('page-title', 'Notifications')

@section('content')

<div>

    <!-- Header -->

    <div class="bg-white border-b border-slate-200">

        <div class="max-w-6xl mx-auto px-6 py-8">

            <h1 class="text-3xl font-bold text-slate-900">
                Notifications
            </h1>

            <p class="mt-2 text-slate-500">
                Stay updated with discussions, quizzes, announcements and system alerts.
            </p>

        </div>

    </div>




        <div class="space-y-5">

            <!-- Discussion -->

            <div class="bg-white rounded-2xl border border-slate-200 p-6 hover:shadow-md transition">

                <div class="flex justify-between items-start">

                    <div class="flex gap-4">

                        <div class="w-12 h-12 rounded-full bg-blue-100 flex items-center justify-center">

                            💬

                        </div>

                        <div>

                            <h2 class="font-semibold text-slate-900">
                                New reply to your discussion
                            </h2>

                            <p class="text-slate-600 mt-2">
                                Sarah Namukasa replied to your discussion:
                                <strong>"Laravel Authentication Problem"</strong>
                            </p>

                            <p class="text-xs text-slate-400 mt-3">
                                10 minutes ago
                            </p>

                        </div>

                    </div>

                    <span class="w-3 h-3 rounded-full bg-blue-600"></span>

                </div>

            </div>





            <!-- Quiz -->

            <div class="bg-white rounded-2xl border border-slate-200 p-6 hover:shadow-md transition">

                <div class="flex gap-4">

                    <div class="w-12 h-12 rounded-full bg-green-100 flex items-center justify-center">

                        📝

                    </div>

                    <div>

                        <h2 class="font-semibold text-slate-900">
                            New Quiz Available
                        </h2>

                        <p class="text-slate-600 mt-2">
                            Software Engineering Quiz 2 will begin tomorrow at 10:00 AM.
                        </p>

                        <p class="text-xs text-slate-400 mt-3">
                            1 hour ago
                        </p>

                    </div>

                </div>

            </div>





            <!-- Announcement -->

            <div class="bg-white rounded-2xl border border-slate-200 p-6 hover:shadow-md transition">

                <div class="flex gap-4">

                    <div class="w-12 h-12 rounded-full bg-purple-100 flex items-center justify-center">

                        📢

                    </div>

                    <div>

                        <h2 class="font-semibold text-slate-900">
                            Lecturer Announcement
                        </h2>

                        <p class="text-slate-600 mt-2">
                            Assignment deadline has been extended until Friday.
                        </p>

                        <p class="text-xs text-slate-400 mt-3">
                            Yesterday
                        </p>

                    </div>

                </div>

            </div>





            <!-- Warning -->

            <div class="bg-red-50 rounded-2xl border border-red-200 p-6">

                <div class="flex gap-4">

                    <div class="w-12 h-12 rounded-full bg-red-100 flex items-center justify-center">

                        ⚠️

                    </div>

                    <div>

                        <h2 class="font-semibold text-red-700">
                            Inactivity Warning
                        </h2>

                        <p class="text-red-600 mt-2">
                            You have not participated in any discussions for the last 14 days.
                            Please contribute to avoid temporary suspension.
                        </p>

                        <p class="text-xs text-red-500 mt-3">
                            System Notification
                        </p>

                    </div>

                </div>

            </div>

        </div>

    </div>

</div>

@endsection