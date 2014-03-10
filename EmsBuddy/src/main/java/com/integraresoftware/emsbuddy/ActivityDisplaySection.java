package com.integraresoftware.emsbuddy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.integraresoftware.android.emsbuddy.R;
import com.integraresoftware.emsbuddy.data.SectionContract;
import com.integraresoftware.emsbuddy.data.SubsectionContract;

public class ActivityDisplaySection extends ActionBarActivity {
	
	private static final String TAG = "ActivityDisplaySection";
    private int lastSelected;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.display_protocol_fragment);
		
		Fragment f = getSupportFragmentManager().findFragmentByTag(FragmentDisplaySection.TAG);
		Log.d(TAG, "onCreate(): f= " + f);
		
		if (f == null) {
			f = new FragmentDisplaySection();
			Bundle args = new Bundle();
			args.putLong(SubsectionContract.ROW_ID, getIntent().getLongExtra(SubsectionContract.ROW_ID, 0L));
			args.putString(SubsectionContract.COL_TITLE, getIntent().getStringExtra(SubsectionContract.COL_TITLE));
			args.putInt(SectionContract.COL_COLOR, getIntent().getIntExtra(SectionContract.COL_COLOR, 0));
			Log.d(TAG, "args= " + args);
			f.setArguments(args);
			
			FragmentTransaction t = getSupportFragmentManager().beginTransaction();
			t.add(R.id.display_protocol_container, f, FragmentDisplaySection.TAG);
			t.commit();
		}
	}

}
