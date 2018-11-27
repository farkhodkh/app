package ru.laundromat.washer.bluetoothchat;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.execution.CommandCapture;

import ru.laundromat.washer.common.activities.SampleActivityBase;
import ru.laundromat.washer.common.logger.Log;
import ru.laundromat.washer.common.logger.LogFragment;
import ru.laundromat.washer.common.logger.LogWrapper;
import ru.laundromat.washer.common.logger.MessageOnlyLogFilter;


public class MainActivity2 extends SampleActivityBase {
     //   AppCompatActivity {
     // implements OnSharedPreferenceChangeListener {
    public static final String TAG = "MainActivity";
    public MediaPlayer mMediaPlayer;
    // Whether the Log Fragment is currently shown
    private boolean mLogShown;

  //  AudioManager manager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);


/*
    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 0;
    private static final int UI_ANIMATION_DELAY = 0;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            mContentView.setSystemUiVisibility(
                      View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {  actionBar.hide();  }
            mControlsView.setVisibility(View.INVISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {  delayedHide(AUTO_HIDE_DELAY_MILLIS);  }
            return false;
        }
    };
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.ApppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Регистрируем этот OnSharedPreferenceChangeListener
        //  Context context = getApplicationContext();
        //  SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(context);
        //  mSettings.registerOnSharedPreferenceChangeListener(this);

        RootTools.debugMode = true; // debug mode
        // перезагрузка при ошибках
        Thread.setDefaultUncaughtExceptionHandler(new TryMe(this, MainActivity2.class));

         hideSystemBar();
        // showSystemBar();


     //   mVisible = true;
     //   mContentView = findViewById(R.id.sample_content_fragment);

        // Schedule a runnable to remove the status and navigation bar after a delay
      //  mHideHandler.removeCallbacks(mShowPart2Runnable);
      //  mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);

        //  держать экран включенным ----------------------------------------
        //  getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            BluetoothChatFragment fragment = new BluetoothChatFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }

/*
        //HIDE TOOLBAR
        try{
            //REQUIRES ROOT
            Build.VERSION_CODES vc = new Build.VERSION_CODES();
            Build.VERSION vr = new Build.VERSION();
            String ProcID = "79"; //HONEYCOMB AND OLDER

            //v.RELEASE  //4.0.3
            if(vr.SDK_INT >= vc.ICE_CREAM_SANDWICH){
                ProcID = "42"; //ICS AND NEWER
            }

            //REQUIRES ROOT
            Process proc = Runtime.getRuntime().exec(new String[]{"su","-c","service call activity "+ ProcID +" s16 com.android.systemui"}); //WAS 79
            proc.waitFor();

        }catch(Exception ex){
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }

    }*/
/*
    private void showNotification(String text) {
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        Context context = getApplicationContext();
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle("Регбол")
                .setContentText(text)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = builder.getNotification();
        notificationManager.notify(R.drawable.ic_launcher, notification);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String sms_body = intent.getExtras().getString("sms_body");
        showNotification(sms_body);
     //   saveSms(sms_body);

   //     SmsData event = processSms(sms_body);
   //     if (event != null) {
   //         addEvent(event.hh, event.mm, event.description);
   //     }

        return START_STICKY;*/
      //  manager.registerMediaButtonEventReceiver(RemoteControlReceiver2);
    }


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
                mLogShown = !mLogShown;
              //  ViewAnimator output = (ViewAnimator) findViewById(R.id.sample_output);
                if (mLogShown) {
               //     output.setDisplayedChild(1);
                } else {
               //     output.setDisplayedChild(0);
                }
                supportInvalidateOptionsMenu();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    /** Create a chain of targets that will receive log data*/
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


/*


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(0);
    }


    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {  actionBar.hide();  }
      //  mControlsView.setVisibility(View.GONE);
        mVisible = false;
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

*/

/*

    private void showSystemBar() {
        String commandStr = "am startservice -n com.android.systemui/.SystemUIService";
        runAsRoot(commandStr);
    }

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
            Command command = new Command(0, commandStr);
            RootTools.getShell(true).add(command);
        } catch (Exception e) {
            // something went wrong, deal with it here
        }
    }

*/

    private void showSystemBar() {
        String commandStr = "am startservice -n com.android.systemui/.SystemUIService";
        runAsRoot(commandStr);
    }

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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
       // AudibleReadyPlayer audibleReadyPlayer;
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                Toast.makeText(this, "Нажата кнопка Меню", Toast.LENGTH_SHORT).show();
                return true;
            case KeyEvent.KEYCODE_SEARCH:
                Toast.makeText(this, "Нажата кнопка Поиск", Toast.LENGTH_SHORT).show();
                return true;
            case KeyEvent.KEYCODE_BACK:
                // onBackPressed();
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                sendMessage("settings");
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                sendMessage("settings");
                return true;
            case KeyEvent.KEYCODE_HEADSETHOOK:
                sendMessage("moneybill");
                return true;
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                sendMessage("moneybill");
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

/*
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(1);
    }


    private void hide() {
        // Hide UI first

        // mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    */
    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

     */
}


