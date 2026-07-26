<?php

namespace App\Http\Controllers\Web;

use App\Http\Controllers\Controller;
use App\Services\StudentPerformanceService;
use Illuminate\Support\Facades\Auth;

class LecturerStudentController extends Controller
{
    protected StudentPerformanceService $performanceService;

    public function __construct(StudentPerformanceService $performanceService)
    {
        $this->performanceService = $performanceService;
    }

    public function index()
    {
        $data = $this->performanceService->getLecturerStudentsPerformance(Auth::id());

        return view('lecturer.students.index', $data);
    }
}