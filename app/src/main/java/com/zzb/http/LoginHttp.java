package com.zzb.http;

import android.util.Log;

import com.zzb.bean.User;
import com.zzb.util.GsonUtil;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/8/14 0014.
 */

public class LoginHttp {
    private static final String TAG = "LoginHttp";

    //网络请求
    public static User LoginPost(String name, String pwd, String ip){
        String url = ip+"/Account/Login?Username="+name+"&Password="+pwd;
        Log.e(TAG,"URL:"+url);
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            Log.e(TAG,"URL:"+ip);
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String LoginInfo =  response.body().string();
                User users = GsonUtil.parseJsonWithGson(LoginInfo, User.class);
                Log.e(TAG, String.valueOf(users));
                return users;

            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }catch(IllegalArgumentException e){
            e.printStackTrace();
            return null;
        }
    }
}
