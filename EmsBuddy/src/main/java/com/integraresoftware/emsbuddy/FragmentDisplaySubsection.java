package com.integraresoftware.emsbuddy;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.integraresoftware.android.emsbuddy.R;
import com.integraresoftware.emsbuddy.adapter.TouchImageView;
import com.integraresoftware.emsbuddy.data.DbProvider;
import com.integraresoftware.emsbuddy.data.SectionContract;
import com.integraresoftware.emsbuddy.data.SubsectionContract;

public class FragmentDisplaySubsection extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {
    //TODO look at using patterns to pull out text between tags;

    public static final String TAG = "FragmentDisplaySubsection";
    public static final int MAIN_LOADER = 0;

    private Long protocolId;
    private String protocolTitle;
    private String subsection;
    private String subsectionTitle;
    private int protocolColor;

    private Activity activity;

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
        } else {
            throw new IllegalArgumentException("No protocol ID supplied with argument");
        }

        getLoaderManager().initLoader(MAIN_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.display_protocol_fragment_2, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        activity = getActivity();
    }

    private String formatText(String txt) {

        // add bullets
        if (txt.contains("/b ")) {
            txt = txt.replaceAll("/b ", "&nbsp;&nbsp;&nbsp;&#8226;&nbsp;");
        }

        // replace breaks with HTML break
        txt = txt.replaceAll("\n", "<br>");

        return txt;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle bundle) {
        Uri baseUri;

        baseUri = Uri.withAppendedPath(DbProvider.PROTOCOL_CHILD_CONTENT_URI,
                Uri.encode(protocolId.toString()));
        String[] mProjection = new String[] { "_id", subsection };
        return new CursorLoader(getActivity(), baseUri, mProjection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        cursor.moveToFirst();
        String text = cursor.getString(1);

        if (text.contains("<table>")) {
           buildTableView(cursor);
        } else if (text.contains("<image>")) {
            buildImageView(cursor);
        } else {
            buildBasicView(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        ScrollView sv = new ScrollView(activity);

        LinearLayout ll = new LinearLayout(activity);
        ll.setId(R.id.display_protocol_linear_layout);
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);

        activity.setContentView(sv);
    }

    private void buildTableView(Cursor cursor) {
        // create pattern for table
        /*Pattern tablePattern = Pattern.compile("<table>(.+?)</table>");
        Pattern trPattern = Pattern.compile("<tr>(.+?)</tr>");
        Pattern tdPattern = Pattern.compile("<td>(.+?)</tr>");*/

        LinearLayout llTitle = new LinearLayout(activity);
        llTitle.setOrientation(LinearLayout.VERTICAL);
        TextView tv = new TextView(activity);
        tv.setText(subsectionTitle);
        tv.setTextAppearance(activity, R.style.subsection_title_style);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setBackgroundColor(protocolColor);
        llTitle.addView(tv);
        LinearLayout ll = new LinearLayout(activity);
        LinearLayout.LayoutParams lpt = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, 10);
        llTitle.addView(ll, lpt);

        ScrollView sv = new ScrollView(activity);
        llTitle.addView(sv);
        LinearLayout llMain = new LinearLayout(activity);
        llMain.setGravity(Gravity.CENTER);
        llMain.setOrientation(LinearLayout.VERTICAL);
        sv.addView(llMain);

        String text = cursor.getString(1);
        String[] mTableRows = text.split("<tr>");
        int rows = mTableRows.length - 1; // subtract 1 because the first cell in the array is blank
        String[] mTableCells = mTableRows[2].split("<td>");
        int columns = mTableCells.length - 1; // subtract 1 because the first cell in the array is blank

        // check for text before the table
        String[] foreText = text.split("<table>");
        if (foreText[0].length() > 0) {
            ll = new LinearLayout(activity);
            ll.setOrientation(LinearLayout.VERTICAL);

            TextView tv1 = new TextView(activity);
            String mText1 = formatText(foreText[0]);
            tv1.setText(Html.fromHtml(mText1));
            tv1.setTextAppearance(activity, R.style.subsection_style);
            ll.addView(tv1);

            lpt = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lpt.setMargins(20, 10, 10, 10);

            llMain.addView(ll, lpt);
        }

        // start building the table
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(activity);
        TableLayout tableLayout = new TableLayout(activity);

        Log.d(TAG, "Starting table build.\nrows=" + rows + "\ncolumns=" + columns);

        for (int i = 1; i <= rows; i++) {
            Log.d(TAG, "i=" + i);

            TableRow tableRow = new TableRow(activity);
            String[] mDataText = mTableRows[i].split("<td>", columns + 1);

            for (int q = 1; q <= columns; q++) {
                Log.d(TAG, "q=" + q);

                int alignment = mTableCells[q].length() <= 3 ? Gravity.CENTER : Gravity.LEFT;
                String mString;
                if (mDataText[q].length() > 1) mString = formatText(mDataText[q]);
                else mString = mDataText[q];

                tv = new TextView(activity);
                tv.setGravity(alignment);
                tv.setText(Html.fromHtml(mString));
                tv.setTextAppearance(activity, R.style.subsection_table_style);
                tv.setPadding(10, 5, 20, 5);

                // create alternating colors for each row and the darkest color for the Titles
                if (i == 1) {
                    tv.setTypeface(null, Typeface.BOLD);
                    tv.setTextColor(0xffFFFFFF);
                    tv.setBackgroundColor(0xff008FB2);
                } else if (i % 2 == 0) {
                    tv.setBackgroundColor(0xff99E6FF);
                } else {
                    tv.setBackgroundColor(0xffD6F5FF);
                }
                tableRow.addView(tv);
            }
            // tableLayout.setColumnShrinkable(1, true);
            tableLayout.addView(tableRow);
        }



        horizontalScrollView.addView(tableLayout);
        //tableLayout.setColumnShrinkable(1, true);
        llMain.addView(horizontalScrollView);
        activity.setContentView(llTitle);


    }

    private void buildImageView(Cursor cursor) {
        // get the image location and text to go with it
        String dbImage = cursor.getString(1);
        // split the image location from the rest of the text
        String[] imageStringLocation = dbImage.split("<image>");

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

        // if there is text before the image we want to place it after the image
        if(imageStringLocation[0].length() > 2) {
            // we need a scrollview to put the ll in so we can scroll down if needed
            ScrollView sv = new ScrollView(activity);

            // add sv to main view
            llTitle.addView(sv);
            // create a linear layout that will house the text
            // we will add it to the sv when we are done
            ll = new LinearLayout(activity);
            ll.setOrientation(LinearLayout.VERTICAL);

            // reset tv and add the text and change the appearance
            tv = new TextView(activity);
            tv.setText(Html.fromHtml(formatText(imageStringLocation[0])));
            tv.setTextAppearance(activity, R.style.subsection_style);

            // create margins for the text
            lpt = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lpt.setMargins(20, 10, 10, 10);

            // add the textview to the scroll view with attributes set
            ll.addView(tv, lpt);


            // add a zoomed out version of the image and give the option of opening a new
            // activity and being able to zoom in
            //------------------------
            // turn the String value of the location of the image into an int so android knows how to find it
            final int imageIntLocation = activity.getResources().getIdentifier(
                    imageStringLocation[1], "drawable", activity.getPackageName());
            ImageView iv = new ImageView(activity);
            iv.setImageResource(imageIntLocation);
            Log.d(TAG, "imageIntLocation = " + imageIntLocation);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(activity, ActivityZoomImage.class);
                    i.putExtra(SubsectionContract.COL_TITLE, protocolTitle);
                    i.putExtra(SectionContract.COL_COLOR, protocolColor);
                    i.putExtra(FragmentDisplaySection.PROTOCOL_SUBSECTION_TITLE, subsectionTitle);
                    i.putExtra(SubsectionContract.ROW_ID, protocolId);
                    i.putExtra(FragmentDisplaySection.PROTOCOL_SUBSECTION, subsection);
                    i.putExtra("imageId", imageIntLocation);
                    startActivity(i);
                }
            });

            // add the iv to the ll
            ll.addView(iv);
            // create a textview asking to click for further inspection
            tv = new TextView(activity);
            tv.setText("Click to Zoom");
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            tv.setTextColor(Color.BLUE);
            ll.addView(tv);

            // add the linear layout to the main scrollview
            sv.addView(ll);
        } else { // add the image
            // turn the String value of the location of the image into an int so android knows how to find it
            int imageIntLocation = activity.getResources().getIdentifier(
                    imageStringLocation[1], "drawable", activity.getPackageName());
            // create a touch view that will allow zooming of the image and set the image location
            TouchImageView iv = new TouchImageView(activity);
            Log.d(TAG, "imageIntLocation = " + imageIntLocation);
            iv.setImageResource(imageIntLocation);
            iv.setMaxZoom(4f);
            // add the image view to the main ll
            llTitle.addView(iv);
        }


        // set content and replace existing layout
        activity.setContentView(llTitle);

    }

    private void buildBasicView (Cursor cursor) {

        // create main view
        LinearLayout llTitle = new LinearLayout(activity);
        llTitle.setOrientation(LinearLayout.VERTICAL);

        // create a textview that will contain the title and add it
        TextView tv = new TextView(activity);
        tv.setText(subsectionTitle);
        tv.setTextAppearance(activity, R.style.subsection_title_style);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setBackgroundColor(protocolColor);
        // apply textview to main view
        llTitle.addView(tv);

        // create spacer and apply
        LinearLayout ll = new LinearLayout(activity);
        LinearLayout.LayoutParams lpt = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, 10);
        // apply spacer to main view
        llTitle.addView(ll, lpt);

        // get the text that will go into main body
        String text = cursor.getString(1);

        // create a scrollview and add it to the main body.
        // the scrollview goes after the main linear layout because we want the title to stay in the same place when
        // scrolling through the text
        ScrollView sv = new ScrollView(activity);
        llTitle.addView(sv);

        // create the main linear layout that will go into the scrollview and add it
        LinearLayout llMain = new LinearLayout(activity);
        llMain.setOrientation(LinearLayout.VERTICAL);
        sv.addView(llMain);

        // format the main body text
        text = formatText(text);
        // create new textview and add the text
        tv = new TextView(activity);
        tv.setText(Html.fromHtml(text));
        tv.setTextAppearance(activity, R.style.subsection_style);

        // create margins for the body
        lpt = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpt.setMargins(20, 10, 10, 10);

        //add the text with the margin attribute set to the ll within the sv
        llMain.addView(tv, lpt);

        // set the content view of the whole thing and replace the existing view
        activity.setContentView(llTitle);
    }

}
