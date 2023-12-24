package com.alert.alertapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {


    public DataBase(Context c){

        super(c,"alarm",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String sql="create table alarm(id integer primary key,'autoincrement',baslik text,aciklama text,tarih text,saat text,sure number,ringtone text,realtime text)";
            sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("CREATE TABLE backup_table AS SELECT * FROM " + "alerts1");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "alerts1");
        onCreate(sqLiteDatabase);
        sqLiteDatabase.execSQL("INSERT INTO " + "alerts1" + " SELECT * FROM backup_table");
    }
    public  boolean isDataExist(String data) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + "alarm" + " WHERE " + "realtime" + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{data});
        boolean exist = cursor.getCount() > 0;
        cursor.close();
        return exist;
    }
}
