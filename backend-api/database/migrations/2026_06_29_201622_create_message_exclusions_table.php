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
        Schema::create('message_exclusions', function (Blueprint $table) {
            $table->id('msg_ex_id'); // Unique row ID
            $table->foreignId('msg_id')->constrained('messages', 'msg_id')->onDelete('cascade'); // Links to the restricted message
            $table->foreignId('ex_user_id')->constrained('users')->onDelete('cascade'); // Links to the user Id of the user
                                                                                           //who should not see this message
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('message_exclusions');
    }
};
