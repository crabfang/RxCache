package com.cabe.lib.cache.impl;

import com.cabe.lib.cache.AbstractCacheUseCase;
import com.cabe.lib.cache.CacheMethod;
import com.cabe.lib.cache.CacheSource;
import com.cabe.lib.cache.DiskCacheRepository;
import com.cabe.lib.cache.disk.DiskCacheManager;
import com.cabe.lib.cache.exception.DiskExceptionCode;
import com.cabe.lib.cache.exception.RxException;
import com.cabe.lib.cache.http.HttpStringCacheManager;
import com.cabe.lib.cache.http.RequestParams;
import com.cabe.lib.cache.interactor.DefaultSubscriber;
import com.cabe.lib.cache.interactor.HttpCacheRepository;
import com.cabe.lib.cache.interactor.ViewPresenter;
import com.google.gson.reflect.TypeToken;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;

/**
 * 缓存获取用例实现<br>
 * 主要通过<br>
 * {@link com.jakewharton.disklrucache.DiskLruCache} 进行本地缓存<br>
 * {@link retrofit.RestAdapter} 进行网络缓存<br>
 * 也可通过<br>
 * {@link DoubleCacheUseCase#setDiskManager(DiskCacheRepository)}<br>
 * 以及<br>
 * {@link DoubleCacheUseCase#setHttpManager(HttpCacheRepository)}<br>
 * 两个方法来切换实现方式<br>
 * Created by cabe on 16/4/13.
 */
public class DoubleCacheUseCase<T> extends AbstractCacheUseCase<T> {
    private DiskCacheRepository diskManager;
    private HttpCacheRepository<String, T> httpManager;
    private RequestParams params = null;

    private boolean flagSaveData;

    /**
     * 使用observable.flatMap(Fun1)来做数据转换<br>
     * 而不是用observable.compose(Transformer)的原因是<br>
     * Observable.Transformer().call(Observable tObservable)<br>
     * 参数tObservable有可能被忽略从而导致之前的操作流(tObservable)不被执行
     *
     */
    private Func1<T, Observable<T>> httpTransformer;
    private Func1<T, Observable<T>> diskTransformer;

    public DoubleCacheUseCase(TypeToken<T> typeT, RequestParams params, CacheMethod cacheMethod) {
        super(typeT, cacheMethod);
        //DiskCache在主线程调用
        if(cacheMethod != CacheMethod.HTTP) {
            super.setExecutor(null);
            super.setPostThread(null);
        }

        setSaveData(true);
        setRequestParams(params);
        setDiskTransformer(null);

        String diskCachePath = DiskCacheManager.DISK_CACHE_PATH;
        if(diskCachePath != null && !diskCachePath.equals("")) {
            DiskCacheManager diskManager = null;
            try {
                diskManager = new DiskCacheManager(diskCachePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            setDiskManager(diskManager);
        }
        HttpStringCacheManager<T> httpManager = new HttpStringCacheManager<>(getTypeToken());
        setHttpManager(httpManager);
    }

    public DoubleCacheUseCase(TypeToken<T> typeT, RequestParams requestParams) {
        this(typeT, requestParams, CacheMethod.BOTH);
    }

    public void setDiskManager(DiskCacheRepository diskManager) {
        this.diskManager = diskManager;
    }

    public void setHttpManager(HttpCacheRepository<String, T> httpManager) {
        this.httpManager = httpManager;
    }

    public void setDiskTransformer(Func1<T, Observable<T>> transformer) {
        this.diskTransformer = transformer;
    }

    public void setHttpTransformer(Func1<T, Observable<T>> transformer) {
        this.httpTransformer = transformer;
    }

    public void setRequestParams(RequestParams params) {
        this.params = params;
    }

    public void setSaveData(boolean flagSaveData) {
        this.flagSaveData = flagSaveData;
    }

    public DiskCacheRepository getDiskRepository() {
        return diskManager;
    }

    public HttpCacheRepository<String, T> getHttpRepository() {
        return httpManager;
    }

    public boolean saveCacheDisk(T data) {
        boolean saveResult = false;
        if(diskManager != null) {
            saveResult = diskManager.put(getTypeToken(), data);
        }
        return saveResult;
    }

    @Override
    public Observable<T> buildDiskObservable() {
        Observable<T> observable = Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                if(diskManager == null) {
                    throw RxException.build(DiskExceptionCode.DISK_EXCEPTION_PATH, null);
                }

                T data = diskManager.get(getTypeToken());
                subscriber.onNext(data);
                subscriber.onCompleted();
            }
        });
        if(diskTransformer != null) {
            observable = observable.flatMap(diskTransformer);
        }
        return observable;
    }

    @Override
    public Observable<T> buildHttpObservable() {
        Observable<T> observable = httpManager.getHttpObservable(params);
        if(httpTransformer != null) {
            //这里使用flatMap而不是compose的原因是Observable.Transformer可能导致操作流被打断
            observable = observable.flatMap(httpTransformer);
        }
        return observable;
    }

    @Override
    protected Subscriber<T> getSubscriber(CacheSource from, final ViewPresenter<T> presenter) {
        return new DefaultSubscriber<>(from, new ViewPresenter<T>() {
            @Override
            public void error(CacheSource from, int code, String info) {
                if(presenter != null) {
                    presenter.error(from, code, info);
                }
            }
            @Override
            public void load(CacheSource from, T data) {
                if(flagSaveData && getCacheMethod() == CacheMethod.BOTH) {
                    if(from == CacheSource.HTTP) {
                        saveCacheDisk(data);
                    }
                }
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
                    Subscription subscription = getSubscription();
                    if(!subscription.isUnsubscribed()) {
                        executeHttp(presenter);
                    }
                }
            }
        });
    }
}