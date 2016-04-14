package com.cabe.lib.cache.http;

import com.cabe.lib.cache.exception.ExceptionCode;
import com.cabe.lib.cache.exception.RxException;
import com.cabe.lib.cache.interactor.HttpCacheRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import rx.Observable;
import rx.functions.Func1;

/**
 * 网络请求实现
 * Created by cabe on 16/4/14.
 */
public class HttpCacheManager<T> implements HttpCacheRepository<T> {
    private TypeToken<T> typeToken;
    public HttpCacheManager(TypeToken<T> typeToken) {
        this.typeToken = typeToken;
    }
    @Override
    public Observable<T> getHttpObservable(RequestParams params) {
        return StringHttpFactory.createRequest(params).compose(getResponseTransformer());
    }

    @Override
    public HttpTransformer<T> getResponseTransformer() {
        return new HttpTransformer<T>() {
            @Override
            public Observable<T> call(Observable<String> stringObservable) {
                return stringObservable.map(new Func1<String, T>() {
                    @Override
                    public T call(String resp) {
                        if(typeToken == null) {
                            throw RxException.build(ExceptionCode.RX_EXCEPTION_TYPE_UNKNOWN, null);
                        }
                        return new Gson().fromJson(resp, typeToken.getType());
                    }
                });
            }
        };
    }
}
