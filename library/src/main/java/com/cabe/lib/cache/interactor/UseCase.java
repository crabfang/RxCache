package com.cabe.lib.cache.interactor;

import com.google.gson.reflect.TypeToken;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 *
 * Created by cabe on 16/4/12.
 */
public abstract class UseCase<T> {

    private Subscription subscription = Subscriptions.empty();

    private Scheduler subscribeScheduler = Schedulers.io();
    private Scheduler observeScheduler = AndroidSchedulers.mainThread();

    private TypeToken<T> typeT;

    public UseCase(TypeToken<T> typeT) {
        this.typeT = typeT;
    }

    public TypeToken<T> getTypeToken() {
        return typeT;
    }

    public abstract Observable<T> buildUseCaseObservable();

    public void setExecutor(Scheduler subscribeScheduler) {
        this.subscribeScheduler = subscribeScheduler;
    }

    public void setPostThread(Scheduler observeScheduler) {
        this.observeScheduler = observeScheduler;
    }

    public Subscription execute(Subscriber<T> subscriber) {
        Observable<T> observable = this.buildUseCaseObservable();
        if(subscribeScheduler != null) {
            observable.subscribeOn(subscribeScheduler);
        }
        if(observeScheduler != null) {
            observable.observeOn(observeScheduler);
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