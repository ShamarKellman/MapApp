package com.aitc.mapapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

/**
 * Created by shamar on 5/20/2015.
 */
public class ADialogFragment extends DialogFragment {
    public static ADialogFragment newInstance(int title) {
        ADialogFragment frag = new ADialogFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                doPositiveClick();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                doNegativeClick();
            }
        });

        return builder.create();
    }

    private void doPositiveClick() {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Positive click!");
    }

    private void doNegativeClick() {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Negative click!");
    }
}
