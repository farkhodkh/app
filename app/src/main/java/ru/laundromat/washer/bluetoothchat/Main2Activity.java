package ru.laundromat.washer.bluetoothchat;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class Main2Activity extends AppCompatActivity {
BluetoothAdapter mBluetoothAdapter = null;
  // private static final boolean AUTO_HIDE2 = true;

    /**
     * If {@link #AUTO_HIDE2} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
  //  private static final int AUTO_HIDE_DELAY_MILLIS2 = 1;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.

    private static final int UI_ANIMATION_DELAY2 = 1;
    private final Handler mHideHandler2 = new Handler();
    private View mContentView2;
    private final Runnable mShowPart2Runnable2 = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements

            //  mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private final Runnable mHidePart2Runnable2 = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView2.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private boolean mVisible2;
    private final Runnable mHideRunnable2 = new Runnable() {
        @Override
        public void run() {
            hide2();
        }
    };
     */
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.

    private final View.OnTouchListener mDelayHideTouchListener2 = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE2) {
                delayedHide2(AUTO_HIDE_DELAY_MILLIS2);
            }
            return false;
        }
    }; */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
      //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);
      //  mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
      //  mBluetoothAdapter.disable();
      //  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
      //  fab.setOnClickListener(new View.OnClickListener() {
      //      @Override
      //      public void onClick(View view) {
      //          Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
      //                  .setAction("Action", null).show();
      //      }
      //  });
      //  Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_STATE_CHANGED);
      //  startActivity(enableBtIntent);

      //  mVisible2 = false;
      //  mContentView2 = findViewById(R.id.fullscreen_content2);

      //  mHideHandler2.removeCallbacks(mShowPart2Runnable2);
      //  mHideHandler2.postDelayed(mHidePart2Runnable2, UI_ANIMATION_DELAY2);
       final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                startActivity(intent);
            }
        },5000);
/*
        new AlertDialog.Builder(Main2Activity.this)
                .setCancelable(false)
                .setTitle("Connect")
                .setMessage("Now trying to connect ")
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
*/

    }
/*
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide2(1);
    }


    private void hide2() {
        // Hide UI first

        // mControlsView.setVisibility(View.GONE);
        mVisible2 = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler2.removeCallbacks(mShowPart2Runnable2);
        mHideHandler2.postDelayed(mHidePart2Runnable2, UI_ANIMATION_DELAY2);
    }

    @SuppressLint("InlinedApi")

    private void delayedHide2(int delayMillis2) {
        mHideHandler2.removeCallbacks(mHideRunnable2);
        mHideHandler2.postDelayed(mHideRunnable2, delayMillis2);
    }
*/
}
