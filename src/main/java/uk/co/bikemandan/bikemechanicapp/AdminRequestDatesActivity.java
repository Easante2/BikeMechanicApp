package uk.co.bikemandan.bikemechanicapp;

import android.support.v4.app.Fragment;

import uk.co.bikemandan.bikemechanicapp.Fragments.AdminRequestDatesFragment;

/**
 * Created by Emmanuel on 27/04/2016.
 */
public class AdminRequestDatesActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new AdminRequestDatesFragment();
    }
}
