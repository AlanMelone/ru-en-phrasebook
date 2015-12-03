package com.r_mobile.phasebook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.r_mobile.Category;
import com.r_mobile.CategoryDao;
import com.r_mobile.DaoSession;
import com.r_mobile.Phrase;
import com.r_mobile.PhraseBookApp;
import com.r_mobile.PhraseDao;
import com.r_mobile.Translate;

import java.util.ArrayList;
import java.util.List;

public class AddPhraseActivity extends AppCompatActivity implements View.OnClickListener {

    private DaoSession daoSession;
    private CategoryDao categoryDao;

    List<Category> categoryList;
    ArrayList<String> LabelList;
    Spinner spinnerCategories;

    EditText etPhrase;
    EditText etTranscription;
    EditText etTranslate;
    Button btnAddPhrase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phrase);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etPhrase = (EditText) findViewById(R.id.etPhrase);
        etTranscription = (EditText) findViewById(R.id.etTranscription);
        etTranslate = (EditText) findViewById(R.id.etTranslate);
        btnAddPhrase = (Button) findViewById(R.id.btnAddPhrase);
        btnAddPhrase.setOnClickListener(this);

        daoSession = ((PhraseBookApp) getApplicationContext()).daoSession;
        categoryDao = daoSession.getCategoryDao();

        categoryList = categoryDao.loadAll();

        spinnerCategories = (Spinner) findViewById(R.id.spinnerCategories);

        SpinnerAddAdapter spinnerAddAdapter = new SpinnerAddAdapter(this, android.R.layout.simple_list_item_1, categoryList);
        spinnerCategories.setAdapter(spinnerAddAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddPhrase:
                long categoryID = (long) spinnerCategories.getSelectedView().getTag();
                Phrase phrase = new Phrase();
                phrase.setCategoryId(categoryID);
                phrase.setPhrase(etPhrase.getText().toString());
                daoSession.getPhraseDao().insert(phrase);
                Translate translate = new Translate();
                translate.setPhraseId(phrase.getId());
                translate.setLanguage("English");
                translate.setContent(etTranslate.getText().toString());
                translate.setTranscription(etTranscription.getText().toString());
                daoSession.getTranslateDao().insert(translate);
                break;
        }
    }
}
