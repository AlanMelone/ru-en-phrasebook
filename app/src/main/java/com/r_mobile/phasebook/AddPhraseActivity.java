package com.r_mobile.phasebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.r_mobile.phasebook.greenDao.Category;
import com.r_mobile.phasebook.greenDao.CategoryDao;
import com.r_mobile.phasebook.greenDao.DaoSession;
import com.r_mobile.phasebook.greenDao.Phrase;
import com.r_mobile.phasebook.greenDao.PhraseBookApp;
import com.r_mobile.phasebook.greenDao.Translate;
import com.r_mobile.phasebook.adapters.SpinnerAddAdapter;

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

        //Добавляем кнопку назад на экш бар
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Определяем обьекты на вьюшке
        etPhrase = (EditText) findViewById(R.id.etPhrase);
        etTranscription = (EditText) findViewById(R.id.etTranscription);
        etTranslate = (EditText) findViewById(R.id.etTranslate);
        btnAddPhrase = (Button) findViewById(R.id.btnAddPhrase);
        spinnerCategories = (Spinner) findViewById(R.id.spinnerCategories);

        //Задаем обработчик кнопки
        btnAddPhrase.setOnClickListener(this);

        //Получаем таблицу категории
        daoSession = ((PhraseBookApp) getApplicationContext()).daoSession;
        categoryDao = daoSession.getCategoryDao();

        //Достаем из базы все записи о категориях
        categoryList = categoryDao.loadAll();

        //Создаем адаптер
        SpinnerAddAdapter spinnerAddAdapter = new SpinnerAddAdapter(this, android.R.layout.simple_list_item_1, categoryList);

        //Присваиваем адаптер спинеру
        spinnerCategories.setAdapter(spinnerAddAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddPhrase:
                if (isCorrect()) {
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
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isCorrect() {
        boolean error = false;
        if (etPhrase.getText().toString().equals("")) {
            Toast.makeText(this, "Введите фразу", Toast.LENGTH_SHORT).show();
            error = true;
        }
        if (etTranscription.getText().toString().equals("")) {
            Toast.makeText(this, "Введите транскрипцию", Toast.LENGTH_SHORT).show();
            error = true;
        }
        if (etTranslate.getText().toString().equals("")) {
            error = true;
            Toast.makeText(this, "Введите перевод", Toast.LENGTH_SHORT).show();
        }
        if (error == true) {
            return false;
        } else {
            return true;
        }
    }
}
