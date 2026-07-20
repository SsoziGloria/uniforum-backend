<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class UserWarning extends Model
{
    protected $table = 'user_warnings';
    protected $primaryKey = 'warning_id';
    public $timestamps = false;
    protected $fillable = ['user_id', 'group_id', 'issued_by', 'warning_reason', 'issued_at'];

    public function flaggedUser(): BelongsTo
    {
        return $this->belongsTo(User::class, 'user_id', 'user_id');
    }

    public function group(): BelongsTo
    {
        return $this->belongsTo(Group::class, 'group_id', 'group_id');
    }
}
