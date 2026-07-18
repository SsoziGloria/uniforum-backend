@extends('layouts.student')

@section('title', 'Manage Members - UniForum')

@section('page-title', 'Manage Members')

@section('content')

<div class="space-y-6">


    <!-- Header -->

    <div class="bg-white rounded-2xl border border-slate-200 p-6">


        <a href="/student/groups/show"
           class="text-sm text-blue-600 hover:underline">

            ← Back to Group

        </a>



        <h2 class="mt-4 text-2xl font-bold text-slate-800">

            BSSE Year II Discussion Group Members

        </h2>



        <p class="text-slate-500 mt-2">

            Manage group members, administrator privileges, warnings and blacklisted users.


        </p>


    </div>







    <!-- Members Table -->


    <div class="bg-white rounded-2xl border border-slate-200 overflow-hidden">


        <div class="p-6 border-b border-slate-200">


            <h3 class="text-lg font-semibold text-slate-800">

                Group Members

            </h3>


        </div>




        {{-- Backend:
             Retrieve members belonging to this group.
             
             Each member should include:
             - User name
             - Global role (student/lecturer)
             - Group role (admin/member)
        --}}



        <table class="w-full">


            <thead class="bg-slate-100">

                <tr>

                    <th class="text-left px-6 py-4">
                          Name
                    </th>

                    <th class="text-left px-6 py-4">
                         User Role
                    </th>

                    <th class="text-left px-6 py-4">
                         Group Role
                    </th>

                    <th class="text-left px-6 py-4">
                        Status
                    </th>

                    <th class="text-left px-6 py-4">
                          Actions
                    </th>

                </tr>



            </thead>





            <tbody>


                {{-- Backend: Retrieve members belonging to this group.

                Each member should include:
                     - User name
                    - Global role (Student/Lecturer)
                    - Group role (Group Admin/Member)
                    - Member status (Active, Warning 1, Warning 2, Blacklisted)

                 Only Group Admins should be allowed to:
                - Promote a member to Group Admin
                - Remove Group Admin privileges
                - Issue warnings
                - Blacklist members
                - Reinstate blacklisted members
                --}}



                <!-- Example Member -->

                <tr class="border-t">

                    <td class="px-6 py-5">
                        Sarah Namukasa
                    </td>

                    <td class="px-6 py-5">
                         Student
                    </td>

                    <td class="px-6 py-5">

                        <span class="px-3 py-1 rounded-full bg-slate-100 text-slate-700 text-sm">

                          Member

                        </span>

                    </td>

                    <td class="px-6 py-5">

                        <span class="px-3 py-1 rounded-full bg-green-100 text-green-700 text-sm">

                            Active

                        </span>

                    </td>

                    <td class="px-6 py-5 space-x-3">

                        <button class="text-blue-600 hover:underline">
                            Make Group Admin
                        </button>

                        <button class="text-yellow-600 hover:underline">
                              Issue Warning
                        </button>

                        <button class="text-red-600 hover:underline">
                               Blacklist
                        </button>

                    </td>

                </tr>
                


                <!-- Example Lecturer -->


                <tr class="border-t">

                    <td class="px-6 py-5">
                          Dr. John Doe
                    </td>

                    <td class="px-6 py-5">
                       Lecturer
                    </td>

                    <td class="px-6 py-5">

                        <span class="px-3 py-1 rounded-full bg-blue-100 text-blue-700 text-sm">

                               Group Admin

                        </span>

                    </td>

                    <td class="px-6 py-5">

                        <span class="px-3 py-1 rounded-full bg-green-100 text-green-700 text-sm">

                             Active

                        </span>

                    </td>

                    <td class="px-6 py-5 space-x-3">

                        <button class="text-red-600 hover:underline">

                            Remove Admin

                        </button>

                    </td>

                </tr>




                <tr class="border-t">

                    <td class="px-6 py-5">
                           Peter Okello
                    </td>

                    <td class="px-6 py-5">
                          Student
                    </td>

                    <td class="px-6 py-5">

                        <span class="px-3 py-1 rounded-full bg-slate-100 text-slate-700 text-sm">

                               Member

                        </span>

                    </td>

                    <td class="px-6 py-5">

                        <span class="px-3 py-1 rounded-full bg-yellow-100 text-yellow-700 text-sm">

                              Warning 1

                        </span>

                    </td>

                    <td class="px-6 py-5 space-x-3">

                        <button class="text-yellow-600 hover:underline">

                              Issue Warning

                        </button>

                        <button class="text-red-600 hover:underline">

                                Blacklist

                        </button>

                    </td>

                </tr>

                <tr class="border-t">

                    <td class="px-6 py-5">
                            Nankya Scarlett
                    </td>

                    <td class="px-6 py-5">
                          Student
                    </td>

                    <td class="px-6 py-5">

                        <span class="px-3 py-1 rounded-full bg-slate-100 text-slate-700 text-sm">

                               Member

                        </span>

                    </td>

                    <td class="px-6 py-5">

                        <span class="px-3 py-1 rounded-full bg-yellow-100 text-yellow-700 text-sm">

                             Blacklisted

                        </span>

                    </td>

                    <td class="px-6 py-5 space-x-3">

                        <button class="text-yellow-600 hover:underline">

                              Reinstate

                        </button>

                    </td>

                </tr>





            </tbody>



        </table>


    </div>





</div>


@endsection