<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;


return new class extends Migration
{
    public function up(): void
    {
        if (Schema::hasColumn('topics', 'ai_category')) {
            Schema::table('topics', function (Blueprint $table) {
                $table->renameColumn('ai_category', 'ml_category');
            });
        } elseif (!Schema::hasColumn('topics', 'ml_category')) {
            Schema::table('topics', function (Blueprint $table) {
                $table->string('ml_category')->nullable()->after('description');
            });
        }
        // If ml_category already exists (e.g. from your teammate's own
        // migration), this migration does nothing — safe to run.
    }

    public function down(): void
    {
        Schema::table('topics', function (Blueprint $table) {
            $table->dropColumn('ml_category');
        });
    }
};
