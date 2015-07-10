package com.integraresoftware.android.odemsaprotocols.calculators;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.integraresoftware.android.odemsaprotocols.R;

/**
 * This is the brains of the weight converter calculator
 * It will automatically convert the weight after on focus lost
 */
public class weight_converterFragment extends Fragment {

    public weight_converterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weight_converter, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}
