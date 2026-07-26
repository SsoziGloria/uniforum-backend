<?php

namespace App\Http\Controllers\Web;

use App\Http\Controllers\Controller;
use App\Services\LecturerDashboardService;
use Illuminate\Http\Request;
use Illuminate\View\View;

class LecturerDashboardController extends Controller
{
    public function index(Request $request, LecturerDashboardService $service): View
    {
        $lecturerId = $request->user()->id;
        $data = $service->getDashboardMetrics($lecturerId);

        return view('lecturer.dashboard', $data);
    }
}