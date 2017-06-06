package com.jsmeli.permissioncheck.sjml;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;


import com.jsmeli.permissioncheck.sjml.util.PermissionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author:ShaoJian.
 * DATA:2017/04/21.
 * ACTION: permission check.
 * TYPE: function class.
 */

public class PermissionCheck {
    private Object obj;
    private int requestCode;
    private static boolean cue;
    private String[] permissions;
    private static PermissionCallBack callBack;
    public static final int PERMISSION_CHECK_SUCCESS = 1;
    public static final int PERMISSION_CHECK_FAILED = 2;
    public static final int PERMISSION_CHECK_CUE = 0;

    private PermissionCheck(Object object) {
        this.obj = object;
    }

    public static PermissionCheck with(Activity activity) {
        return new PermissionCheck(activity);
    }

    public static PermissionCheck with(Fragment fragment) {
        return new PermissionCheck(fragment);
    }

    public PermissionCheck setRequestCodeAndisCue(int requestCode, boolean cue) {
        this.requestCode = requestCode;
        this.cue = cue;
        return this;
    }

    public PermissionCheck needPermission(String... permissions) {
        this.permissions = permissions;
        return this;
    }

    public PermissionCheck callback(PermissionCallBack permissionCallBack) {
        this.callBack = permissionCallBack;
        return this;
    }

    @TargetApi(value = Build.VERSION_CODES.M)
    public void check() {
        requestPermissions(obj, requestCode, permissions);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void requestPermissions(final Object object, int requestCode, String[] permissions) {
        if (!PermissionUtils.isOverMarshmallow()) {
            callBack.applyResult(requestCode, PERMISSION_CHECK_SUCCESS);
            return;
        }
        if (permissions.length > 1) {
            List<String> applyPermissions = PermissionUtils.checkPermission(PermissionUtils.getActivity(object), permissions);
            if (applyPermissions.size() > 0) {
                applyPermission(object, applyPermissions, requestCode);
            } else {
                callBack.applyResult(requestCode, PERMISSION_CHECK_SUCCESS);
            }
        } else if (permissions.length == 1) {
            int ret = PermissionUtils.checkSinglePermission(PermissionUtils.getActivity(object), permissions[0]);
            if (ret == PERMISSION_CHECK_FAILED) {
                applyPermission(object, Arrays.asList(permissions), requestCode);
            } else if (ret == PERMISSION_CHECK_CUE) {
                if (cue) {
                    new AlertDialog.Builder(PermissionUtils.getActivity(object))
                            .setTitle("温馨提示")
                            .setMessage("我们需要获取存储空间，为您存储相关信息；否则，应用将不能正常运行")
                            .setCancelable(false)
                            .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.fromParts("package", PermissionUtils.getActivity(object).getPackageName(), null));
                                    PermissionUtils.getActivity(object).startActivity(intent);
                                }
                            })
                            .create()
                            .show();
                }
                callBack.applyResult(requestCode, PERMISSION_CHECK_CUE);
            } else {
                callBack.applyResult(requestCode, PERMISSION_CHECK_SUCCESS);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void applyPermission(Object object, List<String> applyPermissions, int requestCode) {
        if (object instanceof Activity) {
            ((Activity) object).requestPermissions(applyPermissions.toArray(new String[applyPermissions.size()]), requestCode);
        } else if (object instanceof Fragment) {
            ((Fragment) object).requestPermissions(applyPermissions.toArray(new String[applyPermissions.size()]), requestCode);
        } else {
            throw new IllegalArgumentException(object.getClass().getName() + " is incorrect , please check the calling location");
        }
    }

    public static void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions,
                                                  int[] grantResults) {
        requestResult(activity, requestCode, permissions, grantResults);
    }

    public static void onRequestPermissionsResult(Fragment fragment, int requestCode, String[] permissions,
                                                  int[] grantResults) {
        requestResult(fragment, requestCode, permissions, grantResults);
    }

    private static void requestResult(final Object obj, final int requestCode, final String[] permissions,
                                      int[] grantResults) {
        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i]);
            }
        }

        if (deniedPermissions.size() > 0) {
            if (permissions.length == 1) {
                int ret = PermissionUtils.checkSinglePermission(PermissionUtils.getActivity(obj), permissions[0]);
                if (ret == PERMISSION_CHECK_CUE) {
                    if (cue) {
                        new AlertDialog.Builder(PermissionUtils.getActivity(obj))
                                .setTitle("温馨提示")
                                .setMessage("我们需要获取存储空间，为您存储相关信息；否则，应用将不能正常运行")
                                .setCancelable(false)
                                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        intent.setData(Uri.fromParts("package", PermissionUtils.getActivity(obj).getPackageName(), null));
                                        PermissionUtils.getActivity(obj).startActivity(intent);
                                    }
                                })
                                .create()
                                .show();
                    }
                    callBack.applyResult(requestCode, PERMISSION_CHECK_CUE);
                } else if (ret == PERMISSION_CHECK_FAILED) {
                    if (cue) {
                        new AlertDialog.Builder(PermissionUtils.getActivity(obj))
                                .setTitle("温馨提示")
                                .setMessage("请您打开存储权限，否则将影响您的使用!")
                                .setCancelable(false)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        applyPermission(obj, Arrays.asList(permissions), requestCode);
                                    }
                                })
                                .create()
                                .show();
                    }
                    callBack.applyResult(requestCode, PERMISSION_CHECK_FAILED);
                }
            } else {
                callBack.applyResult(requestCode, PERMISSION_CHECK_FAILED);
            }
        } else {
            callBack.applyResult(requestCode, PERMISSION_CHECK_SUCCESS);
        }
    }

}
