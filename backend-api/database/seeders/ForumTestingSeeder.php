<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Hash;

class ForumTestingSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        //default student user
         DB::table('users')->insert([
           'user_id' => 1,
           'user_name' => 'Tracy Ahumuza',
           'email' => 'tracyahumuza@gmail.com',
           'password' => Hash::make('password123'),
           'role' => 'student',
           'status' => 'active',
           'online' => true,
           'created_at' => now(),

             ]);

                //default discussion group
           DB::table('groups')->insert([
             'group_id' => 1,
              'group_name' => 'Group 30 Recess Team',
              'created_by' => 1,
              'created_at' => now(),

                ]);

                //default discussion topic thread
            DB::table('topics')->insert([
              'topic_id' => 1,
              'group_id' => 1,
              'title' => 'General System Architecture',
              'ml_category' => 'Software Engineering',
              'created_by' => 1,
              'created_at' => now(),

                ]);
    }
}
