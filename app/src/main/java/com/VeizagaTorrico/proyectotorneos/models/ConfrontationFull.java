package com.VeizagaTorrico.proyectotorneos.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ConfrontationFull implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("grupo")
    private int grupo;

    @SerializedName("competidor1")
    private Competitor competidor1;

    @SerializedName("competidor2")
    private Competitor competidor2;

    @SerializedName("rdoComp1")
    private int rdoc1;

    @SerializedName("rdoComp2")
    private int rdoc2;

    @SerializedName("juez")
    private Object juez;

    @SerializedName("campo")
    private Object campo;

    @SerializedName("turno")
    private Object turno;

    private int idCompetencia;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGrupo() {
        return grupo;
    }

    public void setGrupo(int grupo) {
        this.grupo = grupo;
    }

    public Competitor getCompetidor1() {
        return competidor1;
    }

    public void setCompetidor1(Competitor competidor1) {
        this.competidor1 = competidor1;
    }

    public Competitor getCompetidor2() {
        return competidor2;
    }

    public void setCompetidor2(Competitor competidor2) {
        this.competidor2 = competidor2;
    }

    public int getRdoc1() {
        return rdoc1;
    }

    public void setRdoc1(int rdoc1) {
        this.rdoc1 = rdoc1;
    }

    public int getRdoc2() {
        return rdoc2;
    }

    public void setRdoc2(int rdoc2) {
        this.rdoc2 = rdoc2;
    }

    public Object getJuez() {
        return juez;
    }

    public void setJuez(Object juez) {
        this.juez = juez;
    }

    public Object getCampo() {
        return campo;
    }

    public void setCampo(Object campo) {
        this.campo = campo;
    }

    public Object getTurno() {
        return turno;
    }

    public void setTurno(Object turno) {
        this.turno = turno;
    }

    public int getIdCompetencia() {
        return idCompetencia;
    }

    public void setIdCompetencia(int id) {
        this.idCompetencia = id;
    }
}
