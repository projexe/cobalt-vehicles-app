package com.projexe.cobaltvehiclesapp;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 **Basic settings fragment implements preferences defined in xml/pref_general
 * @author Simon Hutton
 * @version 1.0
 */
public class SettingsFragment extends PreferenceFragment {

    private static String TAG = "SettingsFragment";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_general);
    }

}
