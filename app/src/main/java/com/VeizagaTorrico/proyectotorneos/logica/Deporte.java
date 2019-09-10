package com.VeizagaTorrico.proyectotorneos.logica;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Deporte {

    private int id;
    private String nombre;
    private List<Categoria> categorias;


    public Deporte(int id,String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.categorias = new ArrayList<Categoria>();
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }
    public void addCategoria(Categoria categoria) {
        if (!this.categorias.contains(categoria)) {
            this.categorias.add(categoria);
        }
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Deporte)) return false;
        Deporte deporte = (Deporte) o;
        return id == deporte.id &&
                nombre.equals(deporte.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return nombre ;
    }
}
