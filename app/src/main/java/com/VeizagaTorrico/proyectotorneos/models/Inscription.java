package com.VeizagaTorrico.proyectotorneos.models;

import com.google.gson.annotations.SerializedName;

public class Inscription {

    @SerializedName("id ")
    private int id;

    @SerializedName("fechaIni")
    private String fechaInicio;

    @SerializedName("fechaCierre")
    private String fechaCierre;

    @SerializedName("requisitos")
    private String requisitos;

    @SerializedName("monto")
    private int monto;

    public Inscription(int id, String fechaInicio, String fechaCierre, String requisitos, int monto) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.fechaCierre = fechaCierre;
        this.requisitos = requisitos;
        this.monto = monto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return id +
                "\n" + fechaInicio +
                "\n" + fechaCierre +
                "\n" + requisitos +
                "\n" + monto ;
    }
}
