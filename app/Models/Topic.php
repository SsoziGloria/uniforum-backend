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
  protected $fillable = ['group_id', 'title', 'ml_category', 'created_by', 'created_at'];

     public function group(): BelongsTo
     {
         return $this->belongsTo(Group::class, 'group_id', 'group_id');
     }

     public function messages(): HasMany
     {
         return $this->hasMany(Message::class, 'topic_id', 'topic_id');
     }
}
