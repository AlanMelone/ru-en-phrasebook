package com.r_mobile.phasebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.r_mobile.DaoSession;
import com.r_mobile.Phrase;
import com.r_mobile.PhraseBookApp;
import com.r_mobile.PhraseDao;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TabsPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private SlidingTabLayout tabs;
    private List<Phrase> ownPhrases;
    private List<Phrase> favoritePhrases;
    private List<Phrase> allPhrases;
    private DaoSession daoSession;
    private PhraseDao phraseDao;
    private  FavoriteFragment favoriteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CharSequence Titles[] = {"Свои", "Категории", "Избранное"};
        int NumbOfTabs = 3;

        daoSession = ((PhraseBookApp) this.getApplicationContext()).daoSession;
        phraseDao = daoSession.getPhraseDao();

        ownPhrases = phraseDao.queryBuilder().where(PhraseDao.Properties.Own.eq(1)).list();
        favoritePhrases = phraseDao.queryBuilder().where(PhraseDao.Properties.Favorite.eq(1)).list();
        allPhrases = phraseDao.loadAll();

        //Создаем адаптр
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), Titles, NumbOfTabs);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mViewPager.setAdapter(mAdapter);

        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        tabs.setViewPager(mViewPager);
        mViewPager.setCurrentItem(1);

        CategoriesFragment categoriesFragment = new CategoriesFragment();
        OwnPhrasesFragment ownPhrasesFragment = new OwnPhrasesFragment();
        favoriteFragment = new FavoriteFragment();

        ownPhrasesFragment.setOnItemClickListener(this);
        favoriteFragment.setOnItemClickListener(this);

        mAdapter.setCategoriesFragment(categoriesFragment);
        mAdapter.setFavoriteFragment(favoriteFragment);
        mAdapter.setOwnPhrasesFragment(ownPhrasesFragment);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddPhraseActivity.class));
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    private View getParentTill(View target, int parentId) {
        View parent = (View) target.getParent();

        while(parent.getId() != parentId) {
            parent = (View) parent.getParent();
        }

        return parent;
    }

    @Override
    public void onClick(View v) {
        Log.d("phrasebook", "asdqwe");
        Object id = getParentTill(v, R.id.phrasecardRoot).getTag();
        String idStr = id.toString();
        int idNum = (Integer.valueOf(idStr))-1;
        Integer phraseFavorite = allPhrases.get(idNum).getFavorite();
        Phrase phrase = allPhrases.get(idNum);
        switch (v.getId()) {
            case R.id.ivFavorite:
                ImageView ivFavorite = (ImageView) v.findViewById(R.id.ivFavorite);
                //Немного оптимизировать phraseList.get(position) в отдельную переменную
                if (phraseFavorite.equals(0)) {
                    ivFavorite.setImageResource(android.R.drawable.star_on);
                    phrase.setFavorite(1);
                    phraseDao.update(phrase);
                    favoriteFragment.refresh();
                } else {
                    ivFavorite.setImageResource(android.R.drawable.star_off);
                    phrase.setFavorite(0);
                    phraseDao.update(phrase);
                    favoriteFragment.refresh();
                }
                break;
        }
    }
}
