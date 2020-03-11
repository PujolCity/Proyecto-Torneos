package com.VeizagaTorrico.proyectotorneos.offline.model;

import com.google.gson.annotations.SerializedName;

public class JudgeOff {

    @SerializedName("id")
    int id;
    @SerializedName("nombre")
    String nombre;
    @SerializedName("apellido")
    String apellido;
    @SerializedName("dni")
    String dni;
    @SerializedName("idCompetencia")
    int competencia;

    public JudgeOff(){}

    public JudgeOff(int id, String nombre, String apellido, String dni, int competencia) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.competencia = competencia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public int getCompetencia() {
        return competencia;
    }

    public void setCompetencia(int competencia) {
        this.competencia = competencia;
    }
}
