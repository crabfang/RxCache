package com.cabe.lib.cache;

import com.cabe.lib.cache.interactor.UseCase;
import com.cabe.lib.cache.interactor.ViewPresenter;
import com.google.gson.reflect.TypeToken;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * 缓存用例抽象类<br>
 * 进行本地以及远程二级缓存获取<br>
 * 需实现<br>
 * {@link AbstractCacheUseCase#buildDiskObservable()} <br>
 * {@link AbstractCacheUseCase#buildHttpObservable} <br>
 * 两个方法<br>
 * Created by cabe on 16/4/14.
 */
public abstract class AbstractCacheUseCase<T> extends UseCase<T> {
    private CacheMethod cacheMethod = CacheMethod.BOTH;

    public AbstractCacheUseCase(TypeToken<T> typeT, CacheMethod cacheMethod) {
        super(typeT);
        this.cacheMethod = cacheMethod;
    }

    public CacheMethod getCacheMethod() {
        return cacheMethod;
    }

    @Override
    public Observable<T> buildUseCaseObservable() {
        return getCacheMethod() != CacheMethod.HTTP ? buildDiskObservable() : buildHttpObservable();
    }

    public abstract Observable<T> buildDiskObservable();

    public abstract Observable<T> buildHttpObservable();

    protected abstract Subscriber<T> getSubscriber(CacheSource from, ViewPresenter<T> presenter);

    public Subscription execute(final ViewPresenter<T> presenter) {
        CacheSource source = cacheMethod == CacheMethod.HTTP ? CacheSource.HTTP : CacheSource.DISK;
        Subscription sc = super.execute(getSubscriber(source, presenter));
        super.setSubscription(sc);
        return sc;
    }

    protected Subscription executeHttp(final ViewPresenter<T> presenter) {
        UseCase<T> useCase = new UseCase<T>(getTypeToken()){
            @Override
            public Observable<T> buildUseCaseObservable() {
                return buildHttpObservable();
            }
        };
        Subscription sc = useCase.execute(getSubscriber(CacheSource.HTTP, presenter));
        super.setSubscription(sc);
        return sc;
    }
}
