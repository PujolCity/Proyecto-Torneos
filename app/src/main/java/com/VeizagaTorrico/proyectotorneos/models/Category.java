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

    @SerializedName("deporte")
    private String sport;

    public Category(int id, String nombreCat, String descripcion, int minIntegrantes, String sport) {
        this.id = id;
        this.nombreCat = nombreCat;
        this.descripcion = descripcion;
        this.minIntegrantes = minIntegrantes;
        this.sport = sport;
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

    public String getSport(){
        return this.sport;
    }

    public void setSport(String sport){
        this.sport = sport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return id == category.id &&
                nombreCat.equals(category.nombreCat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public String toString() {
        return nombreCat ;
    }

}
