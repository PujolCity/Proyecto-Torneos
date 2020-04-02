package com.VeizagaTorrico.proyectotorneos.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConfrontationsCompetition {
    @SerializedName("encuentros")
    private List<Confrontation> encuentros;

    @SerializedName("libre")
    private String competidorLibre;

    public ConfrontationsCompetition(List<Confrontation> encuentros, String competitorFree) {
        this.encuentros = encuentros;
        this.competidorLibre = competitorFree;
    }

    public List<Confrontation> getEncuentros() {
        return encuentros;
    }

    public void setEncuentros(List<Confrontation> encuentros) {
        this.encuentros = encuentros;
    }

    public String getCompetidorLibre() {
        return competidorLibre;
    }

    public void setCompetidorLibre(String competidorLibre) {
        this.competidorLibre = competidorLibre;
    }

    @Override
    public String toString() {
        return "Cant de encuentros: "+encuentros.size() ;
    }
}
