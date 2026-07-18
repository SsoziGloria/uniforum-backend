@extends('layouts.student')

@section('title', 'Group Chat - UniForum')

@section('page-title', 'Group Chat')

@section('content')

<div class="min-h-screen bg-slate-50">

    <div class="max-w-5xl mx-auto px-6 py-8 space-y-6">


        <!-- Chat Header -->

        <div class="bg-white rounded-2xl border border-slate-200 p-6">


            <a href="/student/groups/show"
               class="text-sm text-blue-600 hover:underline">

                ← Back to Group

            </a>



            <h1 class="mt-4 text-2xl font-bold text-slate-900">
                BSSE Year II Discussion Group Chat
            </h1>


            <p class="text-slate-500 mt-2">
                Communicate with members of this group.
            </p>


        </div>





        <!-- Messages Area -->

        <div class="bg-white rounded-2xl border border-slate-200 p-6 h-[500px] overflow-y-auto">



            <!-- Loading State -->

            {{-- Backend:
                 Display while messages are being retrieved.
            --}}

            <div class="hidden text-center py-20">


                <div class="text-slate-500">

                    Loading messages...

                </div>


            </div>






            <!-- Empty Chat State -->

            {{-- Backend:
                 Display when the group has no messages yet.
            --}}

            <div class="hidden text-center py-20">


                <div class="text-4xl mb-4">

                    💬

                </div>


                <h3 class="text-lg font-semibold text-slate-800">

                    No messages yet

                </h3>


                <p class="text-slate-500 mt-2">

                    Start the conversation with your group members.

                </p>


            </div>







            <!-- Error State -->

            {{-- Backend:
                 Display if messages cannot be loaded.
            --}}

            <div class="hidden text-center py-20">


                <div class="text-4xl mb-4">

                    ⚠

                </div>


                <h3 class="text-lg font-semibold text-slate-800">

                    Unable to load messages

                </h3>


                <p class="text-slate-500 mt-2">

                    Please try again.

                </p>


                <button
                    class="mt-4 px-5 py-2 rounded-xl bg-blue-600 text-white">

                    Retry

                </button>


            </div>







            <!-- No Access State -->

            {{-- Backend:
                 Display when user is removed,
                 blacklisted or no longer belongs to group.
            --}}

            <div class="hidden text-center py-20">


                <div class="text-4xl mb-4">

                    🔒

                </div>


                <h3 class="text-lg font-semibold text-slate-800">

                    You cannot access this chat

                </h3>


                <p class="text-slate-500 mt-2">

                    You are no longer a member of this group.

                </p>


            </div>







            <!-- Messages List -->

            {{-- Backend:
                 Retrieve messages belonging to this group.
            --}}



            <!-- Received Message -->


            <div class="mb-6">


                <div class="flex items-center gap-3">


                    <div class="w-10 h-10 rounded-full bg-blue-600 text-white flex items-center justify-center font-semibold">

                        S

                    </div>



                    <div>


                        <p class="font-medium text-slate-800">

                            Sarah Namukasa

                        </p>


                        <p class="text-xs text-slate-500">

                            10 minutes ago

                        </p>


                    </div>


                </div>




                <div class="mt-3 bg-slate-100 rounded-xl p-4 max-w-xl">

                    Has anyone completed the database assignment?

                </div>


            </div>







            <!-- Sent Message -->


            <div class="mb-6 flex justify-end">


                <div class="max-w-xl">


                    <div class="bg-blue-600 text-white rounded-xl p-4">

                        I have completed mine. I can share my approach.

                    </div>


                    <p class="text-xs text-slate-500 mt-1 text-right">

                        Sent to all members

                    </p>


                </div>


            </div>




        </div>








        <!-- Message Input -->


        <div class="bg-white rounded-2xl border border-slate-200 p-6">


            <form>



                <textarea
                    rows="3"
                    placeholder="Write a message..."
                    class="w-full rounded-xl border-slate-200 focus:ring-blue-500 focus:border-blue-500"></textarea>






                <!-- Communication Visibility -->


                <div class="mt-5">


                    <h3 class="font-semibold text-slate-800">

                        Message Visibility

                    </h3>



                    <p class="text-sm text-slate-500 mt-1">

                        Choose who can view this message.

                    </p>





                    <div class="mt-4 space-y-3">



                        <label class="flex items-center gap-3">


                            <input
                                type="radio"
                                name="visibility"
                                checked>


                            <span>

                                All group members

                            </span>


                        </label>







                        <label class="flex items-center gap-3">


                            <input
                                type="radio"
                                name="visibility">


                            <span>

                                Select members who should NOT see this message

                            </span>


                        </label>



                    </div>


                </div>









                <!-- Exclude Members -->


                <div class="mt-6 bg-slate-50 rounded-xl p-5">


                    <h3 class="font-semibold text-slate-800">

                        Exclude Members

                    </h3>



                    <p class="text-sm text-slate-500 mt-1">

                        Select members who should not receive this message.

                    </p>





                    {{-- Backend:
                         Retrieve group members dynamically.
                    --}}


                    <div class="mt-4 space-y-3">



                        <label class="flex items-center gap-3">

                            <input type="checkbox">


                            <span>
                                Sarah Namukasa
                                <span class="text-sm text-slate-500">
                                    (Student)
                                </span>
                            </span>


                        </label>





                        <label class="flex items-center gap-3">

                            <input type="checkbox">


                            <span>
                                Dr. John Doe
                                <span class="text-sm text-slate-500">
                                    (Lecturer)
                                </span>
                            </span>


                        </label>






                        <label class="flex items-center gap-3">

                            <input type="checkbox">


                            <span>
                                Peter Okello
                                <span class="text-sm text-slate-500">
                                    (Student)
                                </span>
                            </span>


                        </label>



                    </div>


                </div>








                <!-- Send Button -->


                <div class="mt-6 flex justify-end">


                    <button
                        type="submit"
                        class="px-6 py-3 bg-blue-600 text-white rounded-xl hover:bg-blue-700">


                        Send Message


                    </button>


                </div>



            </form>


        </div>




    </div>


</div>


@endsection