<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>
        @yield('title', 'UniForum Lecturer')
    </title>


    @vite(['resources/css/app.css', 'resources/js/app.js'])

</head>


<body class="bg-slate-50">


<div class="min-h-screen flex">


    <!-- Sidebar -->

    
    <aside class="w-64 bg-white border-r border-slate-200 flex flex-col">
       
        
        <!-- Logo -->

        <div class="px-6 py-6 border-b border-slate-200">

            <h1 class="text-2xl font-bold text-blue-600">
                UniForum
            </h1>

            <p class="text-sm text-slate-500">
                Lecturer Portal
            </p>

        </div>



        <!-- Navigation -->

        <nav class="flex-1 px-4 py-6 space-y-2">
            
            <p class="px-4 text-xs font-semibold text-slate-400 uppercase tracking-wide mb-3">
                  Main Menu
            </p>



            <a href="/lecturer/dashboard"
               class="flex items-center gap-3 px-4 py-3 rounded-xl transition-all duration-200 hover:translate-x-1 {{ request()->is('lecturer/dashboard') 
                ? 'bg-blue-600 text-white shadow-md' 
                : 'text-slate-700 hover:bg-blue-50' }}">

                🏠
                <span>Dashboard</span>

            </a>


            <a href="/lecturer/discussions"
               class="flex items-center gap-3 px-4 py-3 rounded-xl transition-all duration-200 hover:translate-x-1 {{ request()->is('lecturer/discussions*') 
                ? 'bg-blue-600 text-white shadow-md' 
                : 'text-slate-700 hover:bg-blue-50' }}">
            
                💬
                <span>Discussions</span>

            </a>



            <a href="/lecturer/groups"
               class="flex items-center gap-3 px-4 py-3 rounded-xl transition-all duration-200 hover:translate-x-1 {{ request()->is('lecturer/groups*') 
               ? 'bg-blue-600 text-white shadow-md' 
               : 'text-slate-700 hover:bg-blue-50' }}">
                👥
                <span>Groups</span>

            </a>



            <a href="/lecturer/quizzes"
               class="flex items-center gap-3 px-4 py-3 rounded-xl transition-all duration-200 hover:translate-x-1 {{ request()->is('lecturer/quizzes*') 
               ? 'bg-blue-600 text-white shadow-md' 
               : 'text-slate-700 hover:bg-blue-50' }}">
                📝
                <span>Quizzes</span>

            </a>
            
            <a href="/lecturer/students"
               class="flex items-center gap-3 px-4 py-3 rounded-xl transition-all duration-200 hover:translate-x-1 {{ request()->is('lecturer/students*') 
               ? 'bg-blue-600 text-white shadow-md' 
               : 'text-slate-700 hover:bg-blue-50' }}">
                👨‍🎓 
                <span>Students</span>

            </a>


            <a href="/lecturer/notifications"
               class="flex items-center gap-3 px-4 py-3 rounded-xl transition-all duration-200 hover:translate-x-1 {{ request()->is('lecturer/notifications*') 
               ? 'bg-blue-600 text-white shadow-md' 
               : 'text-slate-700 hover:bg-blue-50' }}">
                🔔
                <span>Notifications</span>

            </a>

            <div class="pt-6">

                <p class="px-4 text-xs font-semibold text-slate-400 uppercase tracking-wide mb-3">
                    Account
                </p>


                <a href="#"
                   class="flex items-center gap-3 px-4 py-3 rounded-xl text-slate-700 hover:bg-blue-50 transition">

                    ⚙️
                    <span>
                        Profile Settings
                    </span>

                </a>

            </div>


        </nav>



        <!-- User -->

        <div class="border-t border-slate-200 p-5">


            <div class="flex items-center gap-3">


                <div class="h-10 w-10 rounded-full bg-blue-600 text-white flex items-center justify-center font-bold">

                    {{ strtoupper(substr(auth()->user()->name ?? 'U', 0, 1)) }}

                </div>


                <div>

                    <p class="font-semibold text-slate-800">

                        {{ auth()->user()->name ?? 'Lecturer' }}

                    </p>


                    <p class="text-sm text-slate-500">

                        Lecturer

                    </p>

                </div>


            </div>


        </div>


    </aside>
    
    




    <!-- Main content -->

    <div class="flex-1 flex flex-col">


        <!-- Top Navigation -->

        <header class="h-20 bg-white border-b border-slate-200 flex items-center justify-between px-6">

            
            <div class="flex items-center gap-4">




               <!-- Page Title -->

                <div>

                    <h2 class="text-xl font-bold text-slate-800">

                          @yield('page-title', 'Dashboard')

                    </h2>


                    <p class="text-sm text-slate-500">

                        Welcome back, {{ auth()->user()->name ?? 'Lecturer' }}

                    </p>

                </div>


            </div>




            <!-- Right Side -->

             <div class="flex items-center gap-6">



              <!-- Search -->

                <div class="hidden lg:flex items-center bg-slate-100 rounded-xl px-4 py-2 w-72">


                   <span class="text-slate-400 mr-2">

                       🔍

                   </span>


                   <input 
                       type="text"
                       placeholder="Search..."
                       class="bg-transparent outline-none text-sm w-full">

                </div>






            <!-- Notification -->

                <a href="/lecturer/notifications" class="relative text-xl">


                    🔔


                  <span class="absolute -top-1 -right-2 bg-red-500 text-white text-xs w-5 h-5 rounded-full flex items-center justify-center">

                    !

                  </span>


                </a>







            <!-- Profile -->

                <div class="flex items-center gap-3">


                  <div class="w-10 h-10 rounded-full bg-blue-600 text-white flex items-center justify-center font-bold">


                    {{ strtoupper(substr(auth()->user()->name ?? 'U',0,1)) }}


                  </div>



                        <div class="hidden md:block">


                            <p class="font-semibold text-sm text-slate-800">

                               {{ auth()->user()->name ?? 'Lecturer' }}

                            </p>


                            <p class="text-xs text-slate-500">

                               Lecturer

                            </p>


                        </div>


                 </div>




            </div>


        </header>





        <!-- Page -->

        <main class="flex-1 p-6 overflow-x-auto">

            @yield('content')


        </main>


    </div>


</div>


</body>

</html>