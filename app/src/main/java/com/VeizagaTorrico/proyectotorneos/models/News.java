package com.VeizagaTorrico.proyectotorneos.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Timestamp;


public class News implements Serializable {

    @SerializedName("competencia")
    private String competencia;

    @SerializedName("title")
    private String titulo;

    @SerializedName("resume")
    private String subtitulo;

    @SerializedName("descripcion")
    private String cuerpo;

    @SerializedName("uptime")
    private Timestamp uptime;

    public News(String competencia, String titulo, String subtitulo, String cuerpo, Timestamp uptime) {
        this.competencia = competencia;
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.cuerpo = cuerpo;
        this.uptime = uptime;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public String getCompetencia() {
        return competencia;
    }

    public void setCompetencia(String competencia) {
        this.competencia = competencia;
    }

    public Timestamp getUptime() {
        return uptime;
    }

    public void setUptime(Timestamp uptime) {
        this.uptime = uptime;
    }

    @Override
    public String toString() {
        return competencia + ' ' + titulo + ' '
                + subtitulo + ' ' + cuerpo + ' ' +
                 uptime ;
    }
}
