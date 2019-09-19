package com.VeizagaTorrico.proyectotorneos.models;

import com.google.gson.annotations.SerializedName;

public class Success {

    @SerializedName("existe")
    private boolean existe;

    public Success(boolean existe) {
        this.existe = existe;
    }

    public boolean isExiste() {
        return existe;
    }

    public void setExiste(boolean existe) {
        this.existe = existe;
    }

    @Override
    public String toString() {
        return String.valueOf(existe);
    }
}
