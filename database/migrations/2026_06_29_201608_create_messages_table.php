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
        Schema::create('messages', function (Blueprint $table) {
         $table->id('msg_id'); // Unique ID for the message
         $table->foreignId('topic_id')->constrained('topics', 'topic_id')->onDelete('cascade'); // Links to the id on topic table
         $table->foreignId('sender_id')->constrained('users', 'user_id')->onDelete('cascade'); // Links to the id in user table
         $table->text('msg_txt'); // The actual message content
         $table->boolean('is_synced')->default(true); // Tracks whether offline desktop messages have successfully updated the web server
         $table->boolean('is_restricted')->default(false); // Default: FALSE. Set to TRUE if some members are excluded
         $table->timestamp('posted_at')->useCurrent(); // Exact time sent
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('messages');
    }
};
