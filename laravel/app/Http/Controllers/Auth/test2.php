<?php


namespace App\Http\Controllers\Auth;
use App\User;
use Validator;
use App\Http\Controllers\Controller;
use App\Http\Requests;
use DB;

class test2 extends Controller{
    public function tapi(){
		return 'Hi Emmanuel, Here is the dat that you requested from Intellij';
		
		
		//so when you start making you intellij project, sent a GET request to http://159.203.93.1/test and print 
		//the response out to see if you get this message 'Hi Emmanuel, Here is the dat that you requested from Intellij';
	}
	
	
	
	
	
public function register(Request $request)
  {


      //validate input 
      $validator = Validator::make($request->all(), [
            'name' => 'required|max:255',
            'email' => 'required|email|max:255|unique:users',
            'password' => 'required|confirmed|min:6',
        ]);


        if ($validator->fails()) {

          //return error 
          return $data = Response::json(['data' => $validator->messages()], 500);
          
        }else{

          //create new user 
          $newUser =  \App\User::create([
            'name' => $request['name'],
            'email' => $request['email'],
            'password' => bcrypt($request['password']),
          ]);
        }

        //successful registere
        return $data = ['data' => 'success']; 
      
  }
	
	
	
	
	
	
	
	public function getUserWithId($id){
		//$users = DB::table('users')->get();
		$users = DB::table('users')->where('id', $id)->first();
		
		return json_encode($users);
		
	}
	
	public function getUser(){
		$users = DB::table('users')->get();
		
		return json_encode($users, JSON_PRETTY_PRINT) ;
		
	}
	
	public function getUserWithName(){
		//$users = DB::table('users')->get();
		
		$name = Request::input('name');
		
		$users = DB::table('users')->where('name', $name)->first();
		
		return json_encode($users);
		
	}
	
	
	
	
	public function createUser(){
		
	 DB::table('users')->insertGetId(
    array('email' => 'john@example.com', 'name' => 'John', 'surname' => 'Williams')
);

return 'Executed';
	}
}