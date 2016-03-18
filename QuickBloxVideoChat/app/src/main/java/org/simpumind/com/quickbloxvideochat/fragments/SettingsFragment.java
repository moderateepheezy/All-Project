package org.simpumind.com.quickbloxvideochat.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import org.simpumind.com.quickbloxvideochat.R;


/**
 * QuickBlox team
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
