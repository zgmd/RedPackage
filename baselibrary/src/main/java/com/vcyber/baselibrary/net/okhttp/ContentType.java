package com.vcyber.baselibrary.net.okhttp;

import okhttp3.MediaType;

/**
 * Author   : jack
 * Date     : 2018/8/23 17:48
 * E-mail   : 1215530740@qq.com
 * Describe :
 */
public class ContentType {

    /**
     * "application/x-www-form-urlencoded"，是默认的MIME内容编码类型，一般可以用于所有的情况，但是在传输比较大的二进制或者文本数据时效率低。
     * 这时候应该使用"multipart/form-data"。如上传文件或者二进制数据和非ASCII数据。
     */
    public static final String MEDIA_TYPE_NORAML_FORM = "application/x-www-form-urlencoded;charset=utf-8";

    //既可以提交普通键值对，也可以提交(多个)文件键值对。
    public static final String MEDIA_TYPE_MULTIPART_FORM = "multipart/form-data;charset=utf-8";

    //只能提交二进制，而且只能提交一个二进制，如果提交文件的话，只能提交一个文件,后台接收参数只能有一个，而且只能是流（或者字节数组）
    public static final String MEDIA_TYPE_STREAM = "application/octet-stream";

    public static final String MEDIA_TYPE_TEXT = "text/plain;charset=utf-8";

    public static final String MEDIA_TYPE_JSON = "application/json;charset=utf-8";
}
