package com.integraresoftware.emsbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.integraresoftware.android.emsbuddy.R;
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
		Intent i = new Intent(this, SelectCalcActivity.class);
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

        TextView tv = new TextView(this);
        tv.setText("Be advised, this app is not finished. The 'Purpose' section is mostly complete.\n\n I'm Currently working on the 'Management' section of the protocols. The only protocols that actually work are Adult Cardiovascular Emergencies.\n\nIt may not look like much but this app is mostly done.\n\nBe advised this app will update a lot as I add new features and tweak old ones. Anyone who wants to help, may help. You don't have to know how to program for some of the tasks I have left to do.");
        ll.addView(tv);

        TextView tv1 = new TextView(this);
        tv1.setText("\n\n1/19/14\nThe \"Management\" section is complete. The only management data complete is in Adult Cardiovascular Emergencies. All of the others will be blank.");

        ll.addView(tv1);
    }

}
