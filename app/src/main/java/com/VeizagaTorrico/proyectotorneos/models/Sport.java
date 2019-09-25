package com.VeizagaTorrico.proyectotorneos.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Sport implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("categorias")
    private List<Category> categories;


    public Sport(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.categories = new ArrayList<Category>();
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void addCategoria(Category category) {
        if (!this.categories.contains(category)) {
            this.categories.add(category);
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sport)) return false;
        Sport sport = (Sport) o;
        return id == sport.id &&
                nombre.equals(sport.nombre);
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
