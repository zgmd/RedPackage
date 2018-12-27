package com.vcyber.baselibrary.permission;

import android.app.Activity;
import android.os.Build;
import com.vcyber.baselibrary.utils.StringUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PermissionHelper {


    public static void proxy(final Activity activity, final int proxyId) {
        //申请列表权限
        final List<String> permissionList = new LinkedList<>();
        Method[] methods = activity.getClass().getDeclaredMethods();
        //申请成功之后的，处理方法
        Method proxySuccessMethod = null;
        //申请失败之后的，处理方法
        Method proxyFailMethod = null;
        String permissionName = null;
        String[] permissions = null;
        for (Method m : methods) {
            m.setAccessible(true);
            Permission p = m.getAnnotation(Permission.class);
            if (p != null) {
                final int id = p.id();
                if (proxyId == id) {
                    permissionName = p.permissionName();
                    permissions = p.permissionNames();
                    proxySuccessMethod = m;
                }
            }
            requestPermissionFail fail = m.getAnnotation(requestPermissionFail.class);
            if (fail != null) {
                proxyFailMethod = m;
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //处理权限请求
            if (!StringUtils.isEmptyOrNull(permissionName)) {
                permissionList.add(permissionName);
            }
            if (permissions != null && permissions.length > 0) {
                permissionList.addAll(Arrays.asList(permissions));
            }
            ArrayList<String> permission = PermissionUtils.getFailOrUncommittedPermissions(activity, permissionList);
            if (permission.size() == 0) {
                dispatchMethod(activity, proxyId, proxySuccessMethod);
                return;
            }
            PermissionUtils.checkPermissions(activity, permissionList);
            //申请没有授予过的权限
            final Method finalProxySuccessMethod = proxySuccessMethod;
            final Method finalProxyFailMethod = proxyFailMethod;
            PermissionFragment.newInstant(permission, new PermissionCallBack() {
                @Override
                public void success() {
                    if(permissionList.size()<=1){
                        dispatchMethod(activity, 0, finalProxySuccessMethod);
                    }else{
                        dispatchMethod(activity, proxyId, finalProxySuccessMethod);
                    }
                }
                @Override
                public void fail() {
                    dispatchMethod(activity, proxyId, finalProxyFailMethod);
                }
            }).prepareRequest(activity);

        } else {
            final Method finalProxySuccessMethod = proxySuccessMethod;
            dispatchMethod(activity, proxyId, finalProxySuccessMethod);
        }
    }

    private static void dispatchMethod(Activity activity, int proxyId, Method finalProxySuccessMethod) {
        try {
            finalProxySuccessMethod.invoke(activity, proxyId);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }



}
