/*
 * Kiosk Mode (aka Screen Pinning, aka Task Locking) demo for Android 5+
 *
 * Copyright 2015, SDG Systems, LLC
 */

package ru.laundromat.washer.bluetoothchat;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class KioskModeDemo extends Activity {
  //  private final static String TAG = "KioskModeDemo";
  //  private Button button;
  //  private boolean inKioskMode = false;
    private DevicePolicyManager dpm;
    private ComponentName deviceAdmin;


    private void showToast(String text) {
      //  Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
/*
    private void setKioskMode(boolean on) {
        try {
            if (on) {
                if (dpm.isLockTaskPermitted(this.getPackageName())) {
                    startLockTask();
                    inKioskMode = true;
                    button.setText("Exit Kiosk Mode");
                } else {
                    showToast("Kiosk Mode not permitted");
                }
            } else {
                stopLockTask();
                inKioskMode = false;
                button.setText("Enter Kiosk Mode");
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e);
        }
    }
*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
       // button = (Button) findViewById(R.id.button1);
        deviceAdmin = new ComponentName(this, AdminReceiver.class);
        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (!dpm.isAdminActive(deviceAdmin)) {
          //  showToast("This app is not a device admin!");
        }
        if (dpm.isDeviceOwnerApp(getPackageName())) {
            dpm.setLockTaskPackages(deviceAdmin, new String[] { getPackageName() });
        } else {
           // showToast("This app is not the device owner!");
        }
        startLockTask();
        Common.becomeHomeActivity(this);
        Intent intent = new Intent(KioskModeDemo.this,MainActivity.class);startActivity(intent);
       // inKioskMode = true;


    }

    @Override
    public void onResume() {
        super.onResume();
    }
/*
    public void toggleKioskMode(View view) {
        setKioskMode(!inKioskMode);
    }

    public void restoreLauncher(View view) {
        dpm.clearPackagePersistentPreferredActivities(deviceAdmin,
                this.getPackageName());
        showToast("Home activity: " + Common.getHomeActivity(this));
    }
    public void goLaundry(View view) {
        Intent intent = new Intent(this,MainActivity.class);startActivity(intent);
        showToast("goLaundry");
    }


    public void setLauncher(View view) {
        Common.becomeHomeActivity(this);
    }
    */
}