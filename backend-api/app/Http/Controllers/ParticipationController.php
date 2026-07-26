<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Services\ParticipationService;

class ParticipationController extends Controller
{
    protected ParticipationService $participationService;

    public function __construct(ParticipationService $participationService)
    {
        $this->participationService = $participationService;
    }

    /**
     * Get authenticated student's participation results inside a specific group.
     */
    public function results(Request $request, $groupId)
    {
        $results = $this->participationService
            ->getParticipationResults(
                $groupId,
                $request->user()->id
            );

        return response()->json([
            'status' => 'Success',
            'data'   => $results
        ], 200);
    }

    /**
     * Get complete group participation roster for lecturers.
     */
    public function groupRoster($groupId)
    {
        $criteria = $this->participationService->getCriteriaSummary();
        $roster = $this->participationService->getGroupParticipationRoster($groupId);

        return response()->json([
            'status' => 'Success',
            'data'   => [
                'criteria' => $criteria,
                'roster'   => $roster,
            ]
        ], 200);
    }
}