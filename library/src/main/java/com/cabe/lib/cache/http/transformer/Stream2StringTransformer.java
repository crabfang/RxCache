package com.cabe.lib.cache.http.transformer;

import com.cabe.lib.cache.exception.HttpExceptionCode;
import com.cabe.lib.cache.exception.RxException;
import com.cabe.lib.cache.http.StreamConverterFactory;

import java.io.IOException;
import java.io.InputStream;

import rx.Observable;
import rx.functions.Func1;

/**
 * InputStream transformer String
 * Created by cabe on 16/5/16.
 */
public class Stream2StringTransformer implements Observable.Transformer<InputStream, String> {
    private String encode;
    public Stream2StringTransformer(String encode) {
        if(encode == null || encode.isEmpty()) {
            this.encode = StreamConverterFactory.ENCODE;
        } else {
            this.encode = encode;
        }
    }
    public Stream2StringTransformer() {
        this(null);
    }
    @Override
    public Observable<String> call(Observable<InputStream> observable) {
        return observable.map(new Func1<InputStream, String>() {
            @Override
            public String call(InputStream inputStream) {
                try {
                    return StreamConverterFactory.inputStream2String(inputStream, encode);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw RxException.build(HttpExceptionCode.HTTP_STATUS_LOCAL_RESPONSE_MISSING, e);
                }
            }
        });
    }
}
