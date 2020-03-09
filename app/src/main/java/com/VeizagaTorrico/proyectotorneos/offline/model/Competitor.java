package com.VeizagaTorrico.proyectotorneos.offline.model;

import com.google.gson.annotations.SerializedName;

public class Competitor {

    @SerializedName("id")
    int id;
    @SerializedName("alias")
    String alias;
    @SerializedName("nombreUsuario")
    String usuario;
    @SerializedName("idCompetencia")
    int competencia;

    public Competitor(){}

    public Competitor(int id, String alias, String usuario, int competencia) {
        this.id = id;
        this.alias = alias;
        this.usuario = usuario;
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
}
