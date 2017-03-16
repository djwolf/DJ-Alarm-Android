package com.djsoft.djalarm.util;


import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;

import com.djsoft.djalarm.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, false);
        addPreferencesFromResource(R.xml.preferences);

        Preference listDifficulty = findPreference("list_problem_difficulty");
        RingtonePreference rtP = (RingtonePreference) findPreference("alarm_preference");
        rtP.setRingtoneType(4);
    }
}