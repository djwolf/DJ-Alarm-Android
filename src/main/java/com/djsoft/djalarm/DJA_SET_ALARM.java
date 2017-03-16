package com.djsoft.djalarm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;
import com.djsoft.djalarm.util.Alarm;
import java.io.Serializable;
import java.util.Calendar;

public class DJA_SET_ALARM extends AppCompatActivity implements Serializable {
    private TimePicker setAlarmWidget;
    private Calendar c = Calendar.getInstance();
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);

        settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        setAlarmWidget = (TimePicker) findViewById(R.id.setTime);
        if (DJA_MAIN.alarm != null)
        {
            setAlarmWidget.setCurrentHour(DJA_MAIN.alarm.getTime()[0]);
            setAlarmWidget.setCurrentMinute(DJA_MAIN.alarm.getTime()[1]);
        } else
        {
            setAlarmWidget.setCurrentHour(settings.getInt("SETAL_HOUR",c.get(Calendar.HOUR_OF_DAY)));
            setAlarmWidget.setCurrentMinute(settings.getInt("SETAL_MINUTE",c.get(Calendar.MINUTE)));
        }
    }

    public void onClickOK(View v)
    {
        final DJA_SET_ALARM DJ_S_A = this;
        new Thread()
        {
            @Override
            public void run()
            {
                //if an alarm is going off, kill that service and make a new one
                if (DJA_ALARM_SERVICE.alarmTime)
                {
                    System.out.println("Alarm service running while alarm is going off - killing service and making new one");
                    new Thread()
                    {
                        @Override
                        public void run()
                        {
                            DJA_ALARM_SERVICE.alarmLoop.interrupt();
                        }
                    }.start();
                    while (DJA_ALARM_SERVICE.running) {}
                }

                int curDate = c.get(Calendar.DAY_OF_MONTH);
                int curHour = c.get(Calendar.HOUR_OF_DAY);
                int curMinute = c.get(Calendar.MINUTE);

                //figure out if the alarm should be set for this date or the next one
                if (setAlarmWidget.getCurrentHour() > curHour)
                {
                    System.out.println("set for today");
                } else if(setAlarmWidget.getCurrentHour() == curHour)
                {
                    if (setAlarmWidget.getCurrentMinute() <= curMinute)
                    {
                        System.out.println("Set for tomorrow");
                    } else
                    {
                        System.out.println("set for today");
                    }
                } else
                {
                    System.out.println("alarm set for tomorrow");
                }

                //store this in shared preferences for future use
                final SharedPreferences.Editor settingsEditor = settings.edit();
                settingsEditor.putInt("SETAL_HOUR",setAlarmWidget.getCurrentHour());
                settingsEditor.putInt("SETAL_MINUTE",setAlarmWidget.getCurrentMinute());
                settingsEditor.apply();

                final Alarm al = new Alarm(setAlarmWidget.getCurrentHour(),setAlarmWidget.getCurrentMinute(), getApplicationContext());
                DJA_MAIN.alarm = al;

                //wait for the service to be running before disabling the toggle button
                new Thread()
                {
                    @Override
                    public void run()
                    {
                        while (!DJA_ALARM_SERVICE.running) {}
                        runOnUiThread(new Runnable() { //because android wants me to, i have to do this within the UI thread
                            @Override
                            public void run() {
                                DJA_MAIN.alOnOffButton.setChecked(true);
                            }
                        });
                    }
                }.start();

                if (DJA_ALARM_SERVICE.running)
                {
                    System.out.println("Service is already running. Alarm reset");
                    DJA_ALARM_SERVICE.setAlarm(al);
                } else {
                    System.out.println("Service is not running");
                    startService(new Intent(DJ_S_A, DJA_ALARM_SERVICE.class).putExtra("Alarm", al));
                }
                finish();
            }
        }.start();
    }
}
