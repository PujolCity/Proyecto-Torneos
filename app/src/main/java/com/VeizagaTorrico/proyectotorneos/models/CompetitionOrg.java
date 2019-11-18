package com.VeizagaTorrico.proyectotorneos.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CompetitionOrg implements Serializable{

        @SerializedName("cant_grupo")
        private int n_grupos;

        @SerializedName("cant_jornada")
        private int n_jornadas;

        @SerializedName("messaging")
        private String msg;

        public CompetitionOrg(int grupos , int jornadas, String msg) {
            this.n_grupos = grupos;
            this.n_jornadas = jornadas;
            this.msg = msg;
        }

    public int getN_grupos() {
        return n_grupos;
    }

    public void setN_grupos(int n_grupos) {
        this.n_grupos = n_grupos;
    }

    public int getN_jornadas() {
        return n_jornadas;
    }

    public void setN_jornadas(int n_jornadas) {
        this.n_jornadas = n_jornadas;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Cant grupos: " + n_grupos +" - Cant jornada: "+ n_jornadas +" Mje: "+  msg;
    }
}
