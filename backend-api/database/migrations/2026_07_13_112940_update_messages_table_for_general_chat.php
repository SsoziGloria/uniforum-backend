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
                //Topic_id optional (nullable)
                $table->foreignId('topic_id')->nullable()->change();


               if (!Schema::hasColumn('messages', 'group_id')) {
                               $table->foreignId('group_id')
                                 ->constrained('groups', 'group_id')
                                 ->onDelete('cascade');
                           }
            });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('messages', function (Blueprint $table) {
                    $table->dropForeign(['group_id']);
                    $table->dropColumn('group_id');

                    // Revert topic_id back to non-nullable if original design required it
                    $table->foreignId('topic_id')->nullable(false)->change();
                });
    }
};
