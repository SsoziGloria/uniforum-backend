<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Student Dashboard</title>

    @vite(['resources/css/app.css', 'resources/js/app.js'])
</head>


<body class="bg-gray-100">


<div class="min-h-screen flex">


    <!-- Sidebar -->

    <aside class="w-64 bg-blue-700 text-white p-6">

        <h1 class="text-2xl font-bold mb-10">
            Smart Forum
        </h1>


        <nav class="space-y-4">

            <a href="#" class="block hover:text-gray-200">
                Dashboard
            </a>

            <a href="#" class="block hover:text-gray-200">
                Discussions
            </a>

            <a href="#" class="block hover:text-gray-200">
                Quizzes
            </a>

            <a href="#" class="block hover:text-gray-200">
                Recommendations
            </a>

            <a href="#" class="block hover:text-gray-200">
                Profile
            </a>

        </nav>

    </aside>



    <!-- Main -->

    <main class="flex-1 p-10">


        <h2 class="text-4xl font-bold text-gray-800 mb-2">
            Welcome back, {{ Auth::user()->name ?? 'Student' }}
        </h2>

        <p class="text-gray-600 mb-8">
            Continue learning, discussing and growing.
        </p>



        <!-- Cards -->

        <div class="grid grid-cols-3 gap-6">


            <div class="bg-white p-6 rounded-xl shadow">

                <h3 class="text-xl font-semibold">
                    Discussions
                </h3>

                <p class="text-gray-500 mt-2">
                    12 Active discussions
                </p>

            </div>



            <div class="bg-white p-6 rounded-xl shadow">

                <h3 class="text-xl font-semibold">
                    Quizzes
                </h3>

                <p class="text-gray-500 mt-2">
                    5 Available quizzes
                </p>

            </div>




            <div class="bg-white p-6 rounded-xl shadow">

                <h3 class="text-xl font-semibold">
                    AI Recommendations
                </h3>

                <p class="text-gray-500 mt-2">
                    New topics suggested for you
                </p>

            </div>


        </div>



        <!-- Recent Discussions -->

        <div class="bg-white rounded-xl shadow p-6 mt-8">

            <h3 class="text-2xl font-bold mb-4">
                Recent Discussions
            </h3>


            <ul class="space-y-3">

                <li>
                    How does machine learning work?
                </li>

                <li>
                    Software engineering best practices
                </li>

                <li>
                    Database normalization concepts
                </li>

            </ul>

        </div>


    </main>


</div>


</body>
</html>