package com.cabe.lib.cache.interactor;

import com.cabe.lib.cache.CacheSource;
import com.cabe.lib.cache.exception.ExceptionCode;
import com.cabe.lib.cache.exception.RxException;

import retrofit.RetrofitError;

/**
 *
 * Created by cabe on 16/4/12.
 */
public class DefaultSubscriber<T> extends rx.Subscriber<T> {
    public final static int REPOSITORY_DISK = 0;
    public final static int REPOSITORY_HTTP = 1;

    private CacheSource repository;
    private ViewPresenter<T> presenter;
    public DefaultSubscriber(CacheSource repository, ViewPresenter<T> presenter) {
        this.repository = repository;
        this.presenter = presenter;
    }
    public DefaultSubscriber() {
    }
    @Override public void onCompleted() {
        if(presenter != null) {
            presenter.complete(repository);
        }
    }

    @Override public void onError(Throwable e) {
        int code = ExceptionCode.RX_EXCEPTION_DEFAULT;
        String info;
        if(e instanceof RxException) {
            RxException rxException = (RxException) e;
            code = rxException.code;
            info = rxException.info;
            if(rxException.e != null) {
                rxException.e.printStackTrace();
            }
        } else if(e instanceof RetrofitError) {
            RetrofitError error = (RetrofitError) e;
            code = error.getResponse().getStatus();
            info = error.getResponse().getReason();
            Throwable detail = error.getCause();
            if(detail != null) {
                detail.printStackTrace();
            }
        } else {
            e.printStackTrace();
            info = new ExceptionCode().getInfo(code);
        }
        if(presenter != null) {
            presenter.error(repository, code, info);
        }
    }

    @Override public void onNext(T t) {
        if(presenter != null) {
            presenter.load(repository, t);
        }
    }
}