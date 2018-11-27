package ru.laundromat.washer.bluetoothchat;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;


public class PrefActivity_sms_client extends PreferenceActivity {
     /*   implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String SMSCLIENT  = "smsclient_key";
    private ListPreference mListPreference;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_sms_client);
        // Get a reference to the preferences
     //   mListPreference = (ListPreference)getPreferenceScreen().findPreference(SMSCLIENT);
        mDecorView = getWindow().getDecorView();
        hideSystemUI();

    }
    private View mDecorView;

    private void hideSystemUI() {
        Build.VERSION_CODES vc = new Build.VERSION_CODES();
        Build.VERSION vr = new Build.VERSION();
        if (vr.SDK_INT > vc.KITKAT) {
            // Set the IMMERSIVE flag.
            // Set the content to appear under the system bars so that the content
            // doesn't resize when the system bars hide and show.
            mDecorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }}




/*
    @Override
    protected void onResume() {
        super.onResume();
        // Setup the initial values
        mListPreference.setSummary("Статус: " + mListPreference.getEntry().toString());
        // Set up a listener whenever a key changes            
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes            
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Set new summary, when a preference value changes
        if (key.equals(SMSCLIENT)) {
            mListPreference.setSummary("Статус: " + mListPreference.getEntry().toString());
        }
    } */
}