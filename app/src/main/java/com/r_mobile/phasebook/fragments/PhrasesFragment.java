package com.r_mobile.phasebook.fragments;

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

import java.util.List;

/**
 * Created by r-mobile on 18.12.2015.
 */

public class PhrasesFragment extends Fragment {

    private DaoSession daoSession;
    private PhraseDao phraseDao;

    List<Phrase> phraseList;
    RecyclerView recyclerView;
    PhraseAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    View.OnClickListener onItemClickListener;
    int categoryID;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_phrases, container, false);
        categoryID = getArguments().getInt("categoryID");

        daoSession = ((PhraseBookApp) getActivity().getApplicationContext()).daoSession;
        phraseDao = daoSession.getPhraseDao();


        phraseList = phraseDao.queryBuilder().where(PhraseDao.Properties.CategoryId.eq(categoryID)).list();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_phrases_all);

        adapter = new PhraseAdapter(phraseList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);

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

    public void refresh() {
        if (recyclerView !=null) {
            phraseList = phraseDao.queryBuilder().where(PhraseDao.Properties.CategoryId.eq(categoryID)).list();
            adapter.setmDataset(phraseList);
        }
    }

    public void refreshForDelete(int position) {
        adapter.deleteItem(position);
        phraseList = phraseDao.queryBuilder().where(PhraseDao.Properties.CategoryId.eq(categoryID)).list();
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.scrollToPosition(0);
    }

    private View getParentTill(View target, int parentId) {
        View parent = (View) target.getParent();

        while(parent.getId() != parentId) {
            parent = (View) parent.getParent();
        }

        return parent;
    }

}
