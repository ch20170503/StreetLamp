package com.zzb.sl;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.zzb.bean.TableUrl;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;


/**
 * Created by han.cong on 2017/08
 *　　　　　　　　┏┓　　　┏┓+ +
 *　　　　　　　┏┛┻━━━┛┻┓ + +
 *　　　　　　　┃　　　　　　　┃ 　
 *　　　　　　　┃　　　━　　　┃ ++ + + +
 *　　　　　　 ████━████     ┃  +
 *　　　　　　　┃　　　　　　　┃ +
 *　　　　　　　┃　　　┻　　　┃
 *　　　　　　　┃　　　　　　　┃ + +
 *　　　　　　　┗━┓　　　┏━┛
 *　　　　　　　　　┃　　　┃　　　　　　　　　　　
 *　　　　　　　　　┃　　　┃ + + + +
 *　　　　　　　　　┃　　　┃　　　　　　　　　
 *　　　　　　　　　┃　　　┃ + 　　　　神兽保佑,代码无bug　　
 *　　　　　　　　　┃　　　┃
 *　　　　　　　　　┃　　　┃　　+　　　　　　　　　
 *　　　　　　　　　┃　 　　┗━━━┓ + +
 *　　　　　　　　　┃ 　　　　　　　┣┓
 *　　　　　　　　　┃ 　　　　　　　┏┛
 *　　　　　　　　　┗┓┓┏━┳┓┏┛ + + + +
 *　　　　　　　　　　┃┫┫　┃┫┫
 *　　　　　　　　　　┗┻┛　┗┻┛+ + + +
 */
public class MainActivity extends AppCompatActivity {
    //首页加载时间
    private int time = 3;
    //创建一个Hander
    private Handler handler = new Handler();
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this, "98222219b64b1468c95088e4f192ae93");
        verifyStoragePermissions(this);
        //执行Runnable中的方法
        handler.postDelayed(runnable, 1000);
        //创建一个线程
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    //创建一个Runnable对象
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //将时间减
            time--;
            //判断
            if (time <= 1) {
                getdata();
            } else {
                handler.postDelayed(runnable, 1000);
            }
        }
    };


    /**
     * 查询数据
     */
    public void getdata(){
        BmobQuery<TableUrl> query = new BmobQuery<TableUrl>();
        query.getObject("09d5dd811d", new QueryListener<TableUrl>() {
            @Override
            public void done(TableUrl object, BmobException e) {
                if(e==null){
                    if (object.getContent().equals("0")){
                        Intent startIntent = new Intent(MainActivity.this, Login.class);
                        startActivity(startIntent);
                        finish();
                    }else{
                        //跳转
                        Intent startIntent = new Intent(MainActivity.this, Main22Activity.class);
                        startIntent.putExtra("URLS",object.getContent());
                        startActivity(startIntent);
                        finish();
                    }
                }else{
                    //跳转
                    Intent startIntent = new Intent(MainActivity.this, Login.class);
                    startIntent.putExtra("URLS",object.getContent());
                    startActivity(startIntent);
                    finish();
                }
            }

        });
    }

    public void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
}
