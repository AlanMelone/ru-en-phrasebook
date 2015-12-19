package com.r_mobile.phasebook.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import java.util.List;

/**
 * Created by r-mobile on 18.12.2015.
 */

public class PhrasesFragment extends Fragment {

    private DaoSession daoSession;
    private PhraseDao phraseDao;

    List<Phrase> phraseList;
    List<Phrase> allPhraseList;
    RecyclerView recyclerView;
    PhraseAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    View.OnClickListener onItemClickListener;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_phrases, container, false);
        int categoryID = getArguments().getInt("categoryID");

        daoSession = ((PhraseBookApp) getActivity().getApplicationContext()).daoSession;
        phraseDao = daoSession.getPhraseDao();

        allPhraseList = phraseDao.loadAll();
        phraseList = phraseDao.queryBuilder().where(PhraseDao.Properties.CategoryId.eq(categoryID)).list();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_phrases_all);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PhraseAdapter(phraseList);
        if (onItemClickListener != null) {
            adapter.setOnItemClickListener(onItemClickListener);
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
}
