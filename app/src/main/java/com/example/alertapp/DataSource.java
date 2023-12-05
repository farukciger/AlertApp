package com.example.alertapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DataSource {
    SQLiteDatabase dB;
    DataBase myDb;
    public DataSource(Context c){
        myDb=new DataBase(c);
    }
    public void openDb(){
        dB=myDb.getWritableDatabase();

    }
    public void closeDb(){
        myDb.close();
    }
    public void addAlert(Alerts alert){
        ContentValues val=new ContentValues();
        val.put("baslik",alert.getBaslik());
        val.put("aciklama",alert.getAciklama());val.put("tarih",alert.getTarih());val.put("saat",alert.getSaat());
        long id=dB.insert("alerts",null,val);
        alert.setId(id);
        dB.close();;
    }
    public void removeAlert(Alerts alert){
        long id=alert.getId();
        dB.delete("alerts","id="+id,null);
    }
    public Cursor getData() {
        SQLiteDatabase db = myDb.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + "alerts", null);
    }
}
