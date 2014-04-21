package com.integraresoftware.emsbuddy;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.integraresoftware.android.emsbuddy.R;

public class FragmentManagement extends Fragment {

    public static String TAG = "FragmentManagement";
    private String text;
    private Activity activity;


    public static Fragment newInstance () {
        FragmentManagement frag = new FragmentManagement();

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_management_layout, container, false);

        Bundle args = getArguments();
        text = args.getString("TEXT");

        /*text = formatText(text);

        TextView tv = ((TextView) rootView.findViewById(R.id.mTextview));
        tv.setText(Html.fromHtml(text));
        tv.setTextAppearance(getActivity(), R.style.subsection_style);
        tv.setPadding(10, 5, 20, 5);*/

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        activity = getActivity();

        LinearLayout rl = (LinearLayout) view.findViewById(R.id.management_relative);

        String[] rows;

        if (text.contains("<table>")) {
            rows = text.split("<tr>");
        } else {
            text = formatText(text);
            TextView tv = new TextView(activity);
            tv.setText(Html.fromHtml(text));
            tv.setTextAppearance(getActivity(), R.style.subsection_style);
            tv.setPadding(10, 5, 20, 5);
            rl.addView(tv);
            return;
        }

        int numberRows = rows.length - 1;

        for (int i = 1; i <= numberRows; i++) {
            LinearLayout ll = new LinearLayout(activity);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            String[] mDataText = rows[i].split("<td>");

            for (int q = 1; q <= mDataText.length - 1; q++) {
                String mString = formatText(mDataText[q]);

                if (mString.matches("^[^\\d].*") && q == 1) {
                    TextView tv = new TextView(activity);
                    tv.setPadding(10, 0, 30, 0);
                    ll.addView(tv);
                }

                TextView tv = new TextView(activity);
                tv.setText(Html.fromHtml(mString));

                tv.setTextAppearance(activity, R.style.subsection_table_style);
                tv.setPadding(10, 0, 10, 0);

                ll.addView(tv);

            }
            rl.addView(ll);
            View v = new View(activity);
            v.setBackgroundColor(Color.GRAY);
            LinearLayout.LayoutParams lpt = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 1);
            rl.addView(v, lpt);
        }
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
}