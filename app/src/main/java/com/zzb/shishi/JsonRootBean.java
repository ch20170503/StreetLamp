package com.zzb.shishi;

/**
 * Created by Administrator on 2018/6/5 0005.
 */

public class JsonRootBean {
    private int code;
    private String msg;
    private Data data;
    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public Data getData() {
        return data;
    }
}
