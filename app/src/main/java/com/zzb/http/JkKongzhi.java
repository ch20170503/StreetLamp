package com.zzb.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zzb.shishi.JsonRootBean;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Administrator on 2018/3/6 0006.
 */

public class JkKongzhi {
    public static JsonRootBean thisUser() {

        String url = " ";
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String LoginInfo = response.body().string();
                Gson g = new Gson();
                JsonRootBean user = g.fromJson(LoginInfo, new TypeToken<JsonRootBean>() {
                }.getType());
                if (user != null) {
                    return user;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

}
