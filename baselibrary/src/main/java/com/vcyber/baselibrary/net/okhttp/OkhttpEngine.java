package com.vcyber.baselibrary.net.okhttp;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.vcyber.baselibrary.R;
import com.vcyber.baselibrary.dialog.CustomDialog;
import com.vcyber.baselibrary.net.HttpCallback;
import com.vcyber.baselibrary.net.HttpEngine;
import com.vcyber.baselibrary.utils.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Author   : jack
 * Date     : 2018/8/23 16:32
 * E-mail   : 1215530740@qq.com
 * Describe :
 */
public class OkhttpEngine implements HttpEngine {

    File sdcache = getExternalCacheDir();
    int cacheSize = 10 * 1024 * 1024;

    OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(new LogInterceptor())
            .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize))
            .build();

    CustomDialog dialog;

    public OkhttpEngine(Context context) {
        dialog = new CustomDialog.Builder(context)
                .setContentView(R.layout.dialog_request_progress)
                .setAnimation(0)
                .create();
    }

    @Override
    public <T> void get(Context context, String url, Map headers, Map params, final HttpCallback callback) {
//        RequestBody requestBody = appendBody(mediaType, params);
        Headers headersParams = appendHeader(headers);
        Request request = new Request.Builder()
                .url(url)
                .tag(context)
                .headers(headersParams)
                .build();
        callback.onStart();
        if (dialog != null) {
            dialog.show();
        }
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Error error = new Error(call, e);
                callback.onError(error);
                if (dialog != null) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                callback.onSuccess(JSON.parseObject(body, new TypeReference<T>() {
                }));
                // 缓存处理
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
    }


    @Override
    public <T> void post(Context context, String url, String mediaType, Map headers, Map params, final HttpCallback callback) {
        RequestBody requestBody = appendBody(mediaType, params);
        Headers headersParams = appendHeader(headers);
        Request request = new Request.Builder()
                .url(url)
                .tag(context)
                .post(requestBody)
                .headers(headersParams)
                .build();
        callback.onStart();
        if (dialog != null) {
            dialog.show();
        }
        mOkHttpClient.newCall(request).enqueue(
                new Callback() {
                    @Override
                    public void onFailure(Call call, final IOException e) {
                        Error error = new Error(call, e);
                        callback.onError(error);
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String resultJson = response.body().string();
//                        callback.onSuccess(JSON.parseObject(resultJson, new TypeReference<T>() {}));
                        // 缓存处理
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                }
        );
    }

    @Override
    public <T> void uploadFile(Context context, String url, Map headers, Map params, final HttpCallback callback) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        addMultipartFileBody(params, builder);
        Headers headerParams = appendHeader(headers);
        Request request = new Request.Builder()
                .headers(headerParams)
                .url(url)
                .post(builder.build())
                .build();
        callback.onStart();
        if (dialog != null) {
            dialog.show();
        }
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.e("上传文件失败");
                Error error = new Error(call, e);
                callback.onError(error);
                if (dialog != null) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resultJson = response.body().string();
//                        callback.onSuccess(JSON.parseObject(resultJson, new TypeReference<T>() {}));
                // 缓存处理
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
    }

    @Override
    public <T> void downloadFile(Context context, final String url, Map headers, Map params, final String filePath, final HttpCallback callback) {
        Request request = new Request.Builder().url(url).build();
        callback.onStart();
        if (dialog != null) {
            dialog.show();
        }
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.e("下载文件失败");
                Error error = new Error(call, e);
                // 下载失败
                callback.onError(error);
                if (dialog != null) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath = isExistDir(filePath);
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(savePath, getNameFromUrl(url));
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        callback.onDownloadingProgress(progress);
                    }
                    fos.flush();
                    // 下载完成
                    callback.onSuccess("download success");
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    Error error = new Error(null, e);
                    // 下载失败
                    callback.onError(error);
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });


    }

    /**
     * @param saveDir
     * @return
     * @throws IOException 判断下载目录是否存在
     */
    private String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
        if(!downloadFile.exists()){
            if (!downloadFile.mkdirs()) {
                downloadFile.createNewFile();
            }
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }

    /**
     * 根据下载路径，获取文件名称
     *
     * @param url
     * @return
     */
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }


    /**
     * 添加多文件上传的参数信息
     *
     * @param params
     * @param builder
     */
    private void addMultipartFileBody(Map params, MultipartBody.Builder builder) {
        Iterator iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            if (entry.getValue() instanceof File) {
                File file = (File) entry.getValue();
                RequestBody body = RequestBody.create(MediaType.parse(ContentType.MEDIA_TYPE_MULTIPART_FORM), file);
                builder.addFormDataPart((String) entry.getKey(), file.getName(), body);
            } else {
                builder.addFormDataPart((String) entry.getKey(), (String) entry.getValue());
            }
        }
    }

    /**
     * 添加请求头
     *
     * @param headers
     * @return
     */
    private Headers appendHeader(Map headers) {
        Headers.Builder builder = new Headers.Builder();
        Iterator iterator = headers.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            builder.add((String) entry.getKey(), (String) entry.getValue());
        }
        return builder.build();

    }

    /**
     * 添加请求体
     *
     * @param mediaType
     * @param params
     * @return
     */
    private RequestBody appendBody(String mediaType, Map params) {
        MediaType type;
        RequestBody requestBody = null;
        switch (mediaType) {
            case ContentType.MEDIA_TYPE_NORAML_FORM://可以用于所有情况，默认使用 表单形式
                type = MediaType.parse(ContentType.MEDIA_TYPE_TEXT);
                requestBody = RequestBody.create(type, JSON.toJSONString(params));
                break;
            case ContentType.MEDIA_TYPE_JSON://上传json格式数据
                type = MediaType.parse(ContentType.MEDIA_TYPE_JSON);
                requestBody = RequestBody.create(type, JSON.toJSONString(params));
                break;
            case ContentType.MEDIA_TYPE_TEXT://上传字符串
                type = MediaType.parse(ContentType.MEDIA_TYPE_TEXT);
                requestBody = RequestBody.create(type, JSON.toJSONString(params));
                break;
            case ContentType.MEDIA_TYPE_STREAM://上传流，file也是只能一个
                break;
            case ContentType.MEDIA_TYPE_MULTIPART_FORM://多文件上传
                break;
        }
        return requestBody;
    }


    /**
     * 设置缓存路径
     *
     * @return
     */
    public File getExternalCacheDir() {
        return new File("");
    }
}
