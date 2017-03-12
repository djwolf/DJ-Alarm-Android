package com.djsoft.djalarm.util;


import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.widget.Toast;
import com.djsoft.djalarm.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, false);
        addPreferencesFromResource(R.xml.preferences);

        Preference listDifficulty = findPreference("list_problem_difficulty");
        Preference listRingtone = findPreference("alarm_preference");
        RingtonePreference rtP = (RingtonePreference) findPreference("alarm_preference");
        rtP.setRingtoneType(4);

        listDifficulty.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getActivity(), "More Difficulties will be added in a future update", Toast.LENGTH_LONG).show();
                return true;
            }
        });
        listRingtone.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getActivity(), "NOTE: If you use a ringtone from a 3rd party app, it may not work", Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }
}