<?php

use \App\User as User;
/*
|--------------------------------------------------------------------------
| Routes File
|--------------------------------------------------------------------------
|
| Here is where you will register all of the routes in an application.
| It's a breeze. Simply tell Laravel the URIs it should respond to
| and give it the controller to call when that URI is requested.
|
*/
/*
Route::get('/', function () {
    return view('welcome');
});

Route::get('makeadmin', function () {
    
	$user = User::find(3);
	$user->isAdmin = 1;
	$user->save();
	
});
*/
/*
|--------------------------------------------------------------------------
| Application Routes
|--------------------------------------------------------------------------
|
| This route group applies the "web" middleware group to every route
| it contains. The "web" middleware group is defined in your HTTP
| kernel and includes session state, CSRF protection, and more.
|
*/

Route::get('/json', function () {
    return 'welcome';
});

Route::get('/about', function () {
    return view('pages.about');
});


Route::group(['middleware' => ['api']], function () {
    Route::get('/allusers', 'HomeController@getAllUsers');
	Route::post('/getUserWithName', 'test3@getUserWithName');
	Route::post('/registerUser', 'AuthenCtrl@registerUser');
	Route::get('/testEmail', 'AuthenCtrl@TestEmail');
	Route::post('/forgotPassword', 'AuthenCtrl@forgotPassword');
	
	
});

Route::group(['middleware' => 'web'], function () {
    //Route::auth();
	//when we pass in the parameter through the url i.e. http://159.203.93.1/allusers
	//goes to the HomeCOntroller and executes the @getAllUsers method 
	Route::get('/home', 'HomeController@index');
	Route::get('/allusers', 'HomeController@getAllUsers');
	//when we pass in the parameter through the url i.e. http://159.203.93.1/update/user/{param} 
	//the route directs it to the HomeController and executes the @updateUser method 
	Route::get('/update/user/{name}', 'HomeController@updateUser');
	Route::get('/test1', 'HomeController@test1');
	Route::get('/test3', 'test3@tapi');
	Route::get('/test2', 'Auth\test2@tapi');
	Route::get('/getId/{id}', 'Auth\test2@getUserWithId');
	Route::get('/getUser', 'Auth\test2@getUser');
	Route::post('/validateI', 'Auth\test2@validateI');
	Route::post('/testPhoto', 'test3@testPhoto');
	Route::get('/createUser', 'test3@createUser');
	
	//Route::get
	
	
});

Route::group(['middleware' => 'web'], function () {
    Route::auth();

    Route::get('/home', 'HomeController@index');
});

Route::group(['middleware' => 'web'], function () {
    Route::auth();

    Route::get('/home', 'HomeController@index');
});
