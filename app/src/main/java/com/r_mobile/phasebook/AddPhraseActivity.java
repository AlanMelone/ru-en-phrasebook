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
import com.r_mobile.PhraseBookApp;
import com.r_mobile.PhraseDao;

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
        LabelList = new ArrayList<String>();

        //Добавляем названия категорий в массив, чтобы не париться с адаптером
        for (int i = 0; i<categoryList.size(); i++) {
            LabelList.add(categoryList.get(i).getLabel());
        }

        spinnerCategories = (Spinner) findViewById(R.id.spinnerCategories);

        ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,LabelList);
        data.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerCategories.setAdapter(data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddPhrase:

                break;
        }
    }
}
