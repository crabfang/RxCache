package com.cabe.lib.cache.interactor;

import com.cabe.lib.cache.executor.JobExecutor;
import com.cabe.lib.cache.executor.PostExecutionThread;
import com.cabe.lib.cache.executor.ThreadExecutor;
import com.cabe.lib.cache.executor.UIThread;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * RxJava的线程切换工具
 * Created by cabe on 16/4/12.
 */
public class ThreadTransformer<T> implements Observable.Transformer<T, T> {
    private ThreadExecutor threadExecutor = new JobExecutor();
    private PostExecutionThread postExecutionThread = new UIThread();

    public void setPostExecutionThread(PostExecutionThread postExecutionThread) {
        this.postExecutionThread = postExecutionThread;
    }

    public void setThreadExecutor(ThreadExecutor threadExecutor) {
        this.threadExecutor = threadExecutor;
    }

    @Override
    public Observable<T> call(Observable<T> tObservable) {
        if(tObservable != null) {
            tObservable.subscribeOn(Schedulers.from(threadExecutor));
            tObservable.observeOn(postExecutionThread.getScheduler());
        }
        return tObservable;
    }
}
