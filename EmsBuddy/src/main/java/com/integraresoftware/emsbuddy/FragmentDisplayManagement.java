package com.integraresoftware.emsbuddy;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.integraresoftware.emsbuddy.data.DbProvider;
import com.integraresoftware.emsbuddy.data.ProviderLevelContract;
import com.integraresoftware.emsbuddy.data.SubsectionContract;

public class FragmentDisplayManagement extends Fragment implements LoaderCallbacks<Cursor> {
    //TODO handle orientation change
    //TODO get this to work with api v4

    public static final String TAG = "FragmentDisplayManagement";
    public static final int MAIN_LOADER = 0;

    private Long protocolId;
    private String subsection;
    private int providerLevel;

    private Activity activity;
    private ScrollView sv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();

        Bundle args = getArguments();
        if (args != null) {
            protocolId = args.getLong(SubsectionContract.ROW_ID);
            String protocolTitle = args.getString(SubsectionContract.COL_TITLE);
            subsection = args.getString(FragmentDisplaySection.PROTOCOL_SUBSECTION);
            String subsectionTitle = args.getString(FragmentDisplaySection.PROTOCOL_SUBSECTION_TITLE);
            activity.setTitle(subsectionTitle + " < " + protocolTitle);
        } else {
            throw new IllegalArgumentException("No protocol ID supplied with argument");
        }
        loadSavedPreferences();
        sv = new ScrollView(activity);
        getLoaderManager().initLoader(MAIN_LOADER, null, this);
    }

    public void loadSavedPreferences() {
        SharedPreferences settings = activity.getPreferences(0);
        providerLevel = settings.getInt(ProviderLevelContract.PROVIDER_LEVEL, -1);

        // if preference isn't set
        if (providerLevel == -1) {
            DialogFragment dF = new FragmentProviderLevelDialog();
            dF.show(getFragmentManager(), "getLevel");
            providerLevel = settings.getInt(ProviderLevelContract.PROVIDER_LEVEL, -1);
        }
    }

    private void displayTitle() {
        LinearLayout ll = new LinearLayout(activity);
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);

        TextView tv =  new TextView(activity);
        tv.setText("Management for " + providerLevel);
        ll.addView(tv);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        Uri baseUri;
                baseUri = Uri.withAppendedPath(DbProvider.PROTOCOL_CHILD_CONTENT_URI,
                Uri.encode(protocolId.toString()));
                String[] mProjection = new String[] { "_id", subsection };
                return new CursorLoader(getActivity(), baseUri, mProjection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

        displayTitle();
        activity.setContentView(sv);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }



    //----------------------------------------------------------------------------------------------

}
