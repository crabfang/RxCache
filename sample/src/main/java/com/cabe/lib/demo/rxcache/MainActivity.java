package com.cabe.lib.demo.rxcache;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.cabe.lib.cache.CacheSource;
import com.cabe.lib.cache.CacheUseCase;
import com.cabe.lib.cache.disk.DiskCacheManager;
import com.cabe.lib.cache.http.RequestParams;
import com.cabe.lib.cache.http.StringHttpFactory;
import com.cabe.lib.cache.interactor.ViewPresenter;
import com.cabe.lib.cache.interactor.impl.SimpleViewPresenter;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;
import rx.Observable;

public class MainActivity extends AppCompatActivity {
    protected static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StringHttpFactory.logLevel = RestAdapter.LogLevel.NONE;
        CacheUseCase.DISK_CACHE_PATH = getExternalCacheDir() + File.separator + "data";
    }

    public void clickHttp(View v) {
        Log.w(TAG, "click http");

        RequestParams params = new RequestParams();
        params.host = "https://www.github.com";
        params.path = "crabfang/RxCache";

        CacheUseCase<GitHubBean> useCase = new CacheUseCase<>(new TypeToken<GitHubBean>(){}, params);
        useCase.execute(new ViewPresenter<GitHubBean>() {
            @Override
            public void error(CacheSource from, int code, String info) {
            }
            @Override
            public void load(CacheSource from, GitHubBean data) {
            }
            @Override
            public void complete(CacheSource from) {
            }
        });
    }

    public void clickDisk(View v) {
        Log.w(TAG, "click disk");

        DiskCacheManager cacheManager = new DiskCacheManager(CacheUseCase.DISK_CACHE_PATH);

        TypeToken<List<Person>> typeToken = new TypeToken<List<Person>>(){};
        if(!cacheManager.exits(typeToken)) {
            List<Person> list = new ArrayList<>();
            for(int i=0;i<10;i++) {
                Person p = new Person();
                p.name = "name " + i;
                p.age = 10 + i;
                list.add(p);
            }
            cacheManager.put(typeToken, list);
        }

        CacheUseCase<List<Person>> useCase = new CacheUseCase<List<Person>>(new TypeToken<List<Person>>(){}, null){
            @Override
            public Observable<List<Person>> buildDiskObservable() {
                return super.buildDiskObservable();
            }
        };
        useCase.setDiskOnly(true);
        useCase.execute(new SimpleViewPresenter<List<Person>>());
    }
}