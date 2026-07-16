<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class StudentAnswer extends Model
{
    protected $table = 'student_answers';
    protected $primaryKey = 'answer_id';

    protected $fillable = [
        'submission_id',
        'quiz_qn_id',
        'selected_option',
        'is_correct'
    ];

    public function submission(): BelongsTo
    {
        return $this->belongsTo(StudentSubmission::class, 'submission_id', 'submission_id');
    }

    public function question(): BelongsTo
    {
        return $this->belongsTo(QuizQuestion::class, 'quiz_qn_id', 'quiz_qn_id');
    }
}
