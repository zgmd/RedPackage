package com.vcyber.baselibrary.pictureselector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

public class PicSelector {

    public static final String SELECT_RESULT = "SELECT_RESULT";
    public static final int MODE_CLIP_CIRCLE  = 0X12;
    public static final int MODE_CLIP_SQUARE  = 0X13;

    public static Builder builder(Context context) {
        return new Builder(context);
    }

    public static class Builder {
        PicSelectorControl control;
        private Context mContext;

        public Builder(Context context) {
            this.mContext = context;
            control = new PicSelectorControl();
        }

        public Builder isCustomView(boolean isCustomView) {
            control.mCustomView = isCustomView;
            return this;
        }

        public Builder setPicSelectorView(int viewId) {
            control.mPicSelectorViewId = viewId;
            return this;
        }

        private Builder setPicSelectorView(View view) {
            control.mPicSelectorView = view;
            return this;
        }


        /**
         * 多选配置
         * @param multiSelect 是否支持多选
         * @param maxSize  最多支持选择图片个数
         * @return
         */
        public Builder multiSelect(boolean multiSelect,int maxSize){
            control.mMultiSelect = multiSelect;
            control.mMaxSize = maxSize;
            return this;
        }
        /**
         * 多选配置
         * @param multiSelect 是否支持多选
         * @return
         */
        public Builder multiSelect(boolean multiSelect){
            control.mMultiSelect = multiSelect;
            return this;
        }
        //直接打开相机
        public Builder opneCamera(boolean openCamera){
            control.mOpenCamera = openCamera;
            return this;
        }
        //第一个是否是打开相机
        public Builder firstItemOpneCamera(boolean firstItem){
            control.mFirstItemOpenCamera = firstItem;
            return this;
        }
        //是否裁剪
        public Builder clip(boolean clip){
            control.mClip = clip;
            return this;
        }
        //裁剪模式
        public Builder clipMode(int mode){
            control.mClipMode = mode;
            return this;
        }
        public PicSelector start(){
//
            //开启图片选择的Activity
            if(mContext!=null){
                if(mContext instanceof Activity){
                    Intent intent = new Intent(mContext,PictureSelectorActivity.class);
                    intent.putExtra("control",control);
                    ((Activity) mContext).startActivityForResult(intent,0);
                }
//            Fragment
            }

            return new PicSelector();
        }
    }
}
