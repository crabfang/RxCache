package com.cabe.lib.cache.interactor;

import com.cabe.lib.cache.executor.JobExecutor;
import com.cabe.lib.cache.executor.PostExecutionThread;
import com.cabe.lib.cache.executor.ThreadExecutor;
import com.cabe.lib.cache.executor.UIThread;
import com.google.gson.reflect.TypeToken;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 *
 * Created by cabe on 16/4/12.
 */
public abstract class UseCase<T> {
    private ThreadExecutor executor = new JobExecutor();
    private PostExecutionThread postThread = new UIThread();

    private Subscription subscription = Subscriptions.empty();

    private TypeToken<T> typeT;

    public UseCase(TypeToken<T> typeT) {
        this.typeT = typeT;
    }

    public TypeToken<T> getTypeToken() {
        return typeT;
    }

    public abstract Observable<T> buildUseCaseObservable();

    public void setExecutor(ThreadExecutor executor) {
        this.executor = executor;
    }

    public void setPostThread(PostExecutionThread postThread) {
        this.postThread = postThread;
    }

    public Subscription execute(Subscriber<T> subscriber) {
        Observable<T> observable = this.buildUseCaseObservable();
        if(executor != null) {
            observable.subscribeOn(Schedulers.from(executor));
        }
        if(postThread != null) {
            observable.observeOn(postThread.getScheduler());
        }
        this.subscription = observable.subscribe(subscriber);
        return subscriber;
    }

    public void unsubscribe() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}