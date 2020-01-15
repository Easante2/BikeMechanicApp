<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Bike extends Model
{
     /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $fillable = [
        'make', 'model', 'year', 'changes_since_new', 'tyre_size', 'no_of_front_cogs', 'no_of_rear_cogs', 'no_of_gears', 'brake_type',
		'user_id'
    ];
	
	
}
