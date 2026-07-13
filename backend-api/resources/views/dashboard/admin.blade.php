@extends('layouts.dashboard')


@section('title', 'Admin Dashboard')


@section('heading')
Admin Dashboard
@endsection



@section('content')


<div class="bg-blue-600 rounded-3xl p-8 text-white mb-8">


<h1 class="text-3xl font-bold">
Welcome Administrator 👋
</h1>


<p class="mt-3 text-blue-100 max-w-2xl">

Monitor platform activity, manage users, view statistics,
and maintain a healthy discussion environment.

</p>


<button class="mt-6 bg-white text-blue-600 px-5 py-3 rounded-xl font-semibold">

Manage Users

</button>


</div>





<div class="grid md:grid-cols-4 gap-6">



<div class="bg-white rounded-2xl border p-6">

<div class="text-3xl mb-3">
👥
</div>

<p class="text-sm text-slate-500">
Total Users
</p>

<h3 class="text-3xl font-bold">
1240
</h3>

</div>




<div class="bg-white rounded-2xl border p-6">

<div class="text-3xl mb-3">
💬
</div>

<p class="text-sm text-slate-500">
Messages Today
</p>

<h3 class="text-3xl font-bold">
5420
</h3>

</div>




<div class="bg-white rounded-2xl border p-6">

<div class="text-3xl mb-3">
⚠️
</div>

<p class="text-sm text-slate-500">
Warnings Issued
</p>

<h3 class="text-3xl font-bold">
16
</h3>

</div>




<div class="bg-white rounded-2xl border p-6">

<div class="text-3xl mb-3">
🚫
</div>

<p class="text-sm text-slate-500">
Blacklisted Users
</p>

<h3 class="text-3xl font-bold">
4
</h3>

</div>



</div>





<div class="grid lg:grid-cols-3 gap-6 mt-8">



<!-- Statistics -->


<div class="lg:col-span-2 bg-white rounded-2xl border p-6">


<h2 class="text-xl font-bold mb-5">
Group Statistics
</h2>



<div class="space-y-5">



<div class="flex justify-between p-4 bg-slate-50 rounded-xl">

<span>
Software Engineering
</span>

<span class="font-semibold">
560 members
</span>

</div>




<div class="flex justify-between p-4 bg-slate-50 rounded-xl">

<span>
Computer Science
</span>

<span class="font-semibold">
430 members
</span>

</div>




<div class="flex justify-between p-4 bg-slate-50 rounded-xl">

<span>
Information Systems
</span>

<span class="font-semibold">
250 members
</span>

</div>



</div>


</div>





<!-- Admin Actions -->


<div class="bg-white rounded-2xl border p-6">


<h2 class="text-xl font-bold mb-5">
Administration
</h2>


<div class="space-y-3">


<button class="w-full text-left p-4 bg-blue-50 rounded-xl text-blue-700">
👥 Manage Members
</button>


<button class="w-full text-left p-4 bg-blue-50 rounded-xl text-blue-700">
📊 Platform Reports
</button>


<button class="w-full text-left p-4 bg-blue-50 rounded-xl text-blue-700">
⚙️ System Settings
</button>


</div>


</div>



</div>





<div class="mt-8 bg-white rounded-2xl border p-6">


<h2 class="text-xl font-bold mb-5">
Recent Admin Activity
</h2>


<ul class="space-y-4">


<li>
New member registration approved
</li>


<li>
Inactive user warning issued
</li>


<li>
Discussion category updated
</li>


</ul>


</div>



@endsection