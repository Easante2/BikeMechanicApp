<?php

namespace App\Http\Controllers;

use App\Http\Requests;
use Illuminate\Http\Request;

class HomeController extends Controller
{
    /**
     * Create a new controller instance.
     *
     * @return void
     */
    public function __construct()
    {
        //$this->middleware('auth');
    }

    /**
     * Show the application dashboard.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        return view('home');
    }


    public function uploadsImage(Request $request){

         $file = $request->get('image');

              $rules = array('file' => 'required'); //'required|mimes:png,gif,jpeg,txt,pdf,doc'
              $validator = Validator::make(array('file'=> $file), $rules);
              
              if($validator->passes()){

                $destinationPath = 'uploads';
                $filename = rand(9223232, 152322221121) . time() .'.'.$file->getClientOriginalExtension();
                $upload_success = $file->move($destinationPath, $filename);

                $img = new \App\Bike();
                $img->imageUrl  = 'uploads/'.$filename;
              
               $img->save();
                
              }
                
    }
}
