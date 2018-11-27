package ru.laundromat.washer.bluetoothchat;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.view.View;


public class PrefFragment_tarif_valuta extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String VALUTA  = "valuta_key";
    private ListPreference mListPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_tarif_valuta);
            // Get a reference to the preferences
            mListPreference = (ListPreference)getPreferenceScreen().findPreference(VALUTA);
        mDecorView = getActivity().getWindow().getDecorView();
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
    public void onResume() {
        super.onResume();
            // Setup the initial values
            mListPreference.setSummary("Текущая валюта: " + mListPreference.getEntry().toString());
            // Set up a listener whenever a key changes
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Set new summary, when a preference value changes
        if (key.equals(VALUTA)) {
            mListPreference.setSummary("Текущая валюта: " + mListPreference.getEntry().toString());
        }
    }
}