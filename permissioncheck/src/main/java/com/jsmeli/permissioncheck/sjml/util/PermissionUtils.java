package com.jsmeli.permissioncheck.sjml.util;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;


import com.jsmeli.permissioncheck.sjml.PermissionCheck;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Shaojian
 * DATA:2017/05/17.
 * ACTION: utility class
 * TYPE: normal
 */

public class PermissionUtils {

    public static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static List<String> checkPermission(Activity activity, String...permissions) {
        List<String> applyPermissions = new ArrayList<>();
        for(String value : permissions){
            if(activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED){
                applyPermissions.add(value);
            }
        }
        return applyPermissions;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static int checkSinglePermission(Activity activity, String permissions) {
        int ret = PermissionCheck.PERMISSION_CHECK_FAILED;
        String[] string = permissions.split("\\.");
        if (activity.checkSelfPermission(permissions) != PackageManager.PERMISSION_GRANTED) {
            boolean isFirst = (boolean)SPUtils.get(activity, string[string.length - 1], true);
            if (isFirst) {
                //表明用户没有彻底禁止弹出权限请求
                ret = PermissionCheck.PERMISSION_CHECK_FAILED;
                SPUtils.put(activity, string[string.length - 1], false);
            } else {
                if (activity.shouldShowRequestPermissionRationale(permissions)) {
                    //表明用户没有彻底禁止弹出权限请求
                    ret = PermissionCheck.PERMISSION_CHECK_FAILED;
                } else {
                    //表明用户已经彻底禁止弹出权限请求
                    ret = PermissionCheck.PERMISSION_CHECK_CUE;
                }
            }
        } else {
            ret = PermissionCheck.PERMISSION_CHECK_SUCCESS;
        }
        return ret;
    }

    public static Activity getActivity(Object object){
        if(object instanceof Fragment){
            return ((Fragment)object).getActivity();
        } else if(object instanceof Activity){
            return (Activity) object;
        }
        return null;
    }
}