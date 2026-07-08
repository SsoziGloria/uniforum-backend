<?php

namespace App\Http\Controllers;

use App\Models\Topic;
use Illuminate\Http\Request;

class TopicController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index($groupId)
    {
        $topics = Topic::where('group_id', $groupId)->get();

        return response()->json([
                    'success' => true,
                    'data' => $topics
                ], 200);
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request, $groupId)
    {
        // Validate that the topic has a name and belongs to an existing group
            $validated = $request->validate([
                'title' => 'required|string|max:255',
                'description' => 'nullable|string'
            ]);

            //Create and save the topic
            $topic = Topic::create([
            'group_id' => $groupId,
            'title'    => $validated['title'],
            'description' => $validated['description'] ?? null,
            'created_by' => $request->user()->user_id,
            'created_at'  => now(),
            ]);

            // Return the created topic
            return response()->json([
            'success' => true,
            'message' => 'Topic created successfully!',
            'data' => $topic
            ], 201);
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
