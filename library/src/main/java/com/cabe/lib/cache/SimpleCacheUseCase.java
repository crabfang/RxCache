package com.cabe.lib.cache;

import com.cabe.lib.cache.disk.DiskCacheManager;
import com.cabe.lib.cache.exception.DiskExceptionCode;
import com.cabe.lib.cache.exception.RxException;
import com.cabe.lib.cache.http.HttpCacheManager;
import com.cabe.lib.cache.http.RequestParams;
import com.cabe.lib.cache.interactor.DefaultSubscriber;
import com.cabe.lib.cache.interactor.HttpCacheRepository;
import com.cabe.lib.cache.interactor.ViewPresenter;
import com.google.gson.reflect.TypeToken;

import rx.Observable;
import rx.Subscriber;

/**
 * 缓存获取用例实现<br>
 * 主要通过<br>
 * {@link com.jakewharton.disklrucache.DiskLruCache} 进行本地缓存<br>
 * {@link retrofit.RestAdapter} 进行网络缓存<br>
 * 也可通过<br>
 * {@link SimpleCacheUseCase#setDiskManager(DiskCacheRepository)}<br>
 * 以及<br>
 * {@link SimpleCacheUseCase#setHttpManager(HttpCacheRepository)}<br>
 * 两个方法来切换实现方式<br>
 * Created by cabe on 16/4/13.
 */
public class SimpleCacheUseCase<T> extends AbstractCacheUseCase<T> {
    private DiskCacheRepository diskManager;
    private HttpCacheRepository<T> httpManager;
    private RequestParams params = null;

    public SimpleCacheUseCase(TypeToken<T> typeT, RequestParams params) {
        super(typeT);
        //DiskCache在主线程调用
        super.setExecutor(null);
        super.setPostThread(null);

        this.params = params;

        String diskCachePath = DiskCacheManager.DISK_CACHE_PATH;
        if(diskCachePath != null && !diskCachePath.equals("")) {
            diskManager = new DiskCacheManager(diskCachePath);
        }
        httpManager = new HttpCacheManager<>(getTypeToken());
    }

    public void setDiskManager(DiskCacheRepository diskManager) {
        this.diskManager = diskManager;
    }

    public void setHttpManager(HttpCacheRepository<T> httpManager) {
        this.httpManager = httpManager;
    }

    @Override
    public Observable<T> buildDiskObservable() {
        if(diskManager == null) {
            throw RxException.build(DiskExceptionCode.DISK_EXCEPTION_PATH, null);
        }
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                T data = diskManager.get(getTypeToken());
                subscriber.onNext(data);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<T> buildHttpObservable() {
        return httpManager.getHttpObservable(params);
    }

    @Override
    protected Subscriber<T> getSubscriber(CacheSource from, final ViewPresenter<T> presenter) {
        return new DefaultSubscriber<>(CacheSource.HTTP, new ViewPresenter<T>() {
            @Override
            public void error(CacheSource from, int code, String info) {
                if(presenter != null) {
                    presenter.error(from, code, info);
                }
            }
            @Override
            public void load(CacheSource from, T data) {
                if(diskManager != null && from == CacheSource.HTTP) {
                    diskManager.put(getTypeToken(), data);
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
                if(!isDiskOnly()) {
                    executeHttp(presenter);
                }
            }
        });
    }
}