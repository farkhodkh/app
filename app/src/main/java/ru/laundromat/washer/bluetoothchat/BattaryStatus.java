package ru.laundromat.washer.bluetoothchat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.LevelListDrawable;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class BattaryStatus  extends Activity {

    private TextView mBatteryLevelTextView, mBatteryVoltageTextView, mBatteryTemperatureTextView,
            mBatteryTechnologyTextView, mBatteryStatusTextView, mBatteryHealthTextView,
            mBatteryCurrentTextView, mBatteryChargeTextView;
    private ImageView mBatteryIconImageView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battary_status);

        mBatteryLevelTextView = (TextView) findViewById(R.id.textViewLevel);
        mBatteryVoltageTextView = (TextView) findViewById(R.id.textViewVoltage);
        mBatteryTemperatureTextView = (TextView) findViewById(R.id.textViewTemperature);
        mBatteryTechnologyTextView = (TextView) findViewById(R.id.textViewTechology);
        mBatteryStatusTextView = (TextView) findViewById(R.id.textViewStatus);
        mBatteryHealthTextView = (TextView) findViewById(R.id.textViewHealth);
        mBatteryCurrentTextView = (TextView) findViewById(R.id.textViewCurrent);
        mBatteryChargeTextView = (TextView) findViewById(R.id.textViewCharge);
        mBatteryIconImageView = (ImageView) findViewById(R.id.ImageViewIcon);

        this.registerReceiver(this.batteryReceiver, new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED));
        mDecorView = getWindow().getDecorView();
        hideSystemUI();

    }
    private View mDecorView;

    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        Build.VERSION_CODES vc = new Build.VERSION_CODES();
        Build.VERSION vr = new Build.VERSION();
        if (vr.SDK_INT > vc.KITKAT) {
            mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }}


    @Override
    protected void onPause() {
        if (batteryReceiver != null) {
            unregisterReceiver(batteryReceiver);
            batteryReceiver = null;
        }
        super.onPause();
    }

    private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                mBatteryLevelTextView.setText("Уровень заряда: "
                        + String.valueOf(intent.getIntExtra(
                        BatteryManager.EXTRA_LEVEL, 0)) + "%");
                mBatteryVoltageTextView.setText("Вольтаж: "
                        + String.valueOf((float) intent.getIntExtra(
                        BatteryManager.EXTRA_VOLTAGE, 0) / 1000) + "V");
                mBatteryTemperatureTextView.setText("Температура батареи: "
                        + String.valueOf((float) intent.getIntExtra(
                        BatteryManager.EXTRA_TEMPERATURE, 0) / 10)
                        + "°C");
                mBatteryTechnologyTextView
                        .setText("Тип батареи: "
                                + intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY));

                int batteryIconId = intent.getIntExtra(
                        BatteryManager.EXTRA_ICON_SMALL, 0);
                LevelListDrawable batteryLevel = (LevelListDrawable) getResources()
                        .getDrawable(batteryIconId);
                mBatteryIconImageView.setBackground(batteryLevel);

                // альтернативный способ вычисления текущего уровня зарядки
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                float batteryPct = level / (float) scale;
                mBatteryCurrentTextView.setText("Уровень зарядки: " + batteryPct);

                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,
                        BatteryManager.BATTERY_STATUS_UNKNOWN);
                String strStatus;
                if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
                    strStatus = "Заряжается";
                } else if (status == BatteryManager.BATTERY_STATUS_DISCHARGING) {
                    strStatus = "Разряжается";
                } else if (status == BatteryManager.BATTERY_STATUS_NOT_CHARGING) {
                    strStatus = "Не заряжается";
                } else if (status == BatteryManager.BATTERY_STATUS_FULL) {
                    strStatus = "Полный заряд";
                } else {
                    strStatus = "Ничего не понимаю";
                }
                mBatteryStatusTextView.setText("Статус: " + strStatus);

                int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH,
                        BatteryManager.BATTERY_HEALTH_UNKNOWN);
                String strHealth;
                if (health == BatteryManager.BATTERY_HEALTH_GOOD) {
                    strHealth = "Хорошее";
                } else if (health == BatteryManager.BATTERY_HEALTH_OVERHEAT) {
                    strHealth = "Перегретое";
                } else if (health == BatteryManager.BATTERY_HEALTH_DEAD) {
                    strHealth = "Убитое";
                } else if (health == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE) {
                    strHealth = "Over Voltage";
                } else if (health == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE) {
                    strHealth = "Unspecified Failure";
                } else {
                    strHealth = "Неизвестно";
                }
                mBatteryHealthTextView.setText("Состояние батареи: " + strHealth);

                // Каким образом проходит зарядка?
                int chargePlug = intent.getIntExtra(
                        BatteryManager.EXTRA_PLUGGED,
                        BatteryManager.BATTERY_STATUS_UNKNOWN);
                String charge;
                if (chargePlug == BatteryManager.BATTERY_PLUGGED_USB) {
                    charge = "Заряжается от USB";
                } else if (chargePlug == BatteryManager.BATTERY_PLUGGED_AC) {
                    charge = "Заряжается от сети";
                } else if (chargePlug == BatteryManager.BATTERY_PLUGGED_WIRELESS) {
                    charge = "Беспроводная зарядка";
                } else {
                    charge = "Неизвестно";
                }
                mBatteryChargeTextView.setText("Тип зарядки: " + charge);
            }
        }
    };
}