package com.r_mobile.phasebook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by AlanMelone on 11.11.2015.
 */

public class Copy {


    public Copy() {

    }

    public boolean checkDataBase() {
        SQLiteDatabase checkDB = null;

        try{
            String myPath = "/data/data/com.r_mobile.phasebook/databases/" + "phrases";
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }catch(SQLiteException e){
            //база еще не существует
        }

        if(checkDB != null){
            checkDB.close();
        }

        return checkDB != null;
    }

    public void copyDataBase(Context context) throws IOException {
        InputStream myInput = context.getAssets().open("categories.sqlite");

        String outFileName = "/data/data/com.r_mobile.phasebook/databases/" + "phrases";

        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }
}
