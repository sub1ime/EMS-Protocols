package com.integraresoftware.android.odemsaprotocols;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

public class ActivityDisplayList extends ActionBarActivity {
	
	public static final String TAG = "DisplayProtocolActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set the view for the activity, in this case, the fragment layout
		Log.d(TAG, "onCreate()");
		
		setContentView(R.layout.display_list_fragment);


	}
}
