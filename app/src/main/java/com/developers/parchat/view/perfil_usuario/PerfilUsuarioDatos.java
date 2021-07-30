package com.developers.parchat.view.perfil_usuario;

import java.util.ArrayList;
import java.util.List;

public class PerfilUsuarioDatos {
    private String nombreCompleto;
    private String email;
    private String password;
    private String numeroCel;

    public PerfilUsuarioDatos(String nombreCompleto, String email, String numeroCel) {
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.numeroCel = numeroCel;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getSoloNombre() {
        List<String> nombreComp = new ArrayList<String>();
        for (String val : nombreCompleto.split(" ")) {
            nombreComp.add(val);
        }
        return nombreComp.get(0);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumeroCel() {
        return numeroCel;
    }

    public void setNumeroCel(String numeroCel) {
        this.numeroCel = numeroCel;
    }
}
