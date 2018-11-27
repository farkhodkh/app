/*
 * Kiosk Mode (aka Screen Pinning, aka Task Locking) demo for Android 5+
 *
 * Copyright 2015, SDG Systems, LLC
 */

package ru.laundromat.washer.bluetoothchat;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.os.Bundle;

public class KioskModeDemo2 extends  MainActivity {
private DevicePolicyManager dpm;
private ComponentName deviceAdmin;

@Override
protected void onCreate(Bundle savedInstanceState) {
      //  setTheme(R.style.ApppTheme);
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);

     //   deviceAdmin = new ComponentName(this, AdminReceiver.class);
     //   dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);

     stopLockTask();
    // dpm.clearPackagePersistentPreferredActivities(deviceAdmin, this.getPackageName());

        // Schedule a runnable to remove the status and navigation bar after a delay
       // mHideHandler.removeCallbacks(mShowPart2Runnable);
       // mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
        }


        }