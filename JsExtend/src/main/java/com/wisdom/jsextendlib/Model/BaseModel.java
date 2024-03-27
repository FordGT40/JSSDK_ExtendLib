package com.wisdom.jsextendlib.Model;

/**
 * Created by Tsing on 2020/8/19
 */
public class BaseModel {
    public String msg;
    public String idCardHeadImgBase64;
    public int code;
    public Object data;

    public BaseModel(String msg, int code, Object data) {
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public BaseModel(String msg, String idCardHeadImgBase64, int code, Object data) {
        this.msg = msg;
        this.code = code;
        this.data = data;
        this.idCardHeadImgBase64 = idCardHeadImgBase64;
    }
}
