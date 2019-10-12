package com.VeizagaTorrico.proyectotorneos.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


    public class CompetitionS implements Serializable {

        @SerializedName("nombre")
        private String name;


        @SerializedName("categoria")
        private String category;

        @SerializedName("tipo_organizacion")
        private String typesOrganization;


        public CompetitionS() {
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

        public void setTypesOrganization(String typesOrganization) {
            this.typesOrganization = typesOrganization;
        }

        @Override
        public String toString() {
            return name;
        }

}


