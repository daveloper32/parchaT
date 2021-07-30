package com.developers.parchat.model.entity;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String nombreCompleto;
    private String email;
    private String password;
    private String numeroCel;

    public Usuario() {
    }

    public Usuario(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Usuario(String nombreCompleto, String email, String numeroCel) {
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.numeroCel = numeroCel;
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

    public String getSoloNombre() {
        List<String> nombreComp = new ArrayList<String>();
        for (String val : nombreCompleto.split(" ")) {
            nombreComp.add(val);
        }
        return nombreComp.get(0);
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
