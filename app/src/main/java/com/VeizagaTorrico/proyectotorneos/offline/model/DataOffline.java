package com.VeizagaTorrico.proyectotorneos.offline.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataOffline {

    @SerializedName("competencia")
    Competition competencia;

    @SerializedName("competidores")
    List<Competitor> competidores;

    @SerializedName("fields")
    List<Field> campos;

    @SerializedName("judges")
    List<Judge> jueces;

    @SerializedName("inscription")
    Inscription inscripcion;

    public Competition getCompetencia() {
        return competencia;
    }

    public void setCompetencia(Competition competencia) {
        this.competencia = competencia;
    }

    public List<Competitor> getCompetidores() {
        return competidores;
    }

    public void setCompetidores(List<Competitor> competidores) {
        this.competidores = competidores;
    }

    public List<Field> getCampos() {
        return campos;
    }

    public void setCampos(List<Field> campos) {
        this.campos = campos;
    }

    public List<Judge> getJueces() {
        return jueces;
    }

    public void setJueces(List<Judge> jueces) {
        this.jueces = jueces;
    }

    public Inscription getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(Inscription inscripcion) {
        this.inscripcion = inscripcion;
    }

    @Override
    public String toString() {
        return "DataOffline: "+competencia.getNombre();
    }
}
