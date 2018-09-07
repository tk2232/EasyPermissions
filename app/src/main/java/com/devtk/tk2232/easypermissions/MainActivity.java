package com.devtk.tk2232.easypermissions;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.devtk.tk2232.library.EasyPermissions;
import com.devtk.tk2232.library.PermissionCallback;

public class MainActivity extends AppCompatActivity {
    private Button requestBtn;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestBtn = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);

        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestFineLocation();
            }
        });
    }

    private void takePicture() {
        if (!EasyPermissions.config(this).checkCameraAndStorragePermissions()) {
            //takePicture
        } else {
            EasyPermissions.config(this).requestCameraAndStorragePermissions();
        }
    }

    private void requestPermission() {
        EasyPermissions.config(this).requestCameraAndStorragePermissions();
    }

    private void requestFineLocation() {
        EasyPermissions.config(this).requestFineLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.handleRequestPermissionsResult(requestCode, permissions, grantResults, new PermissionCallback() {
            @Override
            public void onPermissionGranted(int requestCode, String[] permissions) {
                log("Granted", permissions);
            }

            @Override
            public void onPermissionDenied(int requestCode, String[] permissions, int[] grantResults) {
                log("Denied", permissions);
            }
        });
    }

    private void log(String state, String[] permissions) {
        Log.d(MainActivity.class.getSimpleName(), state);
        for (String permission : permissions) {
            Log.d(MainActivity.class.getSimpleName(), permission);
        }
    }
}
