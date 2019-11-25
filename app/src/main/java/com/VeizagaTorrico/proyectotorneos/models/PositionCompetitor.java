package com.VeizagaTorrico.proyectotorneos.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PositionCompetitor implements Serializable {

    @SerializedName("alias")
    private String competidor;

    @SerializedName("PJ")
    private String jugados;

    @SerializedName("PG")
    private String ganados;

    @SerializedName("PE")
    private String empatados;

    @SerializedName("PP")
    private String perdidos;

    @SerializedName("Pts")
    private String puntos;

    public PositionCompetitor(String competidor, String jugados, String ganados, String empatados, String perdidos, String puntos) {
        this.competidor = competidor;
        this.jugados = jugados;
        this.ganados = ganados;
        this.empatados = empatados;
        this.perdidos = perdidos;
        this.puntos = puntos;
    }

    public String getCompetidor() {
        return competidor;
    }

    public void setCompetidor(String competidor) {
        this.competidor = competidor;
    }

    public String getJugados() {
        return jugados;
    }

    public void setJugados(String jugados) {
        this.jugados = jugados;
    }

    public String getGanados() {
        return ganados;
    }

    public void setGanados(String ganados) {
        this.ganados = ganados;
    }

    public String getEmpatados() {
        return empatados;
    }

    public void setEmpatados(String empatados) {
        this.empatados = empatados;
    }

    public String getPerdidos() {
        return perdidos;
    }

    public void setPerdidos(String perdidos) {
        this.perdidos = perdidos;
    }

    public String getPuntos() {
        return puntos;
    }

    public void setPuntos(String puntos) {
        this.puntos = puntos;
    }

    @Override
    public String toString() {
        return "Competidor " + this.competidor + " - Pts: " + this.puntos;
    }
}
