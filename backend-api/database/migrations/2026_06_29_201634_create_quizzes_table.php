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
        Schema::create('quizzes', function (Blueprint $table) {
            $table->id('quiz_id'); // Unique ID for quiz
            $table->foreignId('group_id')->constrained('groups', 'group_id')->onDelete('cascade'); // Specifies which group takes the quiz
            $table->foreignId('lecturer_id')->constrained('users', 'user_id')->onDelete('cascade'); // Links to id in the user table
            $table->string('quiz_title', 150); // Title of the quiz
            $table->date('quiz_date'); // The configured calendar date for the quiz
            $table->time('start_time'); // The exact scheduled start time
            $table->integer('duration_minutes'); // The allowed time limit
            $table->string('student_category'); // Links target student grouping
            $table->boolean('is_published')->default(false); // Set to TRUE to trigger the student announcement visibility
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('quizzes');
    }
};
