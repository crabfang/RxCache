package com.cabe.lib.cache.http;

import java.util.Map;

/**
 * Http Request Params Bean
 * Created by cabe on 16/4/12.
 */
public class RequestParams {
    public final static int REQUEST_METHOD_GET = 0;
    public final static int REQUEST_METHOD_POST = 1;
    public final static int REQUEST_METHOD_PUT = 2;
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
}
