package com.cabe.lib.cache;

import com.cabe.lib.cache.interactor.UseCase;
import com.cabe.lib.cache.interactor.ViewPresenter;
import com.google.gson.reflect.TypeToken;

import rx.Observable;
import rx.Subscriber;

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
    private boolean diskOnly = false;

    public AbstractCacheUseCase(TypeToken<T> typeT) {
        super(typeT);
    }

    public void setDiskOnly(boolean diskOnly) {
        this.diskOnly = diskOnly;
    }

    public boolean isDiskOnly() {
        return diskOnly;
    }

    @Override
    public Observable<T> buildUseCaseObservable() {
        return buildDiskObservable();
    }

    public abstract Observable<T> buildDiskObservable();

    public abstract Observable<T> buildHttpObservable();

    protected abstract Subscriber<T> getSubscriber(CacheSource from, ViewPresenter<T> presenter);

    public void execute(final ViewPresenter<T> presenter) {
        super.execute(getSubscriber(CacheSource.DISK, presenter));
    }

    protected void executeHttp(final ViewPresenter<T> presenter) {
        new UseCase<T>(getTypeToken()){
            @Override
            public Observable<T> buildUseCaseObservable() {
                return buildHttpObservable();
            }
        }.execute(getSubscriber(CacheSource.HTTP, presenter));
    }
}
