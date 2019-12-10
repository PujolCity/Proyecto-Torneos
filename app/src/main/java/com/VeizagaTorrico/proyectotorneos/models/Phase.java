package com.VeizagaTorrico.proyectotorneos.models;

import com.google.gson.annotations.SerializedName;

public class Phase {

    @SerializedName("phase")
    private int fase;

    public Phase(int fase) {
        this.fase = fase;
    }

    public int getFase() {
        return fase;
    }

    public void setFase(int fase) {
        this.fase = fase;
    }

    @Override
    public String toString() {
        return "fase " + fase;
    }
}
