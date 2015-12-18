package com.r_mobile.phasebook.greenDao;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.r_mobile.phasebook.Copy;
import com.r_mobile.phasebook.greenDao.DaoMaster;
import com.r_mobile.phasebook.greenDao.DaoSession;

import java.io.IOException;

/**
 * Created by nemol on 12.11.2015.
 */
public class PhraseBookApp extends Application {

    private SQLiteDatabase db;

    private DaoMaster daoMaster;
    public DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        Copy copyDb = new Copy();
        boolean dbExist = copyDb.checkDataBase();
        if (dbExist) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getApplicationContext(), "phrases", null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
        } else {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getApplicationContext(), "phrases", null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            try {
                copyDb.copyDataBase(getApplicationContext());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
