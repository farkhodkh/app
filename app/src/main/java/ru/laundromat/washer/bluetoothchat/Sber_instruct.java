package ru.laundromat.washer.bluetoothchat;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

/**
 * Created by My on 11.06.2016.
 */
public class Sber_instruct extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sber_instruct_form_light);
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


}
