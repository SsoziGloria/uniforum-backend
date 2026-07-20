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
               'role'  => 'required|string|in:student,lecturer',
               'lecturer_passcode' => 'nullable|string', // Passcode if signing up as lecturer

               ], [ // Custom error message passed back UI
               'rules_accepted.required' => 'You must accept the platform rules and guidelines to complete registration.',
               'rules_accepted.accepted' => 'You must accept the platform rules and guidelines to complete registration.'
           ]);

           $role = 'student'; // Default role

           // If they are registering as a lecturer, verify the secret staff passcode
           if ($validated['role'] === 'lecturer') {
            $secretStaffCode = 'MUK-STAFF-2026'; // Same secret token you use for groups

             if (($validated['lecturer_passcode'] ?? '') === $secretStaffCode) {
                      $role = 'lecturer';
              } else {
                return response()->json([
                   'status' => 'Error',
                   'message' => 'Invalid lecturer secret passcode. Registration failed.'
                       ], 403);
                     }
                 }

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
               'role'       =>  $role,
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
                   'role' => $user->role
               ]
           ], 201);
       }
}
