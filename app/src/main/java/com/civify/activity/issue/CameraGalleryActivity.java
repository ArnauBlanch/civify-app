package com.civify.activity.issue;

import android.Manifest.permission;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.civify.R;
import com.civify.activity.BaseActivity;

import java.io.IOException;

public abstract class CameraGalleryActivity extends BaseActivity {
    public static final String DATA = "data";
    public static final String IMAGE = "image/*";
    private static final int CAMERA_REQUEST = 0;
    private static final int GALLERY_REQUEST = 1;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 2;

    protected abstract void handlePhotoResult(Bitmap imageBitmap);

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
            int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cameraIntent();
                } else {
                    Toast.makeText(this, getString(R.string.camera_permission_denied),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case GALLERY_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    galleryIntent();
                } else {
                    Toast.makeText(this, getString(R.string.gallery_permission_denied),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void cameraButtonListener(View v) {
        if (checkAndRequestPermission(permission.CAMERA,
                R.string.need_to_allow_camera_permission)) {
            cameraIntent();
        }
    }

    public void galleryButtonListener(View v) {
        if (checkAndRequestPermission(permission.READ_EXTERNAL_STORAGE,
                R.string.need_to_allow_read_storage_permission)) {
            galleryIntent();
        }
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

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(
                        getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                Log.e(getLocalClassName(), e.getMessage());
            }
        }
        handlePhotoResult(bm);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get(DATA);
        handlePhotoResult(thumbnail);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType(IMAGE);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_photo)),
                GALLERY_REQUEST);
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
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            showMessageOkCancel(getString(messageRes),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(CameraGalleryActivity.this,
                                    new String[] {permission}, REQUEST_CODE_ASK_PERMISSIONS);
                        }
                    });
            return;
        }
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
}
