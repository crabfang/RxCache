package com.cabe.lib.demo.rxcache;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.cabe.lib.cache.AbstractCacheUseCase;
import com.cabe.lib.cache.CacheMethod;
import com.cabe.lib.cache.disk.DiskCacheManager;
import com.cabe.lib.cache.http.RequestParams;
import com.cabe.lib.cache.http.StringHttpFactory;
import com.cabe.lib.cache.impl.DoubleCacheUseCase;
import com.cabe.lib.cache.interactor.impl.SimpleViewPresenter;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RestAdapter;

public class MainActivity extends AppCompatActivity {
    protected static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StringHttpFactory.logLevel = RestAdapter.LogLevel.NONE;
        DiskCacheManager.DISK_CACHE_PATH = getExternalCacheDir() + File.separator + "data";
    }

    public void clickBoth(View v) {
        Log.w(TAG, "click both");

        RequestParams params = new RequestParams();
        params.host = "https://www.cabe.com";
        params.path = "appLoadingImage.htm";
        Map<String, String> query = new HashMap<>();
        query.put("loadingImageType", "android-large");
        params.query = query;

        AbstractCacheUseCase<GitHubBean> useCase = new DoubleCacheUseCase<>(new TypeToken<GitHubBean>(){}, params);
        useCase.execute(new SimpleViewPresenter<GitHubBean>());
    }

    public void clickDisk(View v) {
        Log.w(TAG, "click disk");

        DiskCacheManager cacheManager = new DiskCacheManager();

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

        DoubleCacheUseCase<List<Person>> useCase = new DoubleCacheUseCase<>(new TypeToken<List<Person>>(){}, null, CacheMethod.DISK);
        useCase.execute(new SimpleViewPresenter<List<Person>>());
    }

    public void clickHttp(View v) {
        Log.w(TAG, "click http");

        RequestParams params = new RequestParams();
        params.host = "https://www.cabe.com";
        params.path = "appLoadingImage.htm";
        Map<String, String> query = new HashMap<>();
        query.put("loadingImageType", "android-large");
        params.query = query;

        AbstractCacheUseCase<GitHubBean> useCase = new DoubleCacheUseCase<>(new TypeToken<GitHubBean>(){}, params, CacheMethod.HTTP);
        useCase.execute(new SimpleViewPresenter<GitHubBean>());
    }

    private class Person {
        public String name;
        public int age;
        public void setAge(int age) {
            this.age = age;
        }
        public void setName(String name) {
            this.name = name;
        }
        @Override
        public String toString() {
            return name + "_" + age;
        }
    }

    private class GitHubBean {
        public String str;
        public void setStr(String str) {
            this.str = str;
        }
    }
}