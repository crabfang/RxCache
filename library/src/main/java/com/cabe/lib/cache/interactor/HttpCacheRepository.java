package com.cabe.lib.cache.interactor;

import com.cabe.lib.cache.http.RequestParams;

import retrofit.converter.Converter;
import rx.Observable;

/**
 * 网络缓存相关接口
 * Created by cabe on 16/4/14.
 */
public interface HttpCacheRepository<D, T> {
    Observable<T> getHttpObservable(RequestParams params);
    void setHttpConverter(Converter converter);
    void setResponseTransformer(Observable.Transformer<D, T> transformer);
}