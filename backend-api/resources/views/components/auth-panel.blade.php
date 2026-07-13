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


<h2 class="text-3xl font-bold text-white leading-tight">

Where knowledge meets collaboration.

</h2>


<p class="text-blue-100">

Join thousands of students and educators engaging in meaningful academic discussions every day.

</p>




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





<div class="relative z-10 bg-white/10 backdrop-blur-sm border border-white/20 rounded-2xl p-5">


<div class="text-yellow-300">
★★★★★
</div>


<p class="text-blue-50 text-sm mt-3">

"UniForum has become the backbone of our academic communication."

</p>


<div class="mt-3">

<p class="text-white text-xs font-semibold">
Prof. Karen Weston
</p>

<p class="text-blue-200 text-xs">
University of Cambridge
</p>

</div>


</div>


</div>