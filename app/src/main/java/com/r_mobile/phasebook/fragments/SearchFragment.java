package com.r_mobile.phasebook.fragments;


import android.app.DownloadManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
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
    View.OnClickListener onItemClickListener;

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
        if (recyclerView != null) {
            recyclerView.setAdapter(adapter);
        }
    }

    public void refreshData(String newText) {
        if (newText != null) {
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

    private View getParentTill(View target, int parentId) {
        View parent = (View) target.getParent();

        while(parent.getId() != parentId) {
            parent = (View) parent.getParent();
        }

        return parent;
    }

    public void refreshForDelete(int position) {
        adapter.deleteItem(position);
        adapter.setmDataset(phraseList);
    }

    public int getViewPosition(View v) {
        return recyclerView.getChildLayoutPosition(getParentTill(v, R.id.phrasecardRoot));
    }

}
