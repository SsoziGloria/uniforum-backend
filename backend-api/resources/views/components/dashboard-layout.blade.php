<div class="min-h-screen bg-slate-50 flex">

    <!-- Sidebar -->
    <aside class="w-64 bg-white border-r border-border hidden md:flex flex-col">

        <div class="h-16 flex items-center px-6 border-b border-border">
            <div class="w-8 h-8 rounded-xl bg-primary flex items-center justify-center">
                <svg class="w-4 h-4 text-white" fill="none" stroke="currentColor" stroke-width="2"
                    viewBox="0 0 24 24">
                    <path d="M12 14l9-5-9-5-9 5 9 5z"/>
                    <path d="M12 14l6.16-3.422"/>
                </svg>
            </div>

            <span class="ml-2 font-bold text-lg text-foreground">
                Uni<span class="text-primary">Forum</span>
            </span>
        </div>


        <nav class="flex-1 px-4 py-6 space-y-2">

            <a href="#"
               class="flex items-center gap-3 px-4 py-3 rounded-xl bg-blue-50 text-primary font-medium">
                <span>🏠</span>
                Dashboard
            </a>


            <a href="#"
               class="flex items-center gap-3 px-4 py-3 rounded-xl text-slate-600 hover:bg-slate-100">
                <span>💬</span>
                Discussions
            </a>


            <a href="#"
               class="flex items-center gap-3 px-4 py-3 rounded-xl text-slate-600 hover:bg-slate-100">
                <span>📚</span>
                My Courses
            </a>


            <a href="#"
               class="flex items-center gap-3 px-4 py-3 rounded-xl text-slate-600 hover:bg-slate-100">
                <span>📝</span>
                Quizzes
            </a>


            <a href="#"
               class="flex items-center gap-3 px-4 py-3 rounded-xl text-slate-600 hover:bg-slate-100">
                <span>🧠</span>
                AI Recommendations
            </a>

        </nav>


        <div class="p-4 border-t border-border">

            <form method="POST" action="{{ route('logout') }}">
                @csrf

                <button
                    class="w-full flex items-center gap-3 px-4 py-3 rounded-xl text-slate-600 hover:bg-red-50 hover:text-red-600">

                    🚪 Logout

                </button>

            </form>

        </div>

    </aside>



    <!-- Main -->
    <main class="flex-1">

        <!-- Top bar -->
        <header class="h-16 bg-white border-b border-border flex items-center justify-between px-6">

            <div>
                <h1 class="font-semibold text-lg text-foreground">
                    Student Dashboard
                </h1>
            </div>


            <div class="flex items-center gap-4">

                <button class="relative">

                    🔔

                    <span class="absolute -top-1 -right-1 w-3 h-3 bg-primary rounded-full"></span>

                </button>


                <div class="w-9 h-9 rounded-full bg-primary flex items-center justify-center text-white font-semibold">
                    {{ substr(auth()->user()->name ?? 'U',0,1) }}
                </div>

            </div>

        </header>


        {{ $slot }}

    </main>

</div>