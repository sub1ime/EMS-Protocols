package com.integraresoftware.emsbuddy.calculators;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.integraresoftware.android.emsbuddy.R;

import java.util.HashMap;


public class ActivityCalculatorList extends ListActivity {

    public static final String TAG = "ActivityCalculatorList";

    // list of available calculators that will be displayed
    final private String[] calcList = { "Dopamine Calculator" };
    final private String[] calcListClass = { "com.integraresoftware.emsbuddy.calculators.ActivityDopamineCalculator" };
    private HashMap<Integer, String> mIdMap = new HashMap<Integer, String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (int i = 0; i < calcListClass.length; ++i) {
            mIdMap.put(i, calcListClass[i]);
        }

        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this,
                R.layout.calculator_list_activity,
                R.id.calc_textview,
                calcList);

        setListAdapter(mAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent i = new Intent();
        i.setClassName(this, mIdMap.get(position));
        startActivity(i);
    }
}
