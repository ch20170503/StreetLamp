package com.zzb.sl;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.zzb.bean.SelectUser;
import com.zzb.bean.TotalUrl;
import com.zzb.bean.User;
import com.zzb.db.DBHelper;
import com.zzb.http.LoginHttp;
import com.zzb.http.UserHttp;
import com.zzb.util.ActivityUtil;

import java.util.Calendar;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by han.cong on 2017/08
 *　　　　　　　　┏┓　　　┏┓+ +
 *　　　　　　　┏┛┻━━━┛┻┓ + +
 *　　　　　　　┃　　　　　　　┃ 　
 *　　　　　　　┃　　　━　　　┃ ++ + + +
 *　　　　　　 ████━████ ┃+
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

public class Login extends SwipeBackActivity {
    private long mExitTime;
    private EditText editTextIp;
    private EditText editTextName;
    private EditText editTextPwd;
    private Button LoginButton;
    private CheckBox remember;
    private CheckBox autologin;
    private SharedPreferences sp;
    private String userNameValue,passwordValue,userIP;
    public static final String TAG = "Login";
    private DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSwipeBackLayout().setEnableGesture(false);
        dbHelper= new DBHelper(this, "sl.db", null, 1);
        //设置图标大小
        setEditTextIco();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 实现再按一次退出
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                ActivityUtil.showToasts(this, R.string.PressAgainToExit, 1*1000);
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    //设置EditText图标
    private void setEditTextIco(){
        //控制登录服务器图标大小
        editTextIp = (EditText) findViewById(R.id.login_etIp);
        Drawable drawable1 = getResources().getDrawable(R.drawable.service);
        drawable1.setBounds(0, 0, 50, 50);//第一0是距左边距离，第二0是距上边距离，50分别是长宽
        editTextIp.setCompoundDrawables(drawable1, null, null, null);//只放左边
        //控制登录用户名图标大小
        editTextName = (EditText) findViewById(R.id.login_etName);
        Drawable drawable2 = getResources().getDrawable(R.drawable.username);
        drawable2.setBounds(0, 0, 50, 50);//第一0是距左边距离，第二0是距上边距离，50分别是长宽
        editTextName.setCompoundDrawables(drawable2, null, null, null);//只放左边
        //控制登录密码图标大小
        editTextPwd = (EditText) findViewById(R.id.login_etPwd);
        Drawable drawable3 = getResources().getDrawable(R.drawable.userpwd);
        drawable3.setBounds(0, 0, 50, 50);//第一0是距左边距离，第二0是距上边距离，50分别是长宽
        editTextPwd.setCompoundDrawables(drawable3, null, null, null);//只放左边
        remember = (CheckBox) findViewById(R.id.remember);
        autologin = (CheckBox) findViewById(R.id.autologin);
        sp = getSharedPreferences("userInfo", 0);
        final String name=sp.getString("USER_NAME", "");
        String pass =sp.getString("PASSWORD", "");
        final String ip =sp.getString("IP", "");

        final boolean choseRemember =sp.getBoolean("remember", false);
        final boolean choseAutoLogin =sp.getBoolean("autologin", false);
        //如果上次选了记住密码，那进入登录页面也自动勾选记住密码，并填上用户名和密码
        if(choseRemember){
            editTextIp.setText(ip);
            editTextName.setText(name);
            editTextPwd.setText(pass);
            remember.setChecked(true);
        }
        //如果上次登录选了自动登录，那进入登录页面也自动勾选自动登录
        if(choseAutoLogin){
            autologin.setChecked(true);
            userIP = editTextIp.getText().toString();
            userNameValue = editTextName.getText().toString();
            passwordValue = editTextPwd.getText().toString();
            final SharedPreferences.Editor editor =sp.edit();
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    User user =  LoginHttp.LoginPost(userNameValue,passwordValue,userIP);
                    if(user != null){
                        if(user.getCode() != 1){
                            //登录成功!!保存用户名和密码
                            editor.putString("IP", userIP);
                            editor.putString("USER_NAME", userNameValue);
                            editor.putString("PASSWORD", passwordValue);
                            TotalUrl totalUrl = new TotalUrl();
                            totalUrl.setsURLHEAD(userIP);
                            totalUrl.setUser(user);
                            totalUrl.setName(userNameValue);
                            totalUrl.setPassWord(passwordValue);
                            //是否记住密码
                            if(remember.isChecked()){
                                editor.putBoolean("remember", true);
                            }else{
                                editor.putBoolean("remember", false);
                            }
                            //是否自动登录
                            if(autologin.isChecked()){
                                editor.putBoolean("autologin", true);
                            }else{
                                editor.putBoolean("autologin", false);
                            }
                            editor.commit();
                            List<SelectUser> getIDUser = UserHttp.thisUser(user);
                            if (getIDUser == null || getIDUser.size() <1){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.clear();
                                        editor.commit();
                                        ActivityUtil.showToasts(Login.this, R.string.PleaseLogInAgain, 1*1000);
                                    }
                                });
                            }
                            Log.e(TAG, String.valueOf(getIDUser));
                            //跳转
                            Intent intent =new Intent(Login.this,HomPage.class);
                            if (getIDUser.get(0).getsS_NickName() == null){
                                return;
                            }
                            if (getIDUser.get(0).getsS_Account() == null){
                                return;
                            }
                            intent.putExtra("getsS_NickName",getIDUser.get(0).getsS_NickName()+","+getIDUser.get(0).getsS_Account());
                            startActivity(intent);
                            finish();
                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.clear();
                                    editor.commit();
                                    ActivityUtil.showToasts(Login.this, R.string.PleaseReenter, 1*1000);
                                }
                            });
                        }
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SharedPreferences.Editor editor = sp.edit();
                                editor.clear();
                                editor.commit();
                                sp.edit().putBoolean("autologin",false).commit();
                                sp.edit().putBoolean("remember",false).commit();
                                ActivityUtil.showToasts(Login.this, R.string.PleaseReenter, 1*1000);
                            }
                        });
                    }

                }
            }.start();
        }

        LoginButton = (Button) findViewById(R.id.login_button);
        LoginButton.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (editTextName.getText().toString() == null && editTextPwd.getText().toString()==null  &&  editTextIp.getText().toString() == null){
                    ActivityUtil.showToasts(Login.this, R.string.PleaseReenter, 1*1000);
                }else
                {
                    userIP = editTextIp.getText().toString();
                    userNameValue = editTextName.getText().toString();
                    passwordValue = editTextPwd.getText().toString();
                    final SharedPreferences.Editor editor =sp.edit();
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            User user =  LoginHttp.LoginPost(userNameValue,passwordValue,userIP);
                            if(user != null){
                                if(user.getCode() != 1){
                                    //登录成功!!保存用户名和密码
                                    editor.putString("IP", userIP);
                                    editor.putString("USER_NAME", userNameValue);
                                    editor.putString("PASSWORD", passwordValue);
                                    TotalUrl totalUrl = new TotalUrl();
                                    totalUrl.setsURLHEAD(userIP);
                                    totalUrl.setUser(user);
                                    totalUrl.setName(userNameValue);
                                    totalUrl.setPassWord(passwordValue);
                                    //是否记住密码
                                    if(remember.isChecked()){
                                        editor.putBoolean("remember", true);
                                    }else{
                                        editor.putBoolean("remember", false);
                                    }
                                    //是否自动登录
                                    if(autologin.isChecked()){
                                        editor.putBoolean("autologin", true);
                                    }else{
                                        editor.putBoolean("autologin", false);
                                    }
                                    editor.commit();
                                    if (user.getCode() == 0){
                                        List<SelectUser> getIDUser = UserHttp.thisUser(user);
                                        if (getIDUser== null||getIDUser.size() <1 ){
                                            return;
                                        }
                                        //跳转
                                        Intent intent =new Intent(Login.this,HomPage.class);
                                        intent.putExtra("getsS_NickName",getIDUser.get(0).getsS_NickName()+","+getIDUser.get(0).getsS_Account());
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ActivityUtil.showToasts(Login.this, R.string.PleaseReenter, 1*1000);
                                            }
                                        });
                                    }
                                }else{
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            SharedPreferences.Editor editor = sp.edit();
                                            editor.clear();
                                            editor.commit();
                                            ActivityUtil.showToasts(Login.this, R.string.PleaseReenter, 1*1000);
                                        }
                                    });
                                }
                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.clear();
                                        editor.commit();
                                        sp.edit().putBoolean("autologin",false).commit();
                                        sp.edit().putBoolean("remember",false).commit();
                                        ActivityUtil.showToasts(Login.this, R.string.PleaseReenter, 1*1000);
                                    }
                                });
                            }
                        }
                    }.start();
                }
            }
        });
    }

    //点击确定(防止误操作)
    public abstract class NoDoubleClickListener implements View.OnClickListener {
        public static final int MIN_CLICK_DELAY_TIME = 3000;
        private long lastClickTime = 0;

        @Override
        public void onClick(View v) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                onNoDoubleClick(v);
            }
        }
        protected abstract void onNoDoubleClick(View v);
    }

}
