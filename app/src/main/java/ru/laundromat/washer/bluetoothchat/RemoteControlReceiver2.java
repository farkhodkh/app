package ru.laundromat.washer.bluetoothchat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.widget.Toast;

public class RemoteControlReceiver2 extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
            KeyEvent event = (KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (KeyEvent.KEYCODE_MEDIA_PLAY == event.getKeyCode()) {
                Toast toast = Toast.makeText(context.getApplicationContext(),
                        context.getResources().getString(R.string.app_name), Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }
}