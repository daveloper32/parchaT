package com.developers.parchat.view.main;

import java.util.ArrayList;
import java.util.List;

public class MainActivityDatos {
    private String nombreCompleto;
    private String email;
    private String numero;

    public MainActivityDatos(String nombreCompleto, String email) {
        this.nombreCompleto = nombreCompleto;
        this.email = email;
    }

    public MainActivityDatos(String nombreCompleto, String email, String numero) {
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.numero = numero;
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

    public void setEmail(String email) {
        this.email = email;
    }
}
