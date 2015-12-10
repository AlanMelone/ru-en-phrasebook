package com.r_mobile.phasebook;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Admin on 28.10.2015.
 */
public class TabsPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[];
    int NumbOfTabs;

    OwnPhrasesFragment ownPhrasesFragment;
    CategoriesFragment categoriesFragment;
    FavoriteFragment favoriteFragment;

    public TabsPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabs) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabs;
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                // Top Rated fragment activity
                return ownPhrasesFragment;
            case 1:
                // Games fragment activity
                return categoriesFragment;
            case 2:
                return favoriteFragment;
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return NumbOfTabs;
    }

    public OwnPhrasesFragment getOwnPhrasesFragment() {
        return ownPhrasesFragment;
    }

    public CategoriesFragment getCategoriesFragment() {
        return categoriesFragment;
    }

    public FavoriteFragment getFavoriteFragment() {
        return favoriteFragment;
    }

    public void setOwnPhrasesFragment(OwnPhrasesFragment ownPhrasesFragment) {
        this.ownPhrasesFragment = ownPhrasesFragment;
    }

    public void setCategoriesFragment(CategoriesFragment categoriesFragment) {
        this.categoriesFragment = categoriesFragment;
    }

    public void setFavoriteFragment(FavoriteFragment favoriteFragment) {
        this.favoriteFragment = favoriteFragment;
    }


}
