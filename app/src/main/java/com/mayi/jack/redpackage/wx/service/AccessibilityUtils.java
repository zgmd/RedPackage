package com.mayi.jack.redpackage.wx.service;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

import com.vcyber.baselibrary.utils.Logger;

/**
 * author: JACK
 * eamil: 1215530740@qq.com
 * date: 2018/12/27 15:52
 * des:
 */
public class AccessibilityUtils {


    public static boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = "com.mayi.jack.redpackage.wx.service.RedPackageService";
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
           Logger.e( "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Logger.e( "Error finding setting, default accessibility to not found: "
                    + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            Logger.e( "***ACCESSIBILITY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();

                    Logger.e( "-------------- > accessibilityService :: " + accessibilityService + " " + service);
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        Logger.e( "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Logger.e( "***ACCESSIBILITY IS DISABLED***");
        }

        return false;
    }
}
