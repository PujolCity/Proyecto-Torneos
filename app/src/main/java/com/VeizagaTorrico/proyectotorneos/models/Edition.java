package com.VeizagaTorrico.proyectotorneos.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class Edition implements Serializable {

    @SerializedName("operacion")
    private String operacion;

    @SerializedName("editor")
    private String editor;

    @SerializedName("fecha")
    private String fecha;

    public Edition(String operacion, String editor, String fecha) {
        this.operacion = operacion;
        this.editor = editor;
        this.fecha = fecha;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
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
        return operacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edition)) return false;
        Edition edicion = (Edition) o;
        return Objects.equals(operacion, edicion.operacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operacion);
    }
}
