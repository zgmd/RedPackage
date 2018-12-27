package com.vcyber.baselibrary.net.okhttp;

import java.io.IOException;

import okhttp3.Call;

/**
 * Author   : jack
 * Date     : 2018/8/23 18:17
 * E-mail   : 1215530740@qq.com
 * Describe :
 */
public class Error  {
    public Call call;
    public Exception exception;

    public Error(Call call, Exception exception) {
        this.call = call;
        this.exception = exception;
    }
}
