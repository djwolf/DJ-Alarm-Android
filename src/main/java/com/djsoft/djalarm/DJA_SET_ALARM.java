package com.djsoft.djalarm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DJA_SET_ALARM extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        //Intent intent = getIntent();
    }

    public void onClickOK(View v)
    {
        DJA_MAIN.alOnOffButton.setChecked(true);
        this.finish();
    }
}
