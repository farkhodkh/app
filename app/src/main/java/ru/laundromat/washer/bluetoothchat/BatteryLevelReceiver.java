package ru.laundromat.washer.bluetoothchat;

/**
 * Created by My on 22.05.2016.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BatteryLevelReceiver extends BroadcastReceiver {
    public BatteryLevelReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        boolean isBatteryLow = intent.getAction().equals(Intent.ACTION_BATTERY_LOW);

        if (isBatteryLow)
            Toast.makeText(
                    context,
                    "Слишком низкий заряд. Уменьшаю яркость дисплея",
                    Toast.LENGTH_LONG).show();
    }
}