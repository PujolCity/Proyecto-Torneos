package com.VeizagaTorrico.proyectotorneos.models;

import com.google.gson.annotations.SerializedName;

public class ConfigNotification {

    @SerializedName("seguidor")
    private boolean seguidor;

    @SerializedName("competidor")
    private boolean competidor;

    public ConfigNotification(boolean seguidor, boolean competidor) {
        this.seguidor = seguidor;
        this.competidor = competidor;
    }

    public boolean isSeguidor() {
        return seguidor;
    }

    public void setSeguidor(boolean seguidor) {
        this.seguidor = seguidor;
    }

    public boolean isCompetidor() {
        return competidor;
    }

    public void setCompetidor(boolean competidor) {
        this.competidor = competidor;
    }

    @Override
    public String toString() {

        return "{seguidor: "+String.valueOf(seguidor)+" - competidor: "+String.valueOf(competidor);
    }
}
