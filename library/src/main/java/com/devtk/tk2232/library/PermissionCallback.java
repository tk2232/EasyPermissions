package com.devtk.tk2232.library;

public interface PermissionCallback {

    void onPermissionGranted(int requestCode, String[] permissions);

    void onPermissionDenied(int requestCode, String[] permissions, int[] grantResults);
}
