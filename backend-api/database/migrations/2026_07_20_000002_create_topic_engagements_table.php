<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('topic_engagements', function (Blueprint $table) {
            $table->id();
            $table->foreignId('user_id')->constrained()->cascadeOnDelete();

            // Explicitly reference topics.topic_id, not the default "id" —
            // Topic's primary key is topic_id, so the FK must match it.
            $table->foreignId('topic_id')->constrained(table: 'topics', column: 'topic_id')->cascadeOnDelete();

            $table->timestamp('created_at')->useCurrent();
            $table->index(['user_id', 'topic_id']);
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('topic_engagements');
    }
};
