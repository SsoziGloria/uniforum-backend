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
        Schema::create('student_answers', function (Blueprint $table) {
            $table->id('answer_id');
            // Links directly to the student's submission session
            $table->foreignId('submission_id')->constrained('student_submissions', 'submission_id')->onDelete('cascade');
            // Links directly to the specific question
            $table->foreignId('quiz_qn_id')->constrained('quiz_questions', 'quiz_qn_id')->onDelete('cascade');
            $table->string('selected_option'); // Stores what choice they made (e.g., "A")
            $table->boolean('is_correct')->default(false); // Flags if their choice was correct
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('student_answers');
    }
};
