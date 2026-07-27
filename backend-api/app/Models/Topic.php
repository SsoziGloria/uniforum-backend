<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Database\Eloquent\Relations\HasMany;

class Topic extends Model
{
  protected $table = 'topics';
  protected $primaryKey = 'topic_id';
  public $timestamps = false;
  protected $fillable = ['group_id', 'title','description', 'ml_category', 'created_by', 'created_at'];
  protected $casts = ['created_at' => 'datetime'];


    protected static function boot()
    {
        parent::boot();

        static::deleting(function ($topic) {
            $topic->messages()->get()->each(function ($message) {
                $message->delete();
            });
        });
    }
    public function group(): BelongsTo
    {
         return $this->belongsTo(Group::class, 'group_id', 'group_id');
         
    }
     public function creator(): BelongsTo
    {
        return $this->belongsTo(User::class, 'created_by', 'id');
    }
     public function acceptedAnswer(): BelongsTo
    {
        return $this->belongsTo(
            Message::class,
           'accepted_msg_id',
           'msg_id'
        );
    }


     public function messages(): HasMany
     {
         return $this->hasMany(Message::class, 'topic_id', 'topic_id');
     }

     public function questions()
    {
       return $this->messages()
          ->whereNull('parent_msg_id');
    }
}
