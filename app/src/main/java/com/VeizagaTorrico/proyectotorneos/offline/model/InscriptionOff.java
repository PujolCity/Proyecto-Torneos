package com.VeizagaTorrico.proyectotorneos.offline.model;

import com.google.gson.annotations.SerializedName;

public class InscriptionOff {

    @SerializedName("id")
    int id;
    @SerializedName("fechaIni")
    String finicio;
    @SerializedName("fechaCierre")
    String fcierre;
    @SerializedName("monto")
    int monto;
    @SerializedName("requisitos")
    String requisitos;
    @SerializedName("idCompetencia")
    int competencia;

    public InscriptionOff(){}

    public InscriptionOff(int id, String finicio, String fcierre, int monto, String requisitos, int competencia) {
        this.id = id;
        this.finicio = finicio;
        this.fcierre = fcierre;
        this.monto = monto;
        this.requisitos = requisitos;
        this.competencia = competencia;
    }

//    public Inscription(int id, String finicio, String fcierre, int monto, String requisitos, Competition competencia) {
//        this.id = id;
//        this.finicio = finicio;
//        this.fcierre = fcierre;
//        this.monto = monto;
//        this.requisitos = requisitos;
//        this.competencia = competencia;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFinicio() {
        return finicio;
    }

    public void setFinicio(String finicio) {
        this.finicio = finicio;
    }

    public String getFcierre() {
        return fcierre;
    }

    public void setFcierre(String fcierre) {
        this.fcierre = fcierre;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    public String getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(String requisitos) {
        this.requisitos = requisitos;
    }

    public int getCompetencia() {
        return competencia;
    }

    public void setCompetencia(int competencia) {
        this.competencia = competencia;
    }

//    public Competition getCompetencia() {
//        return competencia;
//    }
//
//    public void setCompetencia(Competition competencia) {
//        this.competencia = competencia;
//    }
}
