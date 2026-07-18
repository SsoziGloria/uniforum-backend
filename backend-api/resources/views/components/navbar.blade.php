<header 
    x-data="{ open:false }"
    class="fixed top-0 left-0 right-0 z-50 bg-white/95 backdrop-blur-sm border-b border-border"
>

    <div class="max-w-7xl mx-auto px-6 h-16 flex items-center justify-between">

        <!-- Logo -->
        <a href="{{ route('home') }}" class="flex items-center gap-2.5 group">

            <div class="w-8 h-8 rounded-xl bg-primary flex items-center justify-center shadow-sm group-hover:shadow-md transition-shadow">
                <svg class="w-4 h-4 text-white" fill="none" stroke="currentColor" stroke-width="2"
                     viewBox="0 0 24 24">
                    <path d="M12 14l9-5-9-5-9 5 9 5z"/>
                    <path d="M12 14l6.16-3.422a12.083 12.083 0 01.84 4.422c0 1.657-2.686 3-6 3s-6-1.343-6-3a12.083 12.083 0 01.84-4.422L12 14z"/>
                </svg>
            </div>

            <span class="font-bold text-foreground text-lg tracking-tight">
                Uni<span class="text-primary">Forum</span>
            </span>

        </a>




        <!-- Desktop CTA -->
        <div class="hidden md:flex items-center gap-3">

            <a 
                href="{{ route('login') }}"
                class="text-sm font-medium text-foreground hover:text-primary transition-colors px-4 py-2 rounded-xl hover:bg-blue-50"
            >
                Sign In
            </a>


            <a
                href="{{ route('register') }}"
                class="text-sm font-semibold bg-primary text-white px-5 py-2 rounded-xl hover:bg-blue-700 transition-colors shadow-sm"
            >
                Get Started
            </a>

        </div>



        <!-- Mobile Menu Button -->
        <button
            @click="open=!open"
            class="md:hidden p-2 rounded-xl hover:bg-muted transition-colors"
        >

            <svg x-show="!open"
                 class="w-5 h-5"
                 fill="none"
                 stroke="currentColor"
                 stroke-width="2"
                 viewBox="0 0 24 24">

                <path stroke-linecap="round"
                      stroke-linejoin="round"
                      d="M4 6h16M4 12h16M4 18h16"/>
            </svg>


            <svg x-show="open"
                 class="w-5 h-5"
                 fill="none"
                 stroke="currentColor"
                 stroke-width="2"
                 viewBox="0 0 24 24">

                <path stroke-linecap="round"
                      stroke-linejoin="round"
                      d="M6 18L18 6M6 6l12 12"/>
            </svg>

        </button>

    </div>




    <!-- Mobile Menu -->

    <div
        x-show="open"
        x-transition
        class="md:hidden bg-white border-t border-border px-6 pb-4 flex flex-col gap-3"
    >

        @foreach(['Features','About','Pricing','Contact'] as $item)

            <button class="text-sm font-medium text-muted-foreground text-left py-2">
                {{ $item }}
            </button>

        @endforeach



        <div class="flex gap-3 pt-2">

            <a
                href="{{ route('login') }}"
                class="flex-1 text-center text-sm font-medium border border-border rounded-xl py-2.5 hover:bg-muted"
            >
                Sign In
            </a>


            <a
                href="{{ route('register') }}"
                class="flex-1 text-center text-sm font-semibold bg-primary text-white rounded-xl py-2.5 hover:bg-blue-700"
            >
                Get Started
            </a>

        </div>

    </div>


</header>