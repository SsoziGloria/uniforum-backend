<?php

namespace App\Services;

use App\Models\Topic;
use App\Models\Message;
use App\Models\GroupMember;
use App\Models\ParticipationCriteria;

class ParticipationService
{

    /**
     * Calculate a student's participation score inside a group.
     */
    public function getStudentParticipation($groupId, $userId)
    {

    // Count student activities

        $topicsCreated = Topic::where('group_id', $groupId)
           ->where('created_by', $userId)
           ->count();


        $messagesSent = Message::whereHas('topic', function($query) use ($groupId){

             $query->where('group_id',$groupId);

        })
        ->where('sender_id',$userId)
        ->count();



    /*
     * Retrieve developer-defined criteria
     */

        $criteria = ParticipationCriteria::all();



        $totalScore = 0;


        $breakdown = [

            'topics_created' => $topicsCreated,

            'messages_sent' => $messagesSent,

            'topic_points' => 0,

            'message_points' => 0,

        ];



        foreach($criteria as $criterion){


            switch($criterion->activity_type){


                case 'topic':

                    $points = $topicsCreated * $criterion->points;

                    $breakdown['topic_points'] = $points;

                    $totalScore += $points;

                    break;



                case 'message':

                    $points = $messagesSent * $criterion->points;

                    $breakdown['message_points'] = $points;

                    $totalScore += $points;

                    break;



                /*
                 Future criteria:
             
                  case 'answer':

                 case 'resource':
             
                */


            }

        }



        return [

            'total_score'=>$totalScore,

            'breakdown'=>$breakdown

        ];

    }


    /**
     * Calculate student's rank inside the group.
     */
    public function getStudentRank($groupId, $userId)
    {

        $students = GroupMember::where('group_id', $groupId)

            ->whereHas('user', function($query){

                $query->where('role','student');

            })

            ->with('user')

            ->get();



        $scores = [];



        foreach($students as $student){

            $score = $this->getStudentParticipation(

                $groupId,

                $student->user_id

            );


            $scores[] = [

                'user_id' => $student->user_id,

                'score' => $score['total_score']

            ];

        }



        // Highest score first
        usort($scores, function($a,$b){

            return $b['score'] <=> $a['score'];

        });



        $rank = 1;


        foreach($scores as $student){

            if($student['user_id'] == $userId){

                return $rank;

            }

            $rank++;

        }


        return null;

    }



    /**
     * Complete participation report.
     */
    public function getParticipationResults($groupId,$userId)
    {

        $score = $this->getStudentParticipation(
            $groupId,
            $userId
        );


        $rank = $this->getStudentRank(
            $groupId,
            $userId
        );


        return [

            'total_score' => $score['total_score'],

            'rank' => $rank,

            'breakdown' => $score['breakdown']

        ];

    }

    /**
     * Retrieve active criteria points breakdown.
     */
    public function getCriteriaSummary()
    {
        return ParticipationCriteria::all();
    }

    /**
     * Get full participation scores and ranks for all students in a group (for Lecturer view).
     */
    public function getGroupParticipationRoster($groupId)
    {
        $students = GroupMember::where('group_id', $groupId)
            ->whereHas('user', function ($query) {
                $query->where('role', 'student');
            })
            ->with('user:id,name,email')
            ->get();

        $roster = [];

        foreach ($students as $student) {
            $participation = $this->getStudentParticipation($groupId, $student->user_id);

            $roster[] = [
                'student_id'      => $student->user_id,
                'name'            => $student->user->name,
                'email'           => $student->user->email,
                'topics_created'  => $participation['breakdown']['topics_created'],
                'messages_sent'   => $participation['breakdown']['messages_sent'],
                'total_score'     => $participation['total_score'],
                'breakdown'       => $participation['breakdown'],
            ];
        }

        // Sort highest score first
        usort($roster, function ($a, $b) {
            return $b['total_score'] <=> $a['total_score'];
        });

        // Assign dynamic ranks
        $rank = 1;
        foreach ($roster as &$student) {
            $student['rank'] = $rank++;
        }

        return $roster;
    }

}