package com.r_mobile.phasebook.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.r_mobile.phasebook.R;

/**
 * Created by AlanMalone on 29.02.2016.
 */
public class DeleteDialog extends DialogFragment implements View.OnClickListener {

    LinearLayout llCancelDeletePhrase;
    LinearLayout llDeletePhrase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.delete_phrase_dialog, container, false);

        llCancelDeletePhrase = (LinearLayout) view.findViewById(R.id.llCancelDeletePhrase);
        llDeletePhrase = (LinearLayout) view.findViewById(R.id.llDeletePhrase);

        llCancelDeletePhrase.setOnClickListener(this);
        llDeletePhrase.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llCancelDeletePhrase:
                dismiss();
                break;
        }
    }
}
