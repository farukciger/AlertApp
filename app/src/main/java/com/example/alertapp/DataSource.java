package com.example.alertapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DataSource {
    static SQLiteDatabase dB;
    static DataBase myDb;
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
    public static void removeAlert(String id){
        dB.delete("alerts","id="+id,null);
    }
    public Cursor getData() {
        SQLiteDatabase db = myDb.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + "alerts", null);
    }
    public static void query(String  key){
        MainActivity.uId=key;
        SQLiteDatabase db = myDb.getReadableDatabase();
        String[] columns = {"baslik,aciklama,tarih,saat"};
        String selection= "id"+"=?";
        String[] selectionArgs={key};
        Cursor cursor = db.query("alerts", columns, selection, selectionArgs, null, null, null);
        if (cursor!=null && cursor.moveToFirst()){
            int baslik=cursor.getColumnIndex("baslik");
            String upbaslik=cursor.getString(baslik);
            int aciklam=cursor.getColumnIndex("aciklama");
            String upaciklama=cursor.getString(aciklam);
            int date=cursor.getColumnIndex("tarih");
            String udt=cursor.getString(date);
            int time=cursor.getColumnIndex("saat");
            String utme=cursor.getString(time);
            cursor.close();
            UptadeAlert.utext=upbaslik;UptadeAlert.usubText=upaciklama;
            UptadeAlert.utime=utme;UptadeAlert.udate=udt;
        }
    }
    public static boolean updateValueForKey(String key) {
        SQLiteDatabase db = myDb.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("baslik",UptadeAlert.utext);values.put("aciklama",UptadeAlert.usubText);
        values.put("tarih",UptadeAlert.udate);values.put("saat",UptadeAlert.utime);
        // Güncelleme işlemi
        int rowsAffected = db.update("alerts", values, "id" + "=?", new String[]{key});
        db.close();
        // Eğer en az bir satır güncellendiyse, işlem başarılı kabul edilir
        return rowsAffected > 0;
    }
}
