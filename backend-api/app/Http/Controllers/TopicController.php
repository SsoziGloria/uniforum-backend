<?php

namespace App\Http\Controllers;

use App\Models\Topic;
use Illuminate\Http\Request;
use Barryvdh\DomPDF\Facade\Pdf;

class TopicController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index($groupId)
    {
        $groupIdClean = is_object($groupId) ? $groupId->id : $groupId;
        $topics = Topic::where('group_id', $groupIdClean)->orderBy('created_at', 'desc')->get();


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
            $groupIdClean = is_object($groupId) ? $groupId->id : $groupId;

            //Create and save the topic
            $topic = Topic::create([
            'group_id' => $groupIdClean,
            'title'    => $validated['title'],
            'description' => $validated['description'] ?? null,
            'created_by' => $request->user()->id,
            'created_at'  => now(),
            ]);

            // Return the created topic
            return response()->json([
            'success' => true,
            'message' => 'Topic created successfully!',
            'data' => $topic
            ], 201);
    }

   public function exportPdf($group, $id)
   {
       // Find the topic making sure it belongs to that specific group
       $topic = Topic::where('group_id', $group)->where('topic_id', $id)->firstOrFail();

       // Fetch messages for this topic u
       $messages = $topic->messages;

       // Load into the PDF compiler
       $pdf = Pdf::loadView('pdf.topic', compact('topic', 'messages'));

       return $pdf->download("topic-{$id}-chats.pdf");
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
