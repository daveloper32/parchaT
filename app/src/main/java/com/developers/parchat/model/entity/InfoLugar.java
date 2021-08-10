package com.developers.parchat.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class InfoLugar implements Parcelable {
    private String nombre;
    private String direccion;
    private String ciudad;
    private String pais;
    private String latitud;
    private String longitud;
    private String numero;
    private String sitioweb;
    private String tipositio;
    private String urlimagen;

    public InfoLugar() {
    }

    public InfoLugar(String nombre, String direccion, String ciudad, String pais, String latitud, String longitud, String numero, String sitioweb, String tipositio, String urlimagen) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.pais = pais;
        this.latitud = latitud;
        this.longitud = longitud;
        this.numero = numero;
        this.sitioweb = sitioweb;
        this.tipositio = tipositio;
        this.urlimagen = urlimagen;
    }

    protected InfoLugar(Parcel in) {
        nombre = in.readString();
        direccion = in.readString();
        ciudad = in.readString();
        pais = in.readString();
        latitud = in.readString();
        longitud = in.readString();
        numero = in.readString();
        sitioweb = in.readString();
        tipositio = in.readString();
        urlimagen = in.readString();
    }

    public static final Creator<InfoLugar> CREATOR = new Creator<InfoLugar>() {
        @Override
        public InfoLugar createFromParcel(Parcel in) {
            return new InfoLugar(in);
        }

        @Override
        public InfoLugar[] newArray(int size) {
            return new InfoLugar[size];
        }
    };

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getPais() {
        return pais;
    }

    public String getLatitud() {
        return latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public String getNumero() {
        return numero;
    }

    public String getSitioweb() {
        return sitioweb;
    }

    public String getTipositio() {
        return tipositio;
    }

    public String getUrlimagen() {
        return urlimagen;
    }

    public LatLng getLatLong() {
        LatLng latLngSitio = new LatLng(Double.parseDouble(latitud),
                Double.parseDouble(longitud));
        return latLngSitio;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(direccion);
        dest.writeString(ciudad);
        dest.writeString(pais);
        dest.writeString(latitud);
        dest.writeString(longitud);
        dest.writeString(numero);
        dest.writeString(sitioweb);
        dest.writeString(tipositio);
        dest.writeString(urlimagen);
    }
}
