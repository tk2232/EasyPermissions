package com.devtk.tk2232.library;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

public class EasyPermissions {
    private static final String TAG = EasyPermissions.class.getSimpleName();

    public static final int REQUEST_CAMERA = 100;

    public static final int REQUEST_CAMERA_AND_EXTERNAL_STORRAGE = 101;
    public static final int REQUEST_READ_EXTERNAL_STORRAGE = 200;
    public static final int REQUEST_WRITE_EXTERNAL_STORRAGE = 201;
    public static final int REQUEST_READ_WRITE_EXTERNAL_STORRAGE = 202;

    public static final int REQUEST_FINE_LOCATION = 300;
    public static final int REQUEST_COARSE_LOCATION = 301;

    private static Configuration config;

    public static Configuration config(@NonNull Context context) {
        if (config != null)
            dismissSnackbar();
        return new Configuration(context);
    }

    /**
     * Check if explicetly requesting camera permission is required.<br>
     * It is required in Android Marshmellow and above if "CAMERA" permission is requested in the
     * manifest.<br>
     * See <a
     * href="http://stackoverflow.com/questions/32789027/android-m-camera-intent-permission-bug">StackOverflow
     * question</a>.
     */
    private static boolean isExplicitCameraPermissionRequired() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && config.context.checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED;
    }

    private static boolean checkCameraPermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(config.context, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED;
    }

    private static boolean checkReadWriteStorrageCameraPermission() {
        return checkCameraPermission()
                && checkReadWriteStorragePermission();
    }

    private static boolean checkReadWriteStorragePermission() {
        return ContextCompat.checkSelfPermission(config.context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(config.context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED;
    }

    private static boolean checkReadExternalStorragePermission() {
        return ContextCompat.checkSelfPermission(config.context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED;
    }

    private static boolean checkWriteExternalStorragePermission() {
        return ContextCompat.checkSelfPermission(config.context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED;
    }

    private static boolean checkAccessFineLocationPermission() {
        return ContextCompat.checkSelfPermission(config.context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED;
    }

    private static boolean checkAccessCoarseLocationPermission() {
        return ContextCompat.checkSelfPermission(config.context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED;
    }

    private static void requestCameraPermission() {
        String[] permission = getCameraPermission();
        if (permission != null)
            requestPermissions(permission, REQUEST_CAMERA);
    }

    private static void requestCameraStorragePermission() {
        String[] permissions = getCameraStorragePermissionString();
        requestPermissions(permissions, REQUEST_CAMERA_AND_EXTERNAL_STORRAGE);
    }

    private static void requestReadWriteExternalStorrage() {
        if (checkReadWriteStorragePermission())
            requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_READ_WRITE_EXTERNAL_STORRAGE);
    }

    private static void requestReadExternalStorrage() {
        if (checkReadExternalStorragePermission())
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORRAGE);
    }

    private static void requestWriteExternalStorrage() {
        if (checkWriteExternalStorragePermission())
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORRAGE);
    }

    private static void requestFineLocation() {
        if (checkAccessFineLocationPermission()) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
        }
    }

    private static void requestCoarseLocation() {
        if (checkAccessFineLocationPermission()) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_COARSE_LOCATION);
        }
    }

    private static String[] getCameraStorragePermissionString() {
        if (isExplicitCameraPermissionRequired() && checkReadWriteStorragePermission()) {
            return new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
        } else {
            return new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }
    }

    private static String[] getCameraPermission() {
        if (isExplicitCameraPermissionRequired())
            return new String[]{Manifest.permission.CAMERA};
        else
            return null;
    }

    private static void requestPermissions(String[] permissions, int PERMISSION_REQUEST) {
        boolean shouldProvideRationale = true;
        for (String permission : permissions) {
            // Should we show an explanation?
            shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale((Activity) config.context, permission);
        }
        if (shouldProvideRationale) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            if (config.explanationDialog != null) {
                config.explanationDialog.show();
            }
        }
        // PERMISSION_REQUEST is an
        // app-defined int constant. The callback method gets the
        // result of the request.
        ActivityCompat.requestPermissions((Activity) config.context, permissions, PERMISSION_REQUEST);
    }


    public static void handleRequestPermissionsResult(int requestCode,
                                                      @NonNull String[] permissions,
                                                      @NonNull int[] grantResults, PermissionCallback callback) {
        if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            showSnackbar();
            callback.onPermissionDenied(requestCode, permissions, grantResults);
        } else {
            dismissSnackbar();
            callback.onPermissionGranted(requestCode, permissions);
        }

    }

    private static void dismissSnackbar() {
        if (config.snackbar != null) {
            config.snackbar.dismiss();
        }
    }

    private static void showSnackbar() {
        try {
            if (config.snackbar != null && config.showExplanationSnackBar) {
                config.snackbar.dismiss();
                config.snackbar.show();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }


    public static class Configuration {
        private AlertDialog explanationDialog;
        private Snackbar snackbar;
        private boolean showExplanationSnackBar = true;
        private Context context;

        public Configuration(Context context) {
            this.context = context;
            config = this;
            setupDefaultExplanationSnackBar();
        }

        public boolean isExplicitCameraPermissionRequired() {
            return EasyPermissions.isExplicitCameraPermissionRequired();
        }

        public boolean checkCameraPermission() {
            return EasyPermissions.checkCameraPermission();
        }

        public boolean checkCameraAndStorragePermissions() {
            return EasyPermissions.checkReadWriteStorrageCameraPermission();
        }

        public boolean checkReadWriteStorragePermission() {
            return EasyPermissions.checkReadWriteStorragePermission();
        }

        public boolean checkReadExternalStorragePermission() {
            return EasyPermissions.checkReadExternalStorragePermission();
        }

        public boolean checkWriteExternalStorragePermission() {
            return EasyPermissions.checkWriteExternalStorragePermission();
        }

        public boolean checkAccessFineLocationPermission() {
            return EasyPermissions.checkAccessFineLocationPermission();
        }

        public boolean checkAccessCoarseLocationPermission() {
            return EasyPermissions.checkAccessCoarseLocationPermission();
        }

        public void requestCameraPermission() {
            EasyPermissions.requestCameraPermission();
        }

        public void requestCameraAndStorragePermissions() {
            EasyPermissions.requestCameraStorragePermission();
        }

        public void requestReadWriteExternalStorrage() {
            EasyPermissions.requestReadWriteExternalStorrage();
        }

        public void requestReadExternalStorrage() {
            EasyPermissions.requestReadExternalStorrage();
        }

        public void requestWriteExternalStorrage() {
            EasyPermissions.requestWriteExternalStorrage();
        }

        public void requestFineLocation() {
            EasyPermissions.requestFineLocation();
        }

        public void requestCoarseLocation() {
            EasyPermissions.requestCoarseLocation();
        }

        public void requestPermissions(String[] permissions, int PERMISSION_REQUEST) {
            EasyPermissions.requestPermissions(permissions, PERMISSION_REQUEST);
        }

        public Configuration showExplanationSnackBar(boolean show) {
            showExplanationSnackBar = show;
            return this;
        }

        public Configuration explanationDialog(AlertDialog dialog) {
            explanationDialog = dialog;
            return this;
        }

        public Configuration customExplanationSnackBar(Snackbar snackbar) {
            this.snackbar = snackbar;
            return this;
        }

        public Configuration setupDefaultExplanationSnackBar() {
            buildSnackBar(R.string.permission_denied_explanation, R.string.setting);
            return this;
        }

        public Configuration setupExplanationSnackBar(String actionTitle, String actionDescription) {
            buildSnackBar(actionTitle, actionDescription);
            return this;
        }

        private void buildSnackBar(int actionTitle, int actionDescription) {
            snackbar = Snackbar.make(((Activity) context).findViewById(android.R.id.content),
                    ((Activity) context).getString(actionTitle), Snackbar.LENGTH_INDEFINITE);

            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Build intent that displays the App settings screen.
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
                    intent.setData(uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ((Activity) context).startActivity(intent);
                }
            };
            snackbar.setAction(((Activity) context).getString(actionDescription), onClickListener);
        }

        private void buildSnackBar(String actionTitle, String actionDescription) {
            snackbar = Snackbar.make(((Activity) context).findViewById(android.R.id.content),
                    actionTitle, Snackbar.LENGTH_INDEFINITE);

            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Build intent that displays the App settings screen.
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
                    intent.setData(uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ((Activity) context).startActivity(intent);
                }
            };
            snackbar.setAction(actionDescription, onClickListener);
        }
    }
}
