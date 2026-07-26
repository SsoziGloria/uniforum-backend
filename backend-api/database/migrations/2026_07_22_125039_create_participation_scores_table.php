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
        Schema::create('participation_scores', function (Blueprint $table) {

           $table->id('score_id');


            $table->foreignId('group_id')
                ->constrained('groups','group_id')
                ->onDelete('cascade');


            $table->foreignId('student_id')
                ->constrained('users','id')
                ->onDelete('cascade');


            $table->integer('total_score')
                ->default(0);


            $table->timestamps();

        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('participation_scores');
    }
};
