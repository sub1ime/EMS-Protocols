package com.integraresoftware.android.odemsaprotocols.objects;


import android.app.Activity;
import android.database.Cursor;
import android.graphics.Typeface;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.integraresoftware.android.odemsaprotocols.R;

public class FormatProtocolView {

    public static final String TAG = "FormatProtocolView";

    private Activity activity;
    private int rows;
    private int columns;
    private String text;
    private String[] mTableRows;
    private String[] mTableCells;
    private String[] foreText;


    public FormatProtocolView(Activity activity, Cursor cursor) {
        //TODO AEIOU chart looks funny
        // set the context
        this.activity = activity;

        // set the rows and columns
        // all of the text that will go into the view
        text = cursor.getString(1);
        // split the text into rows in an array
        mTableRows = text.split("<tr>");
        // calculate the number of rows
        rows = mTableRows.length - 1; // subtract 1 because the first cell in the array is blank
        // calculate the number of columns by looking at one of the rows
        mTableCells = mTableRows[2].split("<td>");
        columns = mTableCells.length - 1; // subtract 1 because the first cell in the array is blank
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

    public HorizontalScrollView buildLargeChart() {
        // create the horizontalScrollView
        HorizontalScrollView horizontalSv = new HorizontalScrollView(activity);

        // create the table
        TableLayout tableLayout = new TableLayout(activity);
        tableLayout.setLayoutParams(new HorizontalScrollView.LayoutParams(
                HorizontalScrollView.LayoutParams.MATCH_PARENT,
                HorizontalScrollView.LayoutParams.WRAP_CONTENT
        ));

        // this for loop cycles through the rows
        for (int i = 1; i <= rows; i++) {
            Log.d(TAG, "i=" + i);

            TableRow tableRow = new TableRow(activity);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
            ));
            String[] mDataText = mTableRows[i].split("<td>", columns + 1);

            // create alternating colors for each row and the darkest color for the Titles
            if (i == 1) {
                tableRow.setBackgroundColor(0xff008FB2);
            } else if (i % 2 == 0) {
                tableRow.setBackgroundColor(0xff99E6FF);
            } else {
                tableRow.setBackgroundColor(0xffD6F5FF);
            }

            // this loop cycles through the data cells
            for (int q = 1; q <= columns; q++) {
                Log.d(TAG, "q=" + q);

                int alignment = mTableCells[q].length() <= 3 ? Gravity.CENTER : Gravity.LEFT;
                String mString;
                if (mDataText[q].length() > 1) mString = formatText(mDataText[q]);
                else mString = mDataText[q];

                TextView tv = new TextView(activity);
                tv.setGravity(alignment);
                tv.setText(Html.fromHtml(mString));
                tv.setTextAppearance(activity, R.style.subsection_table_style);
                tv.setPadding(10, 5, 20, 5);

                if (i == 1) {
                    tv.setTypeface(null, Typeface.BOLD);
                    tv.setTextColor(0xffFFFFFF);
                }

                tv.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                ));
                // add the textview to the row
                tableRow.addView(tv);
            }

            tableLayout.addView(tableRow);
        }

        // add the table to the horizontal scroll view
        horizontalSv.addView(tableLayout);
        horizontalSv.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        return horizontalSv;
    }

    public TableLayout buildSmallChart() {
        TableLayout tableLayout = new TableLayout(activity);
        tableLayout.setShrinkAllColumns(true);
        tableLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // this for loop cycles through the rows
        for (int i = 1; i <= rows; i++) {
            Log.d(TAG, "i=" + i);

            TableRow tableRow = new TableRow(activity);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
            ));
            tableRow.setWeightSum(1f);
            String[] mDataText = mTableRows[i].split("<td>", columns + 1);

            // create alternating colors for each row and the darkest color for the Titles
            if (i == 1) {
                tableRow.setBackgroundColor(0xff008FB2);
            } else if (i % 2 == 0) {
                tableRow.setBackgroundColor(0xff99E6FF);
            } else {
                tableRow.setBackgroundColor(0xffD6F5FF);
            }

            // this loop cycles through the data cells
            for (int q = 1; q <= columns; q++) {
                Log.d(TAG, "q=" + q);

                int alignment = mTableCells[q].length() <= 3 ? Gravity.CENTER : Gravity.LEFT;
                String mString;
                if (mDataText[q].length() > 1) mString = formatText(mDataText[q]);
                else mString = mDataText[q];

                TextView tv = new TextView(activity);
                tv.setGravity(alignment);
                tv.setText(Html.fromHtml(mString));
                tv.setTextAppearance(activity, R.style.subsection_table_style);
                tv.setPadding(10, 5, 20, 5);

                if (i == 1) {
                    tv.setTypeface(null, Typeface.BOLD);
                    tv.setTextColor(0xffFFFFFF);
                }

                // set weight to null if greater then two columns,
                // set to 0.5f if with only 2 columns
                Float mWeight;
                switch(columns) {
                    case 0:
                    case 1:
                    case 2:
                        mWeight = 0.5f;
                        break;
                    case 3:
                        mWeight = 0.3f;
                        break;
                    default:
                        mWeight = -1f;
                }

                tv.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        mWeight
                ));
                // add the textview to the row
                tableRow.addView(tv);
            }

            tableLayout.addView(tableRow);

        }
        return tableLayout;
    }

    public TextView buildTitle(String title, int color) {
        // create a view that will contain the header with the title of the protocol
        TextView tv = new TextView(activity);
        tv.setText(title);
        tv.setTextAppearance(activity, R.style.subsection_title_style);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setBackgroundColor(color);
        return tv;
    }

    public LinearLayout returnDivider() {
        LinearLayout ll = new LinearLayout(activity);
        ll.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 10));
        return ll;
    }

    public boolean checkForeText() {
        foreText = text.split("<table>");
        if (foreText.length == 1) {
            foreText = text.split("<Table>");
        }

        return foreText[0].length() > 0;
    }

    public LinearLayout addForeText() {
        foreText = text.split("<table>");
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        // create the textview  to house the foretext
        TextView textView = new TextView(activity);
        String text = formatText(foreText[0]);
        textView.setText(Html.fromHtml(text));
        textView.setTextAppearance(activity, R.style.subsection_style);
        linearLayout.addView(textView);

        return linearLayout;
    }

    public int getColumns() {
        return columns;
    }
}
