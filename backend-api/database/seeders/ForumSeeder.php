<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use App\Models\User;
use App\Models\Group;
use App\Models\Topic;
use App\Models\Message;
use App\Models\Quiz;
use App\Models\QuizQuestion;
use Illuminate\Support\Facades\Hash;

class ForumSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
                // 1.  Lecturer
                $lecturer = User::create([
                    'user_name' => 'Dr. Mugabe Sally',
                    'email' => 'sallymugabe@mak.ac.ug',
                    'password' => Hash::make('Password123'),
                    'role' => 'lecturer',
                    'status' => 'active',
                ]);

                // 2. Admin User
                $admin = User::create([
                    'user_name' => 'Weeza Quintus',
                    'email' => 'quintusweezs3@gmail.com',
                    'password' => Hash::make('AdminSecure2026!'),
                    'role' => 'admin',
                    'status' => 'active',
                ]);

                // 3. Student
                $student = User::create([
                    'user_name' => 'Tracy Ahumuza',
                    'email' => 'tracyahumuza6@gmail.com',
                    'password' => Hash::make('Tracy2026!'),
                    'role' => 'student',
                    'status' => 'active',
                ]);

                // 4. Group Created by a LECTURER
                $lecturerGroup = Group::create([
                    'group_name' => 'Software Engineering Year 1 Recess',
                    'created_by' => $lecturer->user_id,
                ]);

                // 5. Group Created by an ADMIN
                $adminGroup = Group::create([
                    'group_name' => 'General University Announcements',
                    'created_by' => $admin->user_id, // Points to the admin's user_id
                ]);

                // 6. Create a Topic inside the Lecturer's Group
                $topic = Topic::create([
                    'group_id' => $lecturerGroup->group_id,
                    'title' => 'Database Migration',
                    'ml_category' => 'Technical Support',
                    'created_by' => $student->user_id,
                    'created_at' => now(),
                ]);

                // 7. Message inside the topic
                Message::create([
                    'topic_id' => $topic->topic_id,
                    'sender_id' => $student->user_id,
                    'msg_txt' => 'Hello team, I have successfully executed our SDD table migrations using artisan!',
                    'is_synced' => true,
                    'is_restricted' => false,
                    'posted_at' => now(),
                ]);
    }
}
