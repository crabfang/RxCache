package com.cabe.lib.cache.http.repository;

import com.cabe.lib.cache.http.RequestParams;

import retrofit.converter.Converter;
import rx.Observable;

/**
 * http interface
 * Created by cabe on 16/5/16.
 */
public interface HttpFactoryInterface<T> {
    Observable<T> createRequest(RequestParams params, Converter converter);
}
