package com.cabe.lib.demo.rxcache;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.cabe.lib.cache.DiskCacheRepository;
import com.cabe.lib.cache.disk.DiskCacheManager;
import com.cabe.lib.cache.http.HttpTransformer;
import com.cabe.lib.cache.http.RequestParams;
import com.cabe.lib.cache.http.StringHttpFactory;
import com.cabe.lib.cache.impl.DiskCacheUseCase;
import com.cabe.lib.cache.impl.DoubleCacheUseCase;
import com.cabe.lib.cache.impl.HttpCacheUseCase;
import com.cabe.lib.cache.interactor.impl.SimpleViewPresenter;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;
import rx.Observable;
import rx.functions.Func1;

public class MainActivity extends AppCompatActivity {
    protected static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StringHttpFactory.logLevel = RestAdapter.LogLevel.FULL;
        DiskCacheManager.DISK_CACHE_PATH = getExternalCacheDir() + File.separator + "data";
    }

    public void clickBoth(View v) {
        Log.w(TAG, "click both");

        RequestParams params = new RequestParams();
        params.host = "http://www.github.com";
        params.path = "crabfang/RxCache";

        DoubleCacheUseCase<HostBean> useCase = new DoubleCacheUseCase<>(new TypeToken<HostBean>(){}, params);
        useCase.setHttpTransformer(new Func1<HostBean, Observable<HostBean>>() {
            @Override
            public Observable<HostBean> call(HostBean hostBean) {
                RequestParams params = new RequestParams();
                params.host = "https://www.bing.com";
                final DoubleCacheUseCase<HostBean> useCase = new HttpCacheUseCase<>(new TypeToken<HostBean>(){}, params);
                useCase.getHttpRepository().setResponseTransformer(new HttpTransformer<HostBean>() {
                    @Override
                    public HostBean buildData(String responseStr) {
                        return new HostBean("Bing");
                    }
                });
                return useCase.buildHttpObservable().map(new Func1<HostBean, HostBean>() {
                    @Override
                    public HostBean call(HostBean hostBean) {
                        useCase.saveCacheDisk(hostBean);
                        return hostBean;
                    }
                });
            }
        });
        useCase.getHttpRepository().setResponseTransformer(new HttpTransformer<HostBean>() {
            @Override
            public HostBean buildData(String responseStr) {
                return new HostBean("GitHub");
            }
        });
        useCase.execute(new SimpleViewPresenter<HostBean>());
    }

    public void clickDisk(View v) {
        Log.w(TAG, "click disk");

        DoubleCacheUseCase<List<Person>> useCase = new DiskCacheUseCase<>(new TypeToken<List<Person>>(){});
        DiskCacheRepository cacheManager = useCase.getDiskRepository();
        if(cacheManager != null) {
            TypeToken<List<Person>> typeToken = new TypeToken<List<Person>>(){};
            if(!cacheManager.exits(typeToken)) {
                List<Person> list = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    Person p = new Person();
                    p.setName("name " + i);
                    p.setAge(10 + i);
                    list.add(p);
                }
                cacheManager.put(typeToken, list);
            }
        }

        useCase.execute(new SimpleViewPresenter<List<Person>>());
    }

    public void clickHttp(View v) {
        Log.w(TAG, "click http");

        RequestParams params = new RequestParams();
        params.host = "http://www.github.com";
        params.path = "crabfang/RxCache";

        DoubleCacheUseCase<HostBean> useCase = new HttpCacheUseCase<>(new TypeToken<HostBean>(){}, params);
        useCase.getHttpRepository().setResponseTransformer(new HttpTransformer<HostBean>() {
            @Override
            public HostBean buildData(String responseStr) {
                return new HostBean("GitHub");
            }
        });
        useCase.execute(new SimpleViewPresenter<HostBean>());
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

    private class HostBean {
        public String str;
        public HostBean(String str) {
            setStr(str);
        }
        public void setStr(String str) {
            this.str = str;
        }

        public String toString() {
            return "Host:" + str;
        }
    }
}