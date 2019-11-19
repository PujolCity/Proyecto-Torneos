package com.VeizagaTorrico.proyectotorneos.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Confrontation implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("competidor1")
    private String competidor1;

    @SerializedName("competidor2")
    private String competidor2;

    @SerializedName("juez")
    private Referee juez;

    @SerializedName("campo")
    private Field campo;

    @SerializedName("turno")
    private Turn turno;

    @SerializedName("rdoComp1")
    private int resultadoComp1;

    @SerializedName("rdoComp2")
    private int resultadoComp2;

    private int idCompetencia;

    public Confrontation(int id, String competidor1, String competidor2) {
        this.id = id;
        this.competidor1 = competidor1;
        this.competidor2 = competidor2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompetidor1() {
        return competidor1;
    }

    public void setCompetidor1(String competidor1) {
        this.competidor1 = competidor1;
    }

    public String getCompetidor2() {
        return competidor2;
    }

    public void setCompetidor2(String competidor2) {
        this.competidor2 = competidor2;
    }

    public int getIdCompetencia() {
        return idCompetencia;
    }

    public void setIdCompetencia(int idCompetencia) {
        this.idCompetencia = idCompetencia;
    }

    public Referee getJuez() {
        return juez;
    }

    public void setJuez(Referee juez) {
        this.juez = juez;
    }

    public Field getCampo() {
        return campo;
    }

    public void setCampo(Field campo) {
        this.campo = campo;
    }

    public Turn getTurno() {
        return turno;
    }

    public void setTurno(Turn turno) {
        this.turno = turno;
    }

    public int getResultadoComp1() {
        return resultadoComp1;
    }

    public void setResultadoComp1(int resultadoComp1) {
        this.resultadoComp1 = resultadoComp1;
    }

    public int getResultadoComp2() {
        return resultadoComp2;
    }

    public void setResultadoComp2(int resultadoComp2) {
        this.resultadoComp2 = resultadoComp2;
    }

    @Override
    public String toString() {
        return id + "  " + competidor1 +
                "  " + competidor2;
    }
}
