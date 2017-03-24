package com.djsoft.djalarm;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.djsoft.djalarm.util.SettingsFragment;

public class DJA_SETTINGS extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
