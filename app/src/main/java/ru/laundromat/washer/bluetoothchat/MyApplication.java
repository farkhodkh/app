package ru.laundromat.washer.bluetoothchat;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import java.lang.Thread.UncaughtExceptionHandler;

public class MyApplication extends Application {
    private static final String TAG = "MyApp";
    private static final String KEY_APP_CRASHED = "KEY_APP_CRASHED";

    @Override
    public void onCreate() {
        super.onCreate();

        final UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable exception) {
                // Save the fact we crashed out.
                getSharedPreferences(TAG, Context.MODE_PRIVATE).edit()
                        .putBoolean(KEY_APP_CRASHED, true).apply();
                // Chain default exception handler.
                if (defaultHandler != null) {
                    defaultHandler.uncaughtException(thread, exception);
                }
            }
        });

        boolean bRestartAfterCrash = getSharedPreferences(TAG, Context.MODE_PRIVATE)
                .getBoolean(KEY_APP_CRASHED, false);
        if (bRestartAfterCrash) {
            // Clear crash flag.
            getSharedPreferences(TAG, Context.MODE_PRIVATE).edit()
                    .putBoolean(KEY_APP_CRASHED, false).apply();
            // Re-launch from root activity with cleared stack.
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}