<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Database\Eloquent\Relations\HasMany;


class Group extends Model
{
    protected $table = 'groups';
    protected $primaryKey = 'group_id';
    protected $fillable = ['group_name', 'description', 'created_by'];

    //ID is an auto-incrementing integer
    public $incrementing = true;
    protected $keyType = 'int';


    public function creator(): BelongsTo
    {
      return $this->belongsTo(User::class, 'created_by', 'user_id');
    }

    public function topics():HasMany
    {
       return $this->hasMany(Topic::class, 'group_id', 'group_id');
    }

    public function members()
    {
        //links Groups to Users using group_members table
        return $this->belongsToMany(User::class, 'group_members', 'group_id', 'user_id');

    }
}
