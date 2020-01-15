<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateTableBikes extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
          Schema::create('bikes', function (Blueprint $table) {
            $table->increments('id');
            $table->string('make');
            $table->string('model');
            $table->string('year');
            $table->string('changes_since_new');
            $table->string('tyre_size');
            $table->string('no_of_front_cogs');
            $table->string('no_of_rear_cogs');
            $table->string('no_of_gears');
			$table->string('type_of_brakes');
			$table->integer('user_id')->unsigned()->index();
			$table->foreign('user_id')->references('id')->on('users');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        //
    }
}
