package com.cabe.lib.cache.interactor;

import com.cabe.lib.cache.CacheSource;

/**
 *
 * Created by cabe on 16/4/13.
 */
public interface ViewPresenter<T> {
    void error(CacheSource from, int code, String info);
    void load(CacheSource from, T data);
    void complete(CacheSource from);
}