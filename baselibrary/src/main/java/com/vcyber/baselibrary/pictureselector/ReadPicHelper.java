package com.vcyber.baselibrary.pictureselector;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;

import com.vcyber.baselibrary.R;
import com.vcyber.baselibrary.pictureselector.Interface.ILoadPicListener;
import com.vcyber.baselibrary.pictureselector.entity.Folder;
import com.vcyber.baselibrary.pictureselector.entity.MediaFile;
import com.vcyber.baselibrary.utils.Logger;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Author   : jack
 * Date     :2018/9/4 15:15
 * E-mail   : 1215530740@qq.com
 * Describe :
 */
public class ReadPicHelper {


    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;
    public static final int REQUEST_PERMISSION_CODE = 0X11;
    //是否遍历过对应的文件夹了
    private boolean mHasFolderGened;

    private ArrayList<MediaFile> mImgsList = new ArrayList<>();
    private ArrayList<Folder> mFoldersList = new ArrayList<>();


    static ReadPicHelper mReadPicHelper;
    private Context mContext;
    private ILoadPicListener mLoadFinshListener;

    //此处Context可能为空
    private ReadPicHelper(Context context){
        this.mContext = context;
    }

    public static ReadPicHelper getInstacne(Context context){

        if(mReadPicHelper==null){
            synchronized (ReadPicHelper.class){
                if(mReadPicHelper==null){
                    mReadPicHelper = new ReadPicHelper(context);
                }
            }
        }
        return mReadPicHelper;
    }


    /**
     * 加载图片文件
     */
    public LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media._ID};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == LOADER_ALL) {
                return new CursorLoader(mContext,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        null, null, MediaStore.Images.Media.DATE_ADDED + " DESC");
            } else if (id == LOADER_CATEGORY) {
                return new CursorLoader(mContext,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[0] + " not like '%.gif%'", null, MediaStore.Images.Media.DATE_ADDED + " DESC");
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//            String[] columns = data.getColumnNames();
//            for (String str : columns) {
//                Logger.i("读取到的图片文件是--->" + data.getColumnIndex(str) + " = " + str);
//                ;
//            }
            if (data != null) {
                int count = data.getCount();
                if (count > 0) {
                    List<MediaFile> tempImageList = new ArrayList<>();
                    data.moveToFirst();
                    do {
                        //取出文件名称，路径
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        int size = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        //过滤无信息的图片
                        if(size==0){
                            Logger.e("我是图片大小=0的文件"+path);
                            continue;
                        }
                        Logger.i("读取到的图片文件是--->name-->" + name + "\npath--->" + path);
                        ;
                        MediaFile image = new MediaFile(path, name);
                        tempImageList.add(image);
                        if (!mHasFolderGened) {
                            File imageFile = new File(path);
                            File folderFile = imageFile.getParentFile();
                            //过滤一些错误文件
                            if (folderFile == null || !imageFile.exists() || imageFile.length() < 10) {
                                continue;
                            }
                            Folder parent = null;
                            //避免创建过的文件夹重复创建
                            for (Folder folder : mFoldersList) {
                                if (TextUtils.equals(folder.path, folderFile.getAbsolutePath())) {
                                    parent = folder;
                                }
                            }
                            if (parent != null) {
                                //将对应的文件添加到对应的文件夹下
                                parent.images.add(image);
                            } else {
                                parent = new Folder();
                                parent.name = folderFile.getName();
                                parent.path = folderFile.getAbsolutePath();
                                parent.cover = image;
                                List<MediaFile> imageList = new ArrayList<>();
                                imageList.add(image);
                                parent.images = imageList;
                                mFoldersList.add(parent);
                            }
                        }
                    } while (data.moveToNext());
                    if(mFoldersList.get(0).name!=mContext.getResources().getString(R.string.all_images)){
                        mFoldersList.add(0,new Folder(mContext.getResources().getString(R.string.all_images),"",tempImageList.get(0),tempImageList));
                    }
                    mImgsList.addAll(tempImageList);
                    mHasFolderGened = true;
                    if(mLoadFinshListener!=null){
                        mLoadFinshListener.onLoadFinsh(mImgsList,mFoldersList);
                    }

                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    public void setLoadPicListener(ILoadPicListener listener){
        this.mLoadFinshListener = listener;
    }

    public ArrayList<Folder> getmFoldersList() {
        return mFoldersList;
    }

    public ArrayList<MediaFile> getmImgsList() {
        return mImgsList;
    }
}
