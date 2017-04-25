package com.civify.activity.createissue;

import android.Manifest.permission;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.civify.R;
import com.civify.activity.BaseActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class CameraGalleryLocationActivity extends BaseActivity
        implements LocationListener {

    public static final String DATA = "data";
    public static final String IMAGE = "image/*";
    public static final int DEST_WIDTH = 1600;
    public static final String COM_CIVIFY_PROVIDER = "com.civify.provider";
    public static final int MIN_TIME = 400;
    private static final int CAMERA_REQUEST = 0;
    private static final int GALLERY_REQUEST = 1;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 2;

    private String mCameraPhotoPath;
    private LocationManager mLocationManager;
    private String mLocationProvider;
    private Location mLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        mLocationProvider = mLocationManager.getBestProvider(criteria, false);
        if (checkAndRequestPermission(permission.ACCESS_FINE_LOCATION,
                R.string.need_to_allow_location_permission)) {
            mLocation = mLocationManager.getLastKnownLocation(mLocationProvider);

            // Initialize the location fields
            if (mLocation != null) {
                onLocationChanged(mLocation);
            } else {
                mLocationManager.requestLocationUpdates(mLocationProvider, MIN_TIME, 1, this);
                showError(R.string.couldnt_get_location);
            }
        }
    }

    protected abstract void handlePhotoResult(Bitmap imageBitmap);

    // Permissions

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cameraIntent();
                } else {
                    showError(R.string.camera_permission_denied);
                }
                break;
            case GALLERY_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    galleryIntent();
                } else {
                    showError(R.string.gallery_permission_denied);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private boolean checkAndRequestPermission(String permission, int messageRes) {
        int hasPermission = ContextCompat.checkSelfPermission(this, permission);
        if (hasPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermission(permission, messageRes);
            return false;
        }
        return true;
    }

    private void requestPermission(final String permission, int messageRes) {
        ActivityCompat.requestPermissions(this, new String[] {permission},
                REQUEST_CODE_ASK_PERMISSIONS);
    }

    private void showMessageOkCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this).setMessage(message)
                .setPositiveButton(getString(R.string.ok), okListener)
                .setNegativeButton(getString(R.string.cancel), null)
                .create()
                .show();
    }

    // Camera & gallery

    public void cameraButtonListener(View v) {
        if (checkAndRequestPermission(permission.READ_EXTERNAL_STORAGE,
                R.string.need_to_allow_read_storage_permission) && checkAndRequestPermission(
                permission.CAMERA, R.string.need_to_allow_camera_permission)) {
            cameraIntent();
        }
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                showError(R.string.camera_error);
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                try {
                    Uri photoUri = FileProvider.getUriForFile(this, COM_CIVIFY_PROVIDER,
                            createImageFile());
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(intent, CAMERA_REQUEST);
                } catch (IOException ex) {
                    showError(R.string.camera_error);
                    return;
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                "Camera");
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        // Save a file: path for use with ACTION_VIEW intents
        mCameraPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void onCaptureImageResult(Intent data) {
        Uri imageUri = Uri.parse(mCameraPhotoPath);
        File file = new File(imageUri.getPath());
        try {
            InputStream ims = new FileInputStream(file);
            Bitmap bitmap = resizeBitmap(BitmapFactory.decodeStream(ims));
            handlePhotoResult(resizeBitmap(bitmap));
        } catch (FileNotFoundException e) {
            showError(R.string.camera_error);
            return;
        }
    }

    public void galleryButtonListener(View v) {
        if (checkAndRequestPermission(permission.READ_EXTERNAL_STORAGE,
                R.string.need_to_allow_read_storage_permission)) {
            galleryIntent();
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(IMAGE);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_photo)),
                GALLERY_REQUEST);
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap imageBitmap = null;
        if (data != null) {
            Uri selectedImage = data.getData();
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                showError(R.string.gallery_error);
            }
            imageBitmap = resizeBitmap(BitmapFactory.decodeStream(imageStream));
        }
        handlePhotoResult(imageBitmap);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == CAMERA_REQUEST) {
                onCaptureImageResult(data);
            }
        }
    }

    private static Bitmap resizeBitmap(Bitmap bitmap) {
        int origWidth = bitmap.getWidth();
        int origHeight = bitmap.getHeight();
        int destWidth = DEST_WIDTH;
        if (origWidth > destWidth) {
            int destHeight = (destWidth * origHeight) / origWidth;
            return Bitmap.createScaledBitmap(bitmap, destWidth, destHeight, false);
        }
        return bitmap;
    }

    // Location

    protected Location getCurrentLocation() {
        return mLocation;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkAndRequestPermission(permission.ACCESS_FINE_LOCATION, R.string
                .need_to_allow_location_permission)) {
            mLocationManager.requestLocationUpdates(mLocationProvider, MIN_TIME, 1, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    
    protected void showError(int stringId) {
        Snackbar.make(findViewById(android.R.id.content), getString(stringId),
                Snackbar.LENGTH_SHORT).show();
    }
}
