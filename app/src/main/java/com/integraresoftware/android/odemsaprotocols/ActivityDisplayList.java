package com.integraresoftware.android.odemsaprotocols;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

/*
This activity calls a XML layout that will call a fragment
Started from:
    MainActivity.class
Leads to:
    FragmentDisplayList.class
 */
public class ActivityDisplayList extends ActionBarActivity {
	// give a name to this file for debugging
	public static final String TAG = "DisplayProtocolActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set the view for the activity, in this case, the fragment layout
		Log.d(TAG, "onCreate()");
		
		setContentView(R.layout.display_list_fragment);


	}
}
