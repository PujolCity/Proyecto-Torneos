package com.VeizagaTorrico.proyectotorneos.offline.model;

import com.google.gson.annotations.SerializedName;

public class ConfrontationEdit {

    @SerializedName("idEncuentro")
    int id;
    @SerializedName("rdo_comp1")
    int rdo1;
    @SerializedName("rdo_comp2")
    int rdo2;

    public ConfrontationEdit(){}

    public ConfrontationEdit(int id, int rdo1, int rdo2) {
        this.id = id;
        this.rdo1 = rdo1;
        this.rdo2 = rdo2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRdo1() {
        return rdo1;
    }

    public void setRdo1(int rdo1) {
        this.rdo1 = rdo1;
    }

    public int getRdo2() {
        return rdo2;
    }

    public void setRdo2(int rdo2) {
        this.rdo2 = rdo2;
    }

    @Override
    public String toString() {
        return "ConfrontationEdit{" +
                "rdo1=" + rdo1 +
                ", rdo2=" + rdo2 +
                '}';
    }
}
