package com.VeizagaTorrico.proyectotorneos.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;

import androidx.recyclerview.widget.RecyclerView;


public class Competition implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("nombre")
    private String name;


    private Calendar initDate;

    private Calendar endDate;

    @SerializedName("categoria")
    private Category category;

  //  @SerializedName("organizacion")
    private TypesOrganization typesOrganization;

   /* @SerializedName("idUser")
    private int idUser;
*/
    public Competition() {
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public TypesOrganization getTypesOrganization() {
        return typesOrganization;
    }

    public void setTypesOrganization(TypesOrganization typesOrganization) {
        this.typesOrganization = typesOrganization;
    }
/*
    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
*/
    public Calendar getInitDate() {
        return initDate;
    }

    public void setInitDate(Calendar initDate) {
        this.initDate = initDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Competition)) return false;
        Competition that = (Competition) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }

}
