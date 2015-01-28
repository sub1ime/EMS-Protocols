package com.integraresoftware.android.odemsaprotocols.calculators;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import com.integraresoftware.android.odemsaprotocols.R;


public class ActivityDopamineCalculator extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_protocol_fragment);

        Fragment f = getSupportFragmentManager().findFragmentByTag(com.integraresoftware.android.odemsaprotocols.calculators.FragmentDopamineCalculator.TAG);
        if (f == null) {
            f = new FragmentDopamineCalculator();

            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            t.add(R.id.display_protocol_container, f, FragmentDopamineCalculator.TAG);
            t.commit();
        }

    }
}
