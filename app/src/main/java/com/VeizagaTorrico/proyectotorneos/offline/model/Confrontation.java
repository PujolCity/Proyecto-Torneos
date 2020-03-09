package com.VeizagaTorrico.proyectotorneos.offline.model;

import com.google.gson.annotations.SerializedName;

public class Confrontation {

    @SerializedName("id")
    int id;
    @SerializedName("grupo")
    String grupo;
    @SerializedName("rdoComp1")
    int rdo1;
    @SerializedName("rdoComp2")
    int rdo2;
    @SerializedName("jornada")
    String jornada;
    @SerializedName("juez")
    int idJuez;
    @SerializedName("campo")
    int idCampo;
    @SerializedName("turno")
    String turno;
    @SerializedName("competidor1")
    String comp1;
    @SerializedName("competidor2")
    String comp2;
    @SerializedName("fase")
    int fase;
    @SerializedName("fecha")
    String fecha;
    @SerializedName("idCompetencia")
    int competencia;

    public Confrontation(){}

    public Confrontation(int id, String grupo, int rdo1, int rdo2, String jornada, int idJuez, int idCampo, String turno, String comp1, String comp2, int fase, String fecha, int competencia) {
        this.id = id;
        this.grupo = grupo;
        this.rdo1 = rdo1;
        this.rdo2 = rdo2;
        this.jornada = jornada;
        this.idJuez = idJuez;
        this.idCampo = idCampo;
        this.turno = turno;
        this.comp1 = comp1;
        this.comp2 = comp2;
        this.fase = fase;
        this.fecha = fecha;
        this.competencia = competencia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
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

    public String getJornada() {
        return jornada;
    }

    public void setJornada(String jornada) {
        this.jornada = jornada;
    }

    public int getIdJuez() {
        return idJuez;
    }

    public void setIdJuez(int idJuez) {
        this.idJuez = idJuez;
    }

    public int getIdCampo() {
        return idCampo;
    }

    public void setIdCampo(int idCampo) {
        this.idCampo = idCampo;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getComp1() {
        return comp1;
    }

    public void setComp1(String comp1) {
        this.comp1 = comp1;
    }

    public String getComp2() {
        return comp2;
    }

    public void setComp2(String comp2) {
        this.comp2 = comp2;
    }

    public int getFase() {
        return fase;
    }

    public void setFase(int fase) {
        this.fase = fase;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getCompetencia() {
        return competencia;
    }

    public void setCompetencia(int competencia) {
        this.competencia = competencia;
    }
}
