package com.cabe.lib.cache.http.repository;

import com.cabe.lib.cache.exception.HttpExceptionCode;
import com.cabe.lib.cache.exception.RxException;
import com.cabe.lib.cache.http.RequestParams;

import java.io.InputStream;
import java.util.Map;

import retrofit.converter.Converter;
import retrofit.http.Body;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import retrofit.mime.TypedString;
import rx.Observable;
import rx.Subscriber;

/**
 * 简单的Retrofit实现<br>
 * Created by cabe on 16/4/12.
 */
public class InputStreamHttpFactory implements HttpFactoryInterface<InputStream> {
    public Observable<InputStream> createRequest(RequestParams params, Converter converter) {
        if(params == null) {
            return Observable.create(new Observable.OnSubscribe<InputStream>() {
                @Override
                public void call(Subscriber<? super InputStream> subscriber) {
                    subscriber.onError(RxException.build(HttpExceptionCode.HTTP_STATUS_LOCAL_REQUEST_NONE, null));
                }
            });
        }
        ApiService apiService = BaseHttpFactory.buildApiService(params, converter, ApiService.class);
        Observable<InputStream> observable;
        switch(params.requestMethod) {
            default:
            case RequestParams.REQUEST_METHOD_GET:
                observable = apiService.get(params.path, params.query);
                break;
            case RequestParams.REQUEST_METHOD_POST:
                observable = apiService.post(params.path, params.query, params.body);
                break;
            case RequestParams.REQUEST_METHOD_PUT:
                observable = apiService.put(params.path, params.query, new TypedString(params.putBody));
                break;
        }
        return observable;
    }

    private interface ApiService {
        @GET("/{url}")
        Observable<InputStream> get(@Path(value = "url", encode = false) String path, @QueryMap Map<String, String> query);

        @FormUrlEncoded
        @POST("/{url}")
        Observable<InputStream> post(@Path(value = "url", encode = false) String path, @QueryMap Map<String, String> query, @FieldMap Map<String, String> body);

        @POST("/{url}")
        Observable<InputStream> put(@Path(value = "url", encode = false) String path, @QueryMap Map<String, String> query, @Body TypedString body);
    }
}
