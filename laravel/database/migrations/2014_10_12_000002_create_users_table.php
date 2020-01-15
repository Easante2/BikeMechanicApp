<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateUsersTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
         Schema::create('users', function (Blueprint $table) {
            $table->increments('id');
            $table->string('name');
            $table->string('surname');
            $table->string('username');
            $table->string('password', 60);
            $table->string('address_line_1');
            $table->string('address_line_2');
            $table->string('address_line_3');
            $table->string('post_code');
            $table->string('email')->unique();
            $table->rememberToken();
            $table->timestamps();
            $table->smallInteger('isAdmin');
        });
    }
	
	

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::drop('users');
    }
}
