package com.integraresoftware.emsbuddy;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.integraresoftware.android.emsbuddy.R;
import com.integraresoftware.emsbuddy.data.DbProvider;
import com.integraresoftware.emsbuddy.data.SectionContract;
import com.integraresoftware.emsbuddy.data.SubsectionContract;
import com.integraresoftware.emsbuddy.data.SubtitlesContract;

/*
Display the different sections that are inside a protocol for the user to choose which to see
 */
public class FragmentDisplaySection extends ListFragment implements LoaderCallbacks<Cursor> {

    public static final String TAG = "FragmentDisplaySection";
    public static final String PROTOCOL_SUBSECTION = "protocolSubsection";
    public static final String PROTOCOL_SUBSECTION_TITLE = "protocolSubsectionTitle";

    // Loader variables
    private static final int PROTOCOL_GROUP_LOADER = 0;

    // Items this fragment should know when creating
    private Long protocolId;
    private int protocolColor;
    private String protocolTitle;

    public ColorCursorAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");

        Bundle args = getArguments();
        if (args != null) {
            protocolId = args.getLong(SubsectionContract.ROW_ID);
            protocolTitle = args.getString(SubsectionContract.COL_TITLE);
            protocolColor = args.getInt(SectionContract.COL_COLOR);
            getActivity().setTitle(protocolTitle);
        } else {
            throw new IllegalArgumentException("No protocol ID supplied with argument");
        }

        String[] from = new String[] { SubtitlesContract.COL_SUBTITLE };
        int[] to = new int[] { R.id.section_text };

        mAdapter = new ColorCursorAdapter(getActivity(), R.layout.section_display, null, from, to, 0);
        setListAdapter(mAdapter);

        getLoaderManager().initLoader(PROTOCOL_GROUP_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

        Log.d(TAG, "onCreateLoader() id = " + id);

        Uri baseUri;
        switch(id) {
            case PROTOCOL_GROUP_LOADER:
                if (protocolId != null) {
                    baseUri = Uri.withAppendedPath(DbProvider.PROTOCOL_GROUP_CONTENT_URI, Uri.encode(protocolId.toString()));
                } else {
                    throw new IllegalArgumentException("No protocol ID");
                }
                return new CursorLoader(getActivity(), baseUri, null, null, null, null);
            default:
                throw new IllegalArgumentException("Unknown Loader Id: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(TAG, "onLoadFinished() id = " + loader.getId());

        switch(loader.getId()) {
            case PROTOCOL_GROUP_LOADER:
                mAdapter.swapCursor(cursor);
                break;
            default:
                throw new IllegalArgumentException("No such group loader id: " + loader.getId());
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch(loader.getId()) {
            case PROTOCOL_GROUP_LOADER:
                mAdapter.swapCursor(null);
                break;
            default:
                throw new IllegalArgumentException("No such group loader id: " + loader.getId());
        }
    }



    public class ColorCursorAdapter extends SimpleCursorAdapter {

        public ColorCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
        }

        @Override
        public Cursor swapCursor(Cursor c) {
            return super.swapCursor(c);
        }

        @Override
        public void bindView(View view, Context context, final Cursor cursor) {
            TextView textView = (TextView) view.findViewById(R.id.section_text);

            view.setBackgroundColor(protocolColor);

            if (protocolColor == 0xFF000000) {
                textView.setTextColor(0xFFFFFFFF);
            } else {
                textView.setTextColor(0xFF000000);
            }

            ListView listView = getListView();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    cursor.moveToPosition(position);

                    if (cursor.getString(1).equalsIgnoreCase("management")) {
                        Intent i = new Intent(getActivity(), ActivityDisplayManagement.class);
                        i.putExtra(SubsectionContract.ROW_ID, protocolId);
                        i.putExtra(SubsectionContract.COL_TITLE, protocolTitle);
                        i.putExtra(SectionContract.COL_COLOR, protocolColor);
                        i.putExtra(PROTOCOL_SUBSECTION, "content" + (position + 1));
                        i.putExtra(PROTOCOL_SUBSECTION_TITLE, cursor.getString(1));

                        startActivity(i);
                    } else {
                        Intent i = new Intent(getActivity(), ActivityDisplaySubsection.class);
                        i.putExtra(SubsectionContract.ROW_ID, protocolId);
                        i.putExtra(SubsectionContract.COL_TITLE, protocolTitle);
                        i.putExtra(SectionContract.COL_COLOR, protocolColor);
                        i.putExtra(PROTOCOL_SUBSECTION, "content" + (position + 1));
                        i.putExtra(PROTOCOL_SUBSECTION_TITLE, cursor.getString(1));

                        startActivity(i);
                    }
                }
            });

            super.bindView(view, context, cursor);
        }

    }

}
