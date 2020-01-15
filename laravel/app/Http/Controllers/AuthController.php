<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Request;
use App\Http\Requests;
use App\Http\Controllers\Controller;
use DB;
use App\User;

class AuthController extends Controller
{
   public function register(Request $request)
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
		  $name = Request::input('name');
		try{
          //create new user 
          $newUser =  \App\User::create([
            'name' => Request::input('name'),
            'email' => $request['email'],
            'password' => bcrypt($request['password']),
			
          ]);
		  
		  $id = $newUser->id;
		  
		  $id = DB::table('users')->where('name', $name)->where('surname', $surname)->value('id');
		  
		  
		  
		  
        }

        //successful registere
        return $data = ['data' => 'success']; 
      
  }
}
