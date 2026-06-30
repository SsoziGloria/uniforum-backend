<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('quiz_questions', function (Blueprint $table) {
            $table->id('quiz_qn_id'); // Unique ID for question
            $table->foreignId('quiz_id')->constrained('quizzes', 'quiz_id')->onDelete('cascade'); // Links to the id in the quiz table
            $table->text('qn_text'); // The actual question being asked
            $table->integer('marks_worth'); // The score value allocated to this single question
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('quiz_questions');
    }
};
