<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>@yield('title', 'UniForum Lecturer')</title>
    @vite(['resources/css/app.css', 'resources/js/app.js'])
</head>

<body class="bg-slate-50 antialiased">

<div class="h-screen flex overflow-hidden bg-slate-50">

    <!-- Fixed Sidebar -->
    <aside class="w-64 h-screen bg-white border-r border-slate-200 flex flex-col flex-shrink-0">
        
        <!-- Logo -->
        <div class="px-6 py-6 border-b border-slate-200 flex-shrink-0">
            <h1 class="text-2xl font-bold text-blue-600">UniForum</h1>
            <p class="text-sm text-slate-500">Lecturer Portal</p>
        </div>

        <!-- Navigation -->
        <nav class="flex-1 px-4 py-6 space-y-2 overflow-y-auto">
            <p class="px-4 text-xs font-semibold text-slate-400 uppercase tracking-wide mb-3">Main Menu</p>

            <a href="{{ route('lecturer.dashboard') }}"
               class="flex items-center gap-3 px-4 py-3 rounded-xl transition-all duration-200 hover:translate-x-1 {{ request()->routeIs('lecturer.dashboard') ? 'bg-blue-600 text-white shadow-md' : 'text-slate-700 hover:bg-blue-50' }}">
                🏠 <span>Dashboard</span>
            </a>

            <a href="{{ route('lecturer.groups.index') }}"
               class="flex items-center gap-3 px-4 py-3 rounded-xl transition-all duration-200 hover:translate-x-1 {{ request()->routeIs('lecturer.groups.*') ? 'bg-blue-600 text-white shadow-md' : 'text-slate-700 hover:bg-blue-50' }}">
                👥 <span>Groups</span>
            </a>

            <a href="{{ route('lecturer.students.index') }}"
               class="flex items-center gap-3 px-4 py-3 rounded-xl transition-all duration-200 hover:translate-x-1 {{ request()->routeIs('lecturer.students.*') ? 'bg-blue-600 text-white shadow-md' : 'text-slate-700 hover:bg-blue-50' }}">
                👨‍🎓 <span>Students</span>
            </a>

            <a href="{{ route('lecturer.notifications.index') }}"
               class="flex items-center gap-3 px-4 py-3 rounded-xl transition-all duration-200 hover:translate-x-1 {{ request()->routeIs('lecturer.notifications.*') ? 'bg-blue-600 text-white shadow-md' : 'text-slate-700 hover:bg-blue-50' }}">
                🔔 <span>Notifications</span>
            </a>

            <div class="pt-6">
                <p class="px-4 text-xs font-semibold text-slate-400 uppercase tracking-wide mb-3">Account</p>

                <a href="{{ route('lecturer.profile.show') }}"
                   class="flex items-center gap-3 px-4 py-3 rounded-xl transition-all duration-200 hover:translate-x-1 {{ request()->routeIs('lecturer.profile.*') ? 'bg-blue-600 text-white shadow-md' : 'text-slate-700 hover:bg-blue-50' }}">
                    ⚙️ <span>Profile Settings</span>
                </a>
            </div>
        </nav>

        <!-- User Section Fixed at Bottom -->
        <div class="border-t border-slate-200 p-5 flex-shrink-0 bg-white">
            <div class="flex items-center gap-3">
                <div class="h-10 w-10 rounded-full bg-blue-600 text-white flex items-center justify-center font-bold">
                    {{ strtoupper(substr(auth()->user()->name ?? 'U', 0, 1)) }}
                </div>
                <div class="overflow-hidden">
                    <p class="font-semibold text-slate-800 text-sm truncate max-w-[120px]">
                        {{ auth()->user()->name ?? 'Lecturer' }}
                    </p>
                    <p class="text-xs text-slate-500">Lecturer</p>
                </div>
            </div>

            <form method="POST" action="{{ route('logout') }}" class="mt-4">
                 @csrf
                <button type="submit" class="w-full flex items-center gap-3 px-4 py-2.5 rounded-xl text-red-600 hover:bg-red-50 transition text-sm font-medium">
                   🚪 <span>Logout</span>
                </button>
            </form>
        </div>
    </aside>

    <!-- Main Content Area -->
    <div class="flex-1 flex flex-col min-w-0 h-screen overflow-y-auto">

        <!-- Top Navigation Bar -->
        <header class="h-20 bg-white border-b border-slate-200 flex items-center justify-between px-6 flex-shrink-0 sticky top-0 z-10">
            <div class="flex items-center gap-4">
                <div>
                    <h2 class="text-xl font-bold text-slate-800">
                          @yield('page-title', 'Dashboard')
                    </h2>
                    <p class="text-sm text-slate-500">
                        Welcome back, {{ auth()->user()->name ?? 'Lecturer' }}
                    </p>
                </div>
            </div>

            <!-- Header Action Items -->
            <div class="flex items-center gap-6">

                <!-- Notifications Bell Icon -->
                @php
                    $unreadCount = auth()->user()->unreadNotifications()->count();
                @endphp
                <a href="{{ route('lecturer.notifications.index') }}" class="relative text-xl hover:opacity-80 transition">
                    🔔
                    @if($unreadCount > 0)
                        <span class="absolute -top-1 -right-2 bg-red-500 text-white text-[10px] font-bold h-5 min-w-[20px] px-1 rounded-full flex items-center justify-center">
                            {{ $unreadCount > 99 ? '99+' : $unreadCount }}
                        </span>
                    @endif
                </a>

                <!-- User Profile Summary -->
                <a href="{{ route('lecturer.profile.show') }}" class="flex items-center gap-3 hover:opacity-80 transition">
                    <div class="w-10 h-10 rounded-full bg-blue-600 text-white flex items-center justify-center font-bold">
                        {{ strtoupper(substr(auth()->user()->name ?? 'U', 0, 1)) }}
                    </div>

                    <div class="hidden md:block">
                        <p class="font-semibold text-sm text-slate-800">
                            {{ auth()->user()->name ?? 'Lecturer' }}
                        </p>
                        <p class="text-xs text-slate-500">Lecturer</p>
                    </div>
                </a>
            </div>
        </header>

        <!-- Dynamic Main Content -->
        <main class="flex-1 p-6">
            @yield('content')
        </main>
    </div>

</div>

</body>
</html>