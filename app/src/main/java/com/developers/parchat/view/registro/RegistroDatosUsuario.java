package com.developers.parchat.view.registro;

import android.content.Intent;

public class RegistroDatosUsuario {
    private String nombreCompleto;
    private String email;
    private String password;
    private String numeroCel;

    public RegistroDatosUsuario(String nombreCompleto, String email, String password, String numeroCel) {
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.password = password;
        this.numeroCel = numeroCel;
    }

    public RegistroDatosUsuario(String nombreCompleto, String email, String password) {
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.password = password;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNumeroCel() {
        return numeroCel;
    }

    public void setNumeroCel(String numeroCel) {
        this.numeroCel = numeroCel;
    }
}
