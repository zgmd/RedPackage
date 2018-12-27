package com.vcyber.baselibrary.net;

import android.content.Context;

import java.util.Map;

/**
 * Author   : jack
 * Date     : 2018/8/23 15:50
 * E-mail   : 1215530740@qq.com
 * Describe :
 */
public interface HttpEngine {

    <T> void get(Context context, String url, Map headers,Map params,HttpCallback callback);
    <T> void post(Context context, String url,String mediaType, Map headers,Map params,HttpCallback callback);
    <T> void uploadFile(Context context, String url, Map headers,Map params,HttpCallback callback);
    <T> void downloadFile(Context context, String url, Map headers,Map params,String filePath,HttpCallback callback);
}
