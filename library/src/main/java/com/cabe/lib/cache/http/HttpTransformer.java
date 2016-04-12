package com.cabe.lib.cache.http;

import com.cabe.lib.cache.interactor.ThreadTransformer;

import java.util.Observable;

/**
 * Http RxJava 线程以及数据转换类
 * Created by cabe on 16/4/12.
 */
public abstract class HttpTransformer<T> {
    public ThreadTransformer<String> getThreadTransformer() {
        return new ThreadTransformer<>();
    }
    public abstract rx.Observable.Transformer<String, T> getResponseTransformer();
}
