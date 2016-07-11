package com.cabe.lib.cache.http;

import com.cabe.lib.cache.exception.ExceptionCode;
import com.cabe.lib.cache.exception.HttpExceptionCode;
import com.cabe.lib.cache.exception.RxException;
import com.cabe.lib.cache.http.repository.InputStreamHttpFactory;
import com.cabe.lib.cache.http.transformer.HttpStringTransformer;
import com.cabe.lib.cache.http.transformer.Stream2StringTransformer;
import com.cabe.lib.cache.interactor.HttpCacheRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import retrofit.converter.Converter;
import rx.Observable;

/**
 * 网络请求实现
 * Created by cabe on 16/4/14.
 */
public class HttpStringCacheManager<T> implements HttpCacheRepository<String, T> {
    private String encode;
    private TypeToken<T> typeToken;
    private Converter converter = null;
    private Observable.Transformer<String, T> transformer = null;

    public HttpStringCacheManager(TypeToken<T> token) {
        this.typeToken = token;
        setStringEncode(StreamConverterFactory.ENCODE);
        setHttpConverter(StreamConverterFactory.create());
        setResponseTransformer(new HttpStringTransformer<T>() {
            @Override
            public T buildData(String responseStr) {
                if(typeToken == null) {
                    throw RxException.build(ExceptionCode.RX_EXCEPTION_TYPE_UNKNOWN, null);
                }
                if(typeToken.getType() == String.class) {
                    return (T) responseStr;
                } else {
                    try {
                        return new Gson().fromJson(responseStr, typeToken.getType());
                    } catch (Exception e) {
                        throw RxException.build(HttpExceptionCode.RX_EXCEPTION_TYPE_UNKNOWN, e);
                    }
                }
            }
        });
    }
    @Override
    public Observable<T> getHttpObservable(RequestParams params) {
        return new InputStreamHttpFactory().createRequest(params, converter).compose(new Stream2StringTransformer(encode)).compose(transformer);
    }

    @Override
    public void setHttpConverter(Converter converter) {
        this.converter = converter;
    }

    @Override
    public void setResponseTransformer(Observable.Transformer<String, T> transformer) {
        this.transformer = transformer;
    }

    public void setStringEncode(String encode) {
        this.encode = encode;
    }
}
