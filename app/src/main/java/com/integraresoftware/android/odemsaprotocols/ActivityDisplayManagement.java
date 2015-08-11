package com.integraresoftware.android.odemsaprotocols;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.integraresoftware.android.odemsaprotocols.adapter.CursorPagerAdapter;
import com.integraresoftware.android.odemsaprotocols.data.DbProvider;
import com.integraresoftware.android.odemsaprotocols.data.ManagementContract;
import com.integraresoftware.android.odemsaprotocols.data.ProviderLevelContract;
import com.integraresoftware.android.odemsaprotocols.data.SectionContract;
import com.integraresoftware.android.odemsaprotocols.data.SubsectionContract;

public class ActivityDisplayManagement extends ActionBarActivity implements
		ActionBar.TabListener, LoaderManager.LoaderCallbacks<Cursor>,
		com.integraresoftware.android.odemsaprotocols.FragmentProviderLevelDialog.ProviderLevelListener {

<<<<<<< HEAD
    public static final String TAG = "ActivityDisplayManage";
=======
    public static final String TAG = "ActDisplayManagement";
>>>>>>> weight_converter
	public static final String PrefTag = "ProviderLevel";
    private static String[] TITLES = {"EMT-A", "EMT-B", "EMT-EN", "EMT-I", "EMT-P" };
    private static final int MAIN_LOADER = 1;

    public Long protocolId;
    private String[] mProjection = {
            ManagementContract.EMTA,
            ManagementContract.EMTB,
            ManagementContract.EMTE,
            ManagementContract.EMTI,
            ManagementContract.EMTP };

    ActionBar actionBar;
    CursorPagerAdapter mAdapter;
    ViewPager mViewPager;
    Bundle args;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);

        protocolId = getIntent().getLongExtra(SubsectionContract.ROW_ID, 0L);


        args = new Bundle();
        args.putLong(SubsectionContract.ROW_ID,
                protocolId);
        args.putString(SubsectionContract.COL_TITLE,
                getIntent().getStringExtra(SubsectionContract.COL_TITLE));
        args.putString(FragmentDisplaySection.PROTOCOL_SUBSECTION,
                getIntent().getStringExtra(FragmentDisplaySection.PROTOCOL_SUBSECTION));

        mAdapter = new CursorPagerAdapter(getSupportFragmentManager(), com.integraresoftware.android.odemsaprotocols.FragmentManagement.class, mProjection, null);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                        getSupportActionBar().setSelectedNavigationItem(position);
                    }
                });
        mViewPager.setAdapter(mAdapter);

        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                mViewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }
            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }
        };

        for (int i = 0; i < 5; i++) {
            actionBar.addTab(actionBar.newTab().setText(TITLES[i]).setTabListener(tabListener));
        }

        getSupportLoaderManager().initLoader(MAIN_LOADER, args, this);
<<<<<<< HEAD
=======

>>>>>>> weight_converter
        // load provider level
        loadSavedPreferences();
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		Log.d(TAG, "tab = " + tab.getPosition());

        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        switch (i) {
            case MAIN_LOADER:
                Uri baseUri;
                baseUri = Uri.withAppendedPath(DbProvider.MANAGEMENT_URI,
                        Uri.encode(protocolId.toString()));

                String mSelection = ManagementContract.PROTOCOL_ID + "=?";

                String[] mSelectionArgs = { protocolId.toString() };

                return new CursorLoader(this, baseUri, mProjection, mSelection, mSelectionArgs, null);
            default:
                throw new IllegalArgumentException("Wrong loader id, dumbass!");
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case MAIN_LOADER:
                mAdapter.swapCursor(cursor);
                break;
        }

    }

    @Override
    public void onLoaderReset(Loader loader) {
        switch (loader.getId()) {
            case MAIN_LOADER:
                mAdapter.swapCursor(null);
                break;
        }
    }

	public void loadSavedPreferences() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		// int providerLevel = prefs.getInt(ProviderLevelContract.PROVIDER_LEVEL, -1);
        int providerLevel = -1;
		Log.d(TAG, "loadSavedPreferneces().providerLevel = " + providerLevel);
		// if no shared preference
		if (providerLevel == -1) {
			// launch dialog to set providerLevel
			FragmentManager fm = getSupportFragmentManager();
			FragmentProviderLevelDialog mDialog = new FragmentProviderLevelDialog();
			mDialog.show(fm, "provider_level_dialog");
		} else {
			mViewPager.setCurrentItem(providerLevel);
		}
	}


	@Override
	public void onDialogFinished(DialogFragment dialog) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		int providerLevel = prefs.getInt(ProviderLevelContract.PROVIDER_LEVEL, -1);
		Log.d(TAG, "onDialogFinished().providerLevel = " + providerLevel);
		mViewPager.setCurrentItem(providerLevel);
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
			case R.id.action_settings:
				FragmentManager fm = getSupportFragmentManager();
				FragmentProviderLevelDialog mDialog = new FragmentProviderLevelDialog();
				mDialog.show(fm, "provider_level_dialog");
            default:
                return super.onOptionsItemSelected(item);
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.display_management_menu, menu);

		return super.onCreateOptionsMenu(menu);
	}
}

