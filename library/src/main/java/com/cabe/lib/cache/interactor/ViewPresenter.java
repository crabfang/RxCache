package com.cabe.lib.cache.interactor;

/**
 *
 * Created by cabe on 16/4/13.
 */
public interface ViewPresenter<T> {
    void error(int code, String info);
    void load(T data);
    void complete();
}