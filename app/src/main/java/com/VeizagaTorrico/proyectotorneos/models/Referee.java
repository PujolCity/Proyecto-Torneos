package com.VeizagaTorrico.proyectotorneos.models;

import java.io.Serializable;

public class Referee implements Serializable {

    private int id;

    private String nombre;

    private String apellido;

    private int dni;

    private CompetitionMin competencia;


    public Referee(int id, String nombre, String apellido, int dni, CompetitionMin competencia) {
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

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public CompetitionMin getCompetencia() {
        return competencia;
    }

    public void setCompetencia(CompetitionMin competencia) {
        this.competencia = competencia;
    }

    @Override
    public String toString() {
        return  id +
                " " + nombre  +
                "" + apellido +
                " " + dni +
                " " + competencia.getName() ;
    }
}
