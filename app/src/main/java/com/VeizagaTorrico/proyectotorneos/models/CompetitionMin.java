package com.VeizagaTorrico.proyectotorneos.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


    public class CompetitionMin implements Serializable {

        @SerializedName("nombre")
        private String name;

        @SerializedName("categoria")
        private String category;

        @SerializedName("tipo_organizacion")
        private String typesOrganization;

        @SerializedName("ciudad")
        private String ciudad;

        @SerializedName("genero")
        private String genero;


        public CompetitionMin(String name, String category, String typesOrganization, String ciudad, String genero) {
            this.name = name;
            this.category = category;
            this.typesOrganization = typesOrganization;
            this.ciudad = ciudad;
            this.genero = genero;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getTypesOrganization() {
            return typesOrganization;
        }

        public String getCiudad() {
            return ciudad;
        }

        public void setCiudad(String ciudad) {
            this.ciudad = ciudad;
        }

        public String getGenero() {
            return genero;
        }

        public void setGenero(String genero) {
            this.genero = genero;
        }

        public void setTypesOrganization(String typesOrganization) {
            this.typesOrganization = typesOrganization;
        }

        @Override
        public String toString() {
            return name;
        }

}


