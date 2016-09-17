package com.launchdarkly.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static com.launchdarkly.android.Util.isInternetConnected;

public class ConnectivityReceiver extends BroadcastReceiver {
    private static final String TAG = "LDConnectivityReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isInternetConnected(context)) {
            Log.d(TAG, "Connected to the internet");
            try {
                LDClient ldClient = LDClient.get();
                if (Foreground.get(context).isForeground()) {
                    ldClient.startStreaming();
                }
            } catch (LaunchDarklyException e) {
                Log.e(TAG, "Tried to restart stream, but LDClient has not yet been initialized.");
            }
        } else {
            Log.d(TAG, "Not Connected to the internet");
            try {
                LDClient ldClient = LDClient.get();
                ldClient.stopStreaming();
            } catch (LaunchDarklyException e) {
                Log.e(TAG, "Tried to stop stream, but LDClient has not yet been initialized.");
            }
        }
    }
}