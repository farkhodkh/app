package ru.laundromat.washer.bluetoothchat;

/**
 * Created by My on 26.04.2016.
 */
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.view.View;

public class PrefActivity_tarif_time extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String TYPE_WASH1 = "hour1_key";
    public static final String TYPE_WASH2 = "hour2_key";
    public static final String TYPE_WASH11 = "hour1tarif_key";
    public static final String TYPE_WASH22 = "hour2tarif_key";
    public static final String TYPE_WASH33 = "hour3tarif_key";


    private ListPreference mList1Preference, mList2Preference;
    private EditTextPreference gh, gh2,gh3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings_tarif_time);

        // Get a reference to the preferences
        mList1Preference = (ListPreference)getPreferenceScreen().findPreference(TYPE_WASH1);
        mList2Preference = (ListPreference)getPreferenceScreen().findPreference(TYPE_WASH2);
        gh = (EditTextPreference)getPreferenceScreen().findPreference(TYPE_WASH11);
        gh2 = (EditTextPreference)getPreferenceScreen().findPreference(TYPE_WASH22);
        gh3 = (EditTextPreference)getPreferenceScreen().findPreference(TYPE_WASH33);

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
        hideSystemUI();
        // Setup the initial values
        mList1Preference.setTitle("" + mList1Preference.getEntry().toString());
        mList2Preference.setTitle("" + mList2Preference.getEntry().toString());
         gh.setTitle("Утро:  " +  gh.getText().toString()+" руб.");
        gh2.setTitle("День:  " + gh2.getText().toString()+" руб.");
        gh3.setTitle("Вечер: " + gh3.getText().toString()+" руб.");

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
        if (key.equals(TYPE_WASH1)) {
            mList1Preference.setTitle("" + mList1Preference.getEntry().toString());
        }
        if (key.equals(TYPE_WASH2)) {
            mList2Preference.setTitle("" + mList2Preference.getEntry().toString());
        }
        if (key.equals(TYPE_WASH11)) {
            gh.setTitle("Утро:  " + gh.getText().toString()+" руб.");
        }
        if (key.equals(TYPE_WASH22)) {
            gh2.setTitle("День:  " + gh2.getText().toString()+" руб.");
        }
        if (key.equals(TYPE_WASH33)) {
            gh3.setTitle("Вечер: " + gh3.getText().toString()+" руб.");
        }
    }
}
