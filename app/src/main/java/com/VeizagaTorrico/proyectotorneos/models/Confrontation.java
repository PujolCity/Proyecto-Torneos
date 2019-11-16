package com.VeizagaTorrico.proyectotorneos.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Confrontation implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("competidor1")
    private String competidor1;

    @SerializedName("competidor2")
    private String competidor2;

    @SerializedName("grupo")
    private int grupo;

    @SerializedName("jornada")
    private String jornada;

    private String juez;

    private String campo;

    private String turno;

    private String resultadoComp1;

    private String resultadoComp2;

    private int idCompetencia;

    public Confrontation(int id, String competidor1, String competidor2, int grupo, String jornada) {
        this.id = id;
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

    public String getCompetidor1() {
        return competidor1;
    }

    public void setCompetidor1(String competidor1) {
        this.competidor1 = competidor1;
    }

    public String getCompetidor2() {
        return competidor2;
    }

    public void setCompetidor2(String competidor2) {
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

    public int getIdCompetencia() {
        return idCompetencia;
    }

    public void setIdCompetencia(int idCompetencia) {
        this.idCompetencia = idCompetencia;
    }

    public String getJuez() {
        return juez;
    }

    public void setJuez(String juez) {
        this.juez = juez;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getResultadoComp1() {
        return resultadoComp1;
    }

    public void setResultadoComp1(String resultadoComp1) {
        this.resultadoComp1 = resultadoComp1;
    }

    public String getResultadoComp2() {
        return resultadoComp2;
    }

    public void setResultadoComp2(String resultadoComp2) {
        this.resultadoComp2 = resultadoComp2;
    }

    @Override
    public String toString() {
        return id + "  " + competidor1 +
                "  " + competidor2;
    }
}
