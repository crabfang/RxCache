package com.cabe.lib.cache.executor;

import rx.Scheduler;

/**
 *
 * Created by cabe on 16/4/12.
 */
public interface PostExecutionThread {
    Scheduler getScheduler();
}
