package com.cabe.lib.cache.http.transformer;

import rx.Observable;
import rx.functions.Func1;

/**
 * Http RxJava Response数据转换类
 * Created by cabe on 16/4/12.
 */
public abstract class HttpStringTransformer<T> implements Observable.Transformer<String, T> {
    @Override
    public Observable<T> call(rx.Observable<String> stringObservable) {
        return stringObservable.map(new Func1<String, T>() {
            @Override
            public T call(String s) {
                return buildData(s);
            }
        });
    }

    public abstract T buildData(String responseStr);
}
