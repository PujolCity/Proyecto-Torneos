package com.VeizagaTorrico.proyectotorneos.models;

import com.google.gson.annotations.SerializedName;

public class ConfrontationMin {

    @SerializedName("competidor1")
    private Classified competidor1;

    @SerializedName("competidor2")
    private Classified competidor2;


    public ConfrontationMin(Classified competidor1, Classified competidor2) {
        this.competidor1 = competidor1;
        this.competidor2 = competidor2;
    }

    public Classified getCompetidor1() {
        return competidor1;
    }

    public void setCompetidor1(Classified competidor1) {
        this.competidor1 = competidor1;
    }

    public Classified getCompetidor2() {
        return competidor2;
    }

    public void setCompetidor2(Classified competidor2) {
        this.competidor2 = competidor2;
    }

    @Override
    public String toString() {
        return competidor1.getAlias() +
                " vs " + competidor2.getAlias();
    }

}
