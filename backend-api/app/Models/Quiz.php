<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Database\Eloquent\Relations\HasMany;

class Quiz extends Model
{
    protected $table = 'quizzes';
    protected $primaryKey = 'quiz_id';
    protected $fillable = ['group_id', 'lecturer_id', 'quiz_title', 'quiz_date', 'start_time', 'duration_minutes', 'student_category', 'is_published'];

    public function group(): BelongsTo
    {
      return $this->belongsTo(Group::class, 'group_id', 'group_id');
    }

    public function questions(): HasMany
    {
       return $this->hasMany(QuizQuestion::class, 'quiz_id', 'quiz_id');
    }
}
