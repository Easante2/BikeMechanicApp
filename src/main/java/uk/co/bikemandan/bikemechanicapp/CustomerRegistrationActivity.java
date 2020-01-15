package uk.co.bikemandan.bikemechanicapp;

import android.support.v4.app.Fragment;

import uk.co.bikemandan.bikemechanicapp.Fragments.CustomerRegistrationFragment;

public class CustomerRegistrationActivity extends SingleFragmentActivity {




    public  Fragment createFragment() {
        return new CustomerRegistrationFragment();
    }
/*
    //onclick listener {
         String placesRequest = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                    latitude + "," + longitude + "&radius=500&key=" + placesKey;
            PlacesReadFeed process = new PlacesReadFeed();
            process.execute(new String[] {placesRequest});
//}
*/
}
