package com.cabe.lib.cache.http;

import com.cabe.lib.cache.http.repository.InputStreamHttpFactory;
import com.cabe.lib.cache.interactor.HttpCacheRepository;

import java.io.InputStream;

import retrofit.converter.Converter;
import rx.Observable;
import rx.functions.Func1;

/**
 * 网络流管理
 * Created by cabe on 16/5/13.
 */
public class HttpBytesCacheManager implements HttpCacheRepository<InputStream, byte[]> {
    private Converter converter = null;
    private Observable.Transformer<InputStream, byte[]> transformer = null;
    public HttpBytesCacheManager() {
        setHttpConverter(StreamConverterFactory.create());
        setResponseTransformer(new Observable.Transformer<InputStream, byte[]>() {
            @Override
            public Observable<byte[]> call(Observable<InputStream> inputStreamObservable) {
                return inputStreamObservable.map(new Func1<InputStream, byte[]>() {
                    @Override
                    public byte[] call(InputStream inputStream) {
                        return StreamConverterFactory.inputStream2Bytes(inputStream);
                    }
                });
            }
        });
    }

    @Override
    public Observable<byte[]> getHttpObservable(RequestParams params) {
        return new InputStreamHttpFactory().createRequest(params, converter).compose(transformer);
    }

    @Override
    public void setHttpConverter(Converter converter) {
        this.converter = converter;
    }

    @Override
    public void setResponseTransformer(Observable.Transformer<InputStream, byte[]> transformer) {
        this.transformer = transformer;
    }
}
