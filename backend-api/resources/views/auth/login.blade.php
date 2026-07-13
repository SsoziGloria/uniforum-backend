@extends('layouts.app')

@section('title', 'Login - UniForum')

@section('content')

<div class="min-h-screen flex">



<!-- LEFT PANEL -->

<div class="hidden lg:flex lg:w-[52%] relative bg-primary flex-col justify-between p-12 overflow-hidden">


<div class="absolute inset-0 overflow-hidden">

<div class="absolute -top-32 -right-32 w-96 h-96 rounded-full bg-blue-400/20"></div>

<div class="absolute top-1/2 -left-24 w-64 h-64 rounded-full bg-blue-300/10"></div>

<div class="absolute -bottom-20 -right-10 w-80 h-80 rounded-full bg-blue-800/30"></div>

</div>





<div class="relative z-10 flex items-center gap-2.5">


<div class="w-8 h-8 rounded-xl bg-white/20 flex items-center justify-center">


<svg class="w-4 h-4 text-white"
fill="none"
stroke="currentColor"
stroke-width="2"
viewBox="0 0 24 24">

<path d="M12 14l9-5-9-5-9 5 9 5z"/>

</svg>


</div>


<span class="font-bold text-white text-lg">

UniForum

</span>


</div>







<div class="relative z-10 space-y-8">


<div class="space-y-4">


<h2 class="text-3xl font-bold text-white leading-tight">

Where knowledge meets collaboration.

</h2>


<p class="text-blue-100 leading-relaxed">

Join 12,400+ students and educators engaging in meaningful academic discussions every day.

</p>


</div>






<div class="space-y-3">


@foreach([
'Threaded course discussions',
'AI-powered learning recommendations',
'Collaborative study groups',
'Performance analytics for lecturers'
] as $feature)


<div class="flex items-center gap-3">


<div class="w-7 h-7 rounded-lg bg-white/15 flex items-center justify-center text-white">

✓

</div>


<span class="text-blue-50 text-sm">

{{ $feature }}

</span>


</div>


@endforeach


</div>


</div>








<div class="relative z-10 bg-white/10 backdrop-blur-sm border border-white/20 rounded-2xl p-5 space-y-3">


<div class="text-yellow-300">

★★★★★

</div>



<p class="text-blue-50 text-sm">

"UniForum has become the backbone of our academic communication. Students are more engaged than ever."

</p>




<div class="flex items-center gap-3">


<div class="w-8 h-8 rounded-full bg-white/20 flex items-center justify-center text-white text-xs font-bold">

KW

</div>


<div>


<p class="text-xs font-semibold text-white">

Prof. Karen Weston

</p>


<p class="text-xs text-blue-200">

University of Cambridge

</p>


</div>


</div>



</div>



</div>








<!-- RIGHT SIDE FORM -->


<div class="flex-1 flex flex-col items-center justify-center px-6 py-12 bg-background">



<!-- Mobile Logo -->

<div class="lg:hidden mb-8 flex items-center gap-2.5">


<div class="w-8 h-8 rounded-xl bg-primary flex items-center justify-center">


<span class="text-white">

🎓

</span>


</div>


<span class="font-bold text-lg">

Uni<span class="text-primary">Forum</span>

</span>


</div>






<div class="w-full max-w-md space-y-8">


<div>


<h1 class="text-2xl font-bold text-foreground">

Welcome back

</h1>


<p class="text-muted-foreground text-sm mt-1">

Sign in to your UniForum account to continue.

</p>


</div>







<form method="POST" action="{{ route('login') }}" class="space-y-5">

@csrf



<!-- EMAIL -->

<div class="space-y-1.5">


<label class="text-sm font-medium">

Email address

</label>


<input
type="email"
name="email"
value="{{ old('email') }}"
placeholder="you@university.edu"
required
class="w-full px-4 py-3 rounded-xl border border-border bg-input-background focus:ring-2 focus:ring-primary/30 focus:border-primary outline-none"
/>


</div>






<!-- PASSWORD -->


<div class="space-y-1.5">


<div class="flex justify-between">


<label class="text-sm font-medium">

Password

</label>


<a href="#" class="text-xs text-primary font-medium">

Forgot password?

</a>


</div>



<div x-data="{show:false}" class="relative">


<input
: type="show ? 'text':'password'"
type="password"
name="password"
placeholder="••••••••"
required
class="w-full px-4 pr-12 py-3 rounded-xl border border-border bg-input-background focus:ring-2 focus:ring-primary/30 outline-none"
/>


<button
type="button"
@click="show=!show"
class="absolute right-4 top-3 text-muted-foreground"
>

👁

</button>


</div>


</div>







<!-- REMEMBER -->


<label class="flex items-center gap-2 text-sm text-muted-foreground">


<input
type="checkbox"
name="remember"
class="rounded border-border text-primary"
/>


Remember me for 30 days


</label>







<button
type="submit"
class="w-full bg-primary text-white font-semibold py-3.5 rounded-xl hover:bg-blue-700 transition"
>

Sign in →

</button>



</form>








<div class="text-center text-sm text-muted-foreground">


Don't have an account?

<a
href="{{ route('register') }}"
class="text-primary font-semibold"
>

Create one free

</a>


</div>



</div>


</div>


</div>


@endsection