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
          //enum column restricted to 'admin' or 'member', defaulting to 'member'
          $table->enum('role', ['admin', 'member'])->default('member')->after('user_id');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
           Schema::table('group_members', function (Blueprint $table) {
                  $table->dropColumn('role');
        });
    }
};
