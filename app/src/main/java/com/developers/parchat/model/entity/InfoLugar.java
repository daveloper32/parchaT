package com.developers.parchat.model.entity;

import com.google.android.gms.maps.model.LatLng;

public class InfoLugar {
    private String nombre;
    private String direccion;
    private String latitud;
    private String longitud;
    private String sitioweb;
    private String urlimagen;

    public InfoLugar() {
    }

    public InfoLugar(String nombre, String direccion, String latitud, String longitud, String sitioweb, String urlimagen) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.sitioweb = sitioweb;
        this.urlimagen = urlimagen;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getLatitud() {
        return latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public String getSitioweb() {
        return sitioweb;
    }

    public String getUrlimagen() {
        return urlimagen;
    }

    public LatLng getLatLong() {
        LatLng latLngSitio = new LatLng(Double.parseDouble(latitud),
                Double.parseDouble(longitud));
        return latLngSitio;
    }
}
