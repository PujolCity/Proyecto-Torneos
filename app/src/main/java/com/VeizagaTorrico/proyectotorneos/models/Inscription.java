package com.VeizagaTorrico.proyectotorneos.models;

import com.google.gson.annotations.SerializedName;

public class Inscription {

    @SerializedName("idCompetencia ")
    private int idCompetencia;

    @SerializedName("fechaInicio")
    private String fechaInicio;

    @SerializedName("fechaCierre")
    private String fechaCierre;

    @SerializedName("requisitos")
    private String requisitos;

    @SerializedName("monto")
    private int monto;

    public Inscription(int idCompetencia, String fechaInicio, String fechaCierre, String requisitos, int monto) {
        this.idCompetencia = idCompetencia;
        this.fechaInicio = fechaInicio;
        this.fechaCierre = fechaCierre;
        this.requisitos = requisitos;
        this.monto = monto;
    }

    public int getIdCompetencia() {
        return idCompetencia;
    }

    public void setIdCompetencia(int idCompetencia) {
        this.idCompetencia = idCompetencia;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(String fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public String getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(String requisitos) {
        this.requisitos = requisitos;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    @Override
    public String toString() {
        return idCompetencia +
                "\n" + fechaInicio +
                "\n" + fechaCierre +
                "\n" + requisitos +
                "\n" + monto ;
    }
}
