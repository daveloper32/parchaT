package com.developers.parchat.model.entity;

public class Usuario {
    private String nombreCompleto;
    private String email;
    private String password;
    private String numeroCel;

    public Usuario(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Usuario(String nombreCompleto, String email, String password, String numeroCel) {
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.password = password;
        this.numeroCel = numeroCel;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNumeroCel() {
        return numeroCel;
    }

    public void setNumeroCel(String numeroCel) {
        this.numeroCel = numeroCel;
    }
}
