<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Request;
use App\Http\Requests;
use App\Http\Controllers\Controller;
use DB;
use App\User;
use App\Bike;

class MainController extends Controller
{
   	//making appointment
  public function makeAppointment(Request $req){

    

  	//get logged in user 
   	$user = JWTAuth::parseToken()->authenticate();

  	//get logged in users id
 	$userId = $user->id;

 	//get data from request
   	$title = $req->get('title');

  	//create appointment object
  	$appointment = new Appointment;
   	$appointment->title = $title;
    $appointment->save();





  	return ['data' => 'success'];
  }
}
