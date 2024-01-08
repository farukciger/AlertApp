package com.alert.alertapp;
import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
public class AlertProvider extends ContentProvider {
    public static SQLiteDatabase dB;

    public static DataBase myDb;
    public static List<Alerts> alarms = new ArrayList<>();

    public AlertProvider() {
    }//hata verdiği için boş constructor da yapmamız gerekti.

    public AlertProvider(Context c) {

        myDb = new DataBase(c);
    }

    public static void openDb() {
        dB = myDb.getWritableDatabase();
    }

    public static void closeDb() {
        myDb.close();
    }

    public Cursor getData() {
        SQLiteDatabase db = myDb.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + "alarm", null);
    }

    public static ContentValues val;
    static final String CONTENT_AUTHORITY = "com.alert.alertapp.alertprovider";
    static final String PATH_ALERTS1 = "alarm";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ALERTS1);

    static final UriMatcher matcher;

    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(CONTENT_AUTHORITY, PATH_ALERTS1, 1);
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        switch (matcher.match(uri)) {
            case 1:
                SQLiteDatabase db = myDb.getReadableDatabase();
                String selection = "id" + "=?";
                String[] selectionArgs = {MainActivity.uId};
                Cursor cursor = db.query("alarm", strings, selection, selectionArgs, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int baslik = cursor.getColumnIndex("baslik");
                    String upbaslik = cursor.getString(baslik);
                    int aciklam = cursor.getColumnIndex("aciklama");
                    String upaciklama = cursor.getString(aciklam);
                    int date = cursor.getColumnIndex("tarih");
                    String udt = cursor.getString(date);
                    int time = cursor.getColumnIndex("saat");
                    String utime = cursor.getString(time);
                    int leftTim = cursor.getColumnIndex("sure");
                    int leftTime = cursor.getInt(leftTim);
                    int ringTon = cursor.getColumnIndex("ringtone");
                    String ringTone = cursor.getString(ringTon);
                    int rtm = cursor.getColumnIndex("realtime");
                    String realTime=cursor.getString(rtm);
                    cursor.close();
                    UptadeAlert.utext = upbaslik;
                    UptadeAlert.usubText = upaciklama;
                    UptadeAlert.utime = utime;
                    UptadeAlert.udate = udt;
                    UptadeAlert.uLeftTime = leftTime;
                    UptadeAlert.uRingtone = ringTone;
                    UptadeAlert.hour = Integer.parseInt(utime.substring(0, 2));
                    UptadeAlert.minute = Integer.parseInt(utime.substring(3, 5));UptadeAlert.uRealTime=realTime;
                }
                return cursor;
            default:
                throw new IllegalArgumentException("Bilinmeyen Uri " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        switch (matcher.match(uri)) {
            case 1:
                openDb();
                String text = AddAlert.text;
                String subText = AddAlert.subText;
                String d = AddAlert.date;
                String t = AddAlert.time;
                String ringTone = AddAlert.ringTone;
                String realTime = AddAlert.realDateTime;
                int leftTime = AddAlert.leftTime;
                Alerts alert = new Alerts(text, subText, d, t, leftTime, ringTone, realTime);
                val = new ContentValues();
                val.put("baslik", alert.getBaslik());
                val.put("aciklama", alert.getAciklama());
                val.put("tarih", alert.getTarih());
                val.put("saat", alert.getSaat());
                val.put("sure", alert.getLeftTime());
                val.put("ringtone", alert.getRingTone());
                val.put("realtime", realTime);
                long ID = dB.insert("alarm", null, val);
                alert.setId(ID);
                AddAlert.alerts.add(alert);
                closeDb();
                if (ID > 0) {
                    Uri uri1 = ContentUris.withAppendedId(CONTENT_URI, ID);
                    return uri1;
                }
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int rowDelete = 0;
        switch (matcher.match(uri)) {
            case 1:
                rowDelete = dB.delete("alarm", s, strings);
                dB.close();
                break;
            default:
                throw new IllegalArgumentException("Bilinmeyen Uri:" + uri);

        }
        return rowDelete;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase dB = myDb.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("baslik", UptadeAlert.utext);
        values.put("aciklama", UptadeAlert.usubText);
        values.put("tarih", UptadeAlert.udate);
        values.put("saat", UptadeAlert.utime);
        values.put("sure", UptadeAlert.uLeftTime);
        values.put("ringtone", UptadeAlert.uRingtone);
        values.put("realtime", UptadeAlert.uRealTime);
        dB.update("alarm", values, "id=" + MainActivity.uId, null);
        dB.close();
        return 0;
    }

    public static boolean isDatabaseEmpty() {
        // Hataya neden olabilecek bir durumu kontrol et
        if (dB == null) {
            return true;
        } else
            return false;
    }
    public static Cursor getAllAlarms(){
        SQLiteDatabase db=myDb.getReadableDatabase();
        return db.query("alarm", null, null, null, null, null, null);
    }

    @SuppressLint("Range")
    //Burdaki fonksiyon veri tabanındaki tüm alarmları bir listeye ekliyor.
    //Daha sonra Alarmmanageri set etmek içim bu listeyi kullancaz.
    public static List<Alerts> getAlarms() {
        if (!(AlertProvider.isDatabaseEmpty())){
            alarms.clear();
            Cursor cursor=getAllAlarms();
            if (cursor.moveToFirst()){
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("baslik"));
                String aciklama=cursor.getString(cursor.getColumnIndex("aciklama"));
                String tarih=cursor.getString(cursor.getColumnIndex("tarih"));
                String saat=cursor.getString(cursor.getColumnIndex("saat"));
                int leftTime=Integer.parseInt(cursor.getString(cursor.getColumnIndex("sure")));
                String ringtone=cursor.getString(cursor.getColumnIndex("ringtone"));
                String realtime=cursor.getString(cursor.getColumnIndex("realtime"));
                Alerts alert=new Alerts(title,aciklama,tarih,saat,leftTime,ringtone,realtime);
                alert.setId(id);
                alarms.add(alert);
            } while (cursor.moveToNext());
            cursor.close();
        }}
        return alarms;
    }
    public Cursor tquery(String table,String[] columns,String selection,String[]selectionArg,String groupBy,String havingBy,String orderBy){
        return dB.query(table,columns,selection,selectionArg,groupBy,havingBy,orderBy);
    }
    public void deleteAlert(int id){
        dB.delete("alarm","id="+id,null);
        dB.close();
    }
}

