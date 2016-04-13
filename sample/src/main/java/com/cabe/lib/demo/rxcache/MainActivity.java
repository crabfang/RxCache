package com.cabe.lib.demo.rxcache;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.cabe.lib.cache.CacheSource;
import com.cabe.lib.cache.CacheUseCase;
import com.cabe.lib.cache.http.RequestParams;
import com.cabe.lib.cache.http.StringHttpFactory;
import com.cabe.lib.cache.interactor.ViewPresenter;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import retrofit.RestAdapter;

public class MainActivity extends AppCompatActivity {
    protected static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StringHttpFactory.logLevel = RestAdapter.LogLevel.BASIC;
    }

    public void onClick(View v) {
        Log.w(TAG, "click");

        RequestParams params = new RequestParams();
        params.host = "https://www.u51.com/51rp/rpdservice";
        params.path = "appLoadingImage.htm";
        Map<String, String> query = new HashMap<>();
        query.put("loadingImageType", "android-large");
        params.query = query;

        CacheUseCase<SplashInfo> useCase = new CacheUseCase<>(new TypeToken<SplashInfo>(){}, params);
        useCase.setCachePath(getExternalCacheDir() + File.separator + "com.cabe.demo.rxcache");
        useCase.execute(new ViewPresenter<SplashInfo>(){
            @Override
            public void error(CacheSource source, int code, String info) {
                Log.w(TAG, code + "#" + info);
            }
            @Override
            public void load(CacheSource source, SplashInfo data) {
                Log.w(TAG, "load:" + data);
            }
            @Override
            public void complete(CacheSource source) {
                Log.w(TAG, "complete");
            }
        });
    }
}