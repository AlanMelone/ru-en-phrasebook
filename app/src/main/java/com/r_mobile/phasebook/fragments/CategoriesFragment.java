package com.r_mobile.phasebook.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.r_mobile.phasebook.adapters.CategoriesAdapter;
import com.r_mobile.phasebook.adapters.SimpleDividerItemDecoration;
import com.r_mobile.phasebook.greenDao.Category;
import com.r_mobile.phasebook.greenDao.CategoryDao;
import com.r_mobile.phasebook.greenDao.DaoSession;
import com.r_mobile.phasebook.greenDao.PhraseBookApp;
import com.r_mobile.phasebook.R;

import java.util.List;

/**
 * Created by r-mobile on 28.10.2015.
 */

public class CategoriesFragment extends Fragment {
    private DaoSession daoSession;
    private CategoryDao categoryDao;

    CategoriesAdapter cAdapter; //Адаптер категорий
    List<Category> categoryList; //Лист с категориями
    RecyclerView rvCategories;
    RecyclerView.LayoutManager layoutManager;
    View.OnClickListener onItemClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_categories, container, false);

        daoSession = ((PhraseBookApp) getActivity().getApplicationContext()).daoSession;
        categoryDao = daoSession.getCategoryDao();
        categoryList = categoryDao.loadAll(); //Заполняем лист категориями

        cAdapter = new CategoriesAdapter(categoryList);

        rvCategories = (RecyclerView) rootView.findViewById(R.id.rvCategories);

        rvCategories.setHasFixedSize(true);
        rvCategories.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        layoutManager = new LinearLayoutManager(getContext());
        rvCategories.setLayoutManager(layoutManager);

        if (onItemClickListener != null) {
            cAdapter.setOnItemClickListener(onItemClickListener);
        }

        rvCategories.setAdapter(cAdapter);

        return rootView;
    }

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        if (cAdapter != null) {
            cAdapter.setOnItemClickListener(onItemClickListener);
        }
    }
}

