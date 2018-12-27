package com.mayi.jack.redpackage;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mayi.jack.redpackage.wx.service.AccessibilityUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!AccessibilityUtils.isAccessibilitySettingsOn(this)) {
            new AlertDialog.Builder(this)
                    .setMessage("没有开启辅助功能，是否开启")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity(intent);
                }
            }).create().show();
        }
    }
}
