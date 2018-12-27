package com.vcyber.baselibrary.pictureselector.Interface;

import com.vcyber.baselibrary.pictureselector.entity.Folder;
import com.vcyber.baselibrary.pictureselector.entity.MediaFile;

import java.util.ArrayList;

/**
 * Author   : jack
 * Date     :2018/9/4 15:46
 * E-mail   : 1215530740@qq.com
 * Describe :
 */
public interface ILoadPicListener {
    void onLoadFinsh(ArrayList<MediaFile> imgs, ArrayList<Folder> folders);
}
