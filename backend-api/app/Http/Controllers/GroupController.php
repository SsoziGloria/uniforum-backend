<?php

namespace App\Http\Controllers;

use App\Models\Group;
use Illuminate\Http\Request;

class GroupController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
       $groups = Group::all();

       return response()->json([
          'success' => true,
           'data' => $groups
           ], 200);
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
            //Validate the incoming request data
            $validated = $request->validate([
                'group_name' => 'required|string|unique:groups,group_name|max:255',
                'description' => 'nullable|string',
            ]);

            //Create and save the group
            $group = Group::create($validated);

            // Return the created group with a 201 success status
            return response()->json($group, 201);
    }

    /**
     * Display the specified resource.
     */
    public function show(string $id)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, string $id)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(string $id)
    {
        //
    }
}
