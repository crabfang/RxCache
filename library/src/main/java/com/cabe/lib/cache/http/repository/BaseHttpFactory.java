package com.cabe.lib.cache.http.repository;

import android.text.TextUtils;
import android.util.Log;

import com.cabe.lib.cache.http.RequestParams;
import com.squareup.okhttp.OkHttpClient;

import java.util.Iterator;
import java.util.Map;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.Converter;

/**
 * base http
 * Created by cabe on 16/5/16.
 */
public class BaseHttpFactory {
    public static RestAdapter.LogLevel logLevel = RestAdapter.LogLevel.FULL;
    private static OkHttpClient httpClient = OkHttpClientFactory.create();
    public static OkHttpClient getHttpClient() {
        return httpClient;
    }

    protected static <T> T buildApiService(final RequestParams params, Converter converter, Class<T> clazz) {
        String host = params.getHost();
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                Map<String, String> headMap = params.head;
                if(headMap != null) {
                    Iterator<Map.Entry<String, String>> it = headMap.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<String, String> pair = it.next();
                        request.addHeader(pair.getKey(), pair.getValue());
                        it.remove();
                    }
                }
            }
        };
        return getRetrofit(converter, host, requestInterceptor).create(clazz);
    }

    protected static RestAdapter getRetrofit(Converter converter, String baseUrl, RequestInterceptor dataInterceptor) {
        if(TextUtils.isEmpty(baseUrl)) {
            throw new RuntimeException("baseUrl is null");
        } else {
            RestAdapter.Builder retrofit = new RestAdapter.Builder();
            retrofit.setLog(new HttpLog());
            retrofit.setLogLevel(logLevel);
            retrofit.setEndpoint(baseUrl);
            if(converter != null) {
                retrofit.setConverter(converter);
            }
            retrofit.setClient(new OkClient(getHttpClient()));
            if(dataInterceptor != null) {
                retrofit.setRequestInterceptor(dataInterceptor);
            }

            return retrofit.build();
        }
    }

    protected static class HttpLog implements RestAdapter.Log {
        @Override
        public void log(String message) {
            Log.i("RxCache.Http", message);
        }
    }
}