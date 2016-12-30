package com.djsoft.djalarm;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;
import com.djsoft.djalarm.util.Alarm;

import java.io.Serializable;

public class DJA_SET_ALARM extends AppCompatActivity implements Serializable {
    private TimePicker setAlarmWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        setAlarmWidget = (TimePicker) findViewById(R.id.setTime);
        //Intent intent = getIntent();
    }

    public void onClickOK(View v)
    {
        boolean am;
        if (setAlarmWidget.getCurrentHour() >= 12) {am = false;} else {am = true;}
        final Alarm al = new Alarm(0,setAlarmWidget.getCurrentHour(),setAlarmWidget.getCurrentMinute(),am);
        DJA_MAIN.alarm = al;
        DJA_MAIN.alOnOffButton.setChecked(true);
        final DJA_SET_ALARM DJ_S_A = this;
        startService(new Intent(DJ_S_A, DJA_ALARM_SERVICE.class).putExtra("Alarm", al));
        //startService(new Intent(DJ_S_A, DJA_ALARM_SERVICE.class).putExtra("Alarm", al));
        finish();
    }
}
