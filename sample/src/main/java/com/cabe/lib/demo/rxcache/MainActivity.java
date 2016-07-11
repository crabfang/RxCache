package com.cabe.lib.demo.rxcache;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cabe.lib.cache.CacheSource;
import com.cabe.lib.cache.DiskCacheRepository;
import com.cabe.lib.cache.disk.DiskCacheManager;
import com.cabe.lib.cache.http.RequestParams;
import com.cabe.lib.cache.http.repository.BaseHttpFactory;
import com.cabe.lib.cache.http.repository.WebCookiesHandler;
import com.cabe.lib.cache.http.transformer.HttpStringTransformer;
import com.cabe.lib.cache.impl.BytesUseCase;
import com.cabe.lib.cache.impl.DiskCacheUseCase;
import com.cabe.lib.cache.impl.DoubleCacheUseCase;
import com.cabe.lib.cache.impl.HttpCacheUseCase;
import com.cabe.lib.cache.impl.HttpPollUseCase;
import com.cabe.lib.cache.interactor.impl.SimpleViewPresenter;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;
import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity {
    protected static String TAG = "MainActivity";

    private TextView label;
    private ImageView image;
    private EditText input;

    private CompositeSubscription cs = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        label = (TextView) findViewById(R.id.activity_main_label);
        image = (ImageView) findViewById(R.id.activity_main_view_image);
        input = (EditText) findViewById(R.id.activity_main_view_input);

        DiskCacheManager.DISK_CACHE_PATH = getExternalCacheDir() + File.separator + "data";
        BaseHttpFactory.logLevel = RestAdapter.LogLevel.FULL;
        BaseHttpFactory.getHttpClient().setCookieHandler(new WebCookiesHandler());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cs.unsubscribe();
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
                useCase.getHttpRepository().setResponseTransformer(new HttpStringTransformer<HostBean>() {
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
        useCase.getHttpRepository().setResponseTransformer(new HttpStringTransformer<HostBean>() {
            @Override
            public HostBean buildData(String responseStr) {
                return new HostBean("GitHub");
            }
        });
        Subscription sc = useCase.execute(new SimpleViewPresenter<HostBean>(){
            @Override
            public void load(CacheSource from, HostBean data) {
                Log.w("MainActivity", "load:" + from);
                label.setText(String.valueOf("" + from));
            }
        });
        cs.add(sc);
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

        Subscription sc = useCase.execute(new SimpleViewPresenter<List<Person>>(){
            @Override
            public void load(CacheSource from, List<Person> data) {
                Log.w("MainActivity", "load:" + from);
                label.setText(String.valueOf("" + from));
            }
        });
        cs.add(sc);
    }

    public void clickHttp(View v) {
        Log.w(TAG, "click http");

        RequestParams params = new RequestParams();
        params.host = "http://www.github.com";
        params.path = "crabfang/RxCache";

        DoubleCacheUseCase<HostBean> useCase = new HttpCacheUseCase<>(new TypeToken<HostBean>(){}, params);
        useCase.getHttpRepository().setResponseTransformer(new HttpStringTransformer<HostBean>() {
            @Override
            public HostBean buildData(String responseStr) {
                return new HostBean("GitHub");
            }
        });
        Subscription sc = useCase.execute(new SimpleViewPresenter<HostBean>(){
            @Override
            public void load(CacheSource from, HostBean data) {
                Log.w("MainActivity", "load:" + from);
                label.setText(String.valueOf("" + from));
            }
        });
        cs.add(sc);
    }

    public void clickImage(View view) {
        Log.w(TAG, "click image");

        RequestParams params = new RequestParams();
        params.host = "http://www.zjsxyc.com";
        params.path = "/public/public/image.jsp";

        BytesUseCase useCase = new BytesUseCase(params);
        Subscription sc = useCase.execute(new SimpleViewPresenter<byte[]>(){
            @Override
            public void load(CacheSource from, byte[] data) {
                Glide.with(MainActivity.this).load(data).into(image);
            }

            @Override
            public void error(CacheSource from, int code, String info) {
                Log.w("MainActivity", "error:" + info);
            }
        });
        cs.add(sc);
    }

    public void clickLogin(View view) {
        Log.w(TAG, "click login");

        String vfyCode = input.getText().toString();
        if(vfyCode.isEmpty()) {
            clickImage(null);
        } else {
            LoginUseCase useCase = new LoginUseCase("MzgwMDE3ODQ=", "Nzg0", vfyCode);
            Subscription sc = useCase.execute(new SimpleViewPresenter<String>(){
                @Override
                public void load(CacheSource from, String data) {
                    super.load(from, data);
                }
            });
            cs.add(sc);
        }
    }

    public void clickPoll(View view) {
        Log.w(TAG, "click poll");

        RequestParams params = new RequestParams();
        params.host = "http://api.map.baidu.com/";
        params.path = "telematics/v3/weather?location=杭州&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ";
        final HttpPollUseCase<String> useCase = new HttpPollUseCase<String>(new TypeToken<String>() {
        }, params) {
            @Override
            protected boolean pollFinish(String data) {
                return false;
            }
            @Override
            protected long getPollIntervalMills(String data) {
                return 3000;
            }
        };
        useCase.setForceMaxTime(5);
        Subscription sc = useCase.execute(new SimpleViewPresenter<String>(){
            @Override
            public void load(CacheSource from, String data) {
                Log.w(TAG, "load:" + data);
            }
            @Override
            public void error(CacheSource from, int code, String info) {
                Log.w(TAG, "error:" + info);
                useCase.stopPoll();
            }
        });
        cs.add(sc);
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