package com.integraresoftware.emsbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.integraresoftware.android.emsbuddy.R;
import com.integraresoftware.emsbuddy.calculators.ActivityCalculatorList;
import com.integraresoftware.emsbuddy.data.SectionContract;
import com.integraresoftware.emsbuddy.data.SubsectionContract;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        setAnnouncements();
	}
	
	public void startProtocol(View v) {
		Intent i = new Intent(this, ActivityDisplayList.class);
		startActivity(i);
	}
	
	public void startNewProtocolList(View v){
		Intent i = new Intent(this, ActivityDisplayList.class);
		startActivity(i);
	}
	
	public void startCalc(View v) {
		Intent i = new Intent(this, ActivityCalculatorList.class);
		startActivity(i);
	}
	
	public void startProcedure(View v) {
		Intent i = new Intent(this, ListDrugsActivity.class);
		startActivity(i);
	}
	
	public void startAppendices(View v) {
		
	}
	
	public void startAdministration(View v) {
		
	}
	
	public void startPurpose(View v) {
		
	}

    public void shortcutToChestDiscomfort(View v) {
        int id = 9;

        Intent i = new Intent(this, ActivityDisplaySection.class);
        i.putExtra(SubsectionContract.ROW_ID, Long.valueOf(id));
        i.putExtra(SubsectionContract.COL_TITLE, "Non-Traumatic Chest Discomfort");
        i.putExtra(SectionContract.COL_COLOR, -6615281);

        startActivity(i);
    }

    public void shortcutToAcs(View v) {
        int id = 10;

        Intent i = new Intent(this, ActivityDisplaySection.class);
        i.putExtra(SubsectionContract.ROW_ID, Long.valueOf(id));
        i.putExtra(SubsectionContract.COL_TITLE, "Acute Coronary Syndrome");
        i.putExtra(SectionContract.COL_COLOR, -6615281);

        startActivity(i);
    }

	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 * ------------------------------------------------------------------------------------------------------------------
	 * Menus setup
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    public void setAnnouncements() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.main_linearlayout);

        TextView tv1 = new TextView(this);
		tv1.setText(Html.fromHtml("<b>This app is not finished</b><br><br>All of the patient care protocols are finished, including the 'Management' sections.<br><br>I still need to proofread these protocols. If you come across gramatical errors that are different from the gramatical errors in the protocols, please drop me an email at burrusscl@gmail.com or in the 'EmsBuddy' group on groups.google.com.<br><br>Also, please submit bug reports if it gives you the option."));

        ll.addView(tv1);


    }

}
