package com.r_mobile.phasebook.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.r_mobile.phasebook.greenDao.Category;
import com.r_mobile.phasebook.greenDao.CategoryDao;
import com.r_mobile.phasebook.greenDao.DaoSession;
import com.r_mobile.phasebook.greenDao.PhraseBookApp;
import com.r_mobile.phasebook.adapters.CategoryAdapter;
import com.r_mobile.phasebook.R;

import java.util.List;

/**
 * Created by r-mobile on 28.10.2015.
 */

public class CategoriesFragment extends Fragment {
    private DaoSession daoSession;
    private CategoryDao categoryDao;

    CategoryAdapter cAdapter; //Адаптер категорий
    List<Category> categoryList; //Лист с категориями
    AdapterView.OnItemClickListener onItemClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_categories, container, false);

        daoSession = ((PhraseBookApp) getActivity().getApplicationContext()).daoSession;
        categoryDao = daoSession.getCategoryDao();
        categoryList = categoryDao.loadAll(); //Заполняем лист категориями

        cAdapter = new CategoryAdapter(getContext(), categoryList); //Определяем адаптер

        ListView lvCategories = (ListView)rootView.findViewById(R.id.lvCategories);

        lvCategories.setAdapter(cAdapter); //Присваиваем адаптер листвюшке
        //Определяем нажатие на элемент листвью
        lvCategories.setOnItemClickListener(onItemClickListener);

        return rootView;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        if (cAdapter != null) {
            cAdapter.setOnItemClickListener(onItemClickListener);
        }
    }
}

