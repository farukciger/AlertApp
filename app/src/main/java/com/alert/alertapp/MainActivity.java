package com.alert.alertapp;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
public class MainActivity extends AppCompatActivity implements RCClickInterface {
    private static CustomAdapter customAdapter;
    private RecyclerView recyclerView;
    public static  AlertProvider source;
    public static List<Alerts> alarms=AlertProvider.getAlarms();
    public static String uId;
    public final static Uri CONTENT_URI=AlertProvider.CONTENT_URI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AlertProvider.myDb=new DataBase(this);//veri tabanı nesnelerni oluşturuyoruz.
        source=new AlertProvider(this);
        AlertProvider.openDb();
        Button addButton =(Button) findViewById(R.id.addButton);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        customAdapter = new CustomAdapter(this, null,this);
        recyclerView.setAdapter(customAdapter);
        if(UptadeAlert.isUptade)//eğer güncelleme olduysa tüm alarmları kontrol ediyoruz.
        {
            cancelAllAlarms(this);
            this.setALarm();
            UptadeAlert.isUptade=false;
        }
        this.setALarm();//bu metod veri tabanındaki tüm alarmları alarmManager ile set ediyor.
        loadData();//ekrana veri tabanındaki verileri getiriyor.
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddAlert.subText=null;
                AddAlert.text=null;
                Intent intent=new Intent(MainActivity.this,AddAlert.class);
                startActivity(intent);
            }
        });

    }//on create
    public static void loadData() {
        Cursor cursor = source.getData();
        customAdapter.swapCursor(cursor);
    }
    @Override
    public void onItemClick(String content) {
        //alarma tıklandğında o alarm ile ilgili verileri getiriyor
        String[] columns = {"baslik,aciklama,tarih,saat,sure,ringtone,realtime"};
        uId=content;
        getContentResolver().query(CONTENT_URI,columns,null,null,null);
        Intent intent=new Intent(MainActivity.this,UptadeAlert.class);
        startActivity(intent);
    }
    @Override
    public void onItemLongClick(String content){
        //eğer alarma basılı tutulursa o alarm siliniyor.
        getContentResolver().delete(CONTENT_URI,"id="+content,null);
        cancelAllAlarms(this);
        setALarm();
        loadData();
    }
    public  void setALarm(){
        //burda alarm kuruyoruz
        alarms.clear();//bu bir arraylist metod çağrıldğında arraylist önce silinyor.
        alarms = AlertProvider.getAlarms();//sonra tekrar dolduruluyor
        //bu sayede aynı alarm sadece 1 kez ekleniyor.
        if(alarms.isEmpty())//eğer alarm yoksa metod sonlanıyor
            return;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //alarm manager nesnemiz.
        for (Alerts alarm : alarms) {
            // Alarmı ayarlamadan önce, zamanı kontrol etmek önemlidir
            if (isFutureAlarm(alarm.getRealTime()))
            //alarm gelecek bir tarihte ise set ediliyor.
            {
                Intent intent = new Intent(this, AlertReceiver.class);
                intent.setAction("com.alert.BROADCAST_ACTION");
                intent.putExtra("AlertTime", "com.alert.alertapp");
                // requestCode'yi benzersiz bir şekilde ayarlıyoruz.
                int requestCode = (int) alarm.getId();
                //get id aslında veri tabanındaki primary keyimiz.
                intent.putExtra("request_code", requestCode);
                String ringtone=alarm.getRingTone();
                intent.putExtra("ringtone",ringtone);
             PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                // Belirtilen tarih ve saatte alarmı ayarla
                Calendar calendar = Calendar.getInstance();
                /*Burda tarihi biz string olarak veri tabanında tuttuğumuz için
                * integer a çevirmemiz gerekiyor.
                * SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                * yukardaki formatta tutuluyor.*/
                int year=Integer.parseInt(alarm.getRealTime().substring(6,10));
                int month=Integer.parseInt(alarm.getRealTime().substring(3,5))-1;
                int day=Integer.parseInt(alarm.getRealTime().substring(0,2));
                int hour=Integer.parseInt(alarm.getRealTime().substring(11,13));
                int minute=Integer.parseInt(alarm.getRealTime().substring(14,16));
                calendar.set(year,month,day,hour,minute);
                //daha sonrasında alarmManageri kuruyoruz.
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
    }
    public static void cancelAllAlarms(Context context) {
        ArrayList<Integer> requestCodes=new ArrayList<>();
        int maxAlarmCount=alarms.size();
        for (int i = 0; i <maxAlarmCount ; i++) {
            requestCodes.add((int )alarms.get(i).getId());
        }
        for (int i = 1; i <= maxAlarmCount; i++) {
            Intent alarmIntent = new Intent(context, AlertReceiver.class);
            cancelAlarm(context, alarmIntent, i);
        }
    }
    public static void cancelAlarm(Context context, Intent intent, int requestCode) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
    public static boolean isFutureAlarm(String dateString) {
        try {
            // String tarih ve saat bilgilerini Date objesine çevir
           SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date alarmDate = dateFormat.parse(dateString);
            // Şu anki zamanı al
            Calendar now = Calendar.getInstance();
            // Verilen tarih gelecekte bir zamanı temsil ediyorsa true döndür
            return alarmDate.getTime() > now.getTimeInMillis();
        } catch (ParseException e) {
            // Tarih formatı hatalıysa, bir hata durumu yönetilebilir
            e.printStackTrace();
            return false;
        }
    }

}