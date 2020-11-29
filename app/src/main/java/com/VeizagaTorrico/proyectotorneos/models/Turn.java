package com.VeizagaTorrico.proyectotorneos.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Turn implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("idCompetencia")
    private int idCompetencia;

    @SerializedName("horaDesde")
    private String horaDesde;

    @SerializedName("horaHasta")
    private String horaHasta;

    @SerializedName("hora_desde")
    private String hora_desde;

    @SerializedName("hora_hasta")
    private String hora_hasta;

    public Turn(){

    }

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

    public String getHora_desde() {
        return hora_desde;
    }

    public void setHora_desde(String hora_desde) {
        this.hora_desde = hora_desde;
    }

    public String getHora_hasta() {
        return hora_hasta;
    }

    public void setHora_hasta(String hora_hasta) {
        this.hora_hasta = hora_hasta;
    }

    public boolean vacio(){
        if(this.horaHasta == null || this.horaDesde == null)
            return true;
        return false;
    }

    @Override
    public String toString() {
        // esto es un parche dado que a la hora de realizar los servicios se trabajan con 2 nombres de campo distintos
        if(horaDesde == null){
            return hora_desde + " - " + hora_hasta;
        }else
            return parsearHora();
    }

    public String parsearHora() {
        return horaDesde + " - " + horaHasta ;
    }
}
