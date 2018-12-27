package com.vcyber.baselibrary.net;

import android.content.Context;

import com.vcyber.baselibrary.net.okhttp.ContentType;
import com.vcyber.baselibrary.net.okhttp.OkhttpEngine;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Author   : jack
 * Date     : 2018/8/23 15:44
 * E-mail   : 1215530740@qq.com
 * Describe : http请求类
 */
public class HttpUtils {

    private Context mContext;
    private HashMap<Object, Object> mParams;
    private HashMap<String, Object> mHeader;
    private String mUrl;
    private HttpEngine mHttpEngine;
    private int mRequestType;
    //默认使用这个模式
    private String mMediaType = ContentType.MEDIA_TYPE_NORAML_FORM;
    //下载文件，保存路径
    private String mFielPath;

    private HttpUtils(Context context) {
        mHttpEngine = new OkhttpEngine(context);
        this.mContext = context;
        mParams = new HashMap<>();
        mHeader = new HashMap<>();
    }


    public static HttpUtils with(Context context) {
        return new HttpUtils(context);
    }

    public HttpUtils url(String url) {
        this.mUrl = url;
        return this;
    }

    public HttpUtils requestType(int type) {
        this.mRequestType = type;
        return this;
    }

    public HttpUtils addParam(String key, Object vale) {
        this.mParams.put(key, vale);
        return this;
    }

    public HttpUtils addParams(Map params) {
        this.mParams.putAll(params);
        return this;
    }

    public HttpUtils addParams(JSONObject jsonObject) {
        com.alibaba.fastjson.JSONObject.parse(jsonObject.toString());
        this.mParams.putAll((Map<?, ?>) com.alibaba.fastjson.JSONObject.parse(jsonObject.toString()));
        return this;
    }

    public HttpUtils addHeader(String key, Object vale) {
        this.mHeader.put(key, vale);
        return this;
    }

    public HttpUtils addMediaType(String mediaType){
        this.mMediaType = mediaType;
        return this;
    }

    //这里的路径是在SD卡根路径下的文件路径
    public HttpUtils addFilePath(String path){
        this.mFielPath = path;
        return this;
    }

    // 切换引擎
    public HttpUtils exchangeEngine(HttpEngine httpEngine) {
        this.mHttpEngine = httpEngine;
        return this;
    }

    public void execute(HttpCallback callBack) {
        switch (mRequestType) {
            case RequestType.GET:
                mHttpEngine.get(mContext,mUrl,mHeader,mParams,callBack);
                break;
            case RequestType.POST:
                mHttpEngine.post(mContext,mUrl,mMediaType,mHeader,mParams,callBack);
                break;
            case RequestType.UPLOAD_FILE:
                mHttpEngine.uploadFile(mContext,mUrl,mHeader,mParams,callBack);
                break;
            case RequestType.DOWNLOAD_FILE:
                mHttpEngine.downloadFile(mContext,mUrl,mHeader,mParams,mFielPath,callBack);
                break;
        }
    }


}
