<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class MessageExclusion extends Model
{
    protected $table = 'message_exclusions';
    protected $primaryKey = 'msg_ex_id';
    public $timestamps = false;
    protected $fillable = ['msg_id', 'ex_user_id'];

    public function message(): BelongsTo
    {
       return $this->belongsTo(Message::class, 'msg_id', 'msg_id');
    }
/**
     * Relationship: The user who is blocked by this exclusion record.
     */
    public function user(): BelongsTo
    {
        return $this->belongsTo(User::class, 'ex_user_id', 'user_id');
    }
}
