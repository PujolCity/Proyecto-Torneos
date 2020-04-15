package com.VeizagaTorrico.proyectotorneos.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class Edition implements Serializable {

    @SerializedName("tipo")
    private String tipo;

    @SerializedName("editor")
    private String editor;

    @SerializedName("fecha")
    private String fecha;

    public Edition(String tipo, String editor, String fecha) {
        this.tipo = tipo;
        this.editor = editor;
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return tipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edition)) return false;
        Edition edicion = (Edition) o;
        return Objects.equals(tipo, edicion.tipo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tipo);
    }
}
