package com.cabe.lib.cache.interactor;

import com.cabe.lib.cache.http.HttpTransformer;
import com.cabe.lib.cache.http.RequestParams;

import rx.Observable;

/**
 * 网络缓存相关接口
 * Created by cabe on 16/4/14.
 */
public interface HttpCacheRepository<T> {
    Observable<T> getHttpObservable(RequestParams params);
    HttpTransformer<T> getResponseTransformer();
}
