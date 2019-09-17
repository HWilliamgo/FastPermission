package com.william.fastpermisssion;

import java.util.ArrayList;

/**
 * author: HWilliam
 * Date  : 18-9-21
 */

public interface OnPermissionCallback {
    /**
     * 所有请求都允许
     * 该回调发生后其他回调不再发生。
     */
    void onAllGranted();

    /**
     * 部分请求被允许
     *
     * @param grantedPermissions 被允许的请求的字符串数组
     */
    void onGranted(ArrayList<String> grantedPermissions);

    /**
     * 被拒绝的请求
     *
     * @param deniedPermissions 被拒绝的请求的字符串数组
     */
    void onDenied(ArrayList<String> deniedPermissions);

    /**
     * 被永远拒绝
     *
     * @param deniedForeverP 被永远拒绝的请求
     */
    void onDeniedForever(ArrayList<String> deniedForeverP);
}
