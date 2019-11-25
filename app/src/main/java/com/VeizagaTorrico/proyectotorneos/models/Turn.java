package com.VeizagaTorrico.proyectotorneos.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Turn implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("idCompetencia")
    private int idCompetencia;

    @SerializedName("hora_desde")
    private String horaDesde;

    @SerializedName("hora_hasta")
    private String horaHasta;

    public Turn(int id, int idCompetencia, String horaDesde, String horaHasta) {
        this.id = id;
        this.idCompetencia = idCompetencia;
        this.horaDesde = horaDesde;
        this.horaHasta = horaHasta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCompetencia() {
        return idCompetencia;
    }

    public void setIdCompetencia(int idCompetencia) {
        this.idCompetencia = idCompetencia;
    }

    public String getHoraDesde() {
        return horaDesde;
    }

    public void setHoraDesde(String horaDesde) {
        this.horaDesde = horaDesde;
    }

    public String getHoraHasta() {
        return horaHasta;
    }

    public void setHoraHasta(String horaHasta) {
        this.horaHasta = horaHasta;
    }

    @Override
    public String toString() {
        return "Desde : " + horaDesde + '-' +
                " Hasta : " + horaHasta ;
    }
}
