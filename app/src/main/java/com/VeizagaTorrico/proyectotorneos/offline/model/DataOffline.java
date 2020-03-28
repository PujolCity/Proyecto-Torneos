package com.VeizagaTorrico.proyectotorneos.offline.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataOffline {

    @SerializedName("competencia")
    CompetitionOff competencia;

    @SerializedName("competidores")
    List<CompetitorOff> competidores;

    @SerializedName("fields")
    List<FieldOff> campos;

    @SerializedName("judges")
    List<JudgeOff> jueces;

    @SerializedName("inscription")
    InscriptionOff inscripcion;

    @SerializedName("fases")
    String[] fases;

    public CompetitionOff getCompetencia() {
        return competencia;
    }

    public void setCompetencia(CompetitionOff competencia) {
        this.competencia = competencia;
    }

    public List<CompetitorOff> getCompetidores() {
        return competidores;
    }

    public void setCompetidores(List<CompetitorOff> competidores) {
        this.competidores = competidores;
    }

    public List<FieldOff> getCampos() {
        return campos;
    }

    public void setCampos(List<FieldOff> campos) {
        this.campos = campos;
    }

    public List<JudgeOff> getJueces() {
        return jueces;
    }

    public void setJueces(List<JudgeOff> jueces) {
        this.jueces = jueces;
    }

    public InscriptionOff getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(InscriptionOff inscripcion) {
        this.inscripcion = inscripcion;
    }

    public String[] getFases() {
        return fases;
    }

    public void setFases(String[] fases) {
        this.fases = fases;
    }

    @Override
    public String toString() {
        return "DataOffline: "+competencia.getNombre();
    }
}
