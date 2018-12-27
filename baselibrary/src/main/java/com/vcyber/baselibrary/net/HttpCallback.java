package com.vcyber.baselibrary.net;

import com.vcyber.baselibrary.net.okhttp.Error;

/**
 * Author   : jack
 * Date     : 2018/8/23 16:19
 * E-mail   : 1215530740@qq.com
 * Describe :
 */
public interface HttpCallback<T> {
    void onStart();
    void onSuccess(T result);
    void onError(Error error);
    void onDownloadingProgress(int progress);
}
