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
        Schema::create('group_members', function (Blueprint $table) {
         $table->id('member_id'); // Unique identifier
         $table->foreignId('group_id')->constrained('groups', 'group_id')->onDelete('cascade'); // Links to id in groups table
         $table->foreignId('user_id')->constrained('users', 'user_id')->onDelete('cascade'); // Links to id in user table
         $table->timestamp('joined_at')->useCurrent(); // Date they entered the group
         $table->timestamp('last_activity')->nullable(); // Tracks user engagement to calculate inactivity warnings
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('group_members');
    }
};
