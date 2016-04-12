package com.cabe.lib.cache.http;

import android.text.TextUtils;
import android.util.Log;

import java.util.Iterator;
import java.util.Map;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.Converter;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import rx.Observable;

/**
 *
 * Created by cabe on 16/4/12.
 */
public class StringHttpFactory {
    public static RestAdapter.LogLevel logLevel = RestAdapter.LogLevel.BASIC;
    public static <T> Observable<T> createRequest(RequestParams params, HttpTransformer<T> transformer) {
        ApiService apiService = buildApiService(params);
        Observable<String> getApi = params.isPost
                ? apiService.post(params.path, params.query, params.body)
                : apiService.get(params.path, params.query);
        return composePre(getApi, transformer);
    }

    private static <T> Observable<T> composePre(Observable<String> baseResponse, HttpTransformer<T> transformer) {
        Observable.Transformer<String, String> schedulerTransformer = transformer.getThreadTransformer();
        Observable.Transformer<String, T> responseTransformer = transformer.getResponseTransformer();
        return baseResponse
                .compose(schedulerTransformer)
                .compose(responseTransformer);
    }

    private static ApiService buildApiService(final RequestParams params) {
        Converter converter = StringConverterFactory.create();
        String host = params.host;
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                Map<String, String> headMap = params.head;
                if(headMap != null) {
                    Iterator<Map.Entry<String, String>> it = headMap.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<String, String> pair = it.next();
                        request.addHeader(pair.getKey(), pair.getValue());
                        it.remove();
                    }
                }
            }
        };
        return getRetrofit(converter, host, requestInterceptor).create(ApiService.class);
    }

    private static RestAdapter getRetrofit(Converter converter, String baseUrl, RequestInterceptor dataInterceptor) {
        if(TextUtils.isEmpty(baseUrl)) {
            throw new RuntimeException("baseUrl is null");
        } else {
            RestAdapter.Builder retrofit = new RestAdapter.Builder();
            retrofit.setLog(new HttpLog());
            retrofit.setLogLevel(logLevel);
            retrofit.setEndpoint(baseUrl);
            retrofit.setConverter(converter);
            retrofit.setClient(new OkClient(OkHttpClientFactory.create()));
            if(dataInterceptor != null) {
                retrofit.setRequestInterceptor(dataInterceptor);
            }

            return retrofit.build();
        }
    }

    private static class HttpLog implements RestAdapter.Log {
        @Override
        public void log(String message) {
            Log.i("RxCache.Http", message);
        }
    }

    public interface ApiService {
        @GET("/{url}")
        Observable<String> get(@Path(value = "url", encode = false) String path, @QueryMap Map<String, String> query);

        @FormUrlEncoded
        @POST("/{url}")
        Observable<String> post(@Path(value = "url", encode = false) String path, @QueryMap Map<String, String> query, @FieldMap Map<String, String> body);
    }
}
