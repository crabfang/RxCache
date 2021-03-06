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

    private CacheSource from;
    private ViewPresenter<T> presenter;
    public DefaultSubscriber(CacheSource from, ViewPresenter<T> presenter) {
        this.from = from;
        this.presenter = presenter;
    }
    @Override public void onCompleted() {
        if(presenter != null) {
            presenter.complete(from);
        }
    }

    @Override public void onError(Throwable e) {
        int code = ExceptionCode.RX_EXCEPTION_DEFAULT;
        String info = "";
        if(e instanceof RxException) {
            RxException rxException = (RxException) e;
            code = rxException.code;
            info = rxException.info;
            if(rxException.e != null) {
                rxException.e.printStackTrace();
            }
        } else if(e instanceof RetrofitError) {
            RetrofitError error = (RetrofitError) e;
            if(error.getResponse() != null) {
                code = error.getResponse().getStatus();
                info = error.getResponse().getReason();
            }
            Throwable detail = error.getCause();
            if(detail != null) {
                detail.printStackTrace();
            }
        } else {
            e.printStackTrace();
            info = new ExceptionCode().getInfo(code);
        }
        if(presenter != null) {
            presenter.error(from, code, info);
            presenter.complete(from);
        }
    }

    @Override public void onNext(T t) {
        if(presenter != null) {
            presenter.load(from, t);
        }
    }
}