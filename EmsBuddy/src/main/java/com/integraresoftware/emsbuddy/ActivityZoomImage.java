package com.integraresoftware.emsbuddy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import com.integraresoftware.android.emsbuddy.R;
import com.integraresoftware.emsbuddy.data.SectionContract;
import com.integraresoftware.emsbuddy.data.SubsectionContract;

public class ActivityZoomImage extends ActionBarActivity {

    private String protocolTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_protocol_fragment);
        protocolTitle = getIntent().getStringExtra(FragmentDisplaySection.PROTOCOL_SUBSECTION_TITLE);

        Fragment f = getSupportFragmentManager().findFragmentByTag(FragmentZoomImage.TAG);
        if (f == null) {
            f = new FragmentZoomImage();
            Bundle args = new Bundle();
            args.putLong(SubsectionContract.ROW_ID,
                    getIntent().getLongExtra(SubsectionContract.ROW_ID, 0L));
            args.putString(SubsectionContract.COL_TITLE,
                    getIntent().getStringExtra(SubsectionContract.COL_TITLE));
            args.putString(FragmentDisplaySection.PROTOCOL_SUBSECTION,
                    getIntent().getStringExtra(FragmentDisplaySection.PROTOCOL_SUBSECTION));
            args.putString(FragmentDisplaySection.PROTOCOL_SUBSECTION_TITLE,
                    protocolTitle);
            args.putInt(SectionContract.COL_COLOR,
                    getIntent().getIntExtra(SectionContract.COL_COLOR, 0));
            args.putInt("imageId",
                    getIntent().getIntExtra("imageId", 0));
            f.setArguments(args);
            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            t.add(R.id.display_protocol_container, f, FragmentZoomImage.TAG);
            t.commit();
        }

    }
}
