package com.cabe.lib.cache.http.repository;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * web cookies hendler
 * Created by cabe on 16/5/16.
 */
public class WebCookiesHandler extends CookieHandler {
    private Map<String, List<String>> mapCookies = new HashMap<>();
    @Override
    public Map<String, List<String>> get(URI uri, Map<String, List<String>> requestHeaders) throws IOException {
        Map<String, List<String>> newHeaders = new HashMap<>();
        for(String key : requestHeaders.keySet()) {
            newHeaders.put(key, requestHeaders.get(key));
        }
        List<String> cookies = mapCookies.get(uri.getHost());
        if(cookies != null) {//set web cookies
            newHeaders.put("cookie", cookies);
        }
        return newHeaders;
    }
    @Override
    public void put(URI uri, Map<String, List<String>> responseHeaders) throws IOException {
        for(String key : responseHeaders.keySet()) {
            if(key.equals("Set-Cookie")) {//get web cookies
                mapCookies.put(uri.getHost(), responseHeaders.get(key));
            }
        }
    }
}
