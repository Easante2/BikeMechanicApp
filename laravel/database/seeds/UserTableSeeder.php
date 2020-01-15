<?php
use Illuminate\Database\Seeder;
use App\User as User;
  
class UserTableSeeder extends Seeder {
  
    public function run() {
        User::truncate();
  
        User::create( [
            'email' => 'sheepy85@test.com' ,
            'password' => Hash::make( 'password' ) ,
            'name' => 'sheepy85' ,
        ] );
    }
}