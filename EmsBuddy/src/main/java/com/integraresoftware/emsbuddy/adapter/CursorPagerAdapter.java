package com.integraresoftware.emsbuddy.adapter;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CursorPagerAdapter<F extends Fragment> extends FragmentPagerAdapter {
    private final Class<F> fragmentClass;
    public final String[] projection;
    private Cursor cursor;
    public static final String TAG = "CursorPagerAdapter";



    public CursorPagerAdapter(FragmentManager fm, Class<F> fragmentClass,
                              String[] projection, Cursor cursor) {
        super(fm);
        this.fragmentClass = fragmentClass;
        this.projection = projection;
        this.cursor = cursor;
    }

    @Override
    public F getItem(int position) {
        if (cursor == null) // shouldn't happen
            return null;

        cursor.moveToFirst();
        F frag;
        try {
            frag = fragmentClass.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        Bundle args = new Bundle();
        args.putInt(TAG, position);
        args.putString("TEXT", cursor.getString(position));

        frag.setArguments(args);
        return frag;
    }

    @Override
    public int getCount() {
        if (cursor == null)
            return 0;
        else
            return 5;
    }

    public void swapCursor(Cursor c) {
        if (cursor == c)
            return;

        this.cursor = c;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return cursor;
    }
}