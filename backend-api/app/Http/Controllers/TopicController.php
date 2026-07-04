<?php

namespace App\Http\Controllers;

use App\Models\Topic;
use Illuminate\Http\Request;

class TopicController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $topics = Topic::all();

        return response()->json([
                    'success' => true,
                    'data' => $topics
                ], 200);
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        // Validate that the topic has a name and belongs to an existing group
            $validated = $request->validate([
                'group_id' => 'required|exists:groups,group_id',
                'title' => 'required|string|max:255',
                'description' => 'nullable|string'
            ]);

            //Create and save the topic
            $topic = Topic::create($validated);

            // Return the created topic
            return response()->json($topic, 201);
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
