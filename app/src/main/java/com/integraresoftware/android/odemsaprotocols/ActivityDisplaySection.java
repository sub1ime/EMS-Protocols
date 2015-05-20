package com.integraresoftware.android.odemsaprotocols;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;

import com.integraresoftware.android.odemsaprotocols.data.SectionContract;
import com.integraresoftware.android.odemsaprotocols.data.SubsectionContract;

public class ActivityDisplaySection extends ActionBarActivity {
	
	private static final String TAG = "ActivityDisplaySection";
    private int lastSelected;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.display_protocol_fragment);
        lastSelected = getIntent().getIntExtra(FragmentDisplayList.ELV_SELECTED, -1);
		
		Fragment f = getSupportFragmentManager().findFragmentByTag(com.integraresoftware.android.odemsaprotocols.FragmentDisplaySection.TAG);
		Log.d(TAG, "onCreate(): f= " + f);
		
		if (f == null) {
			f = new com.integraresoftware.android.odemsaprotocols.FragmentDisplaySection();
			Bundle args = new Bundle();
			args.putLong(SubsectionContract.ROW_ID, getIntent().getLongExtra(SubsectionContract.ROW_ID, 0L));
			args.putString(SubsectionContract.COL_TITLE, getIntent().getStringExtra(SubsectionContract.COL_TITLE));
			args.putInt(SectionContract.COL_COLOR, getIntent().getIntExtra(SectionContract.COL_COLOR, 0));
			Log.d(TAG, "args= " + args);
			f.setArguments(args);
			
			FragmentTransaction t = getSupportFragmentManager().beginTransaction();
			t.add(R.id.display_protocol_container, f, com.integraresoftware.android.odemsaprotocols.FragmentDisplaySection.TAG);
			t.commit();
		}
	}


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(this, ActivityDisplayList.class);
                i.putExtra(FragmentDisplayList.ELV_SELECTED, lastSelected);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
