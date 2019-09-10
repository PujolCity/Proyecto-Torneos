package com.VeizagaTorrico.proyectotorneos.logica;

import java.util.Objects;

public class Categoria {
    private int id;
    private String nombreCat;
    private String descripcion;
    private int minIntegrantes;


    public Categoria(int id, String nombreCat, String descripcion, int minIntegrantes) {
        this.id = id;
        this.nombreCat = nombreCat;
        this.descripcion = descripcion;
        this.minIntegrantes = minIntegrantes;
    }

    public int getId() {
        return id;
    }

    public String getNombreCat() {
        return nombreCat;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getMinIntegrantes() {
        return minIntegrantes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categoria)) return false;
        Categoria categoria = (Categoria) o;
        return id == categoria.id &&
                nombreCat.equals(categoria.nombreCat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public String toString() {
        return nombreCat ;
    }

}
