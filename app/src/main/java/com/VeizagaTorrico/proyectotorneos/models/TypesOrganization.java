package com.VeizagaTorrico.proyectotorneos.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class TypesOrganization implements Serializable {
    @SerializedName("id")
    private int id;

    @SerializedName("nombre")
    private String name;

    @SerializedName("descripcion")
    private String description;

    public TypesOrganization(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TypesOrganization)) return false;
        TypesOrganization that = (TypesOrganization) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name;
    }
}
