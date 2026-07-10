<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Smart Discussion Forum</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">

    @vite(['resources/css/app.css', 'resources/js/app.js'])
</head>

<body class="bg-gray-100">

<div class="min-h-screen flex">

    <!-- Left Side -->
    <div class="w-1/2 flex items-center justify-center bg-blue-700 text-white p-16">

        <div>
            <h1 class="text-5xl font-bold mb-6">
                Smart Discussion Forum
            </h1>

            <p class="text-xl mb-8 leading-relaxed">
                A collaborative learning platform where students,
                lecturers and administrators discuss ideas,
                take quizzes and receive personalized recommendations.
            </p>

            <div class="flex gap-4">

                <a href="{{ route('login') }}"
                   class="bg-white text-blue-700 px-6 py-3 rounded-lg font-semibold hover:bg-gray-200">
                    Login
                </a>

                <a href="{{ route('register') }}"
                   class="border border-white px-6 py-3 rounded-lg hover:bg-white hover:text-blue-700">
                    Register
                </a>

            </div>
        </div>

    </div>

    <!-- Right Side -->

    <div class="w-1/2 flex items-center justify-center">

        <div class="text-center">

            <h2 class="text-4xl font-bold text-gray-800 mb-4">
                Learn. Discuss. Grow.
            </h2>

            <p class="text-gray-600 text-lg max-w-md">
                Join academic discussions, collaborate with classmates,
                and receive AI-powered recommendations based on your interests.
            </p>

        </div>

    </div>

</div>

</body>
</html>