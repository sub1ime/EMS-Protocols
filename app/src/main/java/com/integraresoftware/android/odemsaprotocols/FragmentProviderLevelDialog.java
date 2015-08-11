package com.integraresoftware.android.odemsaprotocols;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.integraresoftware.android.odemsaprotocols.data.ProviderLevelContract;

public class FragmentProviderLevelDialog extends DialogFragment {

<<<<<<< HEAD
	public static final String TAG = "FragProviderLvlDialog";
=======
	public static final String TAG = "FragProviderLevelDialog";
>>>>>>> weight_converter
    private int providerLevel;

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface ProviderLevelListener {
        public void onDialogFinished(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    ProviderLevelListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (ProviderLevelListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    public FragmentProviderLevelDialog newInstance() {
        FragmentProviderLevelDialog f = new FragmentProviderLevelDialog();
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Default Provider Level")
                .setItems(R.array.provider_level_array,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putInt(ProviderLevelContract.PROVIDER_LEVEL, i);
                                editor.commit();
								Log.d(TAG, "i = " + i);
                                mListener.onDialogFinished(FragmentProviderLevelDialog.this);
                            }
                        }
                );
        return builder.create();
    }



    /*Activity activity;

        public FragmentProviderLevelDialog newInstance(Activity activity) {
            this.activity = activity;
            FragmentProviderLevelDialog frag = new FragmentProviderLevelDialog();
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            return new AlertDialog.Builder(activity)
                    .setTitle("Provider Level")
                    .setMessage("Please select your provider level")
                    .setSingleChoiceItems(R.array.provider_level_array, 0,
                            new DialogInterface.OnClickListener() {
                                //@Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    SharedPreferences settings = getActivity().getPreferences(0);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putInt(ProviderLevelContract.PROVIDER_LEVEL, i);
                                    editor.commit();
                                }
                            }
                    )
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    FragmentDisplayManagement.doPositiveClick();
                                }
                            }
                    )
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    FragmentDisplayManagement.doNegativeClick();
                                }
                            }
                    )
                    .create();
        }*/

}
