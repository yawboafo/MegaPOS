package com.nfortics.megapos.Dialogs;


import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nfortics.megapos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ErrorDialog extends DialogFragment {


    public ErrorDialog() {
        // Required empty public constructor
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Something Went Wrong");
        builder.setMessage("No Internetconnection");
        Dialog dialog=builder.create();
        return super.onCreateDialog(savedInstanceState);
    }
}
