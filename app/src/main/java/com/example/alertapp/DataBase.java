package com.example.alertapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBase extends SQLiteOpenHelper {


    public DataBase(Context c){
        super(c,"alerts",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String sql="create table alerts(id integer primary key,'autoincrement',baslik text,aciklama text,tarih text,saat text)";
            sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
