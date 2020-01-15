<?php

namespace App\Http\Controllers;


use Request;
use App\Http\Requests;
use App\Http\Controllers\Controller;
use DB;
use App\User;
use App\Bike;
use Mail;

class AuthenCtrl extends Controller{
   public function registerUser()
  {

      //validate input 
   //   $validator = Validator::make($request->all(), [
        //    'name' => 'required|max:255',
         //   'email' => 'required|email|max:255|unique:users',
        //    'password' => 'required|confirmed|min:6',
       // ]);


      //  if ($validator->fails()) {

          //return error 
        //  return $data = Response::json(['data' => $validator->messages()], 500);
          
      //  }else{
		 
		try{
          //create new user 
          $newUser =  \App\User::create([
            'name' => Request::input('firstname'),
			'surname' => Request::input('lastname'),
			'username' => Request::input('username'),
			'name' => Request::input('firstname'),
            'email' => Request::input('email'),
            'password' => bcrypt(Request::input('pword')),
			'address_line_1' => Request::input('adL1'),
			'address_line_2' => Request::input('adL2'),
			'address_line_3' => Request::input('adL3'),
			'post_code' => Request::input('post_code'),
			'contact_number' => Request::input('contact_num'),
			'isAdmin' => 0
          ]);
		  
		  $id = $newUser->id;
		  $name = $newUser->name;
		  $email = $newUser->email;
		    $newBike =  \App\Bike::create([
            'make' => Request::input('make'),
			'model' => Request::input('model'),
			'year' => Request::input('year'),
			'changes_since_new' => Request::input('changes'),
            'no_of_gears' => Request::input('gears'),
			'no_of_front_cogs' => Request::input('front_cogs'),
			'no_of_rear_cogs' => Request::input('rear_cogs'),
			'brake_type' => Request::input('brake_type'),
			'tyre_size' => Request::input('tyre_size'),
			'user_id' => $id,
		
          ]);
		  
		  
		  Mail::send('emails.registerConfirm', ['firstname' => $name], function ($message) {
			$message->to($email);
			$message->from('bicyclemechanicapp16@outlook.com', 'Bicycle Mechanic App');
			$message->subject('Registration');
			});
		  
		  
		   $data = ['response' => 'success']; 
		
		return json_encode($data);
      
		  //$id = DB::table('users')->where('name', $name)->where('surname', $surname)->value('id');
		  
        }catch (Exception $e){
        $reply = $e;
		print_r($reply);
		$response_data = ['response' => 'failed',
							'error' => $e];
		 return json_encode($response_data); 
    }

        //successful registere
         $data = ['response' => 'success']; 
		
		return json_encode($data);
      
  }
  
  
  public function testEmail(){
	  
	  $emailMSG = '';
	  
	  
	//  Mail::raw('Hi, welcome user!', function ($message) {
	//		$message->to('e.asante@hotmail.com');
	//		$message->from('bicyclemechanicapp@gmail.com');
	//		$message->subject('Test');
       //});

		Mail::send('emails.registerConfirm', ['firstname' => 'Joe'], function ($message) {
			$message->to('e.asante@hotmail.com');
			$message->from('bicyclemechanicapp@outlook.com');
			$message->subject('Test2');
});

 $data = ['response' => 'success']; 
		
		return json_encode($data);
	  
  }
  
  
  public function forgotPassword(){
	  try{
	  $username = Request::input('username');
	  $password = bcrypt(Request::input('pword'));
	  
	  DB::table('users')
            ->where('username', $username)
            ->update(['password' => $password]);
			
			$data = ['response' => 'success']; 
		
		return json_encode($data);
	  }catch (Exception $e){
        $reply = $e;
		print_r($reply);
		$response_data = ['response' => 'failed',
							'error' => $e];
		 return json_encode($response_data); 
  }
}
