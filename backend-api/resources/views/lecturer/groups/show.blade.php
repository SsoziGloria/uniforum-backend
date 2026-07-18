@extends('layouts.lecturer')

@section('title', 'Group Details - UniForum')

@section('page-title', 'Group Details')

@section('content')

<div>

    <!-- Group Banner -->

    <div class="bg-gradient-to-r from-blue-600 to-blue-800">

        <div class="max-w-7xl mx-auto px-6 py-12 text-white">
            <a href="/lecturer/groups"
               class="text-sm text-white-600 hover:underline">

                ← Back to Groups

            </a>

            <h1 class="mt-4 text-4xl font-bold">
                Software Engineering Year 2 Discussion Group
            </h1>

            <p class="mt-3 text-blue-100 max-w-3xl">
                Discuss assignments, programming concepts, course announcements,
                and collaborate with classmates.
            </p>

            <div class="mt-8 flex flex-wrap gap-8">

                <div>
                  <p class="text-blue-200 text-sm">Members</p>
                  <p class="text-2xl font-bold">--</p>
                </div>


                <div>
                  <p class="text-blue-200 text-sm">Group Role</p>

             {{-- Backend:
                  Determine the authenticated user's role inside this group.
                  Possible values:
                  - Group Admin
                  - Lecturer
                 - Member
                --}}

                    <p class="text-2xl font-bold">
                       Member
                    </p>
                </div>


            </div>

        </div>

    </div>



    <div class="max-w-7xl mx-auto px-6 py-10">

        <div class="grid lg:grid-cols-3 gap-8">

            <!-- Main Content -->

            <div class="lg:col-span-2 space-y-8">

                <!-- Quick Actions -->

                <div class="bg-white rounded-2xl border border-slate-200 p-6">

                    <h2 class="text-lg font-semibold mb-5">
                        Quick Actions

                        {{-- Backend: Determine whether the authenticated user belongs to this group.

                        If the user IS a member:
                           - Show "New Discussion"

                            If the user IS NOT a member:
                            - Show "Join Group"

                           --}}
                    </h2>

                    <div class="grid sm:grid-cols-3 gap-4">
                         

                        <a href="/lecturer/discussions/create"
                           class="flex items-center justify-center rounded-xl bg-blue-600 text-white py-3 hover:bg-blue-700">

                             New Discussion

                        </a>
                        
                        <a href="/lecturer/groups/join"
                           class="flex items-center justify-center rounded-xl bg-blue-600 text-white py-3 hover:bg-blue-700">

                            Join Group

                        </a>
                        <a href="/lecturer/groups/chat"
                           class="flex items-center justify-center rounded-xl bg-blue-600 text-white py-3 hover:bg-blue-700">

                              Group Chat

                        </a>
                       
                        

                        
                    </div>

                </div>



                <!-- Recent Discussions -->

                <div class="bg-white rounded-2xl border border-slate-200">

                    <div class="px-6 py-5 border-b border-slate-200">

                        <h2 class="font-semibold text-lg">
                            Recent Discussions
                        </h2>

                    </div>

                    <div class="divide-y divide-slate-100">

                        <a href="/lecturer/discussions/show"
                           class="block p-6 hover:bg-slate-50 transition">

                            <h3 class="font-semibold text-slate-900">
                                Laravel Authentication Problem
                            </h3>

                            <p class="text-sm text-slate-500 mt-2">
                                18 replies • Last updated 20 minutes ago
                            </p>

                        </a>

                        <a href="/lecturer/discussions/show"
                           class="block p-6 hover:bg-slate-50 transition">

                            <h3 class="font-semibold text-slate-900">
                                Software Engineering Assignment Discussion
                            </h3>

                            <p class="text-sm text-slate-500 mt-2">
                                27 replies • Yesterday
                            </p>

                        </a>

                        <a href="/lecturer/discussions/show"
                           class="block p-6 hover:bg-slate-50 transition">

                            <h3 class="font-semibold text-slate-900">
                                AI Project Ideas
                            </h3>

                            <p class="text-sm text-slate-500 mt-2">
                                39 replies • 2 days ago
                            </p>

                        </a>

                    </div>

                </div>
                <!-- Active Members -->

                <div class="bg-white rounded-2xl border border-slate-200 p-6">

                    <h2 class="font-semibold text-lg mb-5">
                        Active Members
                    </h2>

                    <div class="space-y-4">

                        <div class="flex items-center gap-3">

                            <div class="w-10 h-10 rounded-full bg-blue-600 text-white flex items-center justify-center font-semibold">

                                     S

                            </div>

                            <div>

                                <p class="font-medium">
                                    Sarah Namukasa
                                </p>

                                <p class="text-xs text-slate-500">
                                    Online
                                </p>

                            </div>

                        </div>

                        <div class="flex items-center gap-3">

                            <div class="w-10 h-10 rounded-full bg-green-600 text-white flex items-center justify-center font-semibold">

                                   P

                            </div>
                            <div>

                                <p class="font-medium">
                                    Peter Okello
                                </p>

                                <p class="text-xs text-slate-500">
                                    Active 15 min ago
                                </p>

                            </div>

                        </div>

                    </div>

                </div>



            </div>



            <!-- Sidebar -->

            <div class="space-y-8">

                <!-- Group Information -->

                <div class="bg-white rounded-2xl border border-slate-200 p-6">

                    <h2 class="font-semibold text-lg mb-5">
                        Group Information
                    </h2>

                    <div class="space-y-4 text-sm">

                        <div class="flex justify-between">

                            <span class="text-slate-500">
                                Created
                            </span>

                            <span>
                                Jan 2026
                            </span>

                        </div>

                        <div class="flex justify-between">

                            <span class="text-slate-500">
                               Group Creator
                             </span>

                             {{-- Backend:
                                Display the user who created this group.
                                This user is the group administrator.
                             --}}

                            <span>
                             Gloria Ssozi
                            </span>

                        </div>


                        <div class="flex justify-between">

                            <span class="text-slate-500">
                               Lecturer
                            </span>

                            {{-- Backend:
                            Display assigned lecturer if this group has one.
                            A group may have no lecturer.
                            --}}

                             <span>
                             Dr. Karungi Shamim
                             </span>

                        </div>

                    </div>

                </div>

                
                <!-- Group Management -->

                @if(true)
                 {{-- Backend: Replace this condition with the actual group admin check.

                 Example:
                  @if($groupMember->group_role === 'admin')

                  Only users who are administrators of this group
                 should see these controls.--}}

                <div class="bg-white rounded-2xl border border-slate-200 p-6">


                    <h2 class="font-semibold text-lg mb-5">
                      Admin controls
                    </h2>



                     <div class="space-y-3">


                        <a href="/lecturer/groups/members"
                           class="block w-full text-left px-4 py-3 rounded-xl bg-blue-50 text-blue-700 hover:bg-blue-100">

                               Manage Members

                        </a>



                         <a href="/lecturer/groups/statistics"
                            class="block w-full text-left px-4 py-3 rounded-xl bg-blue-50 text-blue-700 hover:bg-blue-100">

                              View Statistics

                        </a>


                    </div>


                </div>

                @endif

                <!-- Participation Management -->

                <div class="bg-white rounded-2xl border border-slate-200 p-6">


                        <h2 class="font-semibold text-lg mb-5">
                           Participation
                        </h2>


                        <p class="text-sm text-slate-500 mb-4">
                            Configure participation criteria and monitor student performance.
                        </p>



                        <div class="space-y-3">


                            <a href="/lecturer/groups/participation/settings"
                               class="block w-full px-4 py-3 rounded-xl bg-blue-50 text-blue-700 hover:bg-blue-100">

                                Participation Settings

                            </a>



                            <a href="/lecturer/groups/participation"
                               class="block w-full px-4 py-3 rounded-xl bg-blue-50 text-blue-700 hover:bg-blue-100">

                                 View Participation Scores

                            </a>


                        </div>


                </div>


            </div>

        </div>

    </div>

</div>

@endsection