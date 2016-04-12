package com.cabe.lib.cache.executor;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

/**
 * UI Thread
 * Created by cabe on 16/4/12.
 */
public class UIThread implements PostExecutionThread {
    @Override
    public Scheduler getScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
