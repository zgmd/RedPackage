package com.vcyber.baselibrary.net;

/**
 * Author   : jack
 * Date     : 2018/8/23 16:27
 * E-mail   : 1215530740@qq.com
 * Describe :
 */
public abstract class DefaultCallback<T> implements HttpCallback<T>{

    @Override
    public void onStart() {

    }

    public void onDownloadingProgress(int progress){}
}
