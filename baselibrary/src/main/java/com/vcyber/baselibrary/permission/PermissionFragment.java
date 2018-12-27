package com.vcyber.baselibrary.permission;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;


import java.util.ArrayList;
import java.util.List;

public class PermissionFragment extends Fragment {
    private static final String PERMISSION_GROUP = "permission_group";//请求的权限
    private static final int INSTALL_PACKAGES_CODE = 0x55;
    private static final int ALERT_WINDOW_CODE = 0x66;
    private static final int PERMISSION_CODE = 0x88;
    private static PermissionCallBack mCallBack;

    public static PermissionFragment newInstant(ArrayList<String> permissions,PermissionCallBack callBack) {
        mCallBack = callBack;
        PermissionFragment fragment = new PermissionFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(PERMISSION_GROUP, permissions);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 准备请求
     */
    public void prepareRequest(Activity activity) {
        activity.getFragmentManager().beginTransaction().add(this, activity.getClass().getName()).commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<String> permissions = getArguments().getStringArrayList(PERMISSION_GROUP);

        if ((permissions.contains(PermissionBean.REQUEST_INSTALL_PACKAGES) && !PermissionUtils.isHasInstallPermission(getActivity()))
                || (permissions.contains(PermissionBean.SYSTEM_ALERT_WINDOW) && !PermissionUtils.isHasOverlaysPermission(getActivity()))) {

            if (permissions.contains(PermissionBean.REQUEST_INSTALL_PACKAGES) && !PermissionUtils.isHasInstallPermission(getActivity())) {
                //跳转到允许安装未知来源设置页面
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:" + getActivity().getPackageName()));
                startActivityForResult(intent, INSTALL_PACKAGES_CODE);
            }
            if (permissions.contains(PermissionBean.SYSTEM_ALERT_WINDOW) && !PermissionUtils.isHasOverlaysPermission(getActivity())) {
                //跳转到悬浮窗设置页面
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getActivity().getPackageName()));
                startActivityForResult(intent, ALERT_WINDOW_CODE);
            }
        } else {
            requestPermission();
        }
    }

    /**
     * 请求权限
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestPermission() {
        ArrayList<String> permissions = getArguments().getStringArrayList(PERMISSION_GROUP);
        requestPermissions(permissions.toArray(new String[permissions.size() - 1]), PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        //获取授予权限
        List<String> succeedPermissions = PermissionUtils.getSucceedPermissions(permissions, grantResults);
        //如果请求成功的权限集合大小和请求的数组一样大时证明权限已经全部授予
        if (succeedPermissions.size() == permissions.length) {
            //代表申请的所有的权限都授予了
//            call.hasPermission(succeedPermissions, true);
            mCallBack.success();
        } else {
            //获取拒绝权限
//            List<String> failPermissions = PermissionUtils.getFailPermissions(permissions, grantResults);
//            //代表申请的权限中有不同意授予的，如果拒绝的时间过快证明是系统自动拒绝
//            call.noPermission(failPermissions, PermissionUtils.checkPermissionPermanentDenied(getActivity(), failPermissions));
            mCallBack.fail();
        }
        getFragmentManager().beginTransaction().remove(this).commit();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INSTALL_PACKAGES_CODE ||requestCode == ALERT_WINDOW_CODE ) {
            //需要延迟执行，不然有些华为机型授权了但是获取不到权限
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                        //请求其他危险权限
                        requestPermission();
                    }
                }
            }, 500);
        }
    }
}
