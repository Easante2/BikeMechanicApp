package uk.co.bikemandan.bikemechanicapp;

import android.support.v4.app.Fragment;

import uk.co.bikemandan.bikemechanicapp.Fragments.BikeRegistrationFragment;

public class BikeRegistrationActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new BikeRegistrationFragment();
    }



}
