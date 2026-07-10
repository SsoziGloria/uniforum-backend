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
        Schema::create('topics', function (Blueprint $table) {
           $table->id('topic_id'); // Unique ID for the topic
           $table->foreignId('group_id')->constrained('groups', 'group_id')->onDelete('cascade'); // Links to id in the group table
           $table->string('title', 200); // Name of topic thread
           $table->string('ml_category', 100)->nullable(); // The category automatically assigned by the machine learning model
           $table->foreignId('created_by')->constrained('users')->onDelete('cascade'); // User who started the topic
           $table->timestamp('created_at')->useCurrent(); // When the topic thread was started
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('topics');
    }
};
