package com.vcyber.baselibrary.pictureselector.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.vcyber.baselibrary.R;
import com.vcyber.baselibrary.pictureselector.Constant.SelectedImgList;
import com.vcyber.baselibrary.pictureselector.Interface.IPreviewOnClickListener;
import com.vcyber.baselibrary.pictureselector.PicSelectorControl;
import com.vcyber.baselibrary.pictureselector.entity.MediaFile;
import com.vcyber.baselibrary.widget.ToastUtils;
import com.vcyber.baselibrary.widget.previewImage.PhotoView;

import java.util.ArrayList;

/**
 * Author   : jack
 * Date     :2018/9/4 17:04
 * E-mail   : 1215530740@qq.com
 * Describe :
 */
public class PreviewAdapter extends PagerAdapter{

    ArrayList<MediaFile> mImgList;
    Context mContext;
    PicSelectorControl mControl;
    private IPreviewOnClickListener mIPreviewOnClickListener;

    public PreviewAdapter(Context context, ArrayList<MediaFile> imgList, PicSelectorControl control) {
        this.mContext = context;
        this.mImgList = imgList;
        this.mControl = control;
    }

    @Override
    public int getCount() {
        if(mImgList!=null){
            if(mControl.mFirstItemOpenCamera){
                return mImgList.size()-1;
            }else {
                return mImgList.size();
            }
        }else {
            return 0;
        }

    }


    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        View root = View.inflate(mContext, R.layout.item_pager_img_sel, null);
        final PhotoView photoView = (PhotoView) root.findViewById(R.id.ivImage);
        photoView.enable();
        final ImageView ivChecked = (ImageView) root.findViewById(R.id.ivPhotoCheaked);

        if (mControl.mMultiSelect) {
            ivChecked.setVisibility(View.VISIBLE);
            final MediaFile image = mImgList.get(mControl.mFirstItemOpenCamera ? position + 1 : position);
            if (SelectedImgList.mList.contains(image)) {
                ivChecked.setImageResource(R.drawable.ic_checked);
            } else {
                ivChecked.setImageResource(R.drawable.ic_uncheck);
            }

            ivChecked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (listener != null) {
//                        int ret = listener.onCheckedClick(position, image);
//                        if (ret == 1) { // 局部刷新
//                            if (Constant.imageList.contains(image.path)) {
//                                ivChecked.setImageResource(R.drawable.ic_checked);
//                            } else {
//                                ivChecked.setImageResource(R.drawable.ic_uncheck);
//                            }
//                        }
//                    }

                    if(SelectedImgList.mList.contains(image)){
                        SelectedImgList.mList.remove(image);
                        ivChecked.setImageResource(R.drawable.ic_uncheck);
                    }else{
                        if(SelectedImgList.mList.size()>=mControl.mMaxSize){
                            ToastUtils.show(mContext,String.format(mContext.getResources().getString(R.string.max_select_tip),mControl.mMaxSize));
                            return;
                        }
                        SelectedImgList.mList.add(image);
                        ivChecked.setImageResource(R.drawable.ic_checked);
                    }

                    if(mIPreviewOnClickListener!=null){
                        mIPreviewOnClickListener.checkViewOnClickListener(position);
                    }

                }
            });

            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (listener != null) {
//                        listener.onImageClick(position, images.get(position));
//                    }
                    if(mIPreviewOnClickListener!=null){
                        mIPreviewOnClickListener.previewOnClickListener();
                    }
                }
            });
        } else {
            ivChecked.setVisibility(View.GONE);
        }

        container.addView(root, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

//        displayImage(photoView, images.get(config.needCamera ? position + 1 : position).path);
        Glide.with(mContext)
                .load(mImgList.get(mControl.mFirstItemOpenCamera ? position + 1 : position).filePath)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(photoView);

        return root;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setPreviewOnClickListener(IPreviewOnClickListener listener){
        this.mIPreviewOnClickListener = listener;
    }
}
