package ru.laundromat.washer.bluetoothchat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PowerConnectionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
          //  Toast.makeText(context, "Устройство заряжается", Toast.LENGTH_LONG).show();
        }

        if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
           // Toast.makeText(context, "Устройство не заряжается", Toast.LENGTH_LONG).show();
        }}


}