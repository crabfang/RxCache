package com.cabe.lib.cache.http;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 *
 * Created by cabe on 16/4/12.
 */

public class OkHttpClientFactory {
    private static OkHttpClient okHttpClient;

    public OkHttpClientFactory() {
    }

    public static OkHttpClient create() {
        if(null == okHttpClient) {
            Class var0 = OkHttpClientFactory.class;
            synchronized(OkHttpClientFactory.class) {
                if(null == okHttpClient) {
                    OkHttpClient client = new OkHttpClient();
                    okHttpClient = client;
                    client.setConnectTimeout(30L, TimeUnit.SECONDS);
                }
            }
        }

        return okHttpClient;
    }
}