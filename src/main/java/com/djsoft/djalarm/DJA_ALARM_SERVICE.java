package com.djsoft.djalarm;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * Created by djwolf on 12/28/2016.
 */

public class DJA_ALARM_SERVICE extends Service {
    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        DJA_ALARM_SERVICE getService() {
            return DJA_ALARM_SERVICE.this;
        }
    }

    @Override
    public void onCreate()
    {
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId)
    {
        Intent notificationIntent = new Intent(this, DJA_MAIN.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new Notification.Builder(this)
            .setContentTitle("DJ Alarm")
            .setContentText("DJ Alarm is running and is waiting to sound the alarm.")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .build();

        startForeground(3004, notification);
        new Thread()
        {
            public void run()
            {
                while (true)
                {
                    System.out.println("Alarm Serivce is running");
                    try {
                        Thread.sleep(5000);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }
}
