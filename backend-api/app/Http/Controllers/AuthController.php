<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Hash;

class AuthController extends Controller
{
    public function login(Request $request)
        {
            //Validate incoming login requests
            $validated = $request->validate([
                'email'    => 'required|email',
                'password' => 'required|string',
            ]);

            //Get the user profile manually from the database
            $user = DB::table('users')->where('email', $validated['email'])->first();

            // Verify user exists and check if the password matches the database hash
            if (!$user || !Hash::check($validated['password'], $user->password)) {
                return response()->json([
                    'status'  => 'Error',
                    'message' => 'Invalid email or password credentials.'
                ], 401);
            }

           // Use Eloquent to generate a valid Sanctum Token
               // Fetch the User model instance using your custom primary key
               $userModel = \App\Models\User::find($user->id);

               // Create an official, formatted token string
               $tokenStr = $userModel->createToken('JavaDesktopClient')->plainTextToken;



            //Return the raw token and user details to the Java Desktop Application
            return response()->json([
                'status' => 'Success',
                'message' => 'Authentication successful!',
                'token' => $tokenStr, // The Java application captures and saves this string
                'user' => [
                    'user_id'   => $user->id,
                    'user_name' => $user->name,
                   // 'role'      => $user->role,
                    'email'     => $user->email
                ]
            ], 200);
        }

       public function register(Request $request)
       {
           //Validate the incoming sign-up details
           $validated = $request->validate([
               'user_name' => 'required|string|max:255',
               'email'     => 'required|email|max:255',
               'password'  => 'required|string|min:6',
           ]);

           //Check if a user with this email already exists manually
           $existingUser = DB::table('users')->where('email', $validated['email'])->first();
           if ($existingUser) {
               return response()->json([
                   'status' => 'Error',
                   'message' => 'A user account with this email address already exists.'
               ], 425);
           }

           //Insert the new student profile into your users table
           // Note: We use Hash::make() so the password is securely encrypted!
           $userId = DB::table('users')->insertGetId([
               'name'  => $validated['user_name'],
               'email'      => $validated['email'],
               'password'   => Hash::make($validated['password']),
               //'role'       => 'student', // Defaults new self-registrations to student
               //'status'     => 'active',
               //'online'     => false,
               'created_at' => now(),
               'updated_at' => now(),
           ]);

           //Return a successful response
           return response()->json([
               'status' => 'Success',
               'message' => 'User account created successfully!',
               'user' => [
                   'user_id' => $userId,
                   'user_name' => $validated['user_name'],
                   'email' => $validated['email'],
                   'role' => 'student'
               ]
           ], 201);
       }
}
