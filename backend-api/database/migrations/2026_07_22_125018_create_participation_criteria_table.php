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
        Schema::create('participation_criteria', function (Blueprint $table) {

            $table->id('criterion_id');

            $table->string('criterion_name');

        /*
         * Identifies what activity is being counted.
         * Examples:
         * topic
         * message
         * answer
         * resource
         */
            $table->string('activity_type');

            $table->integer('points');

            $table->timestamps();

        });
    }
    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('participation_criteria');
    }
};
