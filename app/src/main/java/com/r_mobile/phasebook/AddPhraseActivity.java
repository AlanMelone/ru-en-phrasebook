package com.r_mobile.phasebook;

import android.content.Intent;
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

        //Создаем адаптер
        SpinnerAddAdapter spinnerAddAdapter = new SpinnerAddAdapter(this, android.R.layout.simple_list_item_1, categoryList);

        //Присваиваем адаптер спинеру
        spinnerCategories.setAdapter(spinnerAddAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddPhrase:
                long categoryID = (long) spinnerCategories.getSelectedView().getTag(); //Получаем ID категории из спинера

                //Создаем фразу и заполняем её
                Phrase phrase = new Phrase();
                phrase.setCategoryId(categoryID);
                phrase.setPhrase(etPhrase.getText().toString());
                phrase.setFavorite(0);
                phrase.setOwn(1);

                daoSession.getPhraseDao().insert(phrase); //И добавляем фразу в таблицу

                //Создаем перевод и заполняем его
                Translate translate = new Translate();
                translate.setPhraseId(phrase.getId()); //Получаем ID фразы
                translate.setLanguage("English");
                translate.setContent(etTranslate.getText().toString());
                translate.setTranscription(etTranscription.getText().toString());

                daoSession.getTranslateDao().insert(translate); //Добавляем перевод в таблицу

                //Переходим на MainActivity
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

                break;
        }
    }
}
