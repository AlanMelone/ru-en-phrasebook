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

import com.r_mobile.phasebook.MainActivity;
import com.r_mobile.phasebook.R;
import com.r_mobile.phasebook.greenDao.DaoSession;
import com.r_mobile.phasebook.greenDao.Phrase;
import com.r_mobile.phasebook.greenDao.PhraseBookApp;
import com.r_mobile.phasebook.greenDao.PhraseDao;

import java.util.List;

/**
 * Created by AlanMalone on 29.02.2016.
 */
public class DeleteDialog extends DialogFragment implements View.OnClickListener {

    LinearLayout llCancelDeletePhrase;
    LinearLayout llDeletePhrase;

    private DaoSession daoSession;
    private PhraseDao phraseDao;

    List<Phrase> phraseList;

    long deletePhraseID;
    int positionNum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.delete_phrase_dialog, container, false);

        deletePhraseID = getArguments().getLong("deletePhraseID");
        positionNum = getArguments().getInt("positionPhrase");

        daoSession = ((PhraseBookApp) getActivity().getApplicationContext()).daoSession;
        phraseDao = daoSession.getPhraseDao();

        phraseList = phraseDao.queryBuilder().where(PhraseDao.Properties.Id.eq(deletePhraseID)).list();

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
            case R.id.llDeletePhrase:
                phraseDao.delete(phraseList.get(0));
                //Phrase phrase = phraseDao.load(deletePhraseID);
                //phraseDao.delete(phrase);
                ((MainActivity) getActivity()).onDeletePhrase(true, positionNum);
                dismiss();
                break;
        }
    }
}
