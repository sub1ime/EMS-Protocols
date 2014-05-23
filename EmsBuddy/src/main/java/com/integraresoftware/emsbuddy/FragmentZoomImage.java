package com.integraresoftware.emsbuddy;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.integraresoftware.android.emsbuddy.R;
import com.integraresoftware.emsbuddy.adapter.TouchImageView;
import com.integraresoftware.emsbuddy.data.SectionContract;
import com.integraresoftware.emsbuddy.data.SubsectionContract;

//TODO image is not showing up

public class FragmentZoomImage extends Fragment {

    public static final String TAG = "FragmentZoomImage";
    private Activity activity;

    private Long protocolId;
    private String protocolTitle;
    private String subsection;
    private String subsectionTitle;
    private int protocolColor;
    private int imageLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            protocolId = args.getLong(SubsectionContract.ROW_ID);
            protocolTitle = args.getString(SubsectionContract.COL_TITLE);
            subsection = args.getString(FragmentDisplaySection.PROTOCOL_SUBSECTION);
            subsectionTitle = args.getString(FragmentDisplaySection.PROTOCOL_SUBSECTION_TITLE);
            getActivity().setTitle(protocolTitle);
            protocolColor = args.getInt(SectionContract.COL_COLOR);
            imageLocation = args.getInt("imageId");
            Log.d(TAG, "imageLocation = " + imageLocation);
        } else {
            throw new IllegalArgumentException("No protocol ID supplied with argument");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.display_protocol_fragment_2, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        activity = getActivity();


        // create the main linear layout
        LinearLayout llTitle = new LinearLayout(activity);
        llTitle.setOrientation(LinearLayout.VERTICAL);
//        llTitle.setBackgroundColor(Color.BLACK);

        // create the textview with the title that will go first in the llMain
        TextView tv = new TextView(activity);
        tv.setText(subsectionTitle);
        tv.setTextAppearance(activity, R.style.subsection_title_style);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setBackgroundColor(protocolColor);
        // add the textview to the linear layout
        llTitle.addView(tv);

        // create a spacer and add it to the main layout
        LinearLayout ll = new LinearLayout(activity);
        LinearLayout.LayoutParams lpt = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, 10);
        llTitle.addView(ll, lpt);

        // create custom image view and set location of picture
        TouchImageView iv = new TouchImageView(activity);
        iv.setImageResource(imageLocation);
        iv.setMaxZoom(4f);

        LinearLayout.LayoutParams lptImage = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        llTitle.addView(iv, lptImage);

        activity.setContentView(llTitle);

    }
}
