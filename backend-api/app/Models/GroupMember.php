<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class GroupMember extends Model
{
    protected $table = 'group_members';
    protected $primaryKey = 'member_id';
    public $timestamps = false;
    protected $fillable = ['group_id', 'user_id', 'role', 'joined_at', 'last_activity','warning_count', 'blacklisted_until'];

   // Optional casts so Laravel treats timestamps as Carbon date objects automatically
    protected $casts = [
     'last_activity' => 'datetime',
     'warning_count' => 'integer',
     'blacklisted_until' => 'datetime',
        ];


    public function group(): BelongsTo
    {
       return $this->belongsTo(Group::class, 'group_id', 'group_id');
    }

    public function user(): BelongsTo
    {
       return $this->belongsTo(User::class, 'user_id', 'id');
    }
}
