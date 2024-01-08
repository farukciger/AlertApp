package com.alert.alertapp;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
public class AlertTime extends AppCompatActivity {
    public TextView txt;public String title; public int id;
    String subTitle;
    public TextView txt1;
    private final BroadcastReceiver stopAppReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Buradaki receiver telefon hareket ettiğinde tetikleniyor ve uygulamayı kapatıyor.
            AlertProvider.myDb=new DataBase(context);
            AlertProvider db=new AlertProvider(context);
            AlertProvider.openDb();
            if (intent != null && intent.getAction() != null) {
                if (intent.getAction().equals("com.alert.shake_detected")) {
                    stopService(new Intent(context, AlertService.class));
                    db.deleteAlert(id);//veri tabanından alarmı sil
                    finishAffinity();// Aktiviteyi sonlandır
                    System.exit(0);
                }
            }
            AlertProvider.closeDb();
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(stopAppReceiver);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*Bu metod başladğında receiverdan gelen id ile veritabanından ilgili
        * alarmın verilerini çekiyor.*/
        super.onCreate(savedInstanceState);
        AlertProvider.myDb=new DataBase(this);
        id=getIntent().getIntExtra("id",-1);
        AlertProvider db=new AlertProvider(this);
        AlertProvider.openDb();
        setContentView(R.layout.activity_alert_time);
        String aciklama="";String saat="";
        String baslik="";
        if (id!=-1){
            String[] columns={"id,baslik,aciklama,ringtone,saat"};
            Cursor cursor=db.tquery("alarm",columns,"id="+id,null,null,null,null);
            if (cursor!=null && cursor.moveToFirst()){
                //veri tabanından alarm ile ilgili bilgileri çekme
                        baslik=cursor.getString(cursor.getColumnIndex("baslik"));
                        aciklama=cursor.getString(cursor.getColumnIndex("aciklama"));
                        saat=cursor.getString(cursor.getColumnIndex("saat"));
            }
        }
        Button alertButton = findViewById(R.id.alertButton);
        txt=findViewById(R.id.alertTimetxt);
        txt1=findViewById(R.id.alertTimetxt1);
        //Başlık kalın alt başlığı ince yazmak içi HTML markupu kullandım
        String text="<b><big>"+baslik+"</big></b><br/><br/>" +
                "<b>"+saat+"</b>";
        title= String.valueOf(Html.fromHtml(text));
        txt1.setText(title);
        subTitle=aciklama;
        txt.setText(subTitle);
        IntentFilter filter = new IntentFilter(ShakeService.ACTION_SHAKE_DETECTED);
        registerReceiver(stopAppReceiver, filter);
        startService(new Intent(this, ShakeService.class));
        //Burda telefonun hareketini dinlemek için shake servisi başlattım.
        alertButton.setOnClickListener(new View.OnClickListener()
        /*Bu butonu emülatörde sunmak için ekstra ekledim emülatörde telefonun hareketini dinleyemeceiğimiz için
        * onun yerine buton kullandım telefon hareket ettiğindeki işlemler ile butona basıldğındaki işlemler aynı
        * */
        {
            @Override
            public void onClick(View view) {
                AlertProvider.openDb();
                stopService(new Intent(AlertTime.this, AlertService.class));
                db.deleteAlert(id);
                AlertProvider.closeDb();
                finish();
                System.exit(0);
            }
        });
        AlertProvider.closeDb();
    }
}