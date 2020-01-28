package com.VeizagaTorrico.proyectotorneos.models;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class UserRegister {

    @SerializedName("nombreUsuario")
    private String nombreUsuario;

    @SerializedName("nombre")
    private  String nombre;

    @SerializedName("apellido")
    private String apellido;

    @SerializedName("correo")
    private String email;

    @SerializedName("pass")
    private String password;


    public UserRegister() {
    }

    public UserRegister(String nombreUsuario, String nombre, String apellido, String email, String password) {
        this.nombreUsuario = nombreUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRegister)) return false;
        UserRegister user = (UserRegister) o;
        return nombreUsuario == user.nombreUsuario;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombreUsuario);
    }

    @Override
    public String toString() {
        return  nombreUsuario ;
    }
}
