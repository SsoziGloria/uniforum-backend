@extends('layouts.app')

@section('title', 'UniForum - Where university minds connect')

@section('content')

@include('components.navbar')


<main class="min-h-screen bg-background">


<!-- HERO SECTION -->

<section class="pt-32 pb-20 px-6 max-w-7xl mx-auto">

<div class="grid lg:grid-cols-2 gap-16 items-center">


<!-- LEFT CONTENT -->

<div class="space-y-8">


<div class="inline-flex items-center gap-2 bg-blue-50 border border-blue-100 text-primary px-4 py-2 rounded-full text-sm font-medium">

<svg class="w-4 h-4 fill-primary" viewBox="0 0 24 24">
<path d="M12 2l2.9 6.3L22 9.2l-5 4.8 1.2 7L12 17.8 5.8 21l1.2-7-5-4.8 7.1-.9L12 2z"/>
</svg>

Now with AI-powered learning insights

</div>



<div class="space-y-4">


<h1 class="text-5xl font-bold text-foreground leading-[1.15] tracking-tight">

Where university

<br>

<span class="text-primary">
minds connect
</span>

<br>

and grow together.

</h1>



<p class="text-lg text-muted-foreground leading-relaxed max-w-md">

A collaborative discussion platform built for students, lecturers, and administrators — powered by AI to keep every conversation meaningful.

</p>


</div>





<div class="flex items-center gap-4 flex-wrap">


<a
href="{{ route('register') }}"
class="flex items-center gap-2 bg-primary text-white font-semibold px-6 py-3.5 rounded-xl hover:bg-blue-700 transition-all shadow-lg"
>

Get started free


<svg class="w-4 h-4"
fill="none"
stroke="currentColor"
stroke-width="2"
viewBox="0 0 24 24">

<path d="M5 12h14M12 5l7 7-7 7"/>

</svg>


</a>



<a
href="{{ route('login') }}"
class="flex items-center gap-2 text-foreground font-medium px-6 py-3.5 rounded-xl border border-border hover:bg-muted"
>

Sign in to your account

</a>



</div>





<div class="flex items-center gap-4 flex-wrap">


@foreach([
'No credit card required',
'Free for students',
'GDPR compliant'
] as $item)


<span class="flex items-center gap-1.5 text-sm text-muted-foreground">


<svg class="w-4 h-4 text-primary"
fill="none"
stroke="currentColor"
stroke-width="3"
viewBox="0 0 24 24">

<path d="M5 13l4 4L19 7"/>

</svg>


{{ $item }}

</span>


@endforeach


</div>


</div>







<!-- RIGHT MOCKUP -->


<div class="flex justify-center lg:justify-end">


<div class="relative w-full max-w-[520px]">


<div class="bg-white rounded-2xl shadow-2xl border border-border overflow-hidden">


<div class="bg-primary px-5 py-4 flex items-center gap-3">


<div class="flex gap-1.5">

<span class="w-3 h-3 rounded-full bg-white/30"></span>
<span class="w-3 h-3 rounded-full bg-white/30"></span>
<span class="w-3 h-3 rounded-full bg-white/30"></span>

</div>


<span class="text-white/90 text-xs font-medium">

Advanced Algorithms — Week 7 Discussion

</span>


</div>






<div class="p-5 space-y-4">


<div class="flex gap-3">


<div class="w-9 h-9 rounded-full bg-blue-100 flex items-center justify-center text-xs font-bold text-primary">

SC

</div>



<div class="flex-1">

<div class="flex items-center gap-2">

<span class="text-xs font-semibold">
Dr. Sarah Chen
</span>


<span class="text-[10px] bg-blue-100 text-primary px-1.5 py-0.5 rounded-md">

Lecturer

</span>

</div>



<p class="text-xs text-muted-foreground mt-2 leading-relaxed">

Can anyone explain the time complexity of Dijkstra's algorithm and when we should prefer A* instead?

</p>


<div class="flex gap-3 mt-2 text-[10px] text-muted-foreground">


<span>
💬 8 replies
</span>


<span>
⭐ 12 upvotes
</span>


</div>


</div>


</div>





<div class="border-t border-border"></div>





<div class="flex gap-3">


<div class="w-9 h-9 rounded-full bg-emerald-100 flex items-center justify-center text-xs font-bold text-emerald-700">

JM

</div>


<div class="flex-1">


<div class="flex items-center gap-2">

<span class="text-xs font-semibold">
James Mitchell
</span>

<span class="text-[10px] bg-slate-100 px-1.5 py-0.5 rounded-md">

Student

</span>


</div>



<p class="text-xs text-muted-foreground mt-2">

Dijkstra's runs in O((V + E) log V) with a min-heap. A* is preferred when we have a good heuristic function...

</p>



<span class="text-[10px] text-emerald-600 font-medium flex items-center gap-1 mt-2">

✓ Best Answer

</span>


</div>

</div>





<div class="bg-blue-50 border border-blue-100 rounded-xl px-3 py-3 flex items-center gap-2">


<span class="text-primary text-sm">
🧠
</span>


<p class="text-[10px] text-primary font-medium">

AI suggests 3 related resources on graph algorithms for you

</p>


</div>





<div class="bg-muted rounded-xl px-3 py-2.5 flex items-center gap-2">


<div class="w-6 h-6 rounded-full bg-primary text-white text-[9px] flex items-center justify-center font-bold">

YO

</div>


<span class="text-xs text-muted-foreground flex-1">

Add your reply...

</span>


<button class="bg-primary text-white text-[10px] px-3 py-1 rounded-lg">

Post

</button>


</div>




</div>


</div>

</div>


</div>


</div>


</section>





<!-- STATS -->

<section class="border-y border-border bg-muted/40">

<div class="max-w-7xl mx-auto px-6 py-10 grid grid-cols-2 md:grid-cols-4 gap-8">


@foreach([
['50+','Universities'],
['12K+','Active Students'],
['98%','Satisfaction Rate'],
['2.4M','Discussions']
] as $stat)


<div class="text-center">

<p class="text-3xl font-bold text-primary">
{{ $stat[0] }}
</p>

<p class="text-sm text-muted-foreground mt-1">
{{ $stat[1] }}
</p>

</div>


@endforeach


</div>

</section>


 <!-- FEATURES -->

<section class="py-24 px-6 max-w-7xl mx-auto">


<div class="text-center mb-16 space-y-3">


<p class="text-sm font-semibold text-primary uppercase tracking-widest">
Platform Features
</p>


<h2 class="text-4xl font-bold text-foreground">

Everything your campus needs,
<br>
in one place.

</h2>


<p class="text-muted-foreground max-w-xl mx-auto">

Designed for the full university ecosystem — from first-year students to department administrators.

</p>


</div>





<div class="grid sm:grid-cols-2 lg:grid-cols-3 gap-6">


@php

$features = [

[
'icon'=>'💬',
'title'=>'Threaded Discussions',
'desc'=>'Course-based forums with upvoting, best-answer tagging, and instructor pinning.'
],

[
'icon'=>'🧠',
'title'=>'AI Recommendations',
'desc'=>'Personalized resource and discussion suggestions powered by learning patterns.'
],

[
'icon'=>'👥',
'title'=>'Study Groups',
'desc'=>'Create private or public groups with shared notes, polls, and file uploads.'
],

[
'icon'=>'📚',
'title'=>'Smart Quizzes',
'desc'=>'Auto-graded assessments with instant feedback and performance analytics.'
],

[
'icon'=>'🔔',
'title'=>'Smart Notifications',
'desc'=>'Contextual alerts for replies, deadlines, quiz results, and announcements.'
],

[
'icon'=>'📊',
'title'=>'Analytics Dashboard',
'desc'=>'Engagement metrics for lecturers and participation insights for administrators.'
]

];

@endphp





@foreach($features as $feature)


<div class="bg-white rounded-2xl border border-border p-6 hover:shadow-lg transition-all">


<div class="w-11 h-11 rounded-xl bg-blue-50 flex items-center justify-center mb-4 text-xl">

{{ $feature['icon'] }}

</div>



<h3 class="font-semibold text-foreground mb-2">

{{ $feature['title'] }}

</h3>



<p class="text-sm text-muted-foreground leading-relaxed">

{{ $feature['desc'] }}

</p>


</div>


@endforeach


</div>


</section>




<!-- TRUST SECTION -->


<section class="py-16 px-6 max-w-7xl mx-auto">


<div class="rounded-3xl overflow-hidden relative h-80 bg-blue-900">


<img
src="https://images.unsplash.com/photo-1523240795612-9a054b0db644?w=1400&h=500&fit=crop&auto=format"
class="w-full h-full object-cover opacity-40"
alt="Students collaborating"
>



<div class="absolute inset-0 flex flex-col items-center justify-center text-center px-6 space-y-4">


<p class="text-sm font-semibold text-blue-200 uppercase tracking-widest">

Trusted by institutions worldwide

</p>



<h2 class="text-3xl md:text-4xl font-bold text-white max-w-xl">

Built for the way students actually learn.

</h2>



<a
href="{{ route('register') }}"
class="flex items-center gap-2 bg-white text-primary font-semibold px-6 py-3 rounded-xl hover:bg-blue-50 shadow-lg"
>

Join your university

→

</a>


</div>


</div>


</section>



<!-- TESTIMONIALS -->


<section class="py-24 px-6 max-w-7xl mx-auto">


<div class="text-center mb-14 space-y-3">


<p class="text-sm font-semibold text-primary uppercase tracking-widest">

Testimonials

</p>


<h2 class="text-4xl font-bold text-foreground">

Loved by educators and students

</h2>


</div>





<div class="grid md:grid-cols-3 gap-6">


@php

$testimonials = [

[
'name'=>'Prof. Amelia Hartley',
'role'=>'Dept. of Computer Science, MIT',
'quote'=>'UniForum transformed how my students engage with course material. The AI recommendations alone have improved assignment quality by 40%.'
],

[
'name'=>'Marcus Thompson',
'role'=>'BSc. Software Engineering, Year 3',
'quote'=>'I used to email my professor every question. Now the forum has my answer before I even finish typing.'
],

[
'name'=>'Dr. Priya Nair',
'role'=>'Academic Director, University of Sydney',
'quote'=>'Students embraced it as their primary collaboration hub within the first week of rollout.'
]

];

@endphp





@foreach($testimonials as $testimonial)


<div class="bg-white rounded-2xl border border-border p-6 space-y-4">


<div class="text-yellow-400">

★★★★★

</div>



<p class="text-sm text-muted-foreground leading-relaxed">

“{{ $testimonial['quote'] }}”

</p>




<div class="border-t border-border pt-4">


<p class="text-sm font-semibold text-foreground">

{{ $testimonial['name'] }}

</p>


<p class="text-xs text-muted-foreground">

{{ $testimonial['role'] }}

</p>


</div>


</div>


@endforeach



</div>


</section>



<!-- CTA SECTION -->

<section class="py-16 px-6 max-w-7xl mx-auto">


<div class="bg-primary rounded-3xl px-8 py-16 text-center space-y-6">


<p class="text-blue-200 text-sm font-semibold uppercase tracking-widest">

Get Started Today

</p>



<h2 class="text-4xl font-bold text-white max-w-xl mx-auto">

Ready to transform how your university collaborates?

</h2>



<p class="text-blue-100 text-base max-w-md mx-auto">

Join thousands of students and educators already using UniForum to make every discussion count.

</p>




<div class="flex items-center justify-center gap-4 flex-wrap pt-2">


<a
href="{{ route('register') }}"
class="flex items-center gap-2 bg-white text-primary font-semibold px-7 py-3.5 rounded-xl hover:bg-blue-50 shadow-lg"
>

Create free account

→

</a>



<button
class="flex items-center gap-2 text-white font-medium px-7 py-3.5 rounded-xl border border-white/30 hover:bg-white/10"
>

Request a demo

</button>


</div>


</div>


</section>








<!-- FOOTER -->


<footer class="border-t border-border bg-muted/30 mt-8">


<div class="max-w-7xl mx-auto px-6 py-16">


<div class="grid md:grid-cols-5 gap-10">



<!-- BRAND -->


<div class="md:col-span-2 space-y-4">


<div class="flex items-center gap-2.5">


<div class="w-8 h-8 rounded-xl bg-primary flex items-center justify-center">


<svg class="w-4 h-4 text-white"
fill="none"
stroke="currentColor"
stroke-width="2"
viewBox="0 0 24 24">

<path d="M12 14l9-5-9-5-9 5 9 5z"/>

</svg>


</div>



<span class="font-bold text-foreground text-lg">

Uni<span class="text-primary">Forum</span>

</span>


</div>





<p class="text-sm text-muted-foreground leading-relaxed max-w-xs">


The collaborative academic platform built for modern universities. Connecting students, lecturers, and institutions worldwide.

</p>



<div class="flex gap-3">


@foreach(['🌐','🛡️','⚡'] as $icon)

<div class="w-8 h-8 rounded-lg bg-white border border-border flex items-center justify-center">

{{ $icon }}

</div>

@endforeach


</div>


</div>







<!-- FOOTER LINKS -->


@php

$footerLinks = [

'Platform'=>[
'Discussions',
'Quizzes',
'Groups',
'Analytics',
'Notifications'
],

'Institution'=>[
'Universities',
'Pricing',
'Enterprise',
'API Docs',
'Integrations'
],

'Company'=>[
'About',
'Blog',
'Careers',
'Privacy',
'Terms'
]

];

@endphp





@foreach($footerLinks as $heading=>$links)


<div class="space-y-4">


<p class="text-sm font-semibold text-foreground">

{{ $heading }}

</p>




<ul class="space-y-2.5">


@foreach($links as $link)


<li>

<button class="text-sm text-muted-foreground hover:text-foreground">

{{ $link }}

</button>


</li>


@endforeach


</ul>


</div>


@endforeach



</div>







<div class="border-t border-border mt-12 pt-8 flex flex-col sm:flex-row items-center justify-between gap-4">


<p class="text-xs text-muted-foreground">

© 2025 UniForum. All rights reserved.

</p>



<p class="text-xs text-muted-foreground">

Trusted by 50+ universities · GDPR compliant · SOC 2 Type II

</p>


</div>




</div>


</footer>


</main>


@endsection