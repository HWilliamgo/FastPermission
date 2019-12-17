package com.william.fastpermisssion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;


import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;

/**
 * author: HWilliam
 * Date  : 18-9-21
 */

public class FastPermission {
    private FastPermission() {
    }

    private static class SingletonHolder {
        private static final FastPermission INSTANCE = new FastPermission();
    }

    public static FastPermission getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void start(Activity activity, ArrayList<String> permissions, OnPermissionCallback callback) {
        //api<23则不需要动态请求权限。
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            callback.onAllGranted();
            return;
        }
        ArrayList<String> target = new ArrayList<>(permissions.size());
        for (int i = 0; i < permissions.size(); i++) {
            String permission = permissions.get(i);
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                target.add(permission);
            }
        }
        //如果所有请求都被允许，那么直接返回，不要去创建Fragment。
        if (target.size() == 0) {
            callback.onAllGranted();
            return;
        }
        VoidFragment fragment = VoidFragment.newInstance(target);
        fragment.setCallback(callback);
        activity.getFragmentManager().beginTransaction().add(fragment, activity.getClass().getSimpleName()).commit();
    }

    /**
     * 跳转到当前app的系统设置页面，用于在权限被永久禁用的情况下，让用户手动开启权限
     *
     * @param context 任何上下文
     */
    public void jump2Settings(Context context) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + context.getApplicationInfo().packageName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
