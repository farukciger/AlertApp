package com.alert.alertapp;
import static androidx.constraintlayout.widget.Constraints.TAG;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
public class ReminderService extends Worker {
    int id;int id1;int baslik;String upbaslik;
    int aciklam;String upaciklama;int date;
    String udt;int time;String utme;
    int ringTon;String ringTone;Cursor cursor;
    int rtm;String realTime;long currentTimeMillis; long nextExecutionTimeInMillis;
    Calendar systemTime;Date alertDate;Calendar alerTime;
    int result;
    public ReminderService(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    String[] columns = {"baslik,id,aciklama,tarih,saat,ringtone,realtime"};

    @NonNull
    @Override
    public Result doWork() {
        currentTimeMillis = System.currentTimeMillis();
       if (!(AlertProvider.isDatabaseEmpty())){
           cursor= AlertProvider.dB.query("alarm",columns,null,null,null,null,null);
           while (cursor!=null && cursor.moveToFirst()){
               id=cursor.getColumnIndex("id");
               id1=cursor.getInt(id);
               baslik=cursor.getColumnIndex("baslik");
               upbaslik=cursor.getString(baslik);
               aciklam=cursor.getColumnIndex("aciklama");
               upaciklama=cursor.getString(aciklam);
               date=cursor.getColumnIndex("tarih");
               udt=cursor.getString(date);
               time=cursor.getColumnIndex("saat");
               utme=cursor.getString(time);
               ringTon=cursor.getColumnIndex("ringtone");
               ringTone=cursor.getString(ringTon);
               rtm=cursor.getColumnIndex("realtime");
               realTime=cursor.getString(rtm);
               systemTime=Calendar.getInstance();
               alertDate=convertStringToDate(realTime);
               alerTime=convertDateToCalendar(alertDate);
               result=systemTime.compareTo(alerTime);
               if (result==0 ||result>0 ){
                   AlertProvider.dB.delete("alarm","id="+id1,null);
                   Handler handler = new Handler(Looper.getMainLooper());
                   handler.post(new Runnable() {
                       @Override
                       public void run() {
                           // UI thread içinde yapılması gereken işlemler
                           MainActivity.loadData();
                       }
                   });
                   break;
               }
               cursor.moveToNext();
           }
       }
        Log.d(TAG, "doWork: work");

        // İşlemleriniz
        // Sonraki çalışma zamanını kontrol etmek için örnek bir değer
         nextExecutionTimeInMillis = currentTimeMillis + TimeUnit.MINUTES.toMillis(1);

        // Tekrar süresini kontrol etmek için
        if (currentTimeMillis < nextExecutionTimeInMillis) {
            return Result.retry(); // İşlemler tamamlandı, ancak tekrar süresi dolmadı
        }

        return Result.success(); // İşlemler tamamlandı
    }
    private  Date convertStringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            // String ifadeyi Date nesnesine çevir
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    private  Calendar convertDateToCalendar(Date date) {
        // Date nesnesini Calendar nesnesine çevir
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
}