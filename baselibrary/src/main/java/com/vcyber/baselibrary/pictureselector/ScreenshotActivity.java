package com.vcyber.baselibrary.pictureselector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.vcyber.baselibrary.R;
import com.vcyber.baselibrary.pictureselector.widget.ClipImageLayout;

public class ScreenshotActivity extends AppCompatActivity {

    public static final String CLIP_MODE = "CLIP_MODE";
    public static final String PIC_PATH = "PIC_PATH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_screenshot);
        final ClipImageLayout clipImageLayout = findViewById(R.id.cl_content);
        Button mBtnSave = findViewById(R.id.btnConfirm);
        ImageView mIvBack = findViewById(R.id.ivBack);
        int mode = getIntent().getIntExtra(CLIP_MODE, 0);
        String picPath = getIntent().getStringExtra(PIC_PATH);
        clipImageLayout.setClipMode(mode);
        ImageView containerView = clipImageLayout.getContainerView();
        Glide.with(this).load(picPath).into(containerView);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = clipImageLayout.clip();
                Intent intent = new Intent();
                intent.putExtra(PicSelector.SELECT_RESULT, path);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
