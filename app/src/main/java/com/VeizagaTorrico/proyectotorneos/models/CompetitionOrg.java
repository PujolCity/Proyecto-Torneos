package com.VeizagaTorrico.proyectotorneos.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CompetitionOrg implements Serializable{

    @SerializedName("cant_grupo")
    private int cantGrupos;

    @SerializedName("cant_jornada")
    private int cantJornadas;

    @SerializedName("fases")
    private String[] cantFases;

    @SerializedName("messaging")
    private String msg;

    public CompetitionOrg(int grupos , int jornadas, String[] fases, String msg) {
        this.cantGrupos = grupos;
        this.cantJornadas = jornadas;
        this.cantFases = fases;
        this.msg = msg;
    }

    public int getCantGrupos() {
        return cantGrupos;
    }

    public void setCantGrupos(int cantGrupos) {
        this.cantGrupos = cantGrupos;
    }

    public int getCantJornadas() {
        return cantJornadas;
    }

    public void setCantJornadas(int cantJornadas) {
        this.cantJornadas = cantJornadas;
    }

    public String[] getCantFases() {
        return cantFases;
    }

    public void setCantFases(String[] cantFases) {
        this.cantFases = cantFases;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Cant grupos: " + cantGrupos +" - Cant jornada: "+ cantJornadas + " - Cant de fase: "+cantFases.length+" Mje: "+  msg;
    }
}
