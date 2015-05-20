package com.integraresoftware.android.odemsaprotocols.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class DbProvider extends ContentProvider {
	
	public static final String TAG = "DbProvider";
	
	// content provider URI and Authority
	public static final String AUTHORITY = "com.integraresoftware.emsbuddy.data.DbProvider";
	public static final Uri SECTION_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/section");
	public static final Uri SUBSECTION_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/subsection");
	public static final Uri PROTOCOL_GROUP_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/protocol");
	public static final Uri PROTOCOL_CHILD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/protocol_child");
    public static final Uri MANAGEMENT_URI = Uri.parse("content://" + AUTHORITY + "/management");
	
	// MIME types use for searching all or finding a single
	public static final String SECTIONS_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.com.integraresoftware.emsbuddy.data.section";
	public static final String SECTION_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.com.integraresoftware.emsbuddy.data.section";
	public static final String SUBSECTIONS_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.com.integraresoftware.emsbuddy.data.subsection";
	public static final String SUBSECTION_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.com.integraresoftware.emsbuddy.data.subsection";
	public static final String PROTOCOL_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.com.integraresoftware.emsbuddy.data.protocol";
	public static final String PROTOCOL_CHILD_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.com.integraresoftware.emsbuddy.data.protocol_child";
    public static final String MANAGEMENT_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.com/integraresoftware.emsbuddy.data.management";
	
	// URI matcher
	private static final int LIST_SECTIONS = 0;
	private static final int ITEM_SECTION = 1;
	private static final int LIST_SUBSECTIONS = 2;
	private static final int ITEM_SUBSECTION = 3;
	private static final int ITEM_PROTOCOL = 4;
	private static final int ITEM_PROTOCOL_CHILD = 6;
	private static final int LIST_SUPPORT = 7;
    private static final int ITEM_MANAGEMENT = 8;
	private static final UriMatcher sUriMatcher = buildMatcher();
	
	// database object
	private SQLiteDatabase mDb;
	
	@Override
	public boolean onCreate() {
		mDb = new DbHandler(getContext()).getReadableDatabase();
		return true;
	}
	
	private static UriMatcher buildMatcher() {
		Log.d(TAG, "buildMatcher called");
		
		UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		matcher.addURI(AUTHORITY, "section", LIST_SECTIONS);
		matcher.addURI(AUTHORITY, "section/#", ITEM_SECTION);
		matcher.addURI(AUTHORITY, "subsection", LIST_SUBSECTIONS);
		matcher.addURI(AUTHORITY, "subsection/#", ITEM_SUBSECTION);
		matcher.addURI(AUTHORITY, "protocol/#", ITEM_PROTOCOL);				// FOR CALLING A SINGLE PROTOCOL TITLES
		matcher.addURI(AUTHORITY, "protocol_child/#", ITEM_PROTOCOL_CHILD);
        matcher.addURI(AUTHORITY, "management/#", ITEM_MANAGEMENT);
		
		Log.d(TAG, "Matcher contents " + matcher);
		return matcher;
	}
	
	@Override
	public String getType(Uri uri) {
		
		Log.d(TAG, "getType called with uri " + uri);
		switch(sUriMatcher.match(uri)) {
		case LIST_SECTIONS:
			return SECTIONS_MIME_TYPE;
		case ITEM_SECTION:
			return SECTION_MIME_TYPE;
		case LIST_SUBSECTIONS:
			return SUBSECTIONS_MIME_TYPE;
		case ITEM_SUBSECTION:
			return SUBSECTION_MIME_TYPE;
		case ITEM_PROTOCOL:
			return PROTOCOL_MIME_TYPE;
		case ITEM_PROTOCOL_CHILD:
			return PROTOCOL_CHILD_MIME_TYPE;
            case ITEM_MANAGEMENT:
            return MANAGEMENT_MIME_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);	
		}
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		Log.d(TAG, "query called with uri " + uri +
				"\nsUriMather.match(uri) = " + sUriMatcher.match(uri));
		
		Cursor c;
		
		switch(sUriMatcher.match(uri)) {
		case LIST_SECTIONS:
			Log.d(TAG, "List_sections switch");
			c = mDb.query(SectionContract.DATABASE_TABLE, 
					new String[] {SectionContract.COL_TITLE, SectionContract.COL_COLOR}, 
					null, null, null, null, null);
			if(c != null) {
				c.moveToFirst();
			}
			break;
		case ITEM_SECTION:
			//TODO this has not been debugged, no use for it yet
			c = mDb.query(SectionContract.DATABASE_TABLE, 
					new String[] { SectionContract.COL_TITLE, SectionContract.COL_COLOR }, 
					SectionContract.ROW_ID + "=?", 
					new String[] { Long.toString(ContentUris.parseId(uri))},
					null, null, null, null);
			if (c != null && c.getCount() > 0) {
				c.moveToFirst();
			}
			break;
		case LIST_SUBSECTIONS:
			String mySubsectionListQuery = "SELECT " + FkSectionContract.COL_GROUP_ID + ", " 
					+ SubsectionContract.DATABASE_TABLE + "." + SubsectionContract.COL_TITLE 
					+ ", " + FkSectionContract.COL_CHILD_ID + " FROM " 
					+ FkSectionContract.DATABASE_TABLE + " JOIN " + SubsectionContract.DATABASE_TABLE
					+ " ON (" + FkSectionContract.DATABASE_TABLE + "." + FkSectionContract.COL_CHILD_ID 
					+ "=" + SubsectionContract.DATABASE_TABLE + "." + SubsectionContract.ROW_ID + ")"
					+ " ORDER BY " + FkSectionContract.COL_GROUP_ID + ";";
			
			Log.d(TAG, "MyRawQuery = " + mySubsectionListQuery);
			
			c = mDb.rawQuery(mySubsectionListQuery, null);
			break;
		case ITEM_SUBSECTION:
			//TODO this has not been debugged, no use for it yet
			String[] mSelectionArgs = { Long.toString(ContentUris.parseId(uri)) };
			String mySubsectionItemQuery = "SELECT group_id, odemsa_subsection.subsection, child_id FROM odemsa_expandview "
					+ "JOIN odemsa_subsection"
					+ " ON (odemsa_expandview.child_id=odemsa_subsection._id)"
					+ " WHERE group_id=?"
					+ " ORDER BY group_id;";
			c = mDb.rawQuery(mySubsectionItemQuery, mSelectionArgs);
			break;
		case ITEM_PROTOCOL:
			String protocolSubtitleQuery = "SELECT odemsa_fk_protocol_subtitle._id, odemsa_subtitles_list.subtitle, odemsa_fk_protocol_subtitle._order FROM odemsa_fk_protocol_subtitle JOIN odemsa_subtitles_list ON (odemsa_fk_protocol_subtitle.subtitles_list_id=odemsa_subtitles_list._id) WHERE protocol_title_id=? ORDER BY _order;";
			Log.d(TAG, "protocolSubtitleQuery= " + protocolSubtitleQuery);
			String [] mSelectionArgsSubs = { Long.toString(ContentUris.parseId(uri)) };
			c = mDb.rawQuery(protocolSubtitleQuery, mSelectionArgsSubs);
			break;
		case ITEM_PROTOCOL_CHILD:
			c = mDb.query(SubsectionContract.DATABASE_TABLE, 
					projection, 
					SubsectionContract.ROW_ID + "=?", 
					new String[] { Long.toString(ContentUris.parseId(uri))}, 
					null, null, null);
			break;
        case ITEM_MANAGEMENT:
            c = mDb.query(ManagementContract.DATABASE_TABLE,
                    projection,
                    selection,
                    selectionArgs,
                    null, null, null );
            break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		
		
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}
	

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// no deletes allowed
		return 0;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// no inserts allowed
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// no updates allowed
		return 0;
	}

}
