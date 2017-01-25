package com.djsoft.djalarm;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dja_main);

        //define UI attributes
        timeText = (TextView) findViewById(R.id.timeText);
        alOnOffButton = (ToggleButton) findViewById(R.id.alOnOffButton);
        setAlarmButton = (Button) findViewById(R.id.setAlarmButton);
        settingsButton = (Button) findViewById(R.id.settingsButton);
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
                            startService(new Intent(DJA_MAIN.this, DJA_ALARM_SERVICE.class).putExtra("Alarm", alarm));
                    }
                } else {
                    System.out.println("the else thingy");
                    if (alarm != null)
                        stopService(new Intent(DJA_MAIN.this,DJA_ALARM_SERVICE.class));
                }
            }
        });

        //ensure persistence
        if (DJA_ALARM_SERVICE.running)
            alOnOffButton.setChecked(true);

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