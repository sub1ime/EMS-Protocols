package com.integraresoftware.android.odemsaprotocols.calculators;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.integraresoftware.android.odemsaprotocols.R;

/**
 * This is the brains of the weight converter calculator
 * It will automatically convert the weight after on focus lost
 */
public class weight_converterFragment extends Fragment implements View.OnTouchListener {
    // activity
    private Activity activity;

    private String lbs_string, kg_string;

    // which edittext had focus last
    public enum lastFocus {
        POUNDS,
        KILOGRAMS
    }
    private lastFocus mIntent;


    public weight_converterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weight_converter, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        activity = getActivity();

        // set calc button action
        Button calc_button = (Button) view.findViewById(R.id.weight_calculate);
        calc_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                calculate(v);
            }
        });
        // get references to text boxes
        EditText kg_data = (EditText) view.findViewById(R.id.kg_edittext);
        EditText lbs_data = (EditText) view.findViewById(R.id.lbs_editText);

        // set change listeners to text boxes to see the user's intent
        // if the user just left a certain box, then he/she would like the other
        kg_data.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mIntent = lastFocus.KILOGRAMS;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        lbs_data.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mIntent = lastFocus.POUNDS;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    // what to do when the calculate button is pressed
    private void calculate(View v) {
        double kg_number, lbs_number;
        double answer;

        // find references to the views and then check for emptiness
        EditText kg_data = (EditText) activity.findViewById(R.id.kg_edittext);
        EditText lbs_data = (EditText) activity.findViewById(R.id.lbs_editText);
        if (!check_numbers(lbs_data, kg_data)) return;

        // get the text out of the edittext boxes
        kg_string = kg_data.getText().toString();
        lbs_string = lbs_data.getText().toString();

//TODO try to get all of these tests in the same function for simplicity
        // check for correct flag toggle
        if (!check_numbers(kg_string, lbs_string)) return;

        // convert to double if not null
        if (mIntent == lastFocus.POUNDS) { // means we want to convert from lbs to kg
            lbs_number = Double.parseDouble(lbs_string);
            if(!check_numbers(lbs_number)) {
                return;
            }
            answer = lbs_number / 2.2;
        } else { // meants we want to convert from kg to lbs
            kg_number = Double.parseDouble(kg_string);
            if (!check_numbers(kg_number)) {
                return;
            }
            answer = kg_number * 2.2;
        }

        String answerString = String.format("%.2f", answer);

        // display answer
        // send to the proper box
        if (mIntent == lastFocus.KILOGRAMS) {
            lbs_data.setText(answerString);
        } else if (mIntent == lastFocus.POUNDS) {
            kg_data.setText(answerString);
        }
    }

    // check the data to make sure they are + numbers, real Numbers, and not too large
    // returns true if data is valid
    private boolean check_numbers(Double number) {
        if (number < 0) {
            Toast.makeText(activity, "Please enter a positive number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (number > 2000) {
            Toast.makeText(activity, "Please enter a number under 2000 lbs or 910 kg", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean check_numbers(EditText kg_data, EditText lbs_data) {

        // are both empty?
        if (kg_data.getText().toString().length() == 0 && lbs_data.getText().toString().length() == 0) {
            kg_data.setError("Enter a number in at least one field.");
            lbs_data.setError("Enter a number in at least one field.");
            return false;
        }


        return true;
    }

    // check Strings before they are parsed
    private boolean check_numbers(String kg_string, String lbs_string) {
        // check for the last focus box to be empty and change mIntent if it is
        if (mIntent == lastFocus.POUNDS && lbs_string.length() == 0) {
            mIntent = lastFocus.KILOGRAMS;
            return true;
        } else if (mIntent == lastFocus.KILOGRAMS && kg_string.length() == 0) {
            mIntent = lastFocus.POUNDS;
            return true;
        }

        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
