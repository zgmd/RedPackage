package com.vcyber.baselibrary.pictureselector;


import android.view.View;
import java.io.Serializable;

public class PicSelectorControl implements Serializable{

//    public Context mContext;
    public int mRequestCode;
    //是否是自定义的view，来加载布局
    public boolean mCustomView;
    //加载图片选择器的view
    public int mPicSelectorViewId;
    //加载图片选择器的view
    public View mPicSelectorView;
    //是否支持多选
    public boolean mMultiSelect;
    //最多支持选择图片个数
    public int mMaxSize;
    //直接打开相机
    public boolean mOpenCamera;
    //第一个item是否是拍照
    public boolean mFirstItemOpenCamera;
    //recyclerViewId
    public int mRecyclerViewId;
    //标题ID
    public int mTitleId;
    //选择图片的列数
    public int mColumn = 4;
    //预览图片viewpager
    public int mVPId;
    //返回键ID
    public int mBackId;
    //确定键ID
    public int mConfirmId;
    //确定键ID
    public int mFolderSelect;
    //是否裁剪
    public boolean mClip;
    //裁剪模式
    public int mClipMode;


//    public PicSelectorControl(Context context) {
//        this.mContext = context;
//    }


    public void startSelector() {

        //是否开启相机
        if(mOpenCamera){
            return;
        }
//        //开启图片选择的Activity
//        if(mContext!=null){
//            if(mContext instanceof Activity){
//                Intent intent = new Intent(mContext,PicSelectorActivity.class);
//                intent.putExtra("control",this);
//                ((Activity) mContext).startActivityForResult(intent,mRequestCode);
//            }
////            Fragment
//        }
    }

    @Override
    public String toString() {
        return "PicSelectorControl{" +
                "mContext="  +
                ", mRequestCode=" + mRequestCode +
                ", mCustomView=" + mCustomView +
                ", mPicSelectorViewId=" + mPicSelectorViewId +
                ", mPicSelectorView=" + mPicSelectorView +
                ", mMultiSelect=" + mMultiSelect +
                ", mMaxSize=" + mMaxSize +
                ", mOpenCamera=" + mOpenCamera +
                ", mFirstItemOpenCamera=" + mFirstItemOpenCamera +
                '}';
    }
}
