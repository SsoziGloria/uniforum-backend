@extends('layouts.app')

@section('title', 'Register - UniForum')

@section('content')

<div 
    x-data="{
        showPassword:false,
        showConfirm:false,
        role:'',
        password:'',
        confirm:'',
        strength(){
            let score=0;
            if(this.password.length >= 8) score++;
            if(/[A-Z]/.test(this.password)) score++;
            if(/[0-9]/.test(this.password)) score++;
            if(/[^A-Za-z0-9]/.test(this.password)) score++;
            return score;
        }
    }"
    class="min-h-screen flex"
>




<!-- LEFT PANEL -->

<div class="hidden lg:flex lg:w-[52%] relative bg-primary flex-col justify-between p-12 overflow-hidden">


<div class="absolute inset-0 overflow-hidden">

<div class="absolute -top-32 -right-32 w-96 h-96 rounded-full bg-blue-400/20"></div>

<div class="absolute top-1/2 -left-24 w-64 h-64 rounded-full bg-blue-300/10"></div>

<div class="absolute -bottom-20 -right-10 w-80 h-80 rounded-full bg-blue-800/30"></div>

</div>




<div class="relative z-10 flex items-center gap-2.5">


<div class="w-8 h-8 rounded-xl bg-white/20 flex items-center justify-center">

🎓

</div>


<span class="font-bold text-white text-lg">

UniForum

</span>


</div>







<div class="relative z-10 space-y-8">


<div class="space-y-4">


<h2 class="text-3xl font-bold text-white">

Where knowledge meets collaboration.

</h2>


<p class="text-blue-100">

Join students and lecturers engaging in meaningful academic discussions every day.

</p>


</div>






<div class="space-y-3">


@foreach([
'Threaded course discussions',
'AI-powered learning recommendations',
'Collaborative study groups',
'Performance analytics for lecturers'
] as $item)


<div class="flex items-center gap-3">


<div class="w-7 h-7 rounded-lg bg-white/15 flex items-center justify-center text-white">

✓

</div>


<span class="text-blue-50 text-sm">

{{ $item }}

</span>


</div>


@endforeach


</div>


</div>







<div class="relative z-10 bg-white/10 backdrop-blur-sm border border-white/20 rounded-2xl p-5">


<div class="text-yellow-300">

★★★★★

</div>



<p class="text-blue-50 text-sm mt-3">

"UniForum has become the backbone of our academic communication."

</p>


</div>



</div>








<!-- RIGHT FORM -->

<div class="flex-1 flex justify-center items-center px-6 py-12 bg-background">


<div class="w-full max-w-md space-y-7">





<div>

<h1 class="text-2xl font-bold">

Create your account

</h1>


<p class="text-sm text-muted-foreground">

Join your university's academic community today.

</p>


</div>








<!-- ROLE -->

<div class="space-y-2">


<label class="text-sm font-medium">

I am a...

</label>



<div class="grid grid-cols-2 gap-3">


<button
type="button"
@click="role='student'"
:class="role==='student' ? 'border-primary bg-blue-50':'border-border'"
class="p-4 border-2 rounded-xl text-left transition"
>


<div class="text-xl">

🎓

</div>


<p class="font-semibold text-sm">

Student

</p>


<p class="text-xs text-muted-foreground">

Access discussions & quizzes

</p>


</button>






<button
type="button"
@click="role='lecturer'"
:class="role==='lecturer' ? 'border-primary bg-blue-50':'border-border'"
class="p-4 border-2 rounded-xl text-left transition"
>


<div class="text-xl">

📚

</div>


<p class="font-semibold text-sm">

Lecturer

</p>


<p class="text-xs text-muted-foreground">

Manage courses & announcements

</p>


</button>


</div>


</div>







<form method="POST" action="{{ route('register') }}" class="space-y-4">

@csrf



<input
type="hidden"
name="role"
x-model="role"
/>






<div>

<label class="text-sm font-medium">

Full Name

</label>


<input
name="name"
type="text"
required
placeholder="Dr. Sarah Chen"
class="w-full mt-1 px-4 py-3 rounded-xl border border-border bg-input-background"
/>


</div>







<div>


<label class="text-sm font-medium">

Email

</label>


<input
name="email"
type="email"
required
placeholder="you@email.com"
class="w-full mt-1 px-4 py-3 rounded-xl border border-border bg-input-background"
/>


</div>







<div>


<label class="text-sm font-medium">

Password

</label>


<div class="relative">


<input
:name="'password'"
: type="showPassword ? 'text':'password'"
type="password"
x-model="password"
required
placeholder="Min. 8 characters"
class="w-full mt-1 px-4 pr-12 py-3 rounded-xl border border-border bg-input-background"
/>



<button
type="button"
@click="showPassword=!showPassword"
class="absolute right-4 top-4"
>

👁

</button>


</div>






<div x-show="password" class="mt-2">


<div class="flex gap-1">


<template x-for="i in 4">

<div
class="h-1 flex-1 rounded-full"
:class="i <= strength() ? 'bg-primary':'bg-gray-200'"
></div>


</template>


</div>



<p class="text-xs text-muted-foreground mt-1">

Password strength:

<span x-text="['','Weak','Fair','Good','Strong'][strength()]"></span>

</p>


</div>


</div>







<div>


<label class="text-sm font-medium">

Confirm Password

</label>


<div class="relative">


<input
name="password_confirmation"
:type="showConfirm ? 'text':'password'"
x-model="confirm"
required
placeholder="Re-enter password"
class="w-full mt-1 px-4 pr-12 py-3 rounded-xl border border-border bg-input-background"
/>



<button
type="button"
@click="showConfirm=!showConfirm"
class="absolute right-4 top-4"
>

👁

</button>


</div>



<p
x-show="confirm && confirm!==password"
class="text-xs text-red-500 mt-1"
>

Passwords do not match.

</p>


</div>








<p class="text-xs text-muted-foreground">

By creating an account, you agree to our

<span class="text-primary">

Terms of Service

</span>

and

<span class="text-primary">

Privacy Policy.

</span>

</p>







<button
type="submit"
:disabled="!role || (confirm && confirm!==password)"
class="w-full bg-primary text-white font-semibold py-3.5 rounded-xl hover:bg-blue-700 disabled:opacity-50"
>

Create account →

</button>



</form>







<p class="text-center text-sm text-muted-foreground">


Already have an account?


<a href="{{ route('login') }}" class="text-primary font-semibold">

Sign in

</a>


</p>




</div>


</div>



</div>


@endsection