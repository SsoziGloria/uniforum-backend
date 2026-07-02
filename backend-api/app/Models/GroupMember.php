<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class GroupMember extends Model
{
    protected $table = 'group_members';
    protected $primaryKey = 'member_id';
    public $timestamps = false;
    protected $fillable = ['group_id', 'user_id', 'joined_at', 'last_activity'];

    public function group(): BelongsTo
    {
       return $this->belongsTo(Group::class, 'group_id', 'group_id');
    }

    public function user(): BelongsTo
    {
       return $this->belongsTo(User::class, 'user_id', 'user_id');
    }
}
