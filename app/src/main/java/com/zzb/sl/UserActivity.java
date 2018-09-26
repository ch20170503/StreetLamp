package com.zzb.sl;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.zzb.bean.AndroidUserInfo;
import com.zzb.bean.SelectUser;
import com.zzb.bean.TotalUrl;
import com.zzb.bean.User;
import com.zzb.db.UserDao;
import com.zzb.db.UserDaoImpl;
import com.zzb.http.UserHttp;
import com.zzb.mainnavigatetabbar.widget.XCRoundImageView;
import com.zzb.util.ActivityUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class UserActivity extends SwipeBackActivity implements View.OnClickListener {
    private static final String TAG = "UserActivity";
    private XCRoundImageView imageView;
    //常量
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHONE = 2;

    //页面值
    private TextView name,nname,zhH,phone,email;

    private UserDao userDao;
    private AndroidUserInfo androidUserInfo;
    //创建list集合
    private boolean b;
    List<AndroidUserInfo> aUi = new ArrayList<>();
    //用户账号
    String Nnname;
    //用户头像
    Bitmap setT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setData();
        imageView = (XCRoundImageView) findViewById(R.id.user_img);
        findViewById(R.id.user_img2).setOnClickListener(this);
        findViewById(R.id.user_img3).setOnClickListener(this);
       /* findViewById(R.id.newsData).setOnClickListener(this);*/
        //获取数据
        Intent intent = getIntent();
         Nnname = intent.getStringExtra("USERACTIVITY");
        //查询用户头像
        aUi = playDate(Nnname);
        if(aUi.size() >=1){
            if (aUi.get(0).getImphoto() == null){
                Drawable drawable = this.getResources().getDrawable(R.drawable.timg);
                Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                imageView.setImageBitmap(bitmap);
            }else {
                imageView.setImageBitmap(aUi.get(0).getImphoto());
            }

        }
        imageView.setOnClickListener(new View.OnClickListener() {
                @Override
            public void onClick(View v) {
                ActivityUtil.belowwindows(UserActivity.this);
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ActivityUtil.doNext(requestCode, grantResults, UserActivity.this);
    }
    //设置图片
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG,"哈哈");
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG,"哈哈");
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //给图片空间赋值
                    try {
                        Bitmap b = BitmapFactory.decodeStream(UserActivity.this.getContentResolver().openInputStream(ActivityUtil.uri));
                        setT = b;
                        b = ThumbnailUtils.extractThumbnail(b, 200, 200);
                        Log.e(TAG,"b:"+b);
                        if (imageView != null) {
                            //清除之前加载过的图片缓存
                            imageView.refreshDrawableState();
                        }
                        imageView.setImageBitmap(b);
                        ActivityUtil.mPopupWindow.dismiss();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHONE:
                if (resultCode == RESULT_OK) {
                    //判断手机版本型号
                    if (Build.VERSION.SDK_INT >= 19) {
                        Log.e(TAG,"data:"+data);
                        imageView.refreshDrawableState();
                        //4.4级以上的调用这个方法
                        ActivityUtil.handleImageOnKitKat(data, UserActivity.this, imageView);
                    } else {
                        Log.e(TAG,"data:"+data);
                        imageView.refreshDrawableState();
                        ActivityUtil.handleImageBeforeKitKat(data, imageView, UserActivity.this);
                    }
                }
                break;
            default:
                break;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_img2:
                finish();
                break;
            case R.id.user_img3:
                if (aUi.size() >= 1 && aUi.get(0).getImphoto() != null) {
                    imageView.setDrawingCacheEnabled(true);
                    Bitmap obmp = Bitmap.createBitmap(imageView.getDrawingCache());
                    Log.e(TAG,"imageView1:"+obmp);
                  //  obmp = ThumbnailUtils.extractThumbnail(obmp, 200, 200);
                    updata(obmp,Nnname);
                    imageView.setDrawingCacheEnabled(false);
                    finish();
                    break;
                }
                if(aUi.size() < 1){
                imageView.setDrawingCacheEnabled(true);
                Bitmap obmp = Bitmap.createBitmap(imageView.getDrawingCache());
               // obmp = ThumbnailUtils.extractThumbnail(obmp, 200, 200);
                Log.e(TAG,"imageView2:"+obmp);
                 getNameandImg(obmp,Nnname);
                imageView.setDrawingCacheEnabled(false);
                finish();
                }
                break;
            /*case R.id.newsData:
                 UpdateManager manager = new UpdateManager(this);
                        // 检查软件更新
                        manager.checkUpdate(false);
                break;*/
        }
    }
    //设置页面值
    private void setData(){
       name = (TextView) findViewById(R.id.user_text_name);
        nname = (TextView) findViewById(R.id.user_text_Nname);
        zhH = (TextView) findViewById(R.id.user_text_zhH);
        phone = (TextView) findViewById(R.id.user_text_phone);
        email = (TextView) findViewById(R.id.user_text_email);
        final User u = TotalUrl.getUser();
        Log.e(TAG,"用户:"+u.toString());
        new Thread(new Runnable() {
            @Override
            public void run() {
                 List<SelectUser> selectUList1 = UserHttp.thisUser(u);

                if (selectUList1 == null){
                    selectUList1 = UserHttp.thisUser(u);
                    if (selectUList1==null){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                name.setText("");
                                nname.setText("");
                                zhH.setText("");
                                phone.setText("");
                                email.setText("");
                                ActivityUtil.showToasts(UserActivity.this, R.string.PleaseCheckTheNetwork, 1 * 1000);
                            }
                        });
                        return;
                    }
                }

                final List<SelectUser> selectUList = selectUList1;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < selectUList.size(); i++) {
                            name.setText(selectUList.get(i).getsS_RealName());
                            nname.setText(selectUList.get(i).getsS_NickName());
                            zhH.setText(selectUList.get(i).getsS_Account());
                            phone.setText(selectUList.get(i).getsS_MobilePhone());
                            email.setText(selectUList.get(i).getsS_Email());
                        }
                    }
                });
            }
        }).start();
    }
    /**
     * 创建数据源
     */
    public  List<AndroidUserInfo> playDate(String name) {
        List<AndroidUserInfo> listUl = new ArrayList<>();
        userDao = new UserDaoImpl(this);
        androidUserInfo = userDao.findByName(name);
        if (androidUserInfo !=null ){
            //将数据保存到集合
            listUl.add(androidUserInfo);
            for (int i = 0; i < listUl.size(); i++) {
                Log.e(TAG,"数据库:"+listUl.get(i).getName()+"  图片:"+listUl.get(i).getImphoto());
            }
        }
        Log.e(TAG,"用户数量:"+listUl.size());
        return listUl;
    }
    //数据库操作
    private void getNameandImg(Bitmap obmp, String name) {
        Log.e(TAG,"数据库操作添加:"+obmp);
        //获取数据库操作对象
        userDao = new UserDaoImpl(this);
        androidUserInfo = new AndroidUserInfo(name, obmp);
        b = userDao.save(androidUserInfo);
        ActivityUtil.showToasts(this,b ? R.string.Addsuccess :R.string.Addfailed,1*1000);
    }
    //数据库操作修改
    private void updata(Bitmap obmp, String name) {
        Log.e(TAG,"数据库操作修改:"+obmp);
        //获取数据库操作对象
        userDao = new UserDaoImpl(this);
        androidUserInfo = new AndroidUserInfo(name, obmp);
        b = userDao.update(androidUserInfo);
        ActivityUtil.showToasts(this,b ? R.string.Addsuccess :R.string.Addfailed,1*1000);
    }
}
