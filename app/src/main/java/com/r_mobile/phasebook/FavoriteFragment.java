package com.r_mobile.phasebook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.r_mobile.DaoSession;
import com.r_mobile.Phrase;
import com.r_mobile.PhraseBookApp;
import com.r_mobile.PhraseDao;

import java.util.List;

/**
 * Created by Admin on 28.10.2015.
 */
public class FavoriteFragment extends Fragment {
    private DaoSession daoSession;
    private PhraseDao phraseDao;

    List<Phrase> phraseList;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);

        daoSession = ((PhraseBookApp) getActivity().getApplicationContext()).daoSession;
        phraseDao = daoSession.getPhraseDao();

        phraseList = phraseDao.queryBuilder().where(PhraseDao.Properties.Favorite.eq(1)).list();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_phrases_favorite);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PhraseAdapter(phraseList);
        recyclerView.setAdapter(adapter);

        return rootView;
    }
}
