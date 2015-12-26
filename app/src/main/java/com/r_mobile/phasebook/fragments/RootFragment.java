package com.r_mobile.phasebook.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.r_mobile.phasebook.R;

/**
 * Created by r-mobile on 26.12.2015.
 */

public class RootFragment extends Fragment {

    private CategoriesFragment categoriesFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		/* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.root_fragment, container, false);

        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
		/*
		 * When this container fragment is created, we fill it with our first
		 * "real" fragment
		 */
        transaction.replace(R.id.root_frame, categoriesFragment);
        transaction.commit();

        return view;
    }

    public void setCategoriesFragment(CategoriesFragment categoriesFragment) {
        this.categoriesFragment = categoriesFragment;
    }
}
