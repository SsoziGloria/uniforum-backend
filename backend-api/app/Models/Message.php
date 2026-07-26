<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Database\Eloquent\Relations\HasMany;

class Message extends Model
{

    protected $table = 'messages';
    protected $primaryKey = 'msg_id';
    public $timestamps = false;
    protected $fillable = ['group_id', 'topic_id','parent_msg_id', 'sender_id', 'msg_txt', 'is_synced', 'is_restricted', 'posted_at'];
    protected $casts = [ 'is_synced' => 'boolean', 'is_restricted' => 'boolean', 'posted_at' => 'datetime'];

    protected static function boot()
    {
        parent::boot();

        static::deleting(function ($message) {
            // Cascade to replies, votes, and exclusions
            $message->replies()->get()->each(function ($reply) {
                $reply->delete();
            });
            $message->votes()->delete();
            $message->exclusions()->delete();
        });
    }

    public function group()
    {
        return $this->belongsTo(\App\Models\Group::class, 'group_id');
    }

    public function topic(): BelongsTo
    {
        return $this->belongsTo(Topic::class, 'topic_id');
    }

    public function sender(): BelongsTo
    {
        return $this->belongsTo(User::class, 'sender_id', 'id');
    }

    /**
     * Relationship: Get all exclusion records for this message.
     */
    public function exclusions(): \Illuminate\Database\Eloquent\Relations\HasMany
    {
        // Points to MessageExclusion model using custom msg_id primary key
        return $this->hasMany(MessageExclusion::class, 'msg_id', 'msg_id');
    }
    public function parent(): BelongsTo
    {
        return $this->belongsTo(
            Message::class,
            'parent_msg_id',
            'msg_id'
        );
    }

    public function replies(): HasMany
    {
            return $this->hasMany(
                Message::class,
                'parent_msg_id',
                'msg_id'
            )->with([
                'sender',
                'replies'
            ]);
    }
    public function votes(): HasMany
    {
        return $this->hasMany(
            MessageVote::class,
            'msg_id',
            'msg_id'
        );
    }
}
