package com.VeizagaTorrico.proyectotorneos.offline.model;

import com.google.gson.annotations.SerializedName;

public class FieldOff {

    @SerializedName("id")
    int id;
    @SerializedName("campo")
    String nombre;
    @SerializedName("predio")
    String predio;

    int competencia;

    public FieldOff(){}

    public FieldOff(int id, String nombre, String predio, int competencia) {
        this.id = id;
        this.nombre = nombre;
        this.predio = predio;
        this.competencia = competencia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPredio() {
        return predio;
    }

    public void setPredio(String predio) {
        this.predio = predio;
    }

    public int getCompetencia() {
        return competencia;
    }

    public void setCompetencia(int competencia) {
        this.competencia = competencia;
    }
}
