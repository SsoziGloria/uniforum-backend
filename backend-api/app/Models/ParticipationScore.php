<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class ParticipationScore extends Model
{
    protected $primaryKey = 'score_id';


    protected $fillable = [

        'group_id',
        'student_id',
        'total_score'

    ];


    public function group()
    {
        return $this->belongsTo(
            Group::class,
            'group_id',
            'group_id'
        );
    }


    public function student()
    {
        return $this->belongsTo(
            User::class,
            'student_id',
            'id'
        );
    }
}
