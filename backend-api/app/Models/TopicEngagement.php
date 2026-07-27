<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class TopicEngagement extends Model
{
    public $timestamps = false; // only created_at is used, set via useCurrent() in the migration

    protected $fillable = ['user_id', 'topic_id'];

    public function user()
    {
        return $this->belongsTo(User::class, 'user_id', 'id');
    }

    public function topic()
    {
        // Topic's primary key is topic_id, not id — must specify both keys explicitly.
        return $this->belongsTo(Topic::class, 'topic_id', 'topic_id');
    }
}
