package com.VeizagaTorrico.proyectotorneos.offline.model;

import com.google.gson.annotations.SerializedName;

public class CompetitionOff {

    @SerializedName("id")
    int id;
    @SerializedName("nombre")
    String nombre;
    @SerializedName("categoria")
    String categoria;
    @SerializedName("organizacion")
    String organizacion;
    @SerializedName("fecha_ini")
    String fecha_ini;
    @SerializedName("genero")
    String genero;
    @SerializedName("frec_dias")
    String frecuencia;
    @SerializedName("ciudad")
    String ciudad;
    @SerializedName("estado")
    String estado;
    @SerializedName("rol")
    String[] rol;
    @SerializedName("fases")
    String[] fases;

    public CompetitionOff(){}

    public CompetitionOff(int id, String nombre, String categoria, String organizacion, String fecha_ini, String genero, String frecuencia, String ciudad, String estado, String[] rol, String[] fases) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.organizacion = organizacion;
        this.fecha_ini = fecha_ini;
        this.genero = genero;
        this.frecuencia = frecuencia;
        this.ciudad = ciudad;
        this.estado = estado;
        this.rol = rol;
        this.fases = fases;
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getOrganizacion() {
        return organizacion;
    }

    public void setOrganizacion(String organizacion) {
        this.organizacion = organizacion;
    }

    public String getFecha_ini() {
        return fecha_ini;
    }

    public void setFecha_ini(String fecha_ini) {
        this.fecha_ini = fecha_ini;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String[] getRol() {
        return rol;
    }

    public void setRol(String[] rol) {
        this.rol = rol;
    }

    public String[] getFases() {
        return fases;
    }

    public void setFases(String[] fases) {
        this.fases = fases;
    }
}
