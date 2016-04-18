package com.cabe.lib.cache;

import com.cabe.lib.cache.interactor.UseCase;
import com.cabe.lib.cache.interactor.ViewPresenter;
import com.google.gson.reflect.TypeToken;

import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

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
    private CompositeSubscription cs = new CompositeSubscription();

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

    public void execute(final ViewPresenter<T> presenter) {
        CacheSource source = cacheMethod == CacheMethod.HTTP ? CacheSource.HTTP : CacheSource.DISK;
        cs.add(super.execute(getSubscriber(source, presenter)));
    }

    protected void executeHttp(final ViewPresenter<T> presenter) {
        UseCase<T> useCase = new UseCase<T>(getTypeToken()){
            @Override
            public Observable<T> buildUseCaseObservable() {
                return buildHttpObservable();
            }
        };
        cs.add(useCase.execute(getSubscriber(CacheSource.HTTP, presenter)));
    }

    @Override
    public void unsubscribe() {
        cs.unsubscribe();
    }
}
