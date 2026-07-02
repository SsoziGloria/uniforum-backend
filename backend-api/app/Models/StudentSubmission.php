<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class StudentSubmission extends Model
{
    protected $table = 'student_submissions';
    protected $primaryKey = 'submission_id';
    public $timestamps = false;
    protected $fillable = ['quiz_id', 'student_id', 'started_at', 'submitted_at', 'status', 'total_score'];

    public function quiz(): BelongsTo
    {
      return $this->belongsTo(Quiz::class, 'quiz_id', 'quiz_id');
    }
}
