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
        Schema::table('messages', function (Blueprint $table) {
            if (!Schema::hasColumn('messages', 'parent_msg_id')) {

            $table->foreignId('parent_msg_id')
                ->nullable()
                ->after('topic_id')
                ->constrained('messages', 'msg_id')
                ->cascadeOnDelete();

        }
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('messages', function (Blueprint $table) {
            if (Schema::hasColumn('messages', 'parent_msg_id')) {

            $table->dropForeign(['parent_msg_id']);
            $table->dropColumn('parent_msg_id');

        }
        });
    }
};
