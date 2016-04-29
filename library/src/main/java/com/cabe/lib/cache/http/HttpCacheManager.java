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
    private HttpTransformer<T> transformer = new HttpTransformer<T>() {
        @Override
        public T buildData(String responseStr) {
            if(typeToken == null) {
                throw RxException.build(ExceptionCode.RX_EXCEPTION_TYPE_UNKNOWN, null);
            }
            return new Gson().fromJson(responseStr, typeToken.getType());
        }
    };
    public HttpCacheManager(TypeToken<T> typeToken) {
        this.typeToken = typeToken;
    }
    @Override
    public Observable<T> getHttpObservable(RequestParams params) {
        return StringHttpFactory.createRequest(params).compose(transformer);
    }

    @Override
    public void setResponseTransformer(HttpTransformer<T> transformer) {
        this.transformer = transformer;
    }
}
