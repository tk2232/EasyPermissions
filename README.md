# EasyPermissions


## Setup

```java
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

```java
dependencies {
	        implementation 'com.github.tk2232:EasyPermissions:v1.0.1'
	}
```

Declare specific permissions in your AndroidMnifest.xml<br/>
Example:
```java
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

## Usage:

### In Activity:

```java
    private void takePicture() {
        if (!EasyPermissions.config(this).checkCameraAndStorragePermissions()) {
            //takePicture
        } else {
            EasyPermissions.config(this).requestCameraAndStorragePermissions();
        }
    }
```

```java
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
```

## Examples
### Check permissions

```java
            EasyPermissions.config(this).checkAccessCoarseLocationPermission();
            EasyPermissions.config(this).checkAccessFineLocationPermission();
            EasyPermissions.config(this).checkCameraAndStorragePermissions();
            EasyPermissions.config(this).checkCameraPermission();
            EasyPermissions.config(this).checkReadExternalStorragePermission();
            EasyPermissions.config(this).checkWriteExternalStorragePermission();
            EasyPermissions.config(this).checkReadWriteStorragePermission();
	    
	    Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
	    EasyPermissions.config(this).isExplicitCameraPermissionRequired();
```
### Request permissions

```java
            EasyPermissions.config(this).requestCameraAndStorragePermissions();
            EasyPermissions.config(this).requestCameraPermission();
            EasyPermissions.config(this).requestCoarseLocation();
            EasyPermissions.config(this).requestFineLocation();
            EasyPermissions.config(this).requestReadExternalStorrage();
            EasyPermissions.config(this).requestWriteExternalStorrage();
            EasyPermissions.config(this).requestReadWriteExternalStorrage();
            EasyPermissions.config(this).requestPermissions(Sring[] permissions, int PERMISSION_REQUEST);
```

### Configuration

```java
            EasyPermissions.config(this).customExplanationSnackBar(Snackbar snackbar);
            EasyPermissions.config(this).setupExplanationSnackBar(String actionTitle, String actionDescription);
            EasyPermissions.config(this).showExplanationSnackBar(boolean show);
            EasyPermissions.config(this).explanationDialog(AlertDialog dialog);
```

