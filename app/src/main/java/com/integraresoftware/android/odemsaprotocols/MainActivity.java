package com.integraresoftware.android.odemsaprotocols;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.integraresoftware.android.odemsaprotocols.calculators.ActivityCalculatorList;
import com.integraresoftware.android.odemsaprotocols.data.SectionContract;
import com.integraresoftware.android.odemsaprotocols.data.SubsectionContract;

/*
This is the first activity that is called.
No variable required to start this activity
Summary:
Displays a list of links that go to different parts of the app, as well as display messages in
beta testing.
 */
public class MainActivity extends ActionBarActivity {
    /*
    System Functions required by android are below
     */

    // the first function called by android
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        setAnnouncements();
	}

    // creates an options menu in the top right of the sceen
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	/* ------------------------------------------------------------------------------------------
	Below is a list of funcitons that are linked to different buttons on the menu
	 */
    // Goes to the main portion of the app, a list of topics
	public void startProtocol(View v) {
		Intent i = new Intent(this, ActivityDisplayList.class);
		startActivity(i);
	}

    // Not Currently Used
	public void startNewProtocolList(View v){
		Intent i = new Intent(this, ActivityDisplayList.class);
		startActivity(i);
	}

    // Displays a list of available calculators
	public void startCalc(View v) {
		Intent i = new Intent(this, ActivityCalculatorList.class);
		startActivity(i);
	}

    // Not Currently Used
	public void startProcedure(View v) {
		Intent i = new Intent(this, ListDrugsActivity.class);
		startActivity(i);
	}

    // Not Currently Used
	public void startAppendices(View v) {
		
	}

    // Not Currently Used
	public void startAdministration(View v) {
		
	}

    // Not Currently Used
	public void startPurpose(View v) {
		
	}

    // A shortcut to the chest pain protocol for testing and demonstration
    public void shortcutToChestDiscomfort(View v) {
        int id = 9;

        Intent i = new Intent(this, ActivityDisplaySection.class);
        i.putExtra(SubsectionContract.ROW_ID, Long.valueOf(id));
        i.putExtra(SubsectionContract.COL_TITLE, "Non-Traumatic Chest Discomfort");
        i.putExtra(SectionContract.COL_COLOR, -6615281);

        startActivity(i);
    }

    // A shortcut to ACS protocol for testing and demonstration
    public void shortcutToAcs(View v) {
        int id = 10;

        Intent i = new Intent(this, ActivityDisplaySection.class);
        i.putExtra(SubsectionContract.ROW_ID, Long.valueOf(id));
        i.putExtra(SubsectionContract.COL_TITLE, "Acute Coronary Syndrome");
        i.putExtra(SectionContract.COL_COLOR, -6615281);

        startActivity(i);
    }

    // this is just for testing, it displays text below the buttons for the purpose of
    // messages and announcements
    public void setAnnouncements() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.main_linearlayout);

        TextView tv1 = new TextView(this);
		tv1.setText(Html.fromHtml("<b>This app is not finished</b><br><br>All of the patient care protocols are finished, including the 'Management' sections.<br><br>I still need to proofread these protocols. If you come across gramatical errors that are different from the gramatical errors in the protocols, please drop me an email at burrusscl@gmail.com or in the 'EmsBuddy' group on groups.google.com.<br><br>Also, please submit bug reports if it gives you the option."));

        ll.addView(tv1);


    }

}
