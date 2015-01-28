package com.integraresoftware.android.odemsaprotocols;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;

import com.integraresoftware.android.odemsaprotocols.data.DbProvider;
import com.integraresoftware.android.odemsaprotocols.data.SectionContract;
import com.integraresoftware.android.odemsaprotocols.data.SubsectionContract;

import java.util.ArrayList;


public class FragmentDisplayList extends Fragment implements LoaderCallbacks<Cursor> {
	// Tags
	public static final String TAG = "FragmentDisplayList";
	public static final String ELV_SELECTED = "children";
	
	// for loader manager threads
	private static final int G_LOADER = 0;
	private static final int C_LOADER = 1;
	
	// for operation of app
	public ArrayList<Object> mChildItem = new ArrayList<Object>();
	private SectionElvAdapter mAdapter;
	private ExpandableListView elv;
	private int lastExpandedGroupPosition = -1;
	public int lastSelected = -1;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView()");
		View v = inflater.inflate(R.layout.section_elv, null);
		return v;
	}		
	
	@Override
	public void onViewCreated(View v, Bundle savedInstanceState) {
		getActivity().setTitle("ODEMSA Protocols");
		
		if (savedInstanceState != null && savedInstanceState.containsKey(ELV_SELECTED)) {
			lastSelected = savedInstanceState.getInt(ELV_SELECTED);
		}
		
		elv = (ExpandableListView) v.findViewById(R.id.list);
		elv.setDividerHeight(2);
		elv.setGroupIndicator(null);
		elv.setClickable(true);
		elv.setOnGroupClickListener(mAdapter);
		
		mAdapter = new SectionElvAdapter(null);
		mAdapter.setInflater((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE), getActivity());

		getLoaderManager().initLoader(G_LOADER, null, this);
		getLoaderManager().initLoader(C_LOADER, null, this);
	}

	
	public void populateChildren(Cursor cursor){
		//TODO change the designations in the string into a name instead of an integer for easier reading
		// and programming adaptivity
		ArrayList<Object> child = new ArrayList<Object>();		
		
		int tmpGroup = 1;
		
		// run through all rows of the cursor loop
		for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			if(tmpGroup == cursor.getInt(0)) {
				String childDetails[] = new String[2];
				childDetails[0] = cursor.getString(1);
				childDetails[1] = cursor.getString(2);
				child.add(childDetails);
			} else {
				tmpGroup++;
				mChildItem.add(child);
				child = new ArrayList<Object>();
				String childDetails[] = new String[2];
				childDetails[0] = cursor.getString(1);
				childDetails[1] = cursor.getString(2);
				child.add(childDetails);
			}
		} // end loop
		
		mChildItem.add(child);		
		mAdapter.setChildren(mChildItem);
		
		Log.d(TAG, "populateChildren Completed");
	}
	
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {
		Log.d(TAG, "onCreateLoader(). id = " + id);
		
		switch(id) {
		case G_LOADER:
			Log.d(TAG, "G_LOADER created");
			return new CursorLoader(getActivity(), DbProvider.SECTION_CONTENT_URI, null, null, null, null);
		case C_LOADER:
			Log.d(TAG, "C_LOADER created");
			return new CursorLoader(getActivity(), DbProvider.SUBSECTION_CONTENT_URI, null, null, null, null);
		default:
			throw new IllegalArgumentException("Unknown thread ID: " + id);
		}

	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		Log.d(TAG, "onLoadFinished(). Loader id:" + loader.getId());
		
		switch(loader.getId()) {
		case G_LOADER:			
			mAdapter.setGroups(cursor);
			elv.setAdapter(mAdapter);
			mAdapter.setSelected();
			break;
		case C_LOADER:
			populateChildren(cursor);
			break;
		default:
			throw new IllegalArgumentException("Unknown loader.getId(): " + loader.getId());
		}	
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		Log.d(TAG, "onLoaderReset()");
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putInt(ELV_SELECTED, lastExpandedGroupPosition);
	}
	
	
	
	
	
	
	
	
	public class SectionElvAdapter extends BaseExpandableListAdapter implements OnGroupClickListener {

		public static final String TAG = "SectionElvAdapter";
		/*
		 * Declare some global variables
		 */
		private Cursor groupItem;
		private ArrayList<Object> childItem, tmpChild;
		private String[] tmpChild2;
		private LayoutInflater minflater;
		public Activity activity;

		public final int gTITLE = 0;
		public final int gCOLOR = 1;
		
		/*
		 * This is the constructor for the adapter.
		 */
		 public SectionElvAdapter(Cursor grList) {
			  groupItem = grList;
			 
			  Log.d(TAG, "Constructor completed");
		 }
		 
		 public void setSelected() {
			 if (lastSelected >= 0) {
				 if (childItem.size() == -1) {
					 try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				 } else {
					 elv.expandGroup(lastSelected);
					 elv.setSelection(lastSelected);
				 }
			 }
		 }
		 
		 public void setGroups(Cursor grList) {
			 groupItem = grList;
		 }


		public void setInflater(LayoutInflater mInflater, Activity act) {
			 Log.d(TAG, "setInflater()");
			// this is the inflater service being called
			this.minflater = mInflater;
		  	// this is the context within which the inflater will work
		  	activity = act;
		 }
		 
		public void setChildren(ArrayList<Object> child) {
			childItem = child;
		}

		 @Override
		 public Object getChild(int groupPosition, int childPosition) {
		  return null;
		 }

		 @Override
		 public long getChildId(int groupPosition, int childPosition) {
		  return 0;
		 }
		 

		 /*
		  * (non-Javadoc)
		  * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View, android.view.ViewGroup)
		  * made at the time the group is clicked
		  */
		@SuppressWarnings("unchecked")
		@Override
		 public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

			tmpChild = (ArrayList<Object>) childItem.get(groupPosition);
			tmpChild2 = (String[]) tmpChild.get(childPosition);
			TextView text = null;
		  
		  if (convertView == null) {
			  convertView = minflater.inflate(R.layout.cat_group_row, null);
		  }
		  
		  text = (TextView) convertView.findViewById(R.id.textView1);
		  text.setText(tmpChild2[0]);
		  
		  // sets the  for each child
		  convertView.setOnClickListener(new OnClickListener() {  
			   @Override
			   public void onClick(View v) {
				   tmpChild = (ArrayList<Object>) childItem.get(groupPosition);
				   tmpChild2 = (String[]) tmpChild.get(childPosition);
				   groupItem.moveToPosition(groupPosition);
				   
				   Log.d(TAG, "setOnClickListener(): tmpChild2[1]= " + tmpChild2[1]);
				   
				   Intent i = new Intent(activity, ActivityDisplaySection.class);
				   i.putExtra(SubsectionContract.ROW_ID, Long.valueOf(tmpChild2[1]).longValue());
				   i.putExtra(SubsectionContract.COL_TITLE, tmpChild2[0]);
				   i.putExtra(SectionContract.COL_COLOR, groupItem.getInt(gCOLOR));
                   i.putExtra(ELV_SELECTED, lastExpandedGroupPosition);
				   activity.startActivity(i);
			   }
		  });

		  return convertView;
		  
		 }

		@SuppressWarnings("unchecked")
		@Override
		 public int getChildrenCount(int groupPosition) {
		  return ((ArrayList<String>) childItem.get(groupPosition)).size();
		 }

		 @Override
		 public Object getGroup(int groupPosition) {
		  return null;
		 }

		 @Override
		 public int getGroupCount() {
			 return groupItem.getCount();
		 }

		 @Override
		 public void onGroupCollapsed(int groupPosition) {
			 lastExpandedGroupPosition = -1;
			 super.onGroupCollapsed(groupPosition);
			 
		 }

		 @Override
		 public void onGroupExpanded(int groupPosition) {
			 
			 if(groupPosition != lastExpandedGroupPosition){
		            elv.collapseGroup(lastExpandedGroupPosition);
		        }

	        super.onGroupExpanded(groupPosition);           
	        lastExpandedGroupPosition = groupPosition;
		 }

		 @Override
		 public long getGroupId(int groupPosition) {
			 return groupPosition;
		 }

		 @Override
		 public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			
			 groupItem.moveToPosition(groupPosition);
			
			 if (convertView == null) {
				  convertView = minflater.inflate(R.layout.activity_select_prot_cat, null);
			  }
			 
			  if (groupItem.getInt(gCOLOR) != 0){
				  if (groupItem.getInt(gCOLOR) == 0xFF000000){
					  ((CheckedTextView) convertView).setBackgroundColor(groupItem.getInt(gCOLOR));
					  ((CheckedTextView) convertView).setTextColor(0xFFFFFFFF);
				  } else {
					  ((CheckedTextView) convertView).setBackgroundColor(groupItem.getInt(gCOLOR));
					  ((CheckedTextView) convertView).setTextColor(0xff000000);
				  }
			  }
			  ((CheckedTextView) convertView).setText(groupItem.getString(gTITLE));
			  ((CheckedTextView) convertView).setChecked(isExpanded);
			 
			 return convertView;
				 
		 }

		 @Override
		 public boolean hasStableIds() {
		  return false;
		 }

		 @Override
		 public boolean isChildSelectable(int groupPosition, int childPosition) {
		  return true;
		 }


		 @Override
		 public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition,
		         long id) {

		     parent.smoothScrollToPosition(groupPosition);

		     // since we overrode the click, we have to do the work it would normally do for itself, that's what this code is for
		     if (parent.isGroupExpanded(groupPosition)) {
		         parent.collapseGroup(groupPosition);
		         // lastExpandedGroupPosition = -1;
		     } else {
		         parent.expandGroup(groupPosition);
		     }

		     return true;
		 }	 
		 
	}





	

}