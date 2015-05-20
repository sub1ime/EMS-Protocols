package com.integraresoftware.android.odemsaprotocols.data;


/*
 * This is for the table with the foreign keys connecting the sections 
 * and subsections.
 * This is just an adapter for the "odemsa_expandview" table.
 */

public class FkSectionContract {
	
	// define the columns in the table
	public static final String ROW_ID = "_id";
	public static final String COL_GROUP_ID = "group_id";
	public static final String COL_CHILD_ID = "child_id";
	
	// define the attributes of the table
	public static final String DATABASE_TABLE = "odemsa_fk_section";
	
}
