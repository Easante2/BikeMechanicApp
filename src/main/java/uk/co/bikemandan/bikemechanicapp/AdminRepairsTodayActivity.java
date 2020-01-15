package uk.co.bikemandan.bikemechanicapp;

import android.support.v4.app.Fragment;

import uk.co.bikemandan.bikemechanicapp.Fragments.AdminRepairsTodayFragment;

/**
 * Created by Emmanuel on 28/04/2016.
 */
public class AdminRepairsTodayActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return new AdminRepairsTodayFragment();
    }
}
