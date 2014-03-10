package com.integraresoftware.emsbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.integraresoftware.android.emsbuddy.R;
import com.integraresoftware.emsbuddy.data.SectionContract;
import com.integraresoftware.emsbuddy.data.SubsectionContract;

public class ActivityDisplaySubsection extends ActionBarActivity {

    private String protocolTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_protocol_fragment);
        protocolTitle = getIntent().getStringExtra(FragmentDisplaySection.PROTOCOL_SUBSECTION_TITLE);

        Fragment f = getSupportFragmentManager().findFragmentByTag(FragmentDisplaySubsection.TAG);
        if (f == null) {
            f = new FragmentDisplaySubsection();
            Bundle args = new Bundle();
            args.putLong(SubsectionContract.ROW_ID,
                    getIntent().getLongExtra(SubsectionContract.ROW_ID, 0L));
            args.putString(SubsectionContract.COL_TITLE,
                    getIntent().getStringExtra(SubsectionContract.COL_TITLE));
            args.putString(FragmentDisplaySection.PROTOCOL_SUBSECTION,
                    getIntent().getStringExtra(FragmentDisplaySection.PROTOCOL_SUBSECTION));
            args.putString(FragmentDisplaySection.PROTOCOL_SUBSECTION_TITLE,
                    protocolTitle);
            f.setArguments(args);
            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            t.add(R.id.display_protocol_container, f, FragmentDisplaySubsection.TAG);
            t.commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(this, ActivityDisplaySection.class);
                i.putExtra(SubsectionContract.ROW_ID, getIntent().getLongExtra(SubsectionContract.ROW_ID, 0L));
                i.putExtra(SubsectionContract.COL_TITLE, getIntent().getStringExtra(SubsectionContract.COL_TITLE));
                i.putExtra(SectionContract.COL_COLOR, getIntent().getIntExtra(SectionContract.COL_COLOR, 0));
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
