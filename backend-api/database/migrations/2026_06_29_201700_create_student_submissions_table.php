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
        Schema::create('student_submissions', function (Blueprint $table) {
            $table->id('submission_id'); // Unique submission id
            $table->foreignId('quiz_id')->constrained('quizzes', 'quiz_id')->onDelete('cascade'); // Links to id in the quiz table
            $table->foreignId('student_id')->constrained('users')->onDelete('cascade'); // Links to id in the user table
            $table->timestamp('started_at')->useCurrent(); // The exact second they opened the quiz window
            $table->timestamp('submitted_at')->nullable(); // Gets a value upon manual submission or automated timeout
            $table->enum('status', ['in-progress', 'submitted', 'auto-submitted'])->default('in-progress'); // Tracks progress
            $table->integer('total_score')->nullable(); // Total marks earned once grading is processed

        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('student_submissions');
    }
};
