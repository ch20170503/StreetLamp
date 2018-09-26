package com.zzb.bean;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/8/16 0016.
 */

public class AndroidUserInfo {
    private int id ;
    private String name;
    private Bitmap imphoto;

    public AndroidUserInfo() {
    }

    public AndroidUserInfo(int id, String name, Bitmap imphoto) {
        this.id = id;
        this.name = name;
        this.imphoto = imphoto;
    }

    public AndroidUserInfo(String name, Bitmap imphoto) {
        this.name = name;
        this.imphoto = imphoto;
    }

    @Override
    public String toString() {
        return "AndroidUserInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imphoto=" + imphoto +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImphoto() {
        return imphoto;
    }

    public void setImphoto(Bitmap imphoto) {
        this.imphoto = imphoto;
    }
}
