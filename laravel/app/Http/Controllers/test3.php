<?php

namespace App\Http\Controllers;

use Request;
use App\Http\Requests;
use App\Http\Controllers\Controller;
use DB;
use App\User;

class test3 extends Controller
{
    public function testPhoto(){
		//return 'Hi Emmanuel, Here is the dat that you requested from Intellij';
		
		if(Request::hasFile('photo')){
			try{
			$photo = Request::file('photo');
			$title = "photo4";
			$destinationPath = public_path() . '/uploads/';
			$photo->move($destinationPath,$title. '.' . $photo->getExtension());
			$reply = "Yess COmplete";
			 print_r($reply);
			}catch (Exception $e){
        $reply = $e;
		print_r($reply);
    }
		}else {
            $reply = 'File Not Found';
			print_r($reply);
        }
	 print_r($reply);

		//so when you start making you intellij project, sent a GET request to http://159.203.93.1/test and print 
		//the response out to see if you get this message 'Hi Emmanuel, Here is the dat that you requested from Intellij';
	}
	
	public function createUser(){
		
	 DB::table('users')->insertGetId(
    array('email' => 'john@example.com', 'name' => 'John', 'surname' => 'Williams')
);

return 'Executed';
	}
	
		public function getUserWithName($name){
			
		//$users = DB::table('users')->get();
		try{
		//$name = Request::input('name');
		
		// $newUser =  \App\User::create([
          //  'name' => $request['name'],
          //  'email' => $request['email'],
           // 'password' => bcrypt($request['password']),
			//'surname'=>$request['surname'],
        //  ]);
		
		$users = DB::table('users')->where('name', $name)->first();
		
		return json_encode($users);
		}
		catch(Exception $e){
			return response()->json(['error' => 'could_not_get'], 500);
		}
	}
}