package com.vcyber.baselibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.vcyber.baselibrary.R;

/**
 * Author   : jack
 * Date     : 2018/8/22 17:27
 * E-mail   : 1215530740@qq.com
 * Describe : 自定义dialog
 */
public class CustomDialog extends Dialog {

    AlertController mAlert;
    public static CustomDialog dialog;

    public CustomDialog(@NonNull Context context) {
        this(context, R.style.CustomDialog);
    }

    public CustomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        dialog = this;
        this.mAlert = new AlertController(this.getContext(), this, this.getWindow());
    }

    @Override
    public void onDetachedFromWindow() {
        dialog = null;
    }

    protected CustomDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {

        private AlertController.AlertParams P;

        public Builder(@NonNull Context context) {
            this(context, 0);
        }

        public Builder(@NonNull Context context, @StyleRes int themeResId) {
            this.P = new AlertController.AlertParams(context, themeResId);
//            this.mTheme = themeResId;
        }

        public Builder setContentView(View view) {
            P.mView = view;
            return this;
        }

        public Builder setContentView(int viewId) {
            P.mViewId = viewId;
            return this;
        }

        /**
         * 设置点击事件
         *
         * @param viewId
         * @param listener
         * @return
         */
        public Builder setOnClickListener(int viewId, View.OnClickListener listener) {
            this.P.mOnClickArray.put(viewId, listener);
            return this;
        }


        /**
         * 设置文本
         *
         * @param viewId
         * @param text
         * @return
         */
        public Builder setText(int viewId, CharSequence text) {
            this.P.mTextArray.put(viewId, text);
            return this;
        }

        public Builder setOutsideCancel(boolean outsideCancel) {
            this.P.mCancelable = outsideCancel;
            return this;
        }

        public Builder setHeight(int height) {
            this.P.mHeight = height;
            return this;
        }

        public Builder setWidth(int width) {
            this.P.mWidth = width;
            return this;
        }

        public Builder setFromBottom() {
            this.P.mGravity = Gravity.BOTTOM;
            return this;
        }

        public Builder setFullWidth() {
            this.P.mWidth = WindowManager.LayoutParams.MATCH_PARENT;
            return this;
        }

        public Builder setAnimation(int animationResId) {
            this.P.mAnimationResId = animationResId;
            return this;
        }

        public Builder setBackKeyCancelable(boolean cancelable) {
            this.P.mKeyCancelable = cancelable;
            return this;
        }

        public CustomDialog create() {
            CustomDialog dialog = new CustomDialog(this.P.mContext);
            this.P.apply(dialog.mAlert);
            dialog.setCancelable(this.P.mCancelable);
            return dialog;
        }

        public CustomDialog show() {
            CustomDialog dialog = this.create();
            dialog.show();
            return dialog;
        }
    }

}
