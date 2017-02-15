package edu.gatech.cs.environmentalodors;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import android.view.ViewGroup;

/**
 * Created by agustinl on 2/10/17.
 */

public class OdorsDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    private View form = null;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        form = getActivity().getLayoutInflater().inflate(R.layout.dialog_layout, null);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Odor Classification Graph");
        builder.setView(form);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }

    @Override
    public void onDismiss(DialogInterface unused) {
        super.onDismiss(unused);

    }

    @Override
    public void onCancel(DialogInterface unused) {
        super.onCancel(unused);
    }

}
