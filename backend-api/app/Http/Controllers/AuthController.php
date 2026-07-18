<?php

namespace App\Http\Controllers;

use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Validator;

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
           $user = User::where('email', $validated['email'])->first();

            // Verify user exists and check if the password matches the database hash
            if (!$user || !Hash::check($validated['password'], $user->password)) {
                return response()->json([
                    'status'  => 'Error',
                    'message' => 'Invalid email or password credentials.'
                ], 401);
            }

               // Create an official, formatted token string
               $tokenStr = $user->createToken('JavaDesktopClient')->plainTextToken;



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
               'email'     => 'required|email|max:255|unique:users,email',
               'password'  => 'required|string|min:6',
               'rules_accepted' => 'required|accepted',

               ], [ // Custom error message passed back UI
               'rules_accepted.required' => 'You must accept the platform rules and guidelines to complete registration.',
               'rules_accepted.accepted' => 'You must accept the platform rules and guidelines to complete registration.'
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
           //Hash::make() so the password is securely encrypted!
           $user  = User::create([
               'name'  => $validated['user_name'],
               'email'      => $validated['email'],
               'password'   => Hash::make($validated['password']),
               //'role'       => 'student', // Defaults new self-registrations to student
               //'status'     => 'active',
               //'online'     => false,
               'rules_accepted' => true,
               'created_at' => now(),
               'updated_at' => now(),
           ]);

           $tokenStr = $user->createToken('JavaDesktopClient')->plainTextToken;

           //Return a successful response
           return response()->json([
               'status' => 'Success',
               'message' => 'User account created successfully!',
               'user' => [
                   'user_id' => $user->id,
                   'user_name' => $user->name,
                   'email' => $user->email,
                   'role' => 'student'
               ]
           ], 201);
       }
}
