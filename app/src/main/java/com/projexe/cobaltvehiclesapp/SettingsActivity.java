package com.projexe.cobaltvehiclesapp;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;

///**
// **Basic Settings activity for managing app preferences
// * @author Simon Hutton
// * @version 1.0
// */
public class SettingsActivity extends AppCompatPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
        ActionBar bar = getSupportActionBar();
        if( bar != null ) {
            // remove back arrow
            bar.setDisplayHomeAsUpEnabled( false );
            bar.setDisplayShowTitleEnabled( true );
        }

    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            // Add the version number to the settings screen for reference
            Preference versionPreference = findPreference(getResources().getString(R.string.preference_version_key));
            String debugString = "";
            if (BuildConfig.DEBUG) {
                debugString = "(debug)";
            }
            versionPreference.setSummary(BuildConfig.VERSION_NAME + ":" +  BuildConfig.FLAVOR + " " + debugString);
        }
    }

}

//

//public class SettingsActivity extends PreferenceFragment {
//
//    public final String TAG = "SettingsActivity";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // Display the fragment as the main content.
//
//        getFragmentManager().beginTransaction()
//                .replace(android.R.id.content, new SettingsFragment())
//                .commit();
//
//        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false); // Required to initialise preference defaults
//
//        // Add the version number to the settings screen for reference
//        Preference versionPreference = findPreference(getResources().getString(R.string.preference_version_key));
//        Preference versionPreference = prefer findPreference(getResources().getString(R.string.preference_version_key));
//        String debugString = "";
//        if (BuildConfig.DEBUG) debugString = "(debug)";
//        versionPreference.setSummary(BuildConfig.VERSION_NAME + debugString);
//
//
//    }
//
//}
