package com.VeizagaTorrico.proyectotorneos.models;

import com.google.gson.annotations.SerializedName;

public class RespRegisterService {

    @SerializedName("messaging")
    private String msg;

    public RespRegisterService(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return this.msg;
    }
}
