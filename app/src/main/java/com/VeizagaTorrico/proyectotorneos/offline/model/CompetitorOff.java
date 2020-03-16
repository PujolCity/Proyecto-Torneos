package com.VeizagaTorrico.proyectotorneos.offline.model;

import com.google.gson.annotations.SerializedName;

public class CompetitorOff {

    @SerializedName("id")
    int id;
    @SerializedName("alias")
    String alias;
    @SerializedName("nombreUsuario")
    String usuario;
    @SerializedName("nombre")
    String nombre;
    @SerializedName("apellido")
    String apellido;
    @SerializedName("correo")
    String correo;
    @SerializedName("idCompetencia")
    int competencia;

    public CompetitorOff(){}

    public CompetitorOff(int id, String alias, String usuario, String nombre, String apellido, String correo, int competencia) {
        this.id = id;
        this.alias = alias;
        this.usuario = usuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.competencia = competencia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getCompetencia() {
        return competencia;
    }

    public void setCompetencia(int competencia) {
        this.competencia = competencia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
