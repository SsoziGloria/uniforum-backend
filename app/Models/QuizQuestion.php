<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class QuizQuestion extends Model
{
    protected $table = 'quiz_questions';
    protected $primaryKey = 'quiz_qn_id';
    public $timestamps = false;
    protected $fillable = ['quiz_id', 'qn_text', 'marks_worth'];

    public function quiz(): BelongsTo
    {
      return $this->belongsTo(Quiz::class, 'quiz_id', 'quiz_id');
    }
}
