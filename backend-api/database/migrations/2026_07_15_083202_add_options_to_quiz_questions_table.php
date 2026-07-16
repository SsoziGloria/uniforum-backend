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
        Schema::table('quiz_questions', function (Blueprint $table) {
            // choices as a JSON column (e.g. ["A", "B", "C", "D"]) and correct_option right after qn_text
            $table->json('options')->nullable()->after('qn_text');
            $table->string('correct_option', 10)->nullable()->after('options'); // e.g. "A" or "B"
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('quiz_questions', function (Blueprint $table) {
            $table->dropColumn(['options', 'correct_option']);
        });
    }
};
