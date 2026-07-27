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
        Schema::table('group_members', function (Blueprint $table) {
            

            // Tracks inactivity warnings
            // 0 = no warning
            // 1 = first warning
            // 2 = second warning
            $table->integer('warning_count')
                  ->default(0)
                  ->after('last_activity');

        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('group_members', function (Blueprint $table) {
            $table->dropColumn('warning_count');
        });
    }
};
