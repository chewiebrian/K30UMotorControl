package com.fgil55.xiaomi.motor;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import static com.fgil55.xiaomi.motor.CameraMotorService.cameraPosition;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final String packageName = getPackageName();
            final Uri packageUri = Uri.parse("package:" + packageName);

            if (!Settings.canDrawOverlays(this)) {
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, packageUri), 0);
            }
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                startActivity(new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, packageUri));
            }
        }

        getApplicationContext().startService(new Intent(getApplicationContext(), CameraMotorService.class));
    }

    public void popup(View view) {
        CameraMotorService.popup(getApplicationContext());
    }

    public void takeback(View view) {
        CameraMotorService.takeback(getApplicationContext());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.set_out:
                cameraPosition.set(1);
                break;
            case R.id.set_in:
                cameraPosition.set(0);
                break;
            default:
                break;

        }
        return true;
    }

}
