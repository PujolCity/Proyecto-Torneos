package com.VeizagaTorrico.proyectotorneos.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class Category implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("nombre")
    private String nombreCat;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("minIntegrantes")
    private int minIntegrantes;

    @SerializedName("duracionDefault")
    private int duracion;

    public Category(int id, String nombreCat, String descripcion, int minIntegrantes, int duracion) {
        this.id = id;
        this.nombreCat = nombreCat;
        this.descripcion = descripcion;
        this.minIntegrantes = minIntegrantes;
        this.duracion = duracion;
    }

    public int getId() {
        return id;
    }

    public String getNombreCat() {
        return nombreCat;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getMinIntegrantes() {
        return minIntegrantes;
    }

    public void setNombreCat(String nombreCat) {
        this.nombreCat = nombreCat;
    }

    public void setMinIntegrantes(int minIntegrantes) {
        this.minIntegrantes = minIntegrantes;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    @Override
    public String toString() {
        return nombreCat ;
    }

}
