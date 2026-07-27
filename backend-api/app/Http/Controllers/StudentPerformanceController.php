<?php

namespace App\Http\Controllers;

use App\Services\StudentPerformanceService;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\Auth;

class StudentPerformanceController extends Controller
{
    protected StudentPerformanceService $performanceService;

    public function __construct(StudentPerformanceService $performanceService)
    {
        $this->performanceService = $performanceService;
    }

    /**
     * Get student performance summary & roster for the authenticated lecturer.
     */
    public function index(): JsonResponse
    {
        $lecturerId = Auth::id();

        if (!$lecturerId) {
            return response()->json([
                'success' => false,
                'message' => 'Unauthenticated user.',
            ], 401);
        }

        $data = $this->performanceService->getLecturerStudentsPerformance($lecturerId);

        // Strip raw Eloquent models before returning JSON response
        $formattedStudents = $data['studentsData']->map(function ($item) {
            unset($item['student']);
            return $item;
        });

        return response()->json([
            'success' => true,
            'data'    => [
                'summary' => [
                    'total_students'        => $data['totalStudents'],
                    'active_this_week'      => $data['activeThisWeekCount'],
                    'average_participation' => $data['avgParticipation'],
                    'average_quiz_score'    => $data['avgQuizScore'],
                ],
                'students' => $formattedStudents,
            ],
        ], 200);
    }
}