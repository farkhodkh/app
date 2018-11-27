package ru.laundromat.washer.bluetoothchat;

import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.execution.CommandCapture;
// c:\adb>adb shell dpm set-device-owner ru.laundryplus.washer/.bluetoothchat.AdminReceiver

public class  MainActivity extends //SampleActivityBase {
        AppCompatActivity {
     // implements OnSharedPreferenceChangeListener {
    public static final String TAG = "MainActivity";
    public MediaPlayer mMediaPlayer;
    private boolean mLogShown; // Whether the Log Fragment is currently shown
  //  AudioManager manager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
    private DevicePolicyManager dpm;
    private ComponentName deviceAdmin;
    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 1;
    private static final int UI_ANIMATION_DELAY = 1;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    public final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            Build.VERSION_CODES vc = new Build.VERSION_CODES();
            Build.VERSION vr = new Build.VERSION();
            if (vr.SDK_INT > vc.KITKAT) {
            mContentView.setSystemUiVisibility(
                      View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }}
    };

    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            Build.VERSION_CODES vc = new Build.VERSION_CODES();
            Build.VERSION vr = new Build.VERSION();
            if (vr.SDK_INT > vc.KITKAT) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {  actionBar.hide();  }
            mControlsView.setVisibility(View.INVISIBLE);
        }}
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            Build.VERSION_CODES vc = new Build.VERSION_CODES();
            Build.VERSION vr = new Build.VERSION();
            if (vr.SDK_INT > vc.KITKAT) {
            hide();
        }}
    };
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {  delayedHide(AUTO_HIDE_DELAY_MILLIS);  }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.ApppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVisible = false;
        mContentView = findViewById(R.id.sample_content_fragment);
        // Регистрируем этот OnSharedPreferenceChangeListener
        //  Context context = getApplicationContext();
        //  SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(context);
        //  mSettings.registerOnSharedPreferenceChangeListener(this);
        Build.VERSION_CODES vc = new Build.VERSION_CODES();
        Build.VERSION vr = new Build.VERSION();
        if (vr.SDK_INT <= vc.KITKAT) {
            RootTools.debugMode = true; // debug mode
            hideSystemBar();
            // showSystemBar();
        }
        // перезагрузка при ошибках
        Thread.setDefaultUncaughtExceptionHandler(new TryMe(this, MainActivity.class));


        //  держать экран включенным ----------------------------------------
        //  getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            ru.laundromat.washer.bluetoothchat.BluetoothChatFragment fragment = new ru.laundromat.washer.bluetoothchat.BluetoothChatFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }


        if (vr.SDK_INT > vc.KITKAT) {

        deviceAdmin = new ComponentName(this, AdminReceiver.class);
        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (!dpm.isAdminActive(deviceAdmin)) {
            // showToast("This app is not a device admin!");
        }
        if (dpm.isDeviceOwnerApp(getPackageName())) {
            dpm.setLockTaskPackages(deviceAdmin,  new String[] { getPackageName() });
        } else {
            //  showToast("This app is not the device owner!");
        }
        startLockTask();
        Common.becomeHomeActivity(this);

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }}


        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem logToggle = menu.findItem(R.id.menu_toggle_log);
        logToggle.setVisible(findViewById(R.id.sample_output) instanceof ViewAnimator);
        logToggle.setTitle(mLogShown ? R.string.sample_hide_log : R.string.sample_show_log);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_toggle_log:
                mLogShown = !mLogShown;  //  ViewAnimator output = (ViewAnimator) findViewById(R.id.sample_output);
                if (mLogShown) { //     output.setDisplayedChild(1);
                } else { //     output.setDisplayedChild(0);
                } supportInvalidateOptionsMenu();  return true;
        }  return super.onOptionsItemSelected(item);
    }



    /** Create a chain of targets that will receive log data
    @Override
    public void initializeLogging() {
        // Wraps Android's native log framework.
        LogWrapper logWrapper = new LogWrapper();
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        Log.setLogNode(logWrapper);

        // Filter strips out everything except the message text.
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);

        // On screen logging via a fragment with a TextView.
        LogFragment logFragment = (LogFragment) getSupportFragmentManager()
                .findFragmentById(R.id.log_fragment);
        msgFilter.setNext(logFragment.getLogView());

        Log.i(TAG, "Ready");
    }
     */


    private void hideSystemBar() {
        try {
            //REQUIRES ROOT
            Build.VERSION_CODES vc = new Build.VERSION_CODES();
            Build.VERSION vr = new Build.VERSION();
            String ProcID = "79"; //HONEYCOMB AND OLDER


            //v.RELEASE  //4.0.3
            if (vr.SDK_INT >= vc.ICE_CREAM_SANDWICH) {
                ProcID = "42"; //ICS AND NEWER
            }


            String commandStr = "service call activity " +
                    ProcID + " s16 com.android.systemui";
            runAsRoot(commandStr);
        } catch (Exception e) {
            // something went wrong, deal with it here
        }
    }


    private void runAsRoot(String commandStr) {
        try {
            CommandCapture command = new CommandCapture(0, commandStr);
            RootTools.getShell(true).add(command).waitForFinish();
        } catch (Exception e) {
            // something went wrong, deal with it here
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(1);
    }


    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {  actionBar.hide();  }  //  mControlsView.setVisibility(View.GONE);
        mVisible = false;
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }





    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
       // AudibleReadyPlayer audibleReadyPlayer;
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:  Toast.makeText(this, "Нажата кнопка Меню", Toast.LENGTH_SHORT).show();
                return true;
            case KeyEvent.KEYCODE_SEARCH:  Toast.makeText(this, "Нажата кнопка Поиск", Toast.LENGTH_SHORT).show();
                return true;
            case KeyEvent.KEYCODE_BACK:  // onBackPressed();
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:  sendMessage("settings");
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:  sendMessage("settings");
                return true;
            case KeyEvent.KEYCODE_HEADSETHOOK:  sendMessage("moneybill");
                return true;
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:  sendMessage("moneybill");
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    // я так отправляю команду во фрагмент
    public void sendMessage(String message) {
        EditText mOutEditText = (EditText) findViewById(R.id.edit_text_out);
        mOutEditText.setText(message);
    }




/*
    public void onSharedPreferenceChanged(SharedPreferences mSettings, String key) {
        // TODO Проверять общие настройки, ключевые параметры и изменять UI
        // или поведение приложения, если потребуется.
        // CheckBox Preference
        /*
        Boolean example_switch = mSettings.getBoolean("example_switch", true);
        if (example_switch) {
            showSystemBar();
        } else {
            hideSystemBar();
        }
        */


//SharedPreferences mySharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);


}


