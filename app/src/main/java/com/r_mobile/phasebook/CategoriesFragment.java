package com.r_mobile.phasebook;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.r_mobile.Category;
import com.r_mobile.CategoryDao;
import com.r_mobile.DaoMaster;
import com.r_mobile.DaoSession;
import com.r_mobile.PhraseBookApp;

import java.io.IOException;
import java.util.List;

/**
 * Created by Admin on 28.10.2015.
 */

public class CategoriesFragment extends Fragment {
    private DaoSession daoSession;
    private CategoryDao categoryDao;

    CategoryAdapter cAdapter;
    List<Category> categoryList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_categories, container, false);

        daoSession = ((PhraseBookApp) getActivity().getApplicationContext()).daoSession;
        categoryDao = daoSession.getCategoryDao();
        categoryList = categoryDao.loadAll();

        cAdapter = new CategoryAdapter(getContext(), categoryList);
        ListView lvCategories = (ListView)rootView.findViewById(R.id.lvCategories);
        lvCategories.setAdapter(cAdapter);
        lvCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PhraseActivity.class);
                intent.putExtra("activityName", cAdapter.getCategory(position).getLabel());
                intent.putExtra("categoryID", cAdapter.getCategory(position).getId());
                startActivity(intent);
                Log.d("phrasebook", cAdapter.getCategory(position).getLabel());
            }
        });

        return rootView;
    }

}

