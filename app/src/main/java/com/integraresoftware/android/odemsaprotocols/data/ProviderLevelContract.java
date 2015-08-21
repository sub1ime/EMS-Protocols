package com.integraresoftware.android.odemsaprotocols.data;

import android.provider.BaseColumns;

/**
 * Created by Chris on 1/7/14.
 */
public class ProviderLevelContract implements BaseColumns {

    // Provider Level represented by integer
    public static final int EMTa = 0;
    public static final int EMTb = 1;
    public static final int EMTe = 2;
    public static final int EMTi = 3;
    public static final int EMTp = 4;

    public static final String PROVIDER_LEVEL = "provider_level";
}
