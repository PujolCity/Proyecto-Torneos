package com.VeizagaTorrico.proyectotorneos.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


    public class CompetitionMin implements Serializable {

        @SerializedName("id")
        private int id;

        @SerializedName("nombre")
        private String name;

        @SerializedName("deporte")
        private String deporte;

        @SerializedName("categoria")
        private String category;

        @SerializedName("tipo_organizacion")
        private String typesOrganization;

        @SerializedName("ciudad")
        private String ciudad;

        @SerializedName("genero")
        private String genero;

        @SerializedName("rol")
        private String rol;


        public CompetitionMin(int id, String name, String deporte, String category, String typesOrganization, String ciudad, String genero, String rol) {
            this.id = id;
            this.name = name;
            this.deporte = deporte;
            this.category = category;
            this.typesOrganization = typesOrganization;
            this.ciudad = ciudad;
            this.genero = genero;
            this.rol = rol;
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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDeporte() {
            return deporte;
        }

        public void setDeporte(String deporte) {
            this.deporte = deporte;
        }

        public String getRol() {
            return rol;
        }

        public void setRol(String rol) {
            this.rol = rol;
        }

        public void setTypesOrganization(String typesOrganization) {
            this.typesOrganization = typesOrganization;
        }

        @Override
        public String toString() {
            return name;
        }

}


