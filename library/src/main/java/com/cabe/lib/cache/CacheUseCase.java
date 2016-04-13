package com.cabe.lib.cache;

import com.cabe.lib.cache.disk.DiskCacheManager;
import com.cabe.lib.cache.exception.DiskExceptionCode;
import com.cabe.lib.cache.exception.RxException;
import com.cabe.lib.cache.http.RequestParams;
import com.cabe.lib.cache.http.StringHttpFactory;
import com.cabe.lib.cache.interactor.DefaultSubscriber;
import com.cabe.lib.cache.interactor.UseCase;
import com.cabe.lib.cache.interactor.ViewPresenter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Cache Domain
 * Created by cabe on 16/4/13.
 */
public class CacheUseCase<T> extends UseCase<T> {
    public static String DISK_CACHE_PATH = "";
    private DiskCacheManager manager;
    private RequestParams params = null;
    private String cachePath = DISK_CACHE_PATH;
    public CacheUseCase(TypeToken<T> typeT, RequestParams params) {
        super(typeT);
        super.setExecutor(null);
        super.setPostThread(null);
        this.params = params;
    }

    public void setCachePath(String cachePath) {
        this.cachePath = cachePath;
    }

    @Override
    public Observable<T> buildUseCaseObservable() {
        return buildDiskObservable();
    }
    public Observable<T> buildDiskObservable() {
        if(cachePath == null || cachePath.equals("")) {
            throw RxException.build(DiskExceptionCode.DISK_EXCEPTION_PATH, null);
        }
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                manager = new DiskCacheManager(cachePath);
                T data = manager.get(getTypeToken());
                subscriber.onNext(data);
                subscriber.onCompleted();
            }
        });
    }

    public Observable<T> buildHttpObservable() {
        return StringHttpFactory.createRequest(params).map(new Func1<String, T>() {
            @Override
            public T call(String s) {
                return new Gson().fromJson(s, getTypeToken().getType());
            }
        });
    }

    public void execute(final ViewPresenter<T> presenter) {
        if(presenter == null) {
            super.execute(new DefaultSubscriber<T>(){
                @Override
                public void onCompleted() {
                    executeHttp(null);
                }
            });
        } else {
            super.execute(new DefaultSubscriber<>(CacheSource.DISK, new ViewPresenter<T>(){
                @Override
                public void error(CacheSource source, int code, String info) {
                    presenter.error(source, code, info);
                }
                @Override
                public void load(CacheSource source, T data) {
                    presenter.load(source, data);
                }
                @Override
                public void complete(CacheSource source) {
                    presenter.complete(source);
                    executeHttp(presenter);
                }
            }));
        }
    }

    public void executeHttp(final ViewPresenter<T> presenter) {
        new UseCase<T>(getTypeToken()){
            @Override
            public Observable<T> buildUseCaseObservable() {
                return buildHttpObservable();
            }
        }.execute(new DefaultSubscriber<>(CacheSource.HTTP, new ViewPresenter<T>() {
            @Override
            public void error(CacheSource source, int code, String info) {
                if(presenter != null) {
                    presenter.error(source, code, info);
                }
            }
            @Override
            public void load(CacheSource source, T data) {
                manager.put(getTypeToken(), data);
                if(presenter != null) {
                    presenter.load(source, data);
                }
            }
            @Override
            public void complete(CacheSource source) {
                if(presenter != null) {
                    presenter.complete(source);
                }
            }
        }));
    }
}