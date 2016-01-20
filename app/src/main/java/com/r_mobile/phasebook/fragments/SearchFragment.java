package com.r_mobile.phasebook.fragments;


import android.app.DownloadManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.r_mobile.phasebook.R;
import com.r_mobile.phasebook.adapters.PhraseAdapter;
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
public class SearchFragment extends Fragment {
    private DaoSession daoSession;
    private PhraseDao phraseDao;

    List<Phrase> phraseList;
    RecyclerView recyclerView;
    PhraseAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

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
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    public void refreshData(String newText) {
        String completeText = "%" + newText + "%";
        phraseList = phraseDao.queryRaw(" inner join " + TranslateDao.TABLENAME + " MCL "
                + " on T._id = MCL." + TranslateDao.Properties.PhraseId.columnName +
                " where MCL." + TranslateDao.Properties.Content.columnName
                + " like '" + completeText + "' or T." + PhraseDao.Properties.Phrase.columnName
                + " like '" + completeText + "'");
        //phraseDao.queryRaw("inner join " + TranslateDao.TABLENAME + " N on T." + PhraseDao.Properties.Id.columnName + " = N."+TranslateDao.Properties.PhraseId.columnName +
        //                " where " + TranslateDao.Properties.Content.columnName + " = " + completeText);
        //daoSession.getDatabase().rawQuery("SELECT * FROM " + PhraseDao.TABLENAME + " INNER JOIN " + TranslateDao.TABLENAME + " ON " + PhraseDao.TABLENAME + "._id = " + TranslateDao.TABLENAME + "." + TranslateDao.Properties.PhraseId.columnName, null);
        //phraseDao.queryRaw("SELECT * FROM " + PhraseDao.TABLENAME + " INNER JOIN " + TranslateDao.TABLENAME + " ON " + PhraseDao.TABLENAME + "._id = " + TranslateDao.TABLENAME + "." + TranslateDao.Properties.PhraseId.columnName);
        /*
        Query query = phraseDao.queryBuilder().where(
                new WhereCondition.StringCondition("_id IN " +
                        "(SELECT PHRASE_ID FROM Translations)")).build();

        phraseList = query.list();
        */
        /*
        QueryBuilder queryBuilder = phraseDao.queryBuilder();
        queryBuilder.join(Phrase.class, PhraseDao.Properties.Id);
        phraseList = queryBuilder.list();
        */
        //phraseList = phraseDao.queryBuilder().where(PhraseDao.Properties.Phrase.like("%" + newText + "%")).list();
        adapter.setmDataset(phraseList);
        recyclerView.setAdapter(adapter);
    }
}
