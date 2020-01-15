
Appointment request sent from : <b>{{ $firstname }} {{ $surname}}</b> 

<p><b> Customer details </b> <br/>
address : {{ $address1 }}, {{ $address2 }}, {{ $address3 }}, {{ $post_code }}<br/>
email : {{ $email }}<br/>
email : {{ $contact }}</p>


<p><b> Bike details :</b> <br/>

make : {{ $make }}<br/>
model : {{ $model }} <br/>
year : {{ $year }} <br/>
changes since new : {{ $changes }} <br/>
tyre size : {{ $tyre_size }} <br/>
number of front cogs : {{ $front_cogs }} <br/>
number of rear cogs : {{ $rear_cogs }} <br/>
number of gears : {{ $gears }} <br/>
brake type : {{ $brake }} <br/></p>

<p><b> Appointment Details : </b><br/>
problem : {{ $problem }}<br/>
bike part fault : {{ $bike_part }}<br/>
problem description : {{ $description }}<br/>
appointment request dates : <br/>
@foreach ({{ appointments }} as $value)
                    $value<br/>
                @endforeach
</p>

<p> Login to the app to confirm the appointment date </p>			

