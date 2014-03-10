package com.integraresoftware.emsbuddy;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.integraresoftware.android.emsbuddy.R;
import com.integraresoftware.emsbuddy.adapter.ImageZoom;
import com.integraresoftware.emsbuddy.data.DbProvider;
import com.integraresoftware.emsbuddy.data.SubsectionContract;

import java.util.regex.Pattern;

public class FragmentDisplaySubsection extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {
    //TODO look at using patterns to pull out text between tags;

    public static final String TAG = "FragmentDisplaySubsection";
    public static final int MAIN_LOADER = 0;

    private Long protocolId;
    private String protocolTitle;
    private String subsection;

    private Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            protocolId = args.getLong(SubsectionContract.ROW_ID);
            protocolTitle = args.getString(SubsectionContract.COL_TITLE);
            subsection = args.getString(FragmentDisplaySection.PROTOCOL_SUBSECTION);
            String subsectionTitle = args.getString(FragmentDisplaySection.PROTOCOL_SUBSECTION_TITLE);
            getActivity().setTitle(subsectionTitle + " < " + protocolTitle);
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
            txt = txt.replaceAll("/b ", "&#8226;");
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
        Pattern tablePattern = Pattern.compile("<table>(.+?)</table>");
        Pattern trPattern = Pattern.compile("<tr>(.+?)</tr>");
        Pattern tdPattern = Pattern.compile("<td>(.+?)</tr>");

        ScrollView sv = new ScrollView(activity);
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
            LinearLayout ll = new LinearLayout(activity);
            ll.setOrientation(LinearLayout.VERTICAL);

            TextView tv1 = new TextView(activity);
            String mText1 = formatText(foreText[0]);
            tv1.setText(Html.fromHtml(mText1));
            tv1.setTextAppearance(activity, R.style.subsection_style);
            ll.addView(tv1);

            LinearLayout.LayoutParams lpt = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lpt.setMargins(20, 10, 10, 10);

            llMain.addView(ll, lpt);
        }

        // start building the table
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(activity);
        TableLayout tableLayout = new TableLayout(activity);

        for (int i = 1; i <= rows; i++) {
            TableRow tableRow = new TableRow(activity);
            String[] mDataText = mTableRows[i].split("<td>");

            for (int q = 1; q <= columns; q++) {
                int alignment = mTableCells[q].length() <= 3 ? Gravity.CENTER : Gravity.LEFT;
                String mString = formatText(mDataText[q]);

                TextView tv = new TextView(activity);
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
        activity.setContentView(sv);


    }

    private void buildImageView(Cursor cursor) {
        String dbImage = cursor.getString(1);
        String[] imageStringLocation = dbImage.split("<image>");
        String image = imageStringLocation[1];

        int imageIntLocation = activity.getResources().getIdentifier(imageStringLocation[1], "drawable", activity.getPackageName());

        /*ScrollView sv = new ScrollView(activity);
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        sv.addView(linearLayout);*/

        ImageZoom iv = new ImageZoom(activity, imageIntLocation);
        // iv.setImageResource(imageIntLocation);
        // linearLayout.addView(iv);

        activity.setContentView(iv);

    }

    private void buildBasicView (Cursor cursor) {
        String text = cursor.getString(1);

        ScrollView sv = new ScrollView(activity);

        LinearLayout llMain = new LinearLayout(activity);
        llMain.setOrientation(LinearLayout.VERTICAL);
        sv.addView(llMain);

        LinearLayout ll = new LinearLayout(activity);
        ll.setOrientation(LinearLayout.VERTICAL);
        // set layout parameters
        LinearLayout.LayoutParams lpt = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpt.setMargins(20, 10, 10, 10);

        llMain.addView(ll, lpt);

        text = formatText(text);
        TextView tv = new TextView(activity);
        tv.setText(Html.fromHtml(text));
        tv.setTextAppearance(activity, R.style.subsection_style);

        ll.addView(tv);

        activity.setContentView(sv);
    }

}
