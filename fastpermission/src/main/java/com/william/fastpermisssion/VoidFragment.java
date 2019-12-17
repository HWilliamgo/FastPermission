package com.william.fastpermisssion;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;


import java.util.ArrayList;

/**
 * author: HWilliam
 * Date  : 18-9-21
 */
public class VoidFragment extends Fragment {
    private static final String TAG = VoidFragment.class.getSimpleName();
    private static final int PERMISSION_GRANT = PackageManager.PERMISSION_GRANTED;
    private static final int PERMISSION_DENIED = PackageManager.PERMISSION_DENIED;

    private static final String sBUNDLE_PERMISSIONS = "bundle_permissions";

    private static final int sREQUEST_CODE = 100;

    private Activity mActivity;

    private OnPermissionCallback mCallback;


    public static VoidFragment newInstance(ArrayList<String> permissions) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(sBUNDLE_PERMISSIONS, permissions);

        VoidFragment fragment = new VoidFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setCallback(OnPermissionCallback callback) {
        mCallback = callback;
    }

    private void checkPermissions(ArrayList<String> p) {
        String[] targetPermissions = p.toArray(new String[p.size()]);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(targetPermissions, sREQUEST_CODE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle == null) {
            Log.e(TAG, "没有要请求的权限");
            return;
        }
        ArrayList<String> permissions = bundle.getStringArrayList(sBUNDLE_PERMISSIONS);
        if (permissions == null) {
            Log.e(TAG, "没有要请求的权限");
            return;
        }
        checkPermissions(permissions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean allGrant = true;
        for (int grantResult : grantResults) {
            if (grantResult == PERMISSION_DENIED) {
                allGrant = false;
                break;
            }
        }
        if (allGrant) {
            mCallback.onAllGranted();
            return;
        }
        ArrayList<String> grantList = new ArrayList<>();
        ArrayList<String> denyList = new ArrayList<>();
        ArrayList<String> denyForever = new ArrayList<>();

        for (int i = 0; i < grantResults.length; i++) {
            String permission = permissions[i];
            if (grantResults[i] == PERMISSION_DENIED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)) {
                    //用户没有永远禁止权限，可以再次请求。
                    denyList.add(permission);
                } else {
                    //权限被永远禁止。
                    denyForever.add(permission);
                }
            } else {
                //权限被允许
                grantList.add(permission);
            }
        }

        //将结果发布
        if (denyList.size() != 0) {
            mCallback.onDenied(denyList);
        }
        if (grantList.size() != 0) {
            mCallback.onGranted(grantList);
        }
        if (denyForever.size() != 0) {
            mCallback.onDeniedForever(denyForever);
        }

        //移除VoidFragment
        getFragmentManager().beginTransaction().remove(this).commit();
    }
}
