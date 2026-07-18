@extends('layouts.lecturer')

@section('title', 'Lecturer Dashboard')

@section('page-title', 'Dashboard')

@section('content')


<!-- Welcome -->

<div class="bg-blue-600 rounded-3xl p-8 text-white mb-8">

    <h1 class="text-3xl font-bold">
        Welcome back, Lecturer 👋
    </h1>

    <p class="mt-3 text-blue-100 max-w-2xl">
        Manage discussions, create quizzes, monitor student participation,
        and guide academic conversations.
    </p>
    <a href="/lecturer/discussions"
       class="inline-block mt-6 bg-white text-blue-600 px-5 py-3 rounded-xl font-semibold hover:bg-blue-50 transition">

    Explore Discussions

    </a>


</div>





<!-- Statistics -->

<div class="grid md:grid-cols-4 gap-6">


<div class="bg-white rounded-2xl border p-6">

<div class="text-3xl mb-3">
💬
</div>

<p class="text-sm text-slate-500">
Active Discussions
</p>

<h3 class="text-3xl font-bold mt-2">
--
</h3>

{{-- Active discussions count from backend --}}

</div>




<div class="bg-white rounded-2xl border p-6">

<div class="text-3xl mb-3">
👨‍🎓
</div>

<p class="text-sm text-slate-500">
Students Engaged
</p>

<h3 class="text-3xl font-bold mt-2">
--
</h3>

</div>




<div class="bg-white rounded-2xl border p-6">

<div class="text-3xl mb-3">
📝
</div>

<p class="text-sm text-slate-500">
Active Quizzes
</p>

<h3 class="text-3xl font-bold mt-2">
--
</h3>

</div>




<div class="bg-white rounded-2xl border p-6">

<div class="text-3xl mb-3">
⭐
</div>

<p class="text-sm text-slate-500">
Average Participation
</p>

<h3 class="text-3xl font-bold mt-2">
--
</h3>

</div>


</div>





<div class="grid lg:grid-cols-3 gap-6 mt-8">


<!-- Quiz Management -->

<div class="lg:col-span-2 bg-white rounded-2xl border p-6">


<div class="flex justify-between items-center mb-5">

<h2 class="text-xl font-bold">
Quiz Management
</h2>


<a href="/lecturer/quizzes/create"
   class="bg-blue-600 text-white px-4 py-2 rounded-xl">

    Create Quiz

</a>

</div>




<div class="space-y-4">


<a href="/lecturer/quizzes/show"
   class="block p-4 bg-slate-50 rounded-xl flex justify-between hover:bg-blue-50 transition">


<div>

<h3 class="font-semibold">
Software Engineering Quiz
</h3>

<p class="text-sm text-slate-500">
Available: 15 July 2026
</p>

</div>


<span class="text-green-600">
Active
</span>


</a>




<a href="/lecturer/quizzes/show"
   class="block p-4 bg-slate-50 rounded-xl flex justify-between hover:bg-blue-50 transition">


<div>

<h3 class="font-semibold">
Database Systems Assessment
</h3>

<p class="text-sm text-slate-500">
Starts tomorrow
</p>

</div>


<span class="text-yellow-600">
Scheduled
</span>


</a>



</div>


</div>





<!-- Participation Analytics -->


<div class="mt-8 bg-white rounded-2xl border p-6">


<h2 class="text-xl font-bold mb-5">
Student Participation Overview
</h2>


<div class="space-y-5">


<div>

<div class="flex justify-between mb-2">

<span>
Computer Science Group
</span>

<span>
--
</span>

</div>


<div class="h-3 bg-slate-200 rounded-full">

<div class="h-3 bg-blue-600 rounded-full w-[85%]">
</div>

</div>

</div>





<div>

<div class="flex justify-between mb-2">

<span>
Software Engineering Group
</span>

<span>
--
</span>

</div>


<div class="h-3 bg-slate-200 rounded-full">

<div class="h-3 bg-blue-600 rounded-full w-[72%]">
</div>

</div>

</div>


</div>


</div>




@endsection