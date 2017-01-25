package com.djsoft.djalarm;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import java.io.Serializable;
import android.net.Uri;
import android.provider.Settings;
import java.util.Calendar;
import com.djsoft.djalarm.util.Alarm;

/**
 * Created by djwolf on 12/28/2016.
 */

public class DJA_ALARM_SERVICE extends Service implements Serializable {
    private final IBinder mBinder = new LocalBinder();
    public static boolean running;
    private static Alarm al;
    private Thread serviceLoop;
    public static Thread alarmLoop;
    public static boolean alarmTime;
    private SharedPreferences settings;
    private Uri alarmToneURI;
    private Ringtone alarmTone;
    public static boolean playAlarm;

    public class LocalBinder extends Binder {
        DJA_ALARM_SERVICE getService() {
            return DJA_ALARM_SERVICE.this;
        }
    }

    @Override
    public void onCreate() {
        running = true;
        settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        try
        {
            alarmToneURI = Uri.parse(settings.getString("alarm_preference",null));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        alarmTone = RingtoneManager.getRingtone(getApplicationContext(),alarmToneURI);
        System.out.println(alarmTone);
    }

    public void soundAlarm()
    { //when the alarm is meant to go off, this function will be called
        Intent notificationIntent = new Intent(this, DJA_STOP_ALARM.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification alNotify = new Notification.Builder(this)
                .setContentTitle("DJ Alarm")
                .setContentText("Alert: Your alarm is now going off!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .getNotification();
        startForeground(3004,alNotify);
        final DJA_ALARM_SERVICE dja_s = this;

        new Thread()
        {
            @Override
            public void run()
            {
                alarmTime = true;
                alarmLoop = this;
                boolean runLoop = true;
                while (runLoop)
                {

                    try
                    {
                        if (!alarmTone.isPlaying()) {
                            alarmTone.play();
                        }
                    } catch (NullPointerException e)
                    {
                        System.out.println("Application crash due to ringtone data error prevented - reverting to default ringtone");
                        e.printStackTrace();
                        alarmTone = RingtoneManager.getRingtone(getApplicationContext(), Settings.System.DEFAULT_RINGTONE_URI);
                    }
                    try
                    {
                        Thread.sleep(2000);
                    } catch (InterruptedException e)
                    {
                        System.out.println("Alarm loop interrupted");
                        e.printStackTrace();
                        playAlarm = false;
                        runLoop = false;
                        try
                        {
                            alarmTone.stop();
                        } catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                }
                System.out.println("Running this section");
                alarmTime = false;
                stopService(new Intent(DJA_ALARM_SERVICE.this,DJA_ALARM_SERVICE.class));
            }
        }.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        al = (Alarm) intent.getSerializableExtra("Alarm");
        Intent notificationIntent = new Intent(this, DJA_MAIN.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new Notification.Builder(this)
                .setContentTitle("DJ Alarm")
                .setContentText("Waiting for alarm time")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .getNotification();
        startForeground(3004, notification);
        new Thread() {
            @Override
            public void run() {
                serviceLoop = this;
                boolean run = true;
                boolean alarmTime = false;
                while (run) {
                    System.out.println("Alarm service is running");
                    Calendar ca = Calendar.getInstance();
                    if (al.getTime()[0] == ca.get(Calendar.HOUR_OF_DAY) && al.getTime()[1] == ca.get(Calendar.MINUTE)) {
                        System.out.println("ALARM TIME!!!");
                        run = false;
                        alarmTime = true;
                    }
                    try {
                        if (alarmTime)
                            interrupt();
                        Thread.sleep(5000);
                    } catch (Exception e) {
                        if (alarmTime)
                            soundAlarm();
                        e.printStackTrace();
                        run = false;
                        System.out.println("Service Loop Interrupted and stopped");
                    }
                }
            }
        }.start();
        return START_STICKY;
    }

    public static void setAlarm(Alarm a) {
        al = a;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        System.out.println("Service Destroyed");
        stopForeground(true);
        if (serviceLoop != null)
            serviceLoop.interrupt();
        if (alarmLoop != null)
            alarmLoop.interrupt();
        running = false;
    }
}