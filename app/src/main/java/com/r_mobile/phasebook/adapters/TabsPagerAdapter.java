package com.r_mobile.phasebook.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.r_mobile.phasebook.fragments.CategoriesFragment;
import com.r_mobile.phasebook.fragments.FavoriteFragment;
import com.r_mobile.phasebook.fragments.OwnPhrasesFragment;
import com.r_mobile.phasebook.fragments.PhrasesFragment;

/**
 * Created by r-mobile on 28.10.2015.
 */

public class TabsPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[];
    int NumbOfTabs;

    Fragment[] allFragments;
    OwnPhrasesFragment ownPhrasesFragment;
    CategoriesFragment categoriesFragment;
    FavoriteFragment favoriteFragment;
    PhrasesFragment phrasesFragment;

    public TabsPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabs) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabs;
        this.allFragments = new Fragment[NumbOfTabs];
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                return allFragments[0]; //ownPhrasesFragment
            case 1:
                return allFragments[1]; //categoriesFragment+phrasesFragment
            case 2:
                return allFragments[2]; //favoriteFragment
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }

    public void setFragment(int position, Fragment fragment) {
        allFragments[position] = fragment;
    }

    public Fragment getFragment(int position) {
        return allFragments[position];
    }

    public OwnPhrasesFragment getOwnPhrasesFragment() { return ownPhrasesFragment; }

    public CategoriesFragment getCategoriesFragment() { return categoriesFragment; }

    public FavoriteFragment getFavoriteFragment() { return favoriteFragment; }

    public PhrasesFragment getPhrasesFragment() { return phrasesFragment; }
}
