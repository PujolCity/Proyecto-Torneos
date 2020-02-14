package com.VeizagaTorrico.proyectotorneos.models;

import com.google.gson.annotations.SerializedName;

public class Invitation {

    @SerializedName("idInvitacion")
    private int id;

    @SerializedName("nombreOrg")
    private String organizador;

    @SerializedName("nombreComp")
    private String competencia;

    @SerializedName("categoria")
    private String categoria;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrganizador() {
        return organizador;
    }

    public void setOrganizador(String organizador) {
        this.organizador = organizador;
    }

    public String getCompetencia() {
        return competencia;
    }

    public void setCompetencia(String competencia) {
        this.competencia = competencia;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return organizador + ' ' +competencia + ' ' + categoria ;
    }
}
