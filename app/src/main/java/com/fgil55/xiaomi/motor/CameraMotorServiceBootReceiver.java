package com.fgil55.xiaomi.motor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CameraMotorServiceBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            context.startService(new Intent(context, CameraMotorService.class));
        }
    }
}
