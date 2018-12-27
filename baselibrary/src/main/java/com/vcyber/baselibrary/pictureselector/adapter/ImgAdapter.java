package com.vcyber.baselibrary.pictureselector.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.vcyber.baselibrary.R;
import com.vcyber.baselibrary.pictureselector.Constant.SelectedImgList;
import com.vcyber.baselibrary.pictureselector.Interface.ItemOnClickListener;
import com.vcyber.baselibrary.pictureselector.PicSelectorControl;
import com.vcyber.baselibrary.pictureselector.entity.MediaFile;
import com.vcyber.baselibrary.utils.Logger;
import com.vcyber.baselibrary.widget.ToastUtils;

import java.io.File;
import java.util.ArrayList;

import javax.sql.DataSource;

public class ImgAdapter extends RecyclerView.Adapter {

    private static final int FIRST_ITEM_OPEN_CAMERA = 1;
    private static final int NORMAL_IMG_ITEM = 2;
    private final Context mContext;
    private ArrayList<MediaFile> mImgList;
    private PicSelectorControl mControl;
    private ItemOnClickListener mItemOnClickListener;

    public ImgAdapter(Context context, PicSelectorControl control, ArrayList<MediaFile> imgList) {
        this.mContext = context;
        this.mControl = control;
        this.mImgList = imgList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == FIRST_ITEM_OPEN_CAMERA) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.first_item_camera_layout, null);
            viewHolder = new FirstOpenCameraViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_img_selector, null);
            viewHolder = new ImgViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int postion) {
        final MediaFile file = mImgList.get(postion);
        if (getItemViewType(postion) == FIRST_ITEM_OPEN_CAMERA) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Logger.i("点击了第一个item");
                    if(mItemOnClickListener!=null){
                        Logger.i("点击了第一个item,,,,");
                        mItemOnClickListener.onBigPicClickListener(postion);
                    }
                }
            });
        } else {
            if(!mControl.mMultiSelect){
                ((ImgViewHolder) viewHolder).mIvPhotoCheaked.setVisibility(View.GONE);
            }
            ((ImgViewHolder) viewHolder).mIvPhotoCheaked.setImageResource(R.drawable.ic_uncheck);
            ColorDrawable drawable = new ColorDrawable(Color.GRAY);
            Glide.with(mContext)
                    .load(mImgList.get(postion).filePath)
                    .placeholder(drawable)
                    .into(((ImgViewHolder) viewHolder).mIvPic);
            //大图点击
            ((ImgViewHolder) viewHolder).mIvPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mItemOnClickListener!=null){
                        mItemOnClickListener.onBigPicClickListener(postion);
                        Logger.i("点击的图片：name-->ddddddddddddddddd" + mImgList.get(postion).fileName);
                    }
                    Logger.i("点击的图片：name-->" + mImgList.get(postion).fileName);
                    Logger.i("点击的图片：path-->" + mImgList.get(postion).filePath);
                }
            });
            //复选框点击事件
            ((ImgViewHolder) viewHolder).mIvPhotoCheaked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Logger.i("选择的数量为--->"+SelectedImgList.mList.size());
                    //检查是否是取消选中
                    if (SelectedImgList.mList.contains(file)) {
                        SelectedImgList.mList.remove(file);
                        ((ImageView) v).setImageResource(R.drawable.ic_uncheck);
                        Logger.i("选择的数量为--->"+SelectedImgList.mList.size());
                        if(mItemOnClickListener!=null){
                            mItemOnClickListener.onSmallPicClickListener(SelectedImgList.mList.size());
                        }
                        return;
                    }
                    //检查是否选择了最大数量的图片
                    if (SelectedImgList.mList.size() >= mControl.mMaxSize) {
//                        ToastUtils.show(mContext, mContext.getResources().getText(R.string.max_select_tip) + mControl.mMaxSize + mContext.getResources().getText(R.string.zhang));
                        ToastUtils.show(mContext,String.format(mContext.getResources().getString(R.string.max_select_tip),mControl.mMaxSize));
                        Logger.i("选择的数量为--->"+SelectedImgList.mList.size());
                        return;
                    }
                    //不包含，选中该图片
                    if (!SelectedImgList.mList.contains(mImgList.get(postion))) {
                        SelectedImgList.mList.add(file);
                        ((ImageView) v).setImageResource(R.drawable.ic_checked);
                        Logger.i("选择的数量为--->"+SelectedImgList.mList.size());
                    }
                    if(mItemOnClickListener!=null){
                        mItemOnClickListener.onSmallPicClickListener(SelectedImgList.mList.size());
                    }
                }
            });

            //有选中的，默认选中
            if (SelectedImgList.mList.size() > 0) {
                if (SelectedImgList.mList.contains(file)) {
                    ((ImgViewHolder) viewHolder).mIvPhotoCheaked.setImageResource(R.drawable.ic_checked);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mImgList != null ? mImgList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mControl.mFirstItemOpenCamera) {
            return FIRST_ITEM_OPEN_CAMERA;
        } else {
            return NORMAL_IMG_ITEM;
        }
    }

    public void setmItemOnClickListener(ItemOnClickListener itemOnClickListener){
        this.mItemOnClickListener = itemOnClickListener;
    }

    public void setImgList(ArrayList<MediaFile> imgList) {
        this.mImgList = imgList;
        notifyDataSetChanged();
    }

    public void addImgList(ArrayList<MediaFile> imgList) {
        if (mImgList != null) {
            this.mImgList.addAll(imgList);
            notifyDataSetChanged();
        }
    }

    public void addImg(MediaFile imgFile) {
        if (mImgList != null) {
            this.mImgList.add(0, imgFile);
            notifyDataSetChanged();
        }
    }


    static class ImgViewHolder extends RecyclerView.ViewHolder {
        public ImageView mIvPic;
        public ImageView mIvPhotoCheaked;

        public ImgViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvPic = itemView.findViewById(R.id.ivImage);
            mIvPhotoCheaked = itemView.findViewById(R.id.ivPhotoCheaked);
        }
    }

    static class FirstOpenCameraViewHolder extends RecyclerView.ViewHolder {
        public ImageView mIvTakePhoto;

        public FirstOpenCameraViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvTakePhoto = itemView.findViewById(R.id.ivTakePhoto);
        }
    }
}
