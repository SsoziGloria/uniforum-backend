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
        Schema::create('user_warnings', function (Blueprint $table) {
           $table->id('warning_id'); // Unique identifier
           $table->foreignId('user_id')->constrained('users', 'user_id')->onDelete('cascade'); // Points to the non-compliant user profile receiving the warning
           $table->foreignId('issued_by')->constrained('users', 'user_id')->onDelete('cascade'); // Tracks the specific group admin who authorized the warning
           $table->string('warning_reason', 255); // Details like ‘Inactivity warning’
           $table->timestamp('issued_at')->useCurrent(); // The exact date and time the warning hit the user
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('user_warnings');
    }
};
