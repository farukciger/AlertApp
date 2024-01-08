package com.alert.alertapp;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /*Burdaki Receiver AlarmManagerdan yayın geldiği zaman tetikleniyor
        * ve Zil Sesi çalma işlemi için servisi başlatıyor.
        * Burdaki requestcode aslında alarm id si*/
        String ringtone;int requestCode;
        requestCode= intent.getIntExtra("request_code",-1);
        ringtone=intent.getStringExtra("ringtone");
        Intent newIntent = new Intent(context, AlertService.class);
        newIntent.putExtra("ringtone",ringtone);
        newIntent.putExtra("id",requestCode);
        context.startService(newIntent);//servis başlatılıyor.
    }

}
