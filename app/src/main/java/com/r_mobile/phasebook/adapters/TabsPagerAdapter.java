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
    private final FragmentManager fragmentManager;
    private Fragment fragmentTemp;

    OwnPhrasesFragment ownPhrasesFragment;
    CategoriesFragment categoriesFragment;
    FavoriteFragment favoriteFragment;
    PhrasesFragment phrasesFragment;

    public TabsPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabs) {
        super(fm);

        this.fragmentManager = fm;
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabs;
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                return ownPhrasesFragment.newInstance(); //ownPhrasesFragment
            case 1:
                if (fragmentTemp == null) {
                    fragmentTemp = categoriesFragment.newInstance(new CategoriesFragmentListener() {
                        @Override
                        public void onSwitchToNextFragment() {
                            fragmentManager.beginTransaction().remove(fragmentTemp).commit();
                            fragmentTemp = phrasesFragment.newInstance();
                            notifyDataSetChanged();
                        }
                    });
                }
                return fragmentTemp; //categoriesFragment+phrasesFragment
            case 2:
                return favoriteFragment.newInstance(); //favoriteFragment
        }
        return null;
    }

    @Override
    public int getItemPosition(Object object)
    {
        if (object instanceof CategoriesFragment && fragmentTemp instanceof PhrasesFragment)
            return POSITION_NONE;
        return POSITION_UNCHANGED;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
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

    public void setPhrasesFragment(PhrasesFragment phrasesFragment) {
        this.phrasesFragment = phrasesFragment;
    }

    public OwnPhrasesFragment getOwnPhrasesFragment() { return ownPhrasesFragment; }

    public CategoriesFragment getCategoriesFragment() { return categoriesFragment; }

    public FavoriteFragment getFavoriteFragment() { return favoriteFragment; }

    public PhrasesFragment getPhrasesFragment() { return phrasesFragment; }
}
