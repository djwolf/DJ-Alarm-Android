package com.djsoft.djalarm;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.djsoft.djalarm.util.Alarm;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.lang.Thread;
import java.lang.Math;

public class DJA_MAIN extends AppCompatActivity {
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat getHour = new SimpleDateFormat("HH");
    private SimpleDateFormat getMinute = new SimpleDateFormat("mm");
    private TextView timeText;
    private Button setAlarmButton;
    private Button settingsButton;
    public static ToggleButton alOnOffButton;
    private String[] curTimeText;
    public static Alarm alarm;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dja_main);

        if (Math.random() >= 0.5)
        {
            AlertDialog.Builder warningBuilder = new AlertDialog.Builder(DJA_MAIN.this);
            warningBuilder.setMessage("This application is in the process of being developed. This is in no way the final version. The owner, djwolf, takes no responsibility for anything that happens in this stage of development. It is highly encouraged that you use a backup alarm that is stable along with testing this one.")
                    .setTitle("Testing Agreement");

            warningBuilder.setPositiveButton("I agree", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                }
            });
            AlertDialog alertDialog = warningBuilder.create();
            alertDialog.show();
        }

        //grab the settings and ensure persistence
        settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        System.out.println(settings.getInt("ALARM_HOUR",0));
        final SharedPreferences.Editor settingsEditor = settings.edit();
        if (!settings.contains("alOnOff"))
        {
            settingsEditor.putBoolean("alOnOff",false);
            settingsEditor.apply();
        }

        //define UI attributes
        timeText = (TextView) findViewById(R.id.timeText);
        alOnOffButton = (ToggleButton) findViewById(R.id.alOnOffButton);
        setAlarmButton = (Button) findViewById(R.id.setAlarmButton);
        settingsButton = (Button) findViewById(R.id.settingsButton);


        //set event handlers for UI elements
        alOnOffButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println("is this thing on");
                if (isChecked) {
                    if (!DJA_ALARM_SERVICE.running)
                    {
                        if (alarm == null)
                        {
                            Toast.makeText(getApplicationContext(),"Set an alarm before turning it on",Toast.LENGTH_SHORT).show();
                            alOnOffButton.setChecked(false);
                        } else
                        {
                            startService(new Intent(DJA_MAIN.this, DJA_ALARM_SERVICE.class).putExtra("Alarm", alarm));
                            settingsEditor.putBoolean("alOnOff",true);
                            settingsEditor.apply();
                        }
                    }
                } else {
                    System.out.println("the else thingy");
                    if (alarm != null)
                    {
                        stopService(new Intent(DJA_MAIN.this,DJA_ALARM_SERVICE.class));
                        settingsEditor.putBoolean("alOnOff",false);
                        settingsEditor.remove("ALARM_HOUR");
                        settingsEditor.remove("ALARM_MINUTE");
                        settingsEditor.remove("ALARM_DAY");
                        settingsEditor.remove("ALARM_MONTH");
                        settingsEditor.remove("ALARM_YEAR");
                        settingsEditor.apply();
                    }
                }
            }
        });
        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), DJA_SET_ALARM.class));
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), DJA_SETTINGS.class));
            }
        });

        //ensure persistence
        if (DJA_ALARM_SERVICE.running)
            alOnOffButton.setChecked(true);
        else {
            if (settings.contains("ALARM_YEAR"))
            {
                alarm = new Alarm(settings.getInt("ALARM_HOUR",0),settings.getInt("ALARM_MINUTE",0),settings.getInt("ALARM_DAY",0),settings.getInt("ALARM_MONTH",0),settings.getInt("ALARM_YEAR",0),getApplicationContext());
                alOnOffButton.setChecked(true);
            }
        }

        curTimeText = new String[]{"12", "00", "A"};
        DJA_MAIN_FNC();
    }

    private void updateClock(final boolean semiOn)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                if (semiOn)
                {
                    timeText.setText(curTimeText[0] + ":" + curTimeText[1] + " " + curTimeText[2] + "M");
                } else {
                    timeText.setText(curTimeText[0] + " " + curTimeText[1] + " " + curTimeText[2] + "M");
                }
            }
        });
    }


    public void DJA_MAIN_FNC() {
        new Thread()
        {
            public void run()
            {
                while (true) {
                    Calendar cal = Calendar.getInstance();
                    String curHourSTR = getHour.format(cal.getTime());
                    String curMinuteSTR = getMinute.format(cal.getTime());
                    int curHour = Integer.parseInt(curHourSTR);
                    int curMinute = Integer.parseInt(curMinuteSTR);
                    String amPM;
                    if (curHour >= 12) {
                        amPM = "P";
                        if (curHour != 12)
                            curHour -= 12;
                    } else {
                        amPM = "A";
                        if (curHour == 0)
                            curHour = 12;
                    }

                    curTimeText[0] = "" + curHour;
                    curTimeText[1] = "" + curMinuteSTR;
                    curTimeText[2] = amPM;
                    updateClock(true);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    updateClock(false);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}