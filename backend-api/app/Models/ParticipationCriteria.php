<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class ParticipationCriteria extends Model
{
    protected $table = 'participation_criteria';
    protected $primaryKey = 'criterion_id';



    protected $fillable = [

        'criterion_name',
        'activity_type',
        'points'

    ];
}
