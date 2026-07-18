<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Database\Eloquent\Relations\HasMany;

class StudentSubmission extends Model
{
    protected $table = 'student_submissions';
    protected $primaryKey = 'submission_id';
    public $timestamps = false;
    protected $fillable = ['quiz_id', 'student_id', 'started_at', 'submitted_at', 'status', 'total_score'];

    //Auto-cast timestamps to carbon instances for easy date math
    protected $casts = [
            'started_at' => 'datetime',
            'submitted_at' => 'datetime'
        ];

    public function quiz(): BelongsTo
    {
      return $this->belongsTo(Quiz::class, 'quiz_id', 'quiz_id');
    }

    //Link to the specific options the student selected
    public function answers(): HasMany
    {
       return $this->hasMany(StudentAnswer::class, 'submission_id', 'submission_id');
    }
}
