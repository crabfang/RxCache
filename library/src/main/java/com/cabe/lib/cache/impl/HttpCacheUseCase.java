package com.cabe.lib.cache.impl;

import com.cabe.lib.cache.CacheMethod;
import com.cabe.lib.cache.http.RequestParams;
import com.google.gson.reflect.TypeToken;

/**
 * 网络缓存用例实现<br>
 * Created by cabe on 16/4/26.
 */
public class HttpCacheUseCase<T> extends DoubleCacheUseCase<T> {
    public HttpCacheUseCase(TypeToken<T> typeT, RequestParams requestParams) {
        super(typeT, requestParams, CacheMethod.HTTP);
    }
}
