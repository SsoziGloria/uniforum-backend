<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>
        @yield('title', 'UniForum Dashboard')
    </title>

    @vite(['resources/css/app.css', 'resources/js/app.js'])
</head>

<body class="bg-slate-50 text-slate-900">

<div class="min-h-screen flex">

    <!-- Sidebar -->
    <aside class="w-64 bg-white border-r border-slate-200 hidden md:flex flex-col">

        <div class="p-6 border-b border-slate-200">

            <a href="/" class="flex items-center gap-3">

                <div class="w-10 h-10 rounded-xl bg-blue-600 flex items-center justify-center text-white font-bold">
                    U
                </div>

                <div>
                    <h1 class="font-bold text-lg">
                        UniForum
                    </h1>

                    <p class="text-xs text-slate-500">
                        Smart Discussion Platform
                    </p>
                </div>

            </a>

        </div>


        <!-- Navigation -->

        <nav class="flex-1 p-4 space-y-2">


            <a href="#"
               class="flex items-center gap-3 px-4 py-3 rounded-xl bg-blue-50 text-blue-700 font-medium">

                <span>🏠</span>
                Dashboard

            </a>


            <a href="#"
               class="flex items-center gap-3 px-4 py-3 rounded-xl hover:bg-slate-100">

                <span>💬</span>
                Discussions

            </a>


            <a href="#"
               class="flex items-center gap-3 px-4 py-3 rounded-xl hover:bg-slate-100">

                <span>📚</span>
                Topics

            </a>


            <a href="#"
               class="flex items-center gap-3 px-4 py-3 rounded-xl hover:bg-slate-100">

                <span>🔔</span>
                Notifications

            </a>


        </nav>


        <!-- User section -->

        <div class="p-4 border-t border-slate-200">

            <div class="flex items-center gap-3">

                <div class="w-10 h-10 rounded-full bg-indigo-100 flex items-center justify-center">
                    👤
                </div>


                <div>

                    <p class="font-medium">
                        User Name
                    </p>

                    <p class="text-xs text-slate-500">
                        Student
                    </p>

                </div>

            </div>


        </div>


    </aside>




    <!-- Main Area -->

    <main class="flex-1">


        <!-- Top bar -->

        <header class="bg-white border-b border-slate-200 px-8 py-5 flex justify-between items-center">


            <div>

                <h2 class="text-2xl font-bold">
                    @yield('heading')
                </h2>

                <p class="text-sm text-slate-500">
                    Welcome back to UniForum
                </p>

            </div>


            <button class="px-4 py-2 rounded-xl bg-blue-600 text-white">
                Profile
            </button>


        </header>



        <!-- Page content -->

        <section class="p-8">

            @yield('content')

        </section>


    </main>


</div>


</body>
</html>