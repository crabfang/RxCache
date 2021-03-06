package com.cabe.lib.cache.http;

import android.net.Uri;

import java.io.File;
import java.util.Map;

/**
 * Http Request Params Bean
 * Created by cabe on 16/4/12.
 */
public class RequestParams {
    public final static int REQUEST_METHOD_GET = 0;
    public final static int REQUEST_METHOD_POST = 1;
    public final static int REQUEST_METHOD_POST_BODY = 2;
    public final static int REQUEST_METHOD_PUT = 3;
    /** https://www.github.com/crabfang/RxCache?key1=val1&key2=val2  */
    /**
     * Http请求的head参数<br>
     * 比如 cookies
     **/
    public Map<String, String> head;
    /**
     * 请求地址的get参数
     * 比如 {"key1":"val1", "key2":"val2"} MapFormat
     **/
    public Map<String, String> query;
    /**  请求地址的post参数 */
    public Map<String, String> body;
    /**  请求地址的put参数 */
    public String putBody = "";
    /**
     * 请求地址的host地址<br>
     * 比如 https://www.github.com
     **/
    public String host = "";
    /**
     * 请求地址的详细地址
     * 比如 crabfang/RxCache
     **/
    public String path = "";
    /**  请求方式，默认为Get */
    public int requestMethod = REQUEST_METHOD_GET;

    private Uri getUri() {
        String pathNew = path;
        if(pathNew != null && pathNew.startsWith(File.separator)) {
            pathNew = pathNew.substring(1);
        }
        String hostNew = host;
        if(hostNew != null && hostNew.endsWith(File.separator)) {
            hostNew = hostNew.substring(0, hostNew.length() - 1);
        }
        String url = hostNew;
        if(pathNew != null && !pathNew.isEmpty()) {
            url += File.separator + pathNew;
        }
        return Uri.parse(url);
    }

    public String getHost() {
        return getUri().getScheme() + "://" + getUri().getAuthority();
    }

    public String getPath() {
        String url = getUri().toString();
        String host = getHost();
        if(url == null || host == null) {
            return "";
        }

        int index = url.indexOf(host);
        if(index < 0) {
            index = 0;
        }
        index += host.length() + 1;
        String path = url.substring(index);
        if(path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }
}
