package com.r_mobile.phasebook.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;

import com.r_mobile.phasebook.MainActivity;
import com.r_mobile.phasebook.R;
import com.r_mobile.phasebook.greenDao.DaoSession;
import com.r_mobile.phasebook.greenDao.Phrase;
import com.r_mobile.phasebook.greenDao.PhraseBookApp;
import com.r_mobile.phasebook.greenDao.PhraseDao;

import java.util.List;

/**
 * Created by Aleksander on 09.03.2016.
 */
public class EditDialog extends DialogFragment {

    private DaoSession daoSession;
    private PhraseDao phraseDao;

    List<Phrase> phraseList;

    EditText etEditPhrase;
    EditText etEditTranscription;
    EditText etEditTranslate;

    long editPhraseID;
    int positionNum;

    Phrase editPhrase;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(inflater.inflate(R.layout.edit_phrase_dialog, null))
                // Add action buttons
                .setPositiveButton(R.string.acceptEditPhrase, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        editPhrase.setPhrase(etEditPhrase.getText().toString());
                        editPhrase.getTranslateList().get(0).setTranscription(etEditTranscription.getText().toString());
                        editPhrase.getTranslateList().get(0).setContent(etEditTranslate.getText().toString());

                        phraseDao.update(editPhrase);

                        ((MainActivity) getActivity()).onEditPhrase(true, positionNum, editPhrase);
                        dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditDialog.this.getDialog().cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        // Get the layout inflater
        editPhraseID = getArguments().getLong("editPhraseID");
        positionNum = getArguments().getInt("positionPhrase");

        daoSession = ((PhraseBookApp) getActivity().getApplicationContext()).daoSession;
        phraseDao = daoSession.getPhraseDao();

        phraseList = phraseDao.queryBuilder().where(PhraseDao.Properties.Id.eq(editPhraseID)).list();

        editPhrase = phraseList.get(0);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        if (nbutton != null) {
            nbutton.setTextColor(getResources().getColor(R.color.backgroundButtonDialog));
        }
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if (pbutton != null) {
            pbutton.setTextColor(getResources().getColor(R.color.backgroundButtonDialog));
        }

        etEditPhrase = (EditText) alertDialog.findViewById(R.id.etEditPhrase);
        etEditTranscription = (EditText) alertDialog.findViewById(R.id.etEditTranscription);
        etEditTranslate = (EditText) alertDialog.findViewById(R.id.etEditTranslate);

        etEditPhrase.setText(editPhrase.getPhrase());
        etEditTranscription.setText(editPhrase.getTranslateList().get(0).getTranscription());
        etEditTranslate.setText(editPhrase.getTranslateList().get(0).getContent());

        return alertDialog;
    }
}
