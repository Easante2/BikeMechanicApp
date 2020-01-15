<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class Appointments extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('appointments', function (Blueprint $table) {
     $table->increments('id');
         $table->date('appointment_date');
         $table->time('appointment_time');
         $table->smallInteger('confirm_appointment');
         $table->smallInteger('notify_user');
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
       Schema::drop('appointments');
    }
}
