package com.aoyuehan.permission.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.aoyuehan.permission.BuildConfig;
import com.aoyuehan.permission.callBack.IPermissionCallback;
import com.aoyuehan.permission.callBack.IPermissionsCallback;
import com.tbruyelle.rxpermissions3.RxPermissions;

import androidx.fragment.app.FragmentActivity;

public class PermissionHelper {

    private RxPermissions rxPermissions;

    private Context context;

    public static PermissionHelper getInstance(FragmentActivity activity) {
        return new PermissionHelper(activity);
    }

    private PermissionHelper(FragmentActivity activity) {
        context = activity;
        rxPermissions = new RxPermissions(activity);
    }

    public static boolean hasPermission(Context context, String permission) {
        PackageManager pm = context.getPackageManager();
        return PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission(permission, context.getPackageName());
    }

    public void check(IPermissionsCallback callback, String... permissions) {
        Log.d("HDH", "-----check muil permission----");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ((Activity) context).runOnUiThread(() -> {
                rxPermissions.request(permissions).subscribe(granted -> {
                    callback.onComplete(granted);
                });
            });
        } else {
            callback.onComplete(true);
        }
    }

    public void check(IPermissionCallback callback, String permission) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ((Activity) context).runOnUiThread(() -> {
                rxPermissions.requestEach(permission).subscribe(p -> {
                    if (callback != null) {
                        if (p.granted) {
                            callback.onGranted();
                        } else {
                            callback.onDeny(p.shouldShowRequestPermissionRationale);
                        }
                    }
                });
            });
        } else {
            callback.onGranted();
        }
    }

    public void check(IPermissionCallback callback, String permission, String... permissions) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ((Activity) context).runOnUiThread(() -> {
                rxPermissions.request(permissions).subscribe(granted -> {
                    if (granted) {
                        callback.onGranted();
                    } else {
                        rxPermissions.requestEach(permission).subscribe(p -> {
                            if (p.granted) {
                                callback.onGranted();
                            } else {
                                callback.onDeny(p.shouldShowRequestPermissionRationale);
                            }
                        });
                    }
                });
            });
        } else {
            callback.onGranted();
        }
    }

    public Intent getSettingsIntel() {
        String brand = Build.BRAND;//手机厂商
        Intent intent = null;
        try {
            if (TextUtils.equals(brand.toLowerCase(), "redmi") || TextUtils.equals(brand.toLowerCase(), "xiaomi")) {
                intent = getSettingsIntel4Miui(context); //小米
            } else if (TextUtils.equals(brand.toLowerCase(), "meizu")) {
                intent = getSettingsIntel4Meizu(context);
            } else if (TextUtils.equals(brand.toLowerCase(), "huawei") || TextUtils.equals(brand.toLowerCase(), "honor")) {
                intent = getSettingsIntel4Huawei(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(intent == null) {
            intent = getAppDetailSettingIntent(context);
        }
        return intent;
    }

    /**
     * miui
     * @param context
     */
    private Intent getSettingsIntel4Miui(Context context) {
        try { // MIUI 8
            Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
            localIntent.putExtra("extra_pkgname", context.getPackageName());
            return localIntent;
        } catch (Exception e) {
            Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            localIntent.putExtra("extra_pkgname", context.getPackageName());
            return localIntent;
        }
    }

    private Intent getSettingsIntel4Meizu(Context context) {
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
        return intent;
    }

    private Intent getSettingsIntel4Huawei(Context context) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
        intent.setComponent(comp);
        return intent;
    }


    private static Intent getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        return localIntent;
    }

    public Context getContext() {
        return context;
    }
}
