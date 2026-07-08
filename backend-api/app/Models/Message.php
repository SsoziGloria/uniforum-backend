<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class Message extends Model
{
    protected $table = 'messages';
    protected $primaryKey = 'msg_id';
    public $timestamps = false;
    protected $fillable = ['topic_id', 'sender_id', 'msg_txt', 'is_synced', 'is_restricted', 'posted_at'];
    protected $casts = [ 'is_synced' => 'boolean', 'is_restricted' => 'boolean', 'posted_at' => 'datetime'];

    public function topic(): BelongsTo
    {
        return $this->belongsTo(Topic::class, 'topic_id', 'topic_id');
    }

    public function sender(): BelongsTo
    {
        return $this->belongsTo(User::class, 'sender_id', 'user_id');
    }

    /**
     * Relationship: Get all exclusion records for this message.
     */
    public function exclusions(): \Illuminate\Database\Eloquent\Relations\HasMany
    {
        // Points to MessageExclusion model using custom msg_id primary key
        return $this->hasMany(MessageExclusion::class, 'msg_id', 'msg_id');
    }
}
