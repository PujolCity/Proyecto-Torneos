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

    @SerializedName("rdoComp1")
    private int rdoc1;

    @SerializedName("rdoComp2")
    private int rdoc2;

    @SerializedName("juez")
    private Referee juez;

    @SerializedName("campo")
    private Field campo;

    @SerializedName("turno")
    private Turn turno;

    private int idCompetencia;

    public Confrontation(int id, String competidor1, String competidor2, int rdoc1, int rdoc2, Referee juez, Field campo) {
        this.id = id;
        this.competidor1 = competidor1;
        this.competidor2 = competidor2;
        this.rdoc1 = rdoc1;
        this.rdoc2 = rdoc2;
        this.juez = juez;
        this.campo = campo;
        this.turno = turno;
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

    public int getRdoc1() {
        return rdoc1;
    }

    public void setRdoc1(int rdoc1) {
        this.rdoc1 = rdoc1;
    }

    public int getRdoc2() {
        return rdoc2;
    }

    public void setRdoc2(int rdoc2) {
        this.rdoc2 = rdoc2;
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

    public int getIdCompetencia() {
        return idCompetencia;
    }

    public void setIdCompetencia(int idCompetencia) {
        this.idCompetencia = idCompetencia;
    }

    @Override
    public String toString() {
        return id + "  " + competidor1 +
                "  " + competidor2 + " " + rdoc1 + " "+ rdoc2;
    }
}
