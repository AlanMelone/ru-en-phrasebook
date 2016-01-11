package com.r_mobile.phasebook;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.r_mobile.phasebook.fragments.PhrasesFragment;
import com.r_mobile.phasebook.fragments.RootFragment;
import com.r_mobile.phasebook.greenDao.DaoSession;
import com.r_mobile.phasebook.greenDao.Phrase;
import com.r_mobile.phasebook.greenDao.PhraseBookApp;
import com.r_mobile.phasebook.greenDao.PhraseDao;
import com.r_mobile.phasebook.adapters.TabsPagerAdapter;
import com.r_mobile.phasebook.fragments.CategoriesFragment;
import com.r_mobile.phasebook.fragments.FavoriteFragment;
import com.r_mobile.phasebook.fragments.OwnPhrasesFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private TabsPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private SlidingTabLayout tabs;
    private List<Phrase> ownPhrases;
    private List<Phrase> favoritePhrases;
    private List<Phrase> allPhrases;
    private DaoSession daoSession;
    private PhraseDao phraseDao;
    private FavoriteFragment favoriteFragment;
    private CategoriesFragment categoriesFragment;
    private PhrasesFragment phrasesFragment;
    private OwnPhrasesFragment ownPhrasesFragment;
    private RootFragment rootFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CharSequence Titles[] = {"Свои", "Фразы", "Избранное"};
        int NumbOfTabs = 3; //Количество вкладок

        daoSession = ((PhraseBookApp) this.getApplicationContext()).daoSession;
        phraseDao = daoSession.getPhraseDao();

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);

        ownPhrases = phraseDao.queryBuilder().where(PhraseDao.Properties.Own.eq(1)).list();
        favoritePhrases = phraseDao.queryBuilder().where(PhraseDao.Properties.Favorite.eq(1)).list();
        allPhrases = phraseDao.loadAll();

        categoriesFragment = new CategoriesFragment();
        ownPhrasesFragment = new OwnPhrasesFragment();
        favoriteFragment = new FavoriteFragment();
        categoriesFragment = new CategoriesFragment();
        phrasesFragment = new PhrasesFragment();
        rootFragment = new RootFragment();
        rootFragment.setCategoriesFragment(categoriesFragment);

        //Создаем адаптр
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), Titles, NumbOfTabs);

        //Передаем адаптеру все ссылки на фрагменты
        mAdapter.setPhrasesFragment(phrasesFragment);
        mAdapter.setCategoriesFragment(categoriesFragment);
        mAdapter.setFavoriteFragment(favoriteFragment);
        mAdapter.setOwnPhrasesFragment(ownPhrasesFragment);
        mAdapter.setRootFragment(rootFragment);

        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(1);

        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        tabs.setViewPager(mViewPager);

        //Присваиваем обработчики
        ownPhrasesFragment.setOnItemClickListener(this);
        favoriteFragment.setOnItemClickListener(this);
        categoriesFragment.setOnItemClickListener(this);
        phrasesFragment.setOnItemClickListener(this);

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
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search_action).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //MainActivity.this.allArrayPhrase.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        };

        searchView.setOnQueryTextListener(onQueryTextListener);

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
    protected void onResume() {
        super.onResume();
        if (favoriteFragment.isAdded()) {
            favoriteFragment.refresh();
        }
    }

    @Override
    public void onClick(View v) {
        Log.d("phrasebook", "asdqwe");
        Object id = getParentTill(v, R.id.phrasecardRoot).getTag(); //Получаем id фразы из tag в корневой вьюшки phrasecard
        String idStr = id.toString();
        int idNum = (Integer.valueOf(idStr))-1;
        Integer phraseFavorite = allPhrases.get(idNum).getFavorite();
        Phrase phrase = allPhrases.get(idNum);
        switch (v.getId()) {
            case R.id.ivFavorite:
                ImageView ivFavorite = (ImageView) v.findViewById(R.id.ivFavorite);
                //Немного оптимизировать phraseList.get(position) в отдельную переменную
                if (phraseFavorite.equals(0)) {
                    ivFavorite.setImageResource(R.drawable.ic_star);
                    phrase.setFavorite(1);
                    phraseDao.update(phrase);
                    favoriteFragment.refresh();
                    phrasesFragment.refresh();
                    ownPhrasesFragment.refresh();
                } else {
                    ivFavorite.setImageResource(R.drawable.ic_star_outline);
                    phrase.setFavorite(0);
                    phraseDao.update(phrase);
                    favoriteFragment.refresh();
                    phrasesFragment.refresh();
                    ownPhrasesFragment.refresh();
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        bundle.putInt("categoryID", position + 1);
        phrasesFragment.setArguments(bundle);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction tx = fragmentManager.beginTransaction();
        tx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        tx.replace(R.id.root_frame, phrasesFragment).addToBackStack(null);
        tx.commit();

    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 1 && phrasesFragment.isVisible()) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }
}
