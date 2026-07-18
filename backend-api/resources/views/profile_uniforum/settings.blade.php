@extends(auth()->user()->role === 'lecturer' ? 'layouts.lecturer' : 'layouts.student')

@section('title', 'Profile Settings - UniForum')

@section('page-title', 'Profile Settings')

@section('content')

<div class="space-y-6">

    <!-- Header -->

    <div class="bg-white rounded-2xl border border-slate-200 p-6">

        <h2 class="text-2xl font-bold text-slate-800">
            Profile Settings
        </h2>

        <p class="text-slate-500 mt-2">
            View and update your account information.
        </p>

    </div>


    <!-- Personal Information -->

    <div class="bg-white rounded-2xl border border-slate-200 p-6">

        <h3 class="text-lg font-semibold text-slate-800">
            Personal Information
        </h3>

        <p class="text-sm text-slate-500 mt-1">
            Update your personal details.
        </p>

        <form class="mt-6 space-y-5">

            {{-- Backend:
                 Populate authenticated user's information.
                 Save updated profile information.
            --}}

            <div>

                <label class="block text-sm font-medium text-slate-700 mb-2">
                    Full Name
                </label>

                <input
                    type="text"
                    value="Gloria Ssozi"
                    class="w-full rounded-xl border-slate-200 focus:ring-blue-500 focus:border-blue-500">

            </div>


            <div>

                <label class="block text-sm font-medium text-slate-700 mb-2">
                    Email Address
                </label>

                <input
                    type="email"
                    value="gloria@example.com"
                    class="w-full rounded-xl border-slate-200 focus:ring-blue-500 focus:border-blue-500">

            </div>


            <div>

                <label class="block text-sm font-medium text-slate-700 mb-2">
                    Role
                </label>

                <input
                    type="text"
                    value="Student"
                    disabled
                    class="w-full rounded-xl bg-slate-100 border-slate-200">

                {{-- Backend:
                     Display Student or Lecturer.
                     Read-only.
                --}}

            </div>

            <div class="flex justify-end">

                <button
                    type="submit"
                    class="px-6 py-3 rounded-xl bg-blue-600 text-white hover:bg-blue-700">

                    Save Changes

                </button>

            </div>

        </form>

    </div>



    <!-- Change Password -->

    <div class="bg-white rounded-2xl border border-slate-200 p-6">

        <h3 class="text-lg font-semibold text-slate-800">
            Change Password
        </h3>

        <p class="text-sm text-slate-500 mt-1">
            Update your account password.
        </p>

        <form class="mt-6 space-y-5">

            {{-- Backend:
                 Validate current password and update password.
            --}}

            <div>

                <label class="block text-sm font-medium text-slate-700 mb-2">
                    Current Password
                </label>

                <input
                    type="password"
                    class="w-full rounded-xl border-slate-200 focus:ring-blue-500 focus:border-blue-500">

            </div>


            <div>

                <label class="block text-sm font-medium text-slate-700 mb-2">
                    New Password
                </label>

                <input
                    type="password"
                    class="w-full rounded-xl border-slate-200 focus:ring-blue-500 focus:border-blue-500">

            </div>


            <div>

                <label class="block text-sm font-medium text-slate-700 mb-2">
                    Confirm Password
                </label>

                <input
                    type="password"
                    class="w-full rounded-xl border-slate-200 focus:ring-blue-500 focus:border-blue-500">

            </div>

            <div class="flex justify-end">

                <button
                    type="submit"
                    class="px-6 py-3 rounded-xl bg-blue-600 text-white hover:bg-blue-700">

                    Update Password

                </button>

            </div>

        </form>

    </div>



    <!-- Account Information -->

    <div class="bg-white rounded-2xl border border-slate-200 p-6">

        <h3 class="text-lg font-semibold text-slate-800">
            Account Information
        </h3>

        <div class="mt-6 grid md:grid-cols-2 gap-6">

            <div>

                <p class="text-sm text-slate-500">
                    Account Type
                </p>

                <p class="font-medium text-slate-800 mt-1">
                    Student
                </p>

            </div>


            <div>

                <p class="text-sm text-slate-500">
                    Email Verification
                </p>

                <p class="font-medium text-green-600 mt-1">
                    Verified
                </p>

            </div>


            <div>

                <p class="text-sm text-slate-500">
                    Member Since
                </p>

                <p class="font-medium text-slate-800 mt-1">
                    January 2026
                </p>

            </div>


            <div>

                <p class="text-sm text-slate-500">
                    Account Status
                </p>

                <p class="font-medium text-green-600 mt-1">
                    Active
                </p>

            </div>

        </div>

        {{-- Backend:
             Populate account metadata from authenticated user.
        --}}

    </div>

</div>

@endsection