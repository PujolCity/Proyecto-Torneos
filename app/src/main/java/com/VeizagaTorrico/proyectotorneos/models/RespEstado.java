package com.VeizagaTorrico.proyectotorneos.models;

import com.google.gson.annotations.SerializedName;

public class RespEstado {

    @SerializedName("estado")
    private String estado;

    public RespEstado(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String msg) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return estado;
    }
}
