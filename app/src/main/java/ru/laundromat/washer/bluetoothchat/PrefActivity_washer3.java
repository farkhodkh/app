package ru.laundromat.washer.bluetoothchat;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.view.View;


public class PrefActivity_washer3 extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String TYPE_WASH3 = "wash3isdry_key";
    public static final String STATUS_WASH3 = "remont3_key";


    private ListPreference mList1Preference, mList2Preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings_washer3);

        // Get a reference to the preferences
        mList1Preference = (ListPreference)getPreferenceScreen().findPreference(TYPE_WASH3);
        mList2Preference = (ListPreference)getPreferenceScreen().findPreference(STATUS_WASH3);

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



    @Override
    protected void onResume() {
        super.onResume();

        // Setup the initial values
        mList1Preference.setSummary("Тип машины: " + mList1Preference.getEntry().toString());
        mList2Preference.setSummary("Текущее состояние: " + mList2Preference.getEntry().toString());

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
        if (key.equals(TYPE_WASH3)) {
            mList1Preference.setSummary("Тип машины: " + mList1Preference.getEntry().toString());
        }
        if (key.equals(STATUS_WASH3)) {
            mList2Preference.setSummary("Текущее состояние: " + mList2Preference.getEntry().toString());

        }
    }
}