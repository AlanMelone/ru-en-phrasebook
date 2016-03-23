package com.r_mobile.phasebook.fragments;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.r_mobile.phasebook.R;
import com.r_mobile.phasebook.Utils;
import com.r_mobile.phasebook.adapters.PhraseAdapter;
import com.r_mobile.phasebook.dialogs.DeleteDialog;
import com.r_mobile.phasebook.dialogs.EditDialog;
import com.r_mobile.phasebook.greenDao.Category;
import com.r_mobile.phasebook.greenDao.CategoryDao;
import com.r_mobile.phasebook.greenDao.DaoSession;
import com.r_mobile.phasebook.greenDao.Phrase;
import com.r_mobile.phasebook.greenDao.PhraseBookApp;
import com.r_mobile.phasebook.greenDao.PhraseDao;

import java.util.List;

/**
 * Created by r-mobile on 18.12.2015.
 */

public class PhrasesFragment extends Fragment implements View.OnClickListener {

    private DaoSession daoSession;
    private PhraseDao phraseDao;
    private CategoryDao categoryDao;

    List<Phrase> phraseList;
    RecyclerView recyclerView;
    PhraseAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    View.OnClickListener onItemClickListener;
    String mCategoryName;

    int categoryID;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_phrases, container, false);

        categoryID = getArguments().getInt("categoryID");

        daoSession = ((PhraseBookApp) getActivity().getApplicationContext()).daoSession;
        phraseDao = daoSession.getPhraseDao();
        categoryDao = daoSession.getCategoryDao();

        phraseList = phraseDao.queryBuilder().where(PhraseDao.Properties.CategoryId.eq(categoryID)).list();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_phrases_all);

        mCategoryName = categoryDao.queryBuilder().where(CategoryDao.Properties.Id.eq(categoryID)).list().get(0).getLabel();

        adapter = new PhraseAdapter(phraseList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);

        if (onItemClickListener != null) {
            adapter.setOnItemClickListener(onItemClickListener);
            adapter.setDeleteClickListener(this);
        }

        recyclerView.setAdapter(adapter);

        getActivity().setTitle(mCategoryName);

        return rootView;
    }

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        if (adapter != null) {
            adapter.setOnItemClickListener(onItemClickListener);
        }
    }

    public void refresh() {
        if (recyclerView !=null) {
            phraseList = phraseDao.queryBuilder().where(PhraseDao.Properties.CategoryId.eq(categoryID)).list();
            adapter.setmDataset(phraseList);
        }
    }

    public void refreshForDelete(int position) {
        adapter.deleteItem(position);
        phraseList = phraseDao.queryBuilder().where(PhraseDao.Properties.CategoryId.eq(categoryID)).list();
    }

    public void refreshForEdit(Phrase phraseEdit, int position) {
        adapter.editItem(phraseEdit, position);
        phraseList = phraseDao.queryBuilder().where(PhraseDao.Properties.CategoryId.eq(categoryID)).list();
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.scrollToPosition(0);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.ivMoreOptions:
                ImageView ivMoreOptions = (ImageView) v.findViewById(R.id.ivMoreOptions);
                PopupMenu popupMenu = new PopupMenu(getContext(), ivMoreOptions);
                popupMenu.inflate(R.menu.menu_card);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()) {
                            case R.id.deletePhrase:
                                Bundle bundleDelete = new Bundle();

                                bundleDelete.putLong("deletePhraseID", getPhraseId(v, R.id.phrasecardRoot));
                                bundleDelete.putInt("positionPhrase", recyclerView.getChildLayoutPosition(Utils.getParentTill(v, R.id.phrasecardRoot)));

                                DialogFragment dialogFragment = new DeleteDialog();
                                dialogFragment.setArguments(bundleDelete);
                                dialogFragment.show(getActivity().getFragmentManager(), "dialogFragment");
                                break;
                            case R.id.editPhrase:
                                Bundle bundleEdit = new Bundle();

                                bundleEdit.putLong("editPhraseID", getPhraseId(v, R.id.phrasecardRoot));
                                bundleEdit.putInt("positionPhrase", recyclerView.getChildLayoutPosition(Utils.getParentTill(v, R.id.phrasecardRoot)));

                                DialogFragment editFragment = new EditDialog();
                                editFragment.setArguments(bundleEdit);
                                editFragment.show(getActivity().getFragmentManager(), "editFragment");
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
                break;
        }
    }

    private long getPhraseId(View v, int rootView) {
        Phrase phrase;
        Object id = Utils.getParentTill(v, rootView).getTag(); //Получаем id фразы из tag в корневй вьюшки phrasecard
        String idStr = id.toString(); //Переводим id в строку
        int idNum = (Integer.valueOf(idStr)); //Переводим id в int
        phrase = phraseDao.queryBuilder().where(PhraseDao.Properties.Id.eq(idNum)).list().get(0); //Получаем фразу
        long phraseId = phrase.getId();
        return phraseId;
    }

    public String getmCategoryName() {
        return mCategoryName;
    }
}
