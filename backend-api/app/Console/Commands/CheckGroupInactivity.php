<?php

namespace App\Console\Commands;

use Illuminate\Console\Command;
use App\Models\UserWarning;
use App\Models\GroupMember;
use Carbon\Carbon;


class CheckGroupInactivity extends Command
{
    /**
     * The name and signature of the console command.
     *
     * @var string
     */
    protected $signature = 'app:check-group-inactivity';


    /**
     * The console command description.
     *
     * @var string
     */
    protected $description = 'Scan group memberships using Eloquent models to issue warnings and blacklist inactive members';

    /**
     * Execute the console command.
     */
    public function handle()
    {
        $inactivityDaysThreshold = 7;
        $blacklistTimeframeDays = 3;

        $thresholdDate = Carbon::now()->subDays($inactivityDaysThreshold);

        //Fetch inactive members using Eloquent
        $inactiveMembers = GroupMember::where(function($query) use ($thresholdDate) {
        $query->where('last_activity', '<', $thresholdDate)
              ->orWhereNull('last_activity');
             })
              ->where(function($query) {
        $query->whereNull('blacklisted_until')
              ->orWhere('blacklisted_until', '<', Carbon::now());
                   })
              ->get();

        foreach ($inactiveMembers as $member) {
               $userId = $member->user_id;
               $groupId = $member->group_id;

        // Count current warnings using the UserWarning model
               $warningCount = UserWarning::where('user_id', $userId)
                    ->where('group_id', $groupId)
                    ->count();

          if ($warningCount == 0) {
               // Issue Warning 1
                UserWarning::create([
                'user_id' => $userId,
                'group_id' => $groupId,
                'issued_by' => null,
                'warning_reason' => 'First inactivity warning: Please participate in the group.',
                    ]);
          $this->info("Issued Warning #1 to User {$userId} in Group {$groupId}");

          } elseif ($warningCount == 1) {
              // Issue Warning 2
                 UserWarning::create([
                 'user_id' => $userId,
                 'group_id' => $groupId,
                 'issued_by' => null,
                 'warning_reason' => 'Second inactivity warning: Final warning before automatic blacklist.',
                       ]);
          $this->info("Issued Warning #2 to User {$userId} in Group {$groupId}");

            } else {
               // Blacklist using the GroupMember model
                  $member->update([
                  'blacklisted_until' => Carbon::now()->addDays($blacklistTimeframeDays)
                     ]);

              // Clear out warnings
               UserWarning::where('user_id', $userId)
                           ->where('group_id', $groupId)
                           ->delete();

             $this->warn("User {$userId} in Group {$groupId} has been blacklisted for {$blacklistTimeframeDays} days.");
                    }
                }

             $this->info('Inactivity check completed successfully');
            }
        }
