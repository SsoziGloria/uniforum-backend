@extends('layouts.student')

@section('title', 'Student Dashboard')

@section('page-title', 'Dashboard')

@section('content')


<!-- Welcome Banner -->

<div class="bg-blue-600 rounded-3xl p-8 text-white mb-8">

    <h1 class="text-3xl font-bold">
    Welcome back, {{ auth()->user()->name ?? 'Student' }} 👋
    </h1>


    <p class="mt-3 text-blue-100 max-w-xl">
        Stay connected with your university discussions,
        discover recommended topics and improve your participation score.
    </p>


    <a href="/student/discussions"
   class="inline-block mt-6 bg-white text-blue-600 px-5 py-3 rounded-xl font-semibold hover:bg-blue-50 transition">

    Explore Discussions

    </a>

</div>




<!-- Statistics Cards -->

<div class="grid md:grid-cols-4 gap-6">


    <div class="bg-white rounded-2xl border border-slate-200 p-6">

        <div class="text-3xl mb-3">
            💬
        </div>

        <p class="text-sm text-slate-500">
            Questions Asked
        </p>

        <h3 class="text-3xl font-bold mt-2">
            24
        </h3>

    </div>




    <div class="bg-white rounded-2xl border border-slate-200 p-6">

        <div class="text-3xl mb-3">
            ⭐
        </div>

        <p class="text-sm text-slate-500">
            Participation Score
        </p>

        <h3 class="text-3xl font-bold mt-2">
            85%
        </h3>

    </div>




    <div class="bg-white rounded-2xl border border-slate-200 p-6">

        <div class="text-3xl mb-3">
            📚
        </div>

        <p class="text-sm text-slate-500">
            Topics Following
        </p>

        <h3 class="text-3xl font-bold mt-2">
            12
        </h3>

    </div>




    <div class="bg-white rounded-2xl border border-slate-200 p-6">

        <div class="text-3xl mb-3">
            📝
        </div>

        <p class="text-sm text-slate-500">
            Pending Quizzes
        </p>

        <h3 class="text-3xl font-bold mt-2">
            3
        </h3>

    </div>


</div>





<!-- Main Content -->

<div class="grid lg:grid-cols-3 gap-6 mt-8">



<!-- Recommended Topics -->

<div class="lg:col-span-2 bg-white rounded-2xl border p-6">


<div class="flex justify-between items-center mb-5">


<h2 class="text-xl font-bold">
Recommended Topics
</h2>


<span class="text-sm text-blue-600">
AI Powered
</span>


</div>



<div class="space-y-4">


<div class="p-4 bg-slate-50 rounded-xl flex justify-between">

<div>

<h3 class="font-semibold">
Laravel Authentication
</h3>

<p class="text-sm text-slate-500">
23 students discussing
</p>

</div>


<button class="text-blue-600">
View
</button>


</div>




<div class="p-4 bg-slate-50 rounded-xl flex justify-between">

<div>

<h3 class="font-semibold">
Machine Learning Basics
</h3>

<p class="text-sm text-slate-500">
18 students discussing
</p>

</div>


<button class="text-blue-600">
View
</button>


</div>




<div class="p-4 bg-slate-50 rounded-xl flex justify-between">

<div>

<h3 class="font-semibold">
Software Design Patterns
</h3>

<p class="text-sm text-slate-500">
31 students discussing
</p>

</div>


<button class="text-blue-600">
View
</button>


</div>


</div>


</div>





<!-- Notifications -->

<div class="bg-white rounded-2xl border p-6">


<h2 class="text-xl font-bold mb-5">
Notifications
</h2>


<div class="space-y-4">


<div class="border-l-4 border-blue-600 pl-4">

<p class="font-medium">
New quiz available
</p>

<p class="text-sm text-slate-500">
Database Systems Quiz
</p>

</div>



<div class="border-l-4 border-green-500 pl-4">

<p class="font-medium">
Your question was answered
</p>

<p class="text-sm text-slate-500">
Operating Systems
</p>

</div>



<div class="border-l-4 border-yellow-500 pl-4">

<p class="font-medium">
Participation reminder
</p>

<p class="text-sm text-slate-500">
Engage more in discussions
</p>

</div>



</div>


</div>


</div>






<!-- Recent Activity -->


<div class="mt-8 bg-white rounded-2xl border p-6">


<h2 class="text-xl font-bold mb-5">
Recent Activity
</h2>


<div class="space-y-4">


<div class="flex justify-between">

<span>
Answered "How does Laravel middleware work?"
</span>

<span class="text-sm text-slate-500">
2 hrs ago
</span>

</div>



<div class="flex justify-between">

<span>
Joined Database Optimization topic
</span>

<span class="text-sm text-slate-500">
Yesterday
</span>

</div>



<div class="flex justify-between">

<span>
Completed Software Engineering quiz
</span>

<span class="text-sm text-slate-500">
3 days ago
</span>

</div>


</div>


</div>



@endsection