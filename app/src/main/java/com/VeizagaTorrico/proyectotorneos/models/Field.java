package com.VeizagaTorrico.proyectotorneos.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class Field implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("capacidad")
    private int capacidad;

    @SerializedName("dimensiones")
    private int dimensiones;

    @SerializedName("predio")
    private Ground predio;


    public Field(int id, String nombre, int capacidad, int dimensiones, Ground predio) {
        this.id = id;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.dimensiones = dimensiones;
        this.predio = predio;
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

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getDimensiones() {
        return dimensiones;
    }

    public void setDimensiones(int dimensiones) {
        this.dimensiones = dimensiones;
    }

    public Ground getPredio() {
        return predio;
    }

    public void setPredio(Ground predio) {
        this.predio = predio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Field)) return false;
        Field field = (Field) o;
        return id == field.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return nombre;
    }
}
