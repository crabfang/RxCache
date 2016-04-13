package com.cabe.lib.cache.http;

import rx.Observable;

/**
 * Http RxJava Response数据转换类
 * Created by cabe on 16/4/12.
 */
public abstract class HttpTransformer<T> implements Observable.Transformer<String, T> {
    @Override
    public abstract Observable<T> call(rx.Observable<String> stringObservable);
}
