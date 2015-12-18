package com.r_mobile.phasebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.r_mobile.phasebook.greenDao.DaoSession;
import com.r_mobile.phasebook.greenDao.Phrase;
import com.r_mobile.phasebook.greenDao.PhraseBookApp;
import com.r_mobile.phasebook.greenDao.PhraseDao;
import com.r_mobile.phasebook.adapters.PhraseAdapter;

import java.util.List;

public class PhraseActivity extends AppCompatActivity {
    private DaoSession daoSession;
    private PhraseDao phraseDao;

    List<Phrase> phraseList;
    List<Phrase> allPhraseList;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phrase);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent intent = getIntent();
        setTitle(intent.getStringExtra("activityName"));

        Log.d("phrasebook", String.valueOf(intent.getLongExtra("categoryID", 2)));

        daoSession = ((PhraseBookApp) getApplicationContext()).daoSession;
        phraseDao = daoSession.getPhraseDao();

        allPhraseList = phraseDao.loadAll();
        phraseList = phraseDao.queryBuilder().where(PhraseDao.Properties.CategoryId.eq(intent.getLongExtra("categoryID", 2))).list();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_phrases);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PhraseAdapter(phraseList);
        recyclerView.setAdapter(adapter);
    }

    private View getParentTill(View target, int parentId) {
        View parent = (View) target.getParent();

        while(parent.getId() != parentId) {
            parent = (View) parent.getParent();
        }

        return parent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((PhraseAdapter) adapter).setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object id = getParentTill(v, R.id.phrasecardRoot).getTag();
                String idStr = id.toString();
                int idNum = (Integer.valueOf(idStr))-1;
                Integer phraseFavorite = allPhraseList.get(idNum).getFavorite();
                Phrase phrase = allPhraseList.get(idNum);
                switch (v.getId()) {
                    case R.id.ivFavorite:
                        ImageView ivFavorite = (ImageView) v.findViewById(R.id.ivFavorite);
                        //Немного оптимизировать phraseList.get(position) в отдельную переменную
                        if (phraseFavorite.equals(0)) {
                            ivFavorite.setImageResource(android.R.drawable.star_on);
                            phrase.setFavorite(1);
                            phraseDao.update(phrase);
                            recyclerView.setAdapter(adapter);
                        } else {
                            ivFavorite.setImageResource(android.R.drawable.star_off);
                            phrase.setFavorite(0);
                            phraseDao.update(phrase);
                            recyclerView.setAdapter(adapter);
                        }
                        break;
                }
            }
        });
    }

}
