<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Smart Discussion Forum</title>

    @vite(['resources/css/app.css', 'resources/js/app.js'])
</head>


<body class="bg-gray-100">


<div class="min-h-screen flex">


    <!-- Left Side Branding -->

    <div class="w-1/2 bg-gradient-to-br from-blue-800 to-blue-600 text-white flex items-center justify-center p-16">

        <div>
             <div class="mb-6">

                <div class="w-16 h-16 bg-white rounded-full flex items-center justify-center">

                    <span class="text-blue-700 text-3xl font-bold">
                      S
                    </span>

                </div>

            </div>

            <h1 class="text-5xl font-bold mb-6">
                Smart Discussion Forum
            </h1>


            <p class="text-xl leading-relaxed">
                Welcome back!
                Continue your academic journey through
                discussions, quizzes and AI-powered learning recommendations.
            </p>

        </div>

    </div>



    <!-- Login Form -->

    <div class="w-1/2 flex items-center justify-center">


        <div class="bg-white shadow-xl rounded-2xl p-10 w-96 border border-gray-100">


            <h2 class="text-3xl font-bold text-gray-800 mb-2 text-center">
                Login
            </h2>
            <p class="text-gray-500 text-center mb-6">
              Continue your discussions and learning journey
            </p>



            <x-validation-errors class="mb-4" />


            <form method="POST" action="{{ route('login') }}">

                @csrf


                <!-- Email -->

                <div class="mb-4">

                    <label class="block text-gray-700 mb-2">
                        Email
                    </label>


                    <input 
                        type="email"
                        name="email"
                        value="{{ old('email') }}"
                        required
                        autofocus
                        class="w-full border rounded-lg px-4 py-3 focus:outline-none focus:ring-2 focus:ring-blue-600"
                    >

                </div>



                <!-- Password -->

                <div class="mb-4">

                    <label class="block text-gray-700 mb-2">
                        Password
                    </label>


                    <input 
                        type="password"
                        name="password"
                        required
                        class="w-full border rounded-lg px-4 py-3 focus:outline-none focus:ring-2 focus:ring-blue-600"
                    >

                </div>



                <!-- Remember -->

                <div class="mb-5">

                    <label class="flex items-center">

                        <input 
                            type="checkbox"
                            name="remember"
                            class="rounded border-gray-300"
                        >

                        <span class="ml-2 text-sm text-gray-600">
                            Remember me
                        </span>

                    </label>

                </div>



                <button
                    type="submit"
                    class="w-full bg-blue-700 text-white py-3 rounded-xl font-semibold hover:bg-blue-800 transition duration-300 shadow-md"
                >
                    Login
                </button>


            </form>



            <p class="text-center text-gray-600 mt-6">

                Don't have an account?

                <a href="{{ route('register') }}"
                   class="text-blue-700 font-semibold hover:underline">
                    Register
                </a>

            </p>


        </div>


    </div>


</div>


</body>

</html>