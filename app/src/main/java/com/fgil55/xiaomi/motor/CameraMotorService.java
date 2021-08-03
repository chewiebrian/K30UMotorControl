package com.fgil55.xiaomi.motor;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class CameraMotorService extends Service {
    static final AtomicInteger cameraPosition = new AtomicInteger(0);

    private final static String[] popupCommand = new String[]{"/system/bin/xiaomi-motor", "popup", "1"};
    private final static String[] takebackCommand = new String[]{"/system/bin/xiaomi-motor", "takeback", "1"};

    private CameraManager mCameraManager;
    private CameraManager.AvailabilityCallback mCameraCallback;
    private Handler mHandler;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCameraCallback = new CameraManager.AvailabilityCallback() {
                @Override
                public void onCameraAvailable(String cameraId) {
                    super.onCameraAvailable(cameraId);

                    if ("1".equals(cameraId)) {
                        takeback(CameraMotorService.this.getApplicationContext());
                    }
                }

                @Override
                public void onCameraUnavailable(String cameraId) {
                    super.onCameraUnavailable(cameraId);

                    if ("1".equals(cameraId)) {
                        popup(CameraMotorService.this.getApplicationContext());
                    }
                }
            };

            mCameraManager.registerAvailabilityCallback(mCameraCallback, mHandler);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCameraManager.unregisterAvailabilityCallback(mCameraCallback);
    }

    private static void execute(String[] takebackCommand, Context context) {
        try {
            Runtime.getRuntime().exec(takebackCommand);
        } catch (IOException e) {
            Toast.makeText(context, "Failure running " + takebackCommand + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    static void popup(Context context) {
        synchronized (cameraPosition) {
            if (cameraPosition.get() == 0) {
                execute(popupCommand, context);
                Toast.makeText(context, "Popup camera extended", Toast.LENGTH_SHORT).show();
                cameraPosition.set(1);
                ;
            } else {
                Toast.makeText(context, "Camera Already Popped Out", Toast.LENGTH_LONG).show();
            }
        }
    }

    static void takeback(Context context) {
        synchronized (cameraPosition) {
            if (cameraPosition.get() == 1) {
                execute(takebackCommand, context);
                Toast.makeText(context, "Popup camera retracted", Toast.LENGTH_SHORT).show();
                cameraPosition.set(0);
            } else {
                Toast.makeText(context, "Camera Already Retracted", Toast.LENGTH_LONG).show();
            }
        }
    }
}
