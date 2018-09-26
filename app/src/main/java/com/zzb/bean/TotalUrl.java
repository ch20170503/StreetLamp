package com.zzb.bean;

/**
 * Created by Administrator on 2017/8/14 0014.
 */

public class TotalUrl {
    public static String sURLHEAD = "http://iot-leyview.com:8011";

    public static String getsURLHEAD() {
        return sURLHEAD;
    }

    public static void setsURLHEAD(String sURLHEAD) {
        TotalUrl.sURLHEAD = sURLHEAD;
    }

    public static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        TotalUrl.user = user;
    }


    //用户名
    public static String PassWord;
    public static String Name;

    public static String getPassWord() {
        return PassWord;
    }

    public static void setPassWord(String passWord) {
        PassWord = passWord;
    }

    public static String getName() {
        return Name;
    }

    public static void setName(String name) {
        Name = name;
    }
}
