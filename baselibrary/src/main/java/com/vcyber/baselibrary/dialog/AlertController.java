package com.vcyber.baselibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.vcyber.baselibrary.R;

/**
 * Author   : jack
 * Date     : 2018/8/23 9:41
 * E-mail   : 1215530740@qq.com
 * Describe :
 */
public class AlertController {

    public Dialog mDialog;
    public Window mWindow;

    public AlertController(Context context, CustomDialog customDialog, Window window) {
        this.mDialog = customDialog;
        this.mWindow = window;
    }

    public static class AlertParams {
        public int mViewId;
        public View mView;
        public Context mContext;
        //点击外部是否取消
        public boolean mCancelable;
        //存放点击事件
        public SparseArray<View.OnClickListener> mOnClickArray;
        //存放文本
        public SparseArray<CharSequence> mTextArray;
        public int mHeight = WindowManager.LayoutParams.WRAP_CONTENT;
        public int mWidth = WindowManager.LayoutParams.WRAP_CONTENT;
        //window中的位置
        public int mGravity = Gravity.CENTER;
        //动画
        public int mAnimationResId = R.style.CustomDialogWindowAnim;
        //返回键，取消
        public boolean mKeyCancelable;

        public int mThemeId;


        public AlertParams(Context context, int themeId) {
            this.mContext = context;
            mOnClickArray = new SparseArray();
            mTextArray = new SparseArray<>();
        }

        public void apply(AlertController mAlert) {
            DialogViewHelper viewHelper = null;
            //设置view
            if (mViewId != 0) {
                viewHelper = new DialogViewHelper(mContext, mViewId);
            }
            if (mView != null) {
                if (viewHelper == null) {
                    viewHelper = new DialogViewHelper(mContext, mView);
                }
                viewHelper.setContentView(mView);
            }
            mAlert.mDialog.setContentView(viewHelper.getContentView());
            //设置点击事件
            int onClickCount = mOnClickArray.size();
            for (int i = 0; i < onClickCount; i++) {
                viewHelper.setOnClickListener(mOnClickArray.keyAt(i), mOnClickArray.valueAt(i));
            }
            //设置文本
            int onTextCount = mTextArray.size();
            for (int i = 0; i < onTextCount; i++) {
                viewHelper.setText(mTextArray.keyAt(i), mTextArray.valueAt(i));
            }
            //设置宽高
            WindowManager.LayoutParams params =  mAlert.mWindow.getAttributes();
            params.height = mHeight;
            params.width = mWidth;
            params.gravity = mGravity;

            mAlert.mWindow.setAttributes(params);

            //设置动画
            mAlert.mWindow.setWindowAnimations(mAnimationResId);

            //其他
            mAlert.mDialog.setCanceledOnTouchOutside(mCancelable);

            mAlert.mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return mKeyCancelable;
                }
            });
        }
    }
}
