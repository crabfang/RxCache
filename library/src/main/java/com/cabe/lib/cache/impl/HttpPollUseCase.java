package com.cabe.lib.cache.impl;

import com.cabe.lib.cache.CacheMethod;
import com.cabe.lib.cache.http.RequestParams;
import com.google.gson.reflect.TypeToken;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;

/**
 * 网络缓存用例实现<br>
 * Created by cabe on 16/4/26.
 */
public abstract class HttpPollUseCase<T> extends DoubleCacheUseCase<T> {
    public HttpPollUseCase(TypeToken<T> typeT, RequestParams requestParams) {
        super(typeT, requestParams, CacheMethod.HTTP);
    }

    /** 单次轮询的最大次数，大于这个次数会被强制停止轮询  */
    protected final int FORCE_MAX_REPEAT_TIME = 10;
    protected boolean flagStop = true;

    private int forceMaxTime = FORCE_MAX_REPEAT_TIME;
    private int repeatTime = 0;
    private T data;

    public void setForceMaxTime(int maxTime) {
        forceMaxTime = maxTime;
    }

    protected abstract boolean pollFinish(T data);
    protected abstract long getPollIntervalMills(T data);

    public void stopPoll() {
        flagStop = true;
    }

    private boolean isRealFinish(T data) {
        boolean validInterval = data != null &&getPollIntervalMills(data) > 0;
        return !validInterval || repeatTime++ >= forceMaxTime || pollFinish(data);
    }

    private Func1<Observable<? extends Throwable>, Observable<?>> getRetryFun() {
        return new Func1<Observable<? extends Throwable>, Observable<?>>() {
            @Override
            public Observable<?> call(Observable<? extends Throwable> errors) {
                return errors.filter(new Func1<Throwable, Boolean>() {
                    @Override
                    public Boolean call(Throwable aVoid) {
                        return !isRealFinish(data);
                    }
                }).flatMap(new Func1<Throwable, Observable<?>>() {
                    @Override
                    public Observable<?> call(Throwable throwable) {
                        long pollInterval = getPollIntervalMills(data);
                        return Observable.timer(pollInterval, TimeUnit.MILLISECONDS).flatMap(new Func1<Long, Observable<T>>() {
                            @Override
                            public Observable<T> call(Long aLong) {
                                return Observable.just(data);
                            }
                        });
                    }
                });
            }
        };
    }

    private Func1<Observable<? extends Void>, Observable<T>> getRepeatFun() {
        return new Func1<Observable<? extends Void>, Observable<T>>() {
            @Override
            public Observable<T> call(final Observable<? extends Void> completed) {
                return completed.filter(new Func1<Void, Boolean>() {
                    @Override
                    public Boolean call(Void aVoid) {
                        return !isRealFinish(data);
                    }
                }).flatMap(new Func1<Void, Observable<T>>() {
                    @Override
                    public Observable<T> call(Void aVoid) {
                        long pollInterval = getPollIntervalMills(data);
                        return Observable.timer(pollInterval, TimeUnit.MILLISECONDS).flatMap(new Func1<Long, Observable<T>>() {
                            @Override
                            public Observable<T> call(Long aLong) {
                                return Observable.just(data);
                            }
                        });
                    }
                });
            }
        };
    }

    @Override
    public final Observable<T> buildHttpObservable() {
        return super.buildHttpObservable().map(new Func1<T, T>() {
            @Override
            public T call(T t) {
                data = t;
                return t;
            }
        }).retryWhen(getRetryFun()).repeatWhen(getRepeatFun());
    }

    @Override
    public final Subscription execute(Subscriber<T> subscriber) {
        repeatTime = 0;
        flagStop = false;
        return super.execute(subscriber);
    }
}