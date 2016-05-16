package com.cabe.lib.cache.impl;

import com.cabe.lib.cache.AbstractCacheUseCase;
import com.cabe.lib.cache.CacheMethod;
import com.cabe.lib.cache.CacheSource;
import com.cabe.lib.cache.http.HttpBytesCacheManager;
import com.cabe.lib.cache.http.RequestParams;
import com.cabe.lib.cache.interactor.DefaultSubscriber;
import com.cabe.lib.cache.interactor.HttpCacheRepository;
import com.cabe.lib.cache.interactor.ViewPresenter;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;

import rx.Observable;
import rx.Subscriber;

/**
 * Bytes UseCase
 * Created by cabe on 16/5/13.
 */
public class BytesUseCase extends AbstractCacheUseCase<byte[]> {
    private RequestParams params = null;
    private HttpCacheRepository<InputStream, byte[]> httpManager = new HttpBytesCacheManager();
    public BytesUseCase(RequestParams params) {
        super(new TypeToken<byte[]>(){}, CacheMethod.HTTP);
        this.params = params;
    }

    @Override
    public Observable<byte[]> buildDiskObservable() {
        return null;
    }

    @Override
    public Observable<byte[]> buildHttpObservable() {
        return httpManager.getHttpObservable(params);
    }

    @Override
    protected Subscriber<byte[]> getSubscriber(CacheSource from, final ViewPresenter<byte[]> presenter) {
        return new DefaultSubscriber<>(from, new ViewPresenter<byte[]>() {
            @Override
            public void error(CacheSource from, int code, String info) {
                if(presenter != null) {
                    presenter.error(from, code, info);
                }
            }
            @Override
            public void load(CacheSource from, byte[] data) {
                if(presenter != null) {
                    presenter.load(from, data);
                }
            }
            @Override
            public void complete(CacheSource from) {
                if(presenter != null) {
                    presenter.complete(from);
                }
                if(from == CacheSource.DISK && getCacheMethod() == CacheMethod.BOTH) {
                    executeHttp(presenter);
                }
            }
        });
    }
}
