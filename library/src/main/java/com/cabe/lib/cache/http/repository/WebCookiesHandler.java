package com.cabe.lib.cache.http.repository;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * web cookies hendler
 * Created by cabe on 16/5/16.
 */
public class WebCookiesHandler extends CookieHandler {
    private Map<String, Map<String, String>> mapCookies = new HashMap<>();
    @Override
    public Map<String, List<String>> get(URI uri, Map<String, List<String>> requestHeaders) throws IOException {
        Map<String, List<String>> newHeaders = new HashMap<>();
        for(String key : requestHeaders.keySet()) {
            newHeaders.put(key, requestHeaders.get(key));
        }
        Map<String, String> cookieMap = mapCookies.get(uri.getHost());
        if(cookieMap != null) {//set web cookies
            List<String> cookies = new ArrayList<>();
            for(String key : cookieMap.keySet()) {
                cookies.add(key + "=" + cookieMap.get(key));
            }
            newHeaders.put("cookie", cookies);
        }
        return newHeaders;
    }
    @Override
    public void put(URI uri, Map<String, List<String>> responseHeaders) throws IOException {
        for(String key : responseHeaders.keySet()) {
            if(key.equals("Set-Cookie")) {//get web cookies
                List<String> cookies = responseHeaders.get(key);
                Map<String, String> cookieMap = mapCookies.containsKey(uri.getHost()) ? mapCookies.get(uri.getHost()) : null;
                if(cookieMap == null) {
                    cookieMap = new HashMap<>();
                }
                for(String cookie : cookies) {
                    String[] group = cookie.trim().split(";");
                    for(String str : group) {
                        String[] array = str.split("=");
                        if(array.length == 2) {
                            cookieMap.put(array[0], array[1]);
                        }
                    }
                }
                mapCookies.put(uri.getHost(), cookieMap);
            }
        }
    }
}
