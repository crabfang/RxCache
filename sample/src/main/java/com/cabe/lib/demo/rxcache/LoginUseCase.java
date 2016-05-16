package com.cabe.lib.demo.rxcache;

import com.cabe.lib.cache.http.transformer.HttpStringTransformer;
import com.cabe.lib.cache.http.RequestParams;
import com.cabe.lib.cache.impl.HttpCacheUseCase;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

/**
 * Login UseCase
 * Created by cabe on 16/5/12.
 */
public class LoginUseCase extends HttpCacheUseCase<String> {
    public LoginUseCase(String username, String password, String vfy) {
        super(new TypeToken<String>(){}, null);

        RequestParams params = new RequestParams();
        params.host = "http://www.zjsxyc.com";
        params.path = "WebLogin.do?method=check";
        params.isPost = true;
        params.body = new HashMap<>();
        params.body.put("formusercode", username);
        params.body.put("formuserpassword", password);
        params.body.put("formconfirmpassword", vfy);
        params.body.put("formusertype", "1");
        setRequestParams(params);

        getHttpRepository().setResponseTransformer(new HttpStringTransformer<String>() {
            @Override
            public String buildData(String responseStr) {
                return responseStr;
            }
        });
    }
}
