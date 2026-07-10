<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Admin Dashboard</title>

    @vite(['resources/css/app.css', 'resources/js/app.js'])
</head>


<body class="bg-gray-100">


<div class="min-h-screen flex">


    <aside class="w-64 bg-blue-700 text-white p-6">

        <h1 class="text-2xl font-bold mb-10">
            Smart Forum
        </h1>


        <nav class="space-y-4">

            <a href="#" class="block hover:text-gray-200">
                Dashboard
            </a>

            <a href="#" class="block hover:text-gray-200">
                Users
            </a>

            <a href="#" class="block hover:text-gray-200">
                Discussions
            </a>

            <a href="#" class="block hover:text-gray-200">
                Reports
            </a>

            <a href="#" class="block hover:text-gray-200">
                Settings
            </a>

        </nav>


    </aside>



    <main class="flex-1 p-10">


        <h2 class="text-4xl font-bold text-gray-800 mb-2">
            Admin Dashboard
        </h2>


        <p class="text-gray-600 mb-8">
            Manage the Smart Discussion Forum system.
        </p>




        <div class="grid grid-cols-3 gap-6">


            <div class="bg-white p-6 rounded-xl shadow">

                <h3 class="text-xl font-semibold">
                    Users
                </h3>

                <p class="text-gray-500 mt-2">
                    Manage students and lecturers
                </p>

            </div>



            <div class="bg-white p-6 rounded-xl shadow">

                <h3 class="text-xl font-semibold">
                    Content
                </h3>

                <p class="text-gray-500 mt-2">
                    Monitor discussions and quizzes
                </p>

            </div>



            <div class="bg-white p-6 rounded-xl shadow">

                <h3 class="text-xl font-semibold">
                    System
                </h3>

                <p class="text-gray-500 mt-2">
                    Configure platform settings
                </p>

            </div>


        </div>


    </main>


</div>


</body>

</html>