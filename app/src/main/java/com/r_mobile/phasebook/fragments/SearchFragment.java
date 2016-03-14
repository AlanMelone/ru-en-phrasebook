package com.r_mobile.phasebook.fragments;


import android.app.DialogFragment;
import android.app.DownloadManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.r_mobile.phasebook.R;
import com.r_mobile.phasebook.Utils;
import com.r_mobile.phasebook.adapters.PhraseAdapter;
import com.r_mobile.phasebook.dialogs.DeleteDialog;
import com.r_mobile.phasebook.dialogs.EditDialog;
import com.r_mobile.phasebook.greenDao.DaoSession;
import com.r_mobile.phasebook.greenDao.Phrase;
import com.r_mobile.phasebook.greenDao.PhraseBookApp;
import com.r_mobile.phasebook.greenDao.PhraseDao;
import com.r_mobile.phasebook.greenDao.Translate;
import com.r_mobile.phasebook.greenDao.TranslateDao;

import java.util.List;

import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;

/**
 * Created by AlanMalone on 20.01.2016.
 */

public class SearchFragment extends Fragment implements View.OnClickListener {
    private DaoSession daoSession;
    private PhraseDao phraseDao;

    List<Phrase> phraseList;
    RecyclerView recyclerView;
    PhraseAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    View.OnClickListener onItemClickListener;

    String currentText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        daoSession = ((PhraseBookApp) getActivity().getApplicationContext()).daoSession;
        phraseDao = daoSession.getPhraseDao();


        phraseList = phraseDao.loadAll();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_search_phrases);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PhraseAdapter(phraseList);
        if (onItemClickListener != null) {
            adapter.setOnItemClickListener(onItemClickListener);
            adapter.setDeleteClickListener(this);
        }
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        if (adapter != null) {
            adapter.setOnItemClickListener(onItemClickListener);
        }
    }

    public void refresh() {
        if (recyclerView !=null) {
            if (currentText != null) {
                refreshData(currentText);
            } else {
                phraseList = phraseDao.loadAll();
                adapter.setmDataset(phraseList);
            }
        }
    }

    public void refreshData(String newText) {
        if (newText != null) {
            currentText = newText;
            String completeText = "%" + newText + "%";
            phraseList = phraseDao.queryRaw(" inner join " + TranslateDao.TABLENAME + " MCL "
                    + " on T._id = MCL." + TranslateDao.Properties.PhraseId.columnName +
                    " where MCL." + TranslateDao.Properties.Content.columnName
                    + " like '" + completeText + "' or T." + PhraseDao.Properties.Phrase.columnName
                    + " like '" + completeText + "'");
        } else {
            phraseList = phraseDao.loadAll();
        }
        adapter.setmDataset(phraseList);
        recyclerView.setAdapter(adapter);
    }

    public void refreshForDelete(int position) {
        adapter.deleteItem(position);
    }

    public void refreshForEdit(Phrase phraseEdit, int position) {
        adapter.editItem(phraseEdit, position);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.ivMoreOptions:
                ImageView ivMoreOptions = (ImageView) v.findViewById(R.id.ivMoreOptions);
                PopupMenu popupMenu = new PopupMenu(getContext(), ivMoreOptions);
                popupMenu.inflate(R.menu.menu_card);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()) {
                            case R.id.deletePhrase:
                                Bundle bundleDelete = new Bundle();

                                bundleDelete.putLong("deletePhraseID", getPhraseId(v, R.id.phrasecardRoot));
                                bundleDelete.putInt("positionPhrase", recyclerView.getChildLayoutPosition(Utils.getParentTill(v, R.id.phrasecardRoot)));

                                DialogFragment dialogFragment = new DeleteDialog();
                                dialogFragment.setArguments(bundleDelete);
                                dialogFragment.show(getActivity().getFragmentManager(), "dialogFragment");
                                break;
                            case R.id.editPhrase:
                                Bundle bundleEdit = new Bundle();

                                bundleEdit.putLong("editPhraseID", getPhraseId(v, R.id.phrasecardRoot));
                                bundleEdit.putInt("positionPhrase", recyclerView.getChildLayoutPosition(Utils.getParentTill(v, R.id.phrasecardRoot)));

                                DialogFragment editFragment = new EditDialog();
                                editFragment.setArguments(bundleEdit);
                                editFragment.show(getActivity().getFragmentManager(), "editFragment");
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
                break;
        }
    }

    private long getPhraseId(View v, int rootView) {
        Phrase phrase;
        Object id = Utils.getParentTill(v, rootView).getTag(); //Получаем id фразы из tag в корневй вьюшки phrasecard
        String idStr = id.toString(); //Переводим id в строку
        int idNum = (Integer.valueOf(idStr)); //Переводим id в int
        phrase = phraseDao.queryBuilder().where(PhraseDao.Properties.Id.eq(idNum)).list().get(0); //Получаем фразу
        long phraseId = phrase.getId();
        return phraseId;
    }

}
