<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class MessageVote extends Model
{
    protected $table = 'message_votes';

    protected $primaryKey = 'vote_id';

    protected $fillable = [
        'msg_id',
        'user_id'
    ];

    public function message()
    {
        return $this->belongsTo(
            Message::class,
            'msg_id',
            'msg_id'
        );
    }

    public function user()
    {
        return $this->belongsTo(
            User::class,
            'user_id'
        );
    }
}