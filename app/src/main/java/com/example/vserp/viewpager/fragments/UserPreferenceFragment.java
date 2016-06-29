package com.example.vserp.viewpager.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.example.vserp.viewpager.R;

/**
 * Created by vserp on 6/28/2016.
 */

public class UserPreferenceFragment extends PreferenceFragment {

    private static final String KEY_FIRST_NAME = "key_first_name";
    private static final String KEY_LAST_NAME = "key_last_name";

    private Preference mUserFirstNamePreference;
    private Preference mUserLastNamePreference;

    private SharedPreferences.OnSharedPreferenceChangeListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.user_prefs);

        // Get the username Preference
        mUserFirstNamePreference = (Preference) getPreferenceManager()
                .findPreference(KEY_FIRST_NAME);
        mUserLastNamePreference = (Preference) getPreferenceManager()
                .findPreference(KEY_LAST_NAME);


        // Attach a listener to update summary when username changes
        mListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(
                    SharedPreferences sharedPreferences, String key) {
                mUserFirstNamePreference.setSummary(sharedPreferences.getString(
                        KEY_FIRST_NAME, "None Set"));
                mUserLastNamePreference.setSummary(sharedPreferences.getString(
                        KEY_LAST_NAME, "None Set"));

            }
        };

        // Get SharedPreferences object managed by the PreferenceManager for
        // this Fragment
        SharedPreferences prefs = getPreferenceManager()
                .getSharedPreferences();

        // Register a listener on the SharedPreferences object
        prefs.registerOnSharedPreferenceChangeListener(mListener);

        // Invoke callback manually to display the current username
        mListener.onSharedPreferenceChanged(prefs, KEY_FIRST_NAME);

    }

}

