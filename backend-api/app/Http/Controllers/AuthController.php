<?php

namespace App\Http\Controllers;

use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Validator;
use App\Services\UserRegistrationService;
use App\Services\UserLoginService;
use Illuminate\Validation\ValidationException;
use App\Services\UserLogoutService;
use App\Services\UserProfileService;

class AuthController extends Controller
{
    protected UserLoginService $loginService;
    protected UserLogoutService $logoutService;
    protected UserProfileService $profileService;

    public function __construct(
        UserLoginService $loginService,
        UserLogoutService $logoutService,
        UserProfileService $profileService)
    {
        $this->loginService = $loginService;
        $this->logoutService = $logoutService;
        $this->profileService = $profileService;
    }

    public function login(Request $request)
    {
       $validated = $request->validate([
          'email'    => 'required|email',
          'password' => 'required|string',
        ]);


        try {

            $user = $this->loginService->login($validated);


            // Create Sanctum token for Java desktop application
            $tokenStr = $user->createToken('JavaDesktopClient')->plainTextToken;


            return response()->json([
               'status' => 'Success',
               'message' => 'Authentication successful!',
               'token' => $tokenStr,

                'user' => [
                   'user_id'   => $user->id,
                   'name' => $user->name,
                   'email'     => $user->email,
                   'role'      => $user->role,
                ]

            ], 200);


        } catch (ValidationException $e) {

            return response()->json([
               'status' => 'Error',
               'message' => 'Invalid email or password credentials.'
            ], 401);

        }
    }
    public function logout(Request $request)
    {
        $this->logoutService->logout($request->user());

        return response()->json([
           'status' => 'Success',
           'message' => 'Logged out successfully.'
        ], 200);
    }

       
    public function register(Request $request)
       {
           //Validate the incoming sign-up details
           $validated = $request->validate([
               'name' => 'required|string|max:255',
               'email'     => 'required|email|max:255|unique:users,email',
               'password'  => 'required|string|min:6',
               'role'  => 'required|string|in:student,lecturer',
               'lecturer_passcode' => 'nullable|string', // Passcode if signing up as lecturer

           ]);

           $user = app(UserRegistrationService::class)->register([
               'name' => $validated['name'],
               'email' => $validated['email'],
               'password' => $validated['password'],
               'role' => $validated['role'],
               'lecturer_passcode' => $validated['lecturer_passcode'] ?? null,
            ]);


           //Return a successful response
           return response()->json([
               'status' => 'Success',
               'message' => 'User account created successfully!',
               'user' => [
                   'user_id' => $user->id,
                   'name' => $user->name,
                   'name' => $user->name,
                   'email' => $user->email,

               ]
           ], 201);
       }
       public function profile(Request $request)
       {
            $profile = $this->profileService->getProfile(
            $request->user()
            );


            return response()->json([
               'status' => 'Success',
               'profile' => $profile
            ], 200);
        }
        public function updateProfile(Request $request)
        {
            $validated = $request->validate([
               'name' => 'required|string|max:255',
               'email' => 'required|email|max:255'
            ]);


            $user = $this->profileService->updateProfile(
               $request->user(),
               $validated
            );


            return response()->json([
               'status' => 'Success',
               'message' => 'Profile updated successfully.',
               'user' => $user
            ],200);
        }
}
