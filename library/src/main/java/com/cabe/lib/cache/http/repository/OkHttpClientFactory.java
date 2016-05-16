package com.cabe.lib.cache.http.repository;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 *
 * Created by cabe on 16/4/12.
 */

public class OkHttpClientFactory {
    private static OkHttpClient okHttpClient;

    private OkHttpClientFactory() {
    }

    public static OkHttpClient create() {
        if(null == okHttpClient) {
            synchronized(OkHttpClientFactory.class) {
                if(null == okHttpClient) {
                    okHttpClient = new OkHttpClient();
                    okHttpClient.setConnectTimeout(10L, TimeUnit.SECONDS);
                }
            }
        }

        return okHttpClient;
    }
}