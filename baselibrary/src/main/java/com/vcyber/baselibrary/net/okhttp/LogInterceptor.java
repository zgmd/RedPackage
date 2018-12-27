package com.vcyber.baselibrary.net.okhttp;

import com.vcyber.baselibrary.utils.Logger;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Author   : jack
 * Date     : 2018/8/23 18:09
 * E-mail   : 1215530740@qq.com
 * Describe :
 */
public class LogInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        //请求部分
        Request request = chain.request();
        long t1 = System.nanoTime();
        Logger.i(String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));


        //响应部分
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        Logger.i(String.format("Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));
        return response;
    }
}
