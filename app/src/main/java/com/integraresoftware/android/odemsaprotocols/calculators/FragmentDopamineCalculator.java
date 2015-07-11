package com.integraresoftware.android.odemsaprotocols.calculators;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.integraresoftware.android.odemsaprotocols.R;
import com.integraresoftware.android.odemsaprotocols.objects.CalculateDopamineDrip;


public class FragmentDopamineCalculator extends Fragment implements View.OnTouchListener {

    public static final String TAG = "FragmentDopamineCalc";

    private Activity activity;

    private ScrollView getScrollView;
    private Spinner inputDopDosage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dopamine_calculator, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        activity = getActivity();

        // set spinner to '5' as default dose
        inputDopDosage = (Spinner) view.findViewById(R.id.spinner_dosage);
        inputDopDosage.setSelection(4);

        // set listeners for buttons
        Button calcButton = (Button) view.findViewById(R.id.button_calculate);
        calcButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                calculate(v);
            }
        });
        Button addButton = (Button) view.findViewById(R.id.button_add_dosage);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(inputDopDosage.getSelectedItemPosition() != 19) {
                    inputDopDosage.setSelection(inputDopDosage.getSelectedItemPosition() + 1);
                }
            }
        });
        Button minusButton = (Button) view.findViewById(R.id.button_minus_dosage);
        minusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(inputDopDosage.getSelectedItemPosition() != 0) {
                    inputDopDosage.setSelection(inputDopDosage.getSelectedItemPosition() - 1);
                }
            }
        });

        // hide soft keyboard when fragment starts
        activity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // set scrollview and listener
        getScrollView = (ScrollView) view.findViewById(R.id.dope_scrollview);
        getScrollView.setOnTouchListener(this);
    }

    public void calculate(View v) {
        double weight, kgWeight, concDopeMcg, concSalineMl;
        int dripset, dopamineDosage;

        // get a reference to all of the input boxes
        // and check for empties
        int emptyOne = 0;
        EditText inputWeight = (EditText) activity.findViewById(R.id.input_pt_weight);
        if (!editTextEmpty(inputWeight)) emptyOne += 1;

        EditText inputConcDopMcg = (EditText) activity.findViewById(R.id.input_dope_amount);
        if (!editTextEmpty(inputConcDopMcg)) emptyOne += 1;

        EditText inputConcSalmL = (EditText) activity.findViewById(R.id.input_saline_amount);
        if (!editTextEmpty(inputConcSalmL)) emptyOne += 1;

        EditText inputDripset = (EditText) activity.findViewById(R.id.input_dripset);
        if (!editTextEmpty(inputDripset)) emptyOne += 1;
        //if one or more boxes were empty, stop the calculation
        if (emptyOne > 0) return;

        inputDopDosage = (Spinner) activity.findViewById(R.id.spinner_dosage);
        RadioGroup selectedLbsKg = (RadioGroup) activity.findViewById(R.id.radiogroup);
        RadioButton lbsRadioButton = (RadioButton) activity.findViewById(R.id.lbs);

        // get the data out of the input boxes
        weight = Double.parseDouble(inputWeight.getText().toString());
        concDopeMcg = Double.parseDouble(inputConcDopMcg.getText().toString());
        concSalineMl = Double.parseDouble(inputConcSalmL.getText().toString());
        dopamineDosage = Integer.parseInt(inputDopDosage.getSelectedItem().toString());
        dripset = Integer.parseInt(inputDripset.getText().toString());

        // find the concentration
        double concentration = (concDopeMcg / concSalineMl) * 1000;

        // get true/false for whether kg is selected or not
        // get id of which button is selected
        int selectedRadioButton = selectedLbsKg.getCheckedRadioButtonId();
        // compare selected button id with kg button id, we want the weight in kg
        if (selectedRadioButton == lbsRadioButton.getId()) {
            kgWeight = weight / 2.2;
        } else {
            kgWeight = weight;
        }

        // call the CalculateDopamineDrip object and do the calculation
        CalculateDopamineDrip mCalc = new CalculateDopamineDrip(dopamineDosage, kgWeight, concentration, dripset);
        mCalc.calculateMlPerHour();
        double answerGttsPerMinute = mCalc.calculateDripsPerMinute();
        String answerString = String.format("%.1f gtts/min", answerGttsPerMinute);
        Log.d(TAG, "answer = " + answerString);

        // populate the answer
        TextView answerView = (TextView) activity.findViewById(R.id.txt_answer_ml_per_hour);
        answerView.setText(answerString);
    }

    private boolean editTextEmpty(final EditText inputObject) {
        if (inputObject.getText().toString().length() == 0) {
            Toast.makeText(activity, "Please enter a number", Toast.LENGTH_SHORT).show();
            inputObject.setError("Field cannot be left blank.");
            return false;
        }
        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view == getScrollView) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getScrollView.getWindowToken(), 0);
            return true;
        }

        return false;
    }
}
