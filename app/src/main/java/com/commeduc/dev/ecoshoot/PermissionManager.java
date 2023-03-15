package com.commeduc.dev.ecoshoot;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionManager {
    //Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int REQUEST_CODE = 5471;
    private static final int PERMISSION_REFUSED = -1;
    private static final int PERMISSION_ACCEPTED = 1;
    private static final int PERMISSION_PENDING = 0;
    private static final String TAG = PermissionManager.class.getName();
    private static boolean isAskingForPermission = false;
    private Map<String, Integer> permissionsToCheck = new HashMap<>();

    public void checkPermission(@NonNull Context context, @NonNull String permission, @NonNull PermissionCallBack callBack) {
        if (!isAskingForPermission) {
            if (ContextCompat.checkSelfPermission(context, permission) != PERMISSION_GRANTED) {
                isAskingForPermission = true;
                if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                        permission)) {
                    if (!permissionsToCheck.isEmpty() && permissionsToCheck.containsKey(permission)) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{permission}, permissionsToCheck.get(permission));
                    } else {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{permission}, REQUEST_CODE);
                    }
                }
            }
        } else {
            callBack.checkComplete(PERMISSION_PENDING);
        }
    }

    public void checkPermission(@NonNull Context context, @NonNull List<String> permissions) {
        Log.d(TAG, "::checkPermission() called with: context = [" + context + "], permissions = [" + permissions + "]");
        permissionsToCheck.clear();
        int start = REQUEST_CODE + 1;
        for (String permission : permissions) {
            permissionsToCheck.put(permission, start++);
        }
        requestPermission(context);
    }

    private void requestPermission(@NonNull final Context context) {
        if (permissionsToCheck.isEmpty())
            return;
        checkPermission(context, (String) permissionsToCheck.keySet().toArray()[0], new PermissionCallBack() {
            @Override
            public void checkComplete(int callState) {
                if (callState != PERMISSION_PENDING) {
                    if (!permissionsToCheck.isEmpty()) {
                        permissionsToCheck.remove((String) permissionsToCheck.keySet().toArray()[0]);
                        requestPermission(context);
                    }
                }
            }
        });
    }


    public void onRequestPermissionsResult(@NonNull Context context, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, @NonNull PermissionCallBack callBack) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callBack.checkComplete(PERMISSION_ACCEPTED);
            } else {
                callBack.checkComplete(PERMISSION_REFUSED);
            }
        }
        isAskingForPermission = false;
    }

    public void onRequestPermissionsResult(@NonNull Context context, int requestCode, String[] permissions, int[] grantResults) {
        final Context context1 = context;
        onRequestPermissionsResult(context, requestCode, permissions, grantResults, new PermissionCallBack() {
            @Override
            public void checkComplete(int callState) {
                if (callState != PERMISSION_PENDING) {
                    if (!permissionsToCheck.isEmpty()) {
                        permissionsToCheck.remove((String) permissionsToCheck.keySet().toArray()[0]);
                        requestPermission(context1);
                    }
                }
            }
        });
    }

    static interface PermissionCallBack {
        void checkComplete(int callState);
    }
}
