package com.developers.parchat.model.entity;

public class SaltarLogin {
    private String emailSesionIniciada;
    private boolean sesionIniciada;

    public SaltarLogin(String emailSesionIniciada, boolean sesionIniciada) {
        this.emailSesionIniciada = emailSesionIniciada;
        this.sesionIniciada = sesionIniciada;
    }

    public boolean isSesionIniciada() {
        return sesionIniciada;
    }

    public void setSesionIniciada(boolean sesionIniciada) {
        this.sesionIniciada = sesionIniciada;
    }
}
