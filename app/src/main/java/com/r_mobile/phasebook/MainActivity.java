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
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.r_mobile.phasebook.fragments.PhrasesFragment;
import com.r_mobile.phasebook.fragments.RootFragment;
import com.r_mobile.phasebook.fragments.SearchFragment;
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
    private SearchFragment searchFragment;
    private SearchView searchView;
    private Speaker speaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CharSequence Titles[] = {"Свои", "Фразы", "Избранное"}; //Название вкладок
        int NumbOfTabs = 3; //Количество вкладок

        speaker = new Speaker(this);

        daoSession = ((PhraseBookApp) this.getApplicationContext()).daoSession;
        phraseDao = daoSession.getPhraseDao();

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);

        ownPhrases = phraseDao.queryBuilder().where(PhraseDao.Properties.Own.eq(1)).list();
        favoritePhrases = phraseDao.queryBuilder().where(PhraseDao.Properties.Favorite.eq(1)).list();
        allPhrases = phraseDao.loadAll();

        //Создаем фрагменты
        categoriesFragment = new CategoriesFragment();
        ownPhrasesFragment = new OwnPhrasesFragment();
        favoriteFragment = new FavoriteFragment();
        categoriesFragment = new CategoriesFragment();
        phrasesFragment = new PhrasesFragment();
        rootFragment = new RootFragment();
        searchFragment = new SearchFragment();
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
        searchFragment.setOnItemClickListener(this);

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

        //Определяем SeachView
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search_action).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        //Placeholder для searchView
        searchView.setQueryHint("Введите фразу, перевод иди транскрипцию");

        //Обработчик редактирования и подтверждения запроса в строке поиска
        SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    searchFragment.refreshData(newText); //Передаем управление searchView
                }
                if (newText.length() == 0) {
                    searchFragment.refreshData(null); //Передаем управление searchView
                }
                return false;
            }
        };

        //Обработчик закрытия поиска
        SearchView.OnCloseListener onCloseListener = new SearchView.OnCloseListener() {

            @Override
            public boolean onClose() {
                getSupportFragmentManager().popBackStack();
                return false;
            }
        };

        //Обработчик нажатия на значек поиска
        SearchView.OnClickListener onClickListener = new SearchView.OnClickListener() {

            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1); //Меняем вкладку в приложении

                //Делаем переход на searchFragment
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager(); //Получаем FragmentManager
                //Стартуем транзакцию
                FragmentTransaction tx = fragmentManager.beginTransaction();
                tx.replace(R.id.root_frame, searchFragment).addToBackStack(null);
                tx.commit();
            }
        };

        searchView.setOnQueryTextListener(onQueryTextListener);
        searchView.setOnSearchClickListener(onClickListener);
        searchView.setOnCloseListener(onCloseListener);

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

    //Метод возвращает родителя конкретного вью-элемента
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

    //Обработчик нажатия на карточку
    @Override
    public void onClick(View v) {
        Log.d("phrasebook", "asdqwe");
        Object id = getParentTill(v, R.id.phrasecardRoot).getTag(); //Получаем id фразы из tag в корневой вьюшки phrasecard
        String idStr = id.toString(); //Переводим id в строку
        int idNum = (Integer.valueOf(idStr))-1; //Переводим id в int
        Integer phraseFavorite = allPhrases.get(idNum).getFavorite(); //Получаем значение, которое указывает, что элемент в избранном
        Phrase phrase = allPhrases.get(idNum); //Получаем фразу
        //int idCard = v.getId();
        //int idPhraseCard = R.id.cv;
        switch (v.getId()) {
            case R.id.cv:
                speaker.allow(true);
                speaker.speak(phrase.getPhrase().toString());
                break;
            case R.id.ivFavorite: //Обрабатываем нажатие на "Избранное"
                ImageView ivFavorite = (ImageView) v.findViewById(R.id.ivFavorite); //Получаем изображение избранного
                //Проверяем, есть ли элемент в "Избранном"
                if (phraseFavorite.equals(0)) { //Если нет, то добавляем
                    ivFavorite.setImageResource(R.drawable.ic_star);  //Устанавливаем картинку
                    phrase.setFavorite(1); //В базе меняем значение избранного на 1
                    phraseDao.update(phrase); //Обновляем сущность
                    //Обновляем фрагменты
                    favoriteFragment.refresh();
                    phrasesFragment.refresh();
                    ownPhrasesFragment.refresh();
                    searchFragment.refresh();
                } else { //Если есть
                    ivFavorite.setImageResource(R.drawable.ic_star_outline); //Устанавливаем изображение
                    phrase.setFavorite(0); //В базе меняем значение избранного на 0
                    phraseDao.update(phrase); //Обновляем сущность
                    //Обновляем фрагменты
                    favoriteFragment.refresh();
                    phrasesFragment.refresh();
                    ownPhrasesFragment.refresh();
                    searchFragment.refresh();
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Передаем фрагменты phraseFragment значение id категории
        Bundle bundle = new Bundle();
        bundle.putInt("categoryID", position + 1);
        phrasesFragment.setArguments(bundle);

        //Делаем переход на phraseFragment
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager(); //Получаем FragmentManager
        //Стартуем транзакцию
        FragmentTransaction tx = fragmentManager.beginTransaction();
        tx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        tx.replace(R.id.root_frame, phrasesFragment).addToBackStack(null);
        tx.commit();

    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 1 && (phrasesFragment.isVisible() || searchFragment.isVisible())) { //Если мы находимся на 1 вкладке(считать с нуля) и phrasesFragment виден
            if (searchFragment.isVisible()) {
                searchView.setIconified(true);
                searchView.onActionViewCollapsed();
            }
            getSupportFragmentManager().popBackStack(); //то переходим на фрагмент со стека
        } else {
            finish(); //закрываем активность
        }
    }
}
