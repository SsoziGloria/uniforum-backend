<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Lecturer Dashboard</title>

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
                Create Quiz
            </a>

            <a href="#" class="block hover:text-gray-200">
                Upload Materials
            </a>

            <a href="#" class="block hover:text-gray-200">
                Students
            </a>

        </nav>

    </aside>



    <!-- Main -->

    <main class="flex-1 p-10">


        <h2 class="text-4xl font-bold text-gray-800 mb-2">
            Welcome, Lecturer
        </h2>

        <p class="text-gray-600 mb-8">
            Manage learning content and student engagement.
        </p>



        <div class="grid grid-cols-3 gap-6">


            <div class="bg-white p-6 rounded-xl shadow">

                <h3 class="text-xl font-semibold">
                    Discussions
                </h3>

                <p class="text-gray-500 mt-2">
                    Manage discussion topics
                </p>

            </div>



            <div class="bg-white p-6 rounded-xl shadow">

                <h3 class="text-xl font-semibold">
                    Quizzes
                </h3>

                <p class="text-gray-500 mt-2">
                    Create and manage quizzes
                </p>

            </div>



            <div class="bg-white p-6 rounded-xl shadow">

                <h3 class="text-xl font-semibold">
                    Students
                </h3>

                <p class="text-gray-500 mt-2">
                    Monitor student activity
                </p>

            </div>


        </div>



        <div class="bg-white rounded-xl shadow p-6 mt-8">

            <h3 class="text-2xl font-bold mb-4">
                Recent Activities
            </h3>


            <ul class="space-y-3">

                <li>
                    Quiz submissions received
                </li>

                <li>
                    New discussion created
                </li>

                <li>
                    Learning materials uploaded
                </li>

            </ul>


        </div>


    </main>


</div>


</body>
</html>