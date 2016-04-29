package com.cabe.lib.cache.interactor.impl;

import android.util.Log;

import com.cabe.lib.cache.CacheSource;
import com.cabe.lib.cache.interactor.ViewPresenter;

/**
 * ViewPresenter Simple Implement
 * Created by cabe on 16/4/13.
 */
public class SimpleViewPresenter<T> implements ViewPresenter<T> {
    protected final static String TAG = "SimpleViewPresenter";
    @Override
    public void error(CacheSource from, int code, String info) {
        Log.d(TAG, "error:" + from + "-->" + code + "#" + info);
    }

    @Override
    public void load(CacheSource from, T data) {
        Log.d(TAG, "load:" + from + "-->" + data);
    }

    @Override
    public void complete(CacheSource from) {
        Log.d(TAG, "complete:" + from);
    }
}
