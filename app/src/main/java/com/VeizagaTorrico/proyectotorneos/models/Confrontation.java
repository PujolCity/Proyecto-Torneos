package com.VeizagaTorrico.proyectotorneos.models;

import com.google.gson.annotations.SerializedName;

public class Confrontation {

    @SerializedName("id")
    private int id;

    @SerializedName("competencia_id")
    private Competition competencia;

    @SerializedName("compuser1_id")
    private User competidor1;

    @SerializedName("compuser2_id")
    private User competidor2;

    @SerializedName("grupo")
    private int grupo;

    @SerializedName("jornada")
    private String jornada;


    public Confrontation(int id, Competition competencia, User competidor1, User competidor2, int grupo, String jornada) {
        this.id = id;
        this.competencia = competencia;
        this.competidor1 = competidor1;
        this.competidor2 = competidor2;
        this.grupo = grupo;
        this.jornada = jornada;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Competition getCompetencia() {
        return competencia;
    }

    public void setCompetencia(Competition competencia) {
        this.competencia = competencia;
    }

    public User getCompetidor1() {
        return competidor1;
    }

    public void setCompetidor1(User competidor1) {
        this.competidor1 = competidor1;
    }

    public User getCompetidor2() {
        return competidor2;
    }

    public void setCompetidor2(User competidor2) {
        this.competidor2 = competidor2;
    }

    public int getGrupo() {
        return grupo;
    }

    public void setGrupo(int grupo) {
        this.grupo = grupo;
    }

    public String getJornada() {
        return jornada;
    }

    public void setJornada(String jornada) {
        this.jornada = jornada;
    }

    @Override
    public String toString() {
        return id +" " + competencia + "  " + competidor1 +
                "  " + competidor2 +"  " + grupo +"  " + jornada;
    }
}
