package com.alert.alertapp;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import java.io.IOException;
public class AlertService extends Service {
    /*Burdaki servis yayın geldiği zaman başlıyor ve kullanıcıya bildirim, ses ve titreşim gönderiyor
    *Bildirime tıklandığında alarm ile ilgili bilgiler ekrana geliyor*/
    String baslik;String aciklama;String ringtone;String saat; MediaPlayer mediaPlayer;
    private Vibrator vibrator;//titreşim nesnemiz
    int id;

    @Override
    public void onCreate() {
        super.onCreate();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build());
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("Range")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        id = intent.getIntExtra("id",-1);
        String[] columns={"id,baslik,aciklama,ringtone,saat"};
        AlertProvider.myDb=new DataBase(this);
        AlertProvider db=new AlertProvider(this);
        AlertProvider.openDb();
        Cursor cursor=db.tquery("alarm",columns,"id="+id,null,null,null,null);
        cursor.moveToFirst();
            baslik=cursor.getString(cursor.getColumnIndex("baslik"));
            aciklama=cursor.getString(cursor.getColumnIndex("aciklama"));
            ringtone=intent.getStringExtra("ringtone");
            saat=cursor.getString(cursor.getColumnIndex("saat"));
            Uri r=Uri.parse(ringtone);

        try {
            mediaPlayer.setDataSource(this, r);
            mediaPlayer.setOnPreparedListener(mp -> {
                VibrationEffect vibrationEffect = VibrationEffect.createWaveform(new long[]{0, 1000, 1000}, 0);
                vibrator.vibrate(vibrationEffect);
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            });
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MediaPlayer", "setDataSource failed: " + e.getMessage());
        }
        AlertProvider.closeDb();
            showNotification(baslik, aciklama, ringtone, id);
       return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void showNotification(String baslik, String aciklama, String ringtone, int id) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Android 8.0 ve sonrasında bildirim kanalı oluşturun
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channelId", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Channel Description");
            notificationManager.createNotificationChannel(channel);
        }
        // Bildirim oluşturun
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channelId")
                .setContentTitle(baslik)
                .setContentText(aciklama)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(Uri.parse(ringtone));
        // Bildirime tıklanınca başlatılacak aktiviteyi belirleyin
        Intent notificationIntent = new Intent(this, AlertTime.class);
        notificationIntent.putExtra("baslik", baslik);
        notificationIntent.putExtra("aciklama", aciklama);
        notificationIntent.putExtra("ringtone", ringtone);
        notificationIntent.putExtra("id", id);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        // Bildirimi gönderin
        notificationManager.notify(id, builder.build());
    }

    @Override
    public void onDestroy() {
        //servis sonlandığında kaynakları ortadan kaldırıypr
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
        super.onDestroy();
        if (vibrator != null) {
            vibrator.cancel();
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
        }
        stopSelf();
    }

}