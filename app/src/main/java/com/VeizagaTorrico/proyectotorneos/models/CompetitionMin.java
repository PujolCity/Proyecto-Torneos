package com.VeizagaTorrico.proyectotorneos.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class CompetitionMin implements Serializable {

        @SerializedName("id")
        private int id;

        @SerializedName("nombre")
        private String name;

        @SerializedName("fecha_ini")
        private String fechaIni;

        @SerializedName("deporte")
        private String deporte;

        @SerializedName("categoria")
        private String category;

        @SerializedName("frecuencia")
        private String frecuencia;

        @SerializedName("tipo_organizacion")
        private String typesOrganization;

        @SerializedName("ciudad")
        private String ciudad;

        @SerializedName("genero")
        private String genero;

        @SerializedName("estado")
        private String estado;

        @SerializedName("fase_actual")
        private String faseActual;

        @SerializedName("rol")
        private List<String> rol;

        @SerializedName("duracionDefault")
        private int duracion;



    public CompetitionMin(int id, String name, String deporte, String category, String typesOrganization, String ciudad, String frecuencia, String estado, String genero, List<String> rol, int duracion) {
            this.id = id;
            this.name = name;
            this.deporte = deporte;
            this.category = category;
            this.typesOrganization = typesOrganization;
            this.ciudad = ciudad;
            this.genero = genero;
            this.estado = estado;
            this.rol = rol;
            this.frecuencia = frecuencia;
            this.duracion = duracion;
        }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTypesOrganization() {
        return typesOrganization;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFaseActual() {
        return faseActual;
    }

    public void setFaseActual(String faseActual) {
        this.faseActual = faseActual;
    }

    public String getDeporte() {
            return deporte;
        }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public List<String> getRol() {
        return rol;
    }

    public void setRol(List<String> rol) {
        this.rol = rol;
    }

    public void setTypesOrganization(String typesOrganization) {
        this.typesOrganization = typesOrganization;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public int getDuracion() {
            return duracion;
        }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(String fechaIni) {
        this.fechaIni = fechaIni;
    }

    @Override
    public String toString() {
        return  id + " "+ name + " " + deporte + " " + category  + " " + typesOrganization + " " + ciudad + " " + genero + " " + estado
                 + " FASE: "+ faseActual + " " + rol + " "+ duracion ;
    }
}


