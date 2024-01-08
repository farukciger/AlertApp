package com.alert.alertapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ShakeService extends Service implements SensorEventListener {
    /*Bu servis telefondaki ivme sensörünü dinliyor eğer hareket olursa
    * ACTION_SHAKE_DEDECTED adlı bir yayın gönderiyor ve biz bu yayını
    * AlertTime aktivitesinde dinliyoruz. */
    private static final float SHAKE_THRESHOLD = 15.0f;//Hareket Hassasiyeti
    public static final String ACTION_SHAKE_DETECTED = "com.alert.shake_detected";

    private final BroadcastReceiver stopAppReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            stopSelf();
        }
    };

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private long lastUpdateTime;
    private float lastX, lastY, lastZ;

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (accelerometer != null) {
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
        IntentFilter filter = new IntentFilter(ACTION_SHAKE_DETECTED);
        registerReceiver(stopAppReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
        unregisterReceiver(stopAppReceiver);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            long timeDifference = currentTime - lastUpdateTime;

            if (timeDifference > 100) { // İki hareket arasında minimum süre
                lastUpdateTime = currentTime;

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                float deltaX = x - lastX;
                float deltaY = y - lastY;
                float deltaZ = z - lastZ;

                float speed = Math.abs(deltaX + deltaY + deltaZ) / timeDifference * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    // Cihaz sallandığında uygulama kapatma işlemini burada yapabilirsiniz.
                    Intent stopAppIntent = new Intent(ACTION_SHAKE_DETECTED);
                    sendBroadcast(stopAppIntent);
                    stopSelf(); // Servisi sonlandırabilirsiniz.
                }

                lastX = x;
                lastY = y;
                lastZ = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Sensor hassasiyet değişiklikleri burada ele alınabilir.
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

