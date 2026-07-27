@extends('layouts.lecturer')

@section('title', 'Profile Settings - UniForum')

@section('page-title', 'Profile Settings')

@section('content')

<div class="max-w-6xl mx-auto">

    <!-- Header -->

    <div class="bg-white rounded-2xl border border-slate-200 p-8 mb-8">

        <h1 class="text-3xl font-bold text-slate-900">
            Profile Settings
        </h1>

        <p class="mt-2 text-slate-500">
            Manage your personal information, account security, active sessions and account preferences.
        </p>

    </div>

    @if (Laravel\Fortify\Features::canUpdateProfileInformation())

        <div class="bg-white rounded-2xl border border-slate-200 p-8 mb-8">

            @livewire('profile.update-profile-information-form')

        </div>

    @endif


    @if (Laravel\Fortify\Features::enabled(Laravel\Fortify\Features::updatePasswords()))

        <div class="bg-white rounded-2xl border border-slate-200 p-8 mb-8">

            @livewire('profile.update-password-form')

        </div>

    @endif


    @if (Laravel\Fortify\Features::canManageTwoFactorAuthentication())

        <div class="bg-white rounded-2xl border border-slate-200 p-8 mb-8">

            @livewire('profile.two-factor-authentication-form')

        </div>

    @endif


    <div class="bg-white rounded-2xl border border-slate-200 p-8 mb-8">

        @livewire('profile.logout-other-browser-sessions-form')

    </div>


    @if (Laravel\Jetstream\Jetstream::hasAccountDeletionFeatures())

        <div class="bg-white rounded-2xl border border-red-200 p-8">

            @livewire('profile.delete-user-form')

        </div>

    @endif

</div>

@endsection