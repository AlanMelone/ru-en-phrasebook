package com.r_mobile.phasebook;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.lang.reflect.Field;

import static com.r_mobile.phasebook.R.drawable.abc_textfield_search_default_mtrl_alpha;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TabsPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
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
    //private MenuItem settingsItem;
    private MenuItem talkItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Name and count tabs
        CharSequence titles[] = {"СВОИ", "ФРАЗЫ", "ИЗБРАННОЕ"}; // Name of tabs
        int numbOfTabs = 3; // Count tabs

        // Create help text to speech object
        speaker = new Speaker(this);

        // Connection database
        daoSession = ((PhraseBookApp) this.getApplicationContext()).daoSession;
        phraseDao = daoSession.getPhraseDao();

        // Initialization objects
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.tabs);
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), titles, numbOfTabs);

        // Create all fragments for work
        createFragments();

        // Assign adapters listener
        setListenersForAdapters();

        // Assign for root categories fragment
        rootFragment.setCategoriesFragment(categoriesFragment);

        // Assign fragments in adapter
        setAdapterFragments(mAdapter);

        // Setting view pager
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(1);

        // Setting sliding tab
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        mSlidingTabLayout.setViewPager(mViewPager);

        // Create floating action button
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

        //settingsItem = menu.findItem(R.id.settings);
        talkItem = menu.findItem(R.id.talk);

        //Определяем SeachView
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search_action).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text).setBackgroundResource(abc_textfield_search_default_mtrl_alpha);

        AutoCompleteTextView searchTextView = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, R.drawable.cursor); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (Exception ignored) {
        }

        //Placeholder для searchView
        searchView.setQueryHint(Html.fromHtml("<font color = #ffffff>" + getResources().getString(R.string.searchPhraseHint) + "</font>"));

        //Обработчик редактирования и подтверждения запроса в строке поиска
        final SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
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

        //Обработчик нажатия на элемент менюшки
        MenuItem.OnMenuItemClickListener onMenuItemClickListener = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    //Нажатие на кнопку "Сказать"
                    case R.id.talk:
                        if (!searchView.getQuery().toString().equals("")) {
                            speaker.allow(true);
                            speaker.speak(searchView.getQuery().toString());
                        } else {
                            Toast.makeText(getApplicationContext(), "Введите фразу в строку поиска", Toast.LENGTH_LONG).show();
                        }
                        break;
                    //Нажатие на кнопку "Настройки
                    /*
                    case R.id.settings:
                        break;
                    */
                }
                return false;
            }
        };

        //Обработчик закрытия поиска
        SearchView.OnCloseListener onCloseListener = new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                getSupportFragmentManager().popBackStack();
                //settingsItem.setVisible(true);
                talkItem.setVisible(false);
                return false;
            }
        };

        //Обработчик нажатия на значек поиска
        SearchView.OnClickListener onClickListener = new SearchView.OnClickListener() {

            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1); //Меняем вкладку в приложении
                //settingsItem.setVisible(false);
                talkItem.setVisible(true);

                //Делаем переход на searchFragment
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager(); //Получаем FragmentManager
                //Стартуем транзакцию
                FragmentTransaction tx = fragmentManager.beginTransaction();
                tx.replace(R.id.root_frame, searchFragment).addToBackStack(null);
                tx.commit();
            }
        };

        talkItem.setOnMenuItemClickListener(onMenuItemClickListener);
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


    @Override
    protected void onResume() {
        super.onResume();
    }

    //Обработчик нажатия на карточку
    @Override
    public void onClick(final View v) {
        Log.d("phrasebook", "asdqwe");

        //int idCard = v.getId();
        //int idPhraseCard = R.id.cv;
        switch (v.getId()) {
            case R.id.rlSpeak:
                Phrase phrase = getPhraseId(v, R.id.phrasecardRoot);
                speaker.allow(true);
                speaker.speak(phrase.getPhrase().toString());
                break;
            case R.id.ivFavorite: //Обрабатываем нажатие на "Избранное"
                phrase = getPhraseId(v, R.id.phrasecardRoot);
                Integer phraseFavorite = getPhraseFavorive(v, R.id.phrasecardRoot);
                ImageView ivFavorite = (ImageView) v.findViewById(R.id.ivFavorite); //Получаем изображение избранного
                //Проверяем, есть ли элемент в "Избранном"
                if (phraseFavorite.equals(0)) { //Если нет, то добавляем
                    ivFavorite.setImageResource(R.drawable.ic_star);  //Устанавливаем картинку
                    phrase.setFavorite(1); //В базе меняем значение избранного на 1
                    phraseDao.update(phrase); //Обновляем сущность
                    //Обновляем фрагменты
                    if (mViewPager.getCurrentItem() == 0) {
                        favoriteFragment.refresh(v, false);
                        phrasesFragment.refresh();
                        searchFragment.refresh();
                    }
                    if (mViewPager.getCurrentItem() == 1 && phrasesFragment.isVisible()) {
                        favoriteFragment.refresh(v, false);
                        ownPhrasesFragment.refresh();
                        searchFragment.refresh();
                    }
                    if (mViewPager.getCurrentItem() == 1 && searchFragment.isVisible()) {
                        favoriteFragment.refresh(v, false);
                        phrasesFragment.refresh();
                        ownPhrasesFragment.refresh();
                    }
                    if (mViewPager.getCurrentItem() == 2) {
                        phrasesFragment.refresh();
                        ownPhrasesFragment.refresh();
                        searchFragment.refresh();
                    }
                } else { //Если есть
                    ivFavorite.setImageResource(R.drawable.ic_star_outline); //Устанавливаем изображение
                    phrase.setFavorite(0); //В базе меняем значение избранного на 0
                    phraseDao.update(phrase); //Обновляем сущность
                    //Обновляем фрагменты
                    if (mViewPager.getCurrentItem() == 0) {
                        favoriteFragment.refresh(v, false);
                        phrasesFragment.refresh();
                        searchFragment.refresh();
                    }
                    if (mViewPager.getCurrentItem() == 1 && phrasesFragment.isVisible()) {
                        favoriteFragment.refresh(v, false);
                        ownPhrasesFragment.refresh();
                        searchFragment.refresh();
                    }
                    if (mViewPager.getCurrentItem() == 1 && searchFragment.isVisible()) {
                        favoriteFragment.refresh(v, false);
                        phrasesFragment.refresh();
                        ownPhrasesFragment.refresh();
                    }
                    if (mViewPager.getCurrentItem() == 2) {
                        favoriteFragment.refresh(v, true);
                        phrasesFragment.refresh();
                        ownPhrasesFragment.refresh();
                        searchFragment.refresh();
                    }
                    //favoriteFragment.refresh();
                    //phrasesFragment.refresh();
                    //ownPhrasesFragment.refresh();
                    //searchFragment.refresh();
                }
                break;
            case R.id.categoryCard:
                Object id = v.getTag(); //Получаем id фразы из tag в корневой вьюшки phrasecard
                String idStr = id.toString(); //Переводим id в строку
                int idNum = (Integer.valueOf(idStr))-1; //Переводим id в int
                Bundle bundle = new Bundle();
                bundle.putInt("categoryID", idNum+1);
                phrasesFragment.setArguments(bundle);

                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction tx = fragmentManager.beginTransaction();
                tx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                tx.replace(R.id.root_frame, phrasesFragment).addToBackStack(null);
                tx.commit();
                break;
        }
    }

    /*
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
    */

    private Phrase getPhraseId(View v, int rootView) {
        Phrase phrase;
        Object id = Utils.getParentTill(v, rootView).getTag(); //Получаем id фразы из tag в корневой вьюшки phrasecard
        String idStr = id.toString(); //Переводим id в строку
        int idNum = (Integer.valueOf(idStr)); //Переводим id в int
        phrase = phraseDao.queryBuilder().where(PhraseDao.Properties.Id.eq(idNum)).list().get(0); //Получаем фразу
        return phrase;
    }

    private Integer getPhraseFavorive(View v, int rootView) {
        Integer phraseFavorite;
        Object id = Utils.getParentTill(v, rootView).getTag(); //Получаем id фразы из tag в корневой вьюшки phrasecard
        String idStr = id.toString(); //Переводим id в строку
        int idNum = (Integer.valueOf(idStr)); //Переводим id в int
        phraseFavorite = phraseDao.queryBuilder().where(PhraseDao.Properties.Id.eq(idNum)).list().get(0).getFavorite(); //Получаем значение, которое указывает, что элемент в избранном
        return phraseFavorite;
    }

    public void onDeletePhrase(boolean deleteOk, int position) {
        if(deleteOk) {
            if (mViewPager.getCurrentItem() == 0) {
                favoriteFragment.refresh(null, false);
                phrasesFragment.refresh();
                searchFragment.refresh();
                ownPhrasesFragment.refreshForDelete(position);
            }
            if (mViewPager.getCurrentItem() == 1 && phrasesFragment.isVisible()) {
                favoriteFragment.refresh(null, false);
                ownPhrasesFragment.refresh();
                searchFragment.refresh();
                phrasesFragment.refreshForDelete(position);
            }
            if (mViewPager.getCurrentItem() == 1 && searchFragment.isVisible()) {
                favoriteFragment.refresh(null, false);
                phrasesFragment.refresh();
                ownPhrasesFragment.refresh();
                searchFragment.refreshForDelete(position);
            }
            if (mViewPager.getCurrentItem() == 2) {
                favoriteFragment.refreshForDelete(position);
                phrasesFragment.refresh();
                ownPhrasesFragment.refresh();
                searchFragment.refresh();
            }
        }

    }

    public void onEditPhrase(boolean editOk, int position, Phrase phraseEdit) {
        if(editOk) {
            if (mViewPager.getCurrentItem() == 0) {
                favoriteFragment.refresh(null, false);
                phrasesFragment.refresh();
                searchFragment.refresh();
                ownPhrasesFragment.refreshForEdit(phraseEdit, position);
            }
            if (mViewPager.getCurrentItem() == 1 && phrasesFragment.isVisible()) {
                favoriteFragment.refresh(null, false);
                ownPhrasesFragment.refresh();
                searchFragment.refresh();
                phrasesFragment.refreshForEdit(phraseEdit, position);
            }
            if (mViewPager.getCurrentItem() == 1 && searchFragment.isVisible()) {
                favoriteFragment.refresh(null, false);
                phrasesFragment.refresh();
                ownPhrasesFragment.refresh();
                searchFragment.refreshForEdit(phraseEdit, position);
            }
            if (mViewPager.getCurrentItem() == 2) {
                favoriteFragment.refreshForEdit(phraseEdit, position);
                phrasesFragment.refresh();
                ownPhrasesFragment.refresh();
                searchFragment.refresh();
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 1 && (phrasesFragment.isVisible() || searchFragment.isVisible())) { //Если мы находимся на 1 вкладке(считать с нуля) и phrasesFragment виден
            if (searchFragment.isVisible()) {
                searchView.setIconified(true);
                //settingsItem.setVisible(true);
                talkItem.setVisible(false);
                searchView.onActionViewCollapsed();
            }
            getSupportFragmentManager().popBackStack(); //то переходим на фрагмент со стека
        } else {
            finish(); //закрываем активность
        }
    }

    private void createFragments() {
        categoriesFragment = new CategoriesFragment();
        ownPhrasesFragment = new OwnPhrasesFragment();
        favoriteFragment = new FavoriteFragment();
        phrasesFragment = new PhrasesFragment();
        rootFragment = new RootFragment();
        searchFragment = new SearchFragment();
    }

    private void setAdapterFragments(TabsPagerAdapter tabsPagerAdapter) {
        tabsPagerAdapter.setPhrasesFragment(phrasesFragment);
        tabsPagerAdapter.setCategoriesFragment(categoriesFragment);
        tabsPagerAdapter.setFavoriteFragment(favoriteFragment);
        tabsPagerAdapter.setOwnPhrasesFragment(ownPhrasesFragment);
        tabsPagerAdapter.setRootFragment(rootFragment);
    }

    private void setListenersForAdapters() {
        ownPhrasesFragment.setOnItemClickListener(this);
        favoriteFragment.setOnItemClickListener(this);
        categoriesFragment.setOnItemClickListener(this);
        phrasesFragment.setOnItemClickListener(this);
        searchFragment.setOnItemClickListener(this);
    }
}
