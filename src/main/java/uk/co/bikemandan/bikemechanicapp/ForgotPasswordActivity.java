package uk.co.bikemandan.bikemechanicapp;

import android.support.v4.app.Fragment;

import uk.co.bikemandan.bikemechanicapp.Fragments.ForgotPasswordFragment;

/**
 * Created by Emmanuel on 20/04/2016.
 */
public class ForgotPasswordActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new ForgotPasswordFragment();
    }
}
