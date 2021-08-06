package com.developers.parchat.view.main.fragment_maps;

import android.location.Location;

import androidx.annotation.NonNull;

import com.developers.parchat.model.entity.InfoLugar;
import com.developers.parchat.model.repository.RepositoryFragmentShowMaps;
import com.developers.parchat.view.registro.RegistroMVP;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DatabaseError;

import java.util.List;

public class FragmentShowMapsPresenter implements FragmentShowMapsMVP.Presenter {

    // Variables modelo MVP
    private final FragmentShowMapsMVP.View vista;
    private final FragmentShowMapsMVP.Model modelo;

    private String keyBusqueda;

    public FragmentShowMapsPresenter(FragmentShowMapsMVP.View vista, String keyBusqueda) {
        this.vista = vista;
        this.keyBusqueda = keyBusqueda;
        this.modelo = new RepositoryFragmentShowMaps(keyBusqueda);
        this.modelo.setPresentadorShowMaps(this, vista.getContext());
    }

    @Override
    public void busquedaSitiosCercanosAUsuario() {
        LatLng usuarioPosition = vista.getUsuarioPosition();
        if (usuarioPosition != null) {
            modelo.setQueryOnUsuarioPosition(usuarioPosition);
        }
    }

    @Override
    public void busquedaSitiosCercanosAMarcador() {
        LatLng markerPosition = vista.getMarkerPosition();
        if (markerPosition != null) {
            modelo.setQueryOnMarkerPosition(markerPosition);
        }
    }

    @Override
    public void busquedaSitiosCercanosAUsuarioPositionNoEncontrados() {
        vista.showToastBusquedaSitiosCercanosNoEncontrados();
    }

    @Override
    public void busquedaSitiosCercanosAUsuarioPositionFallo(DatabaseError error) {
        vista.showToastBusquedaSitiosCercanosFallo(error);
    }

    @Override
    public void busquedaSitiosCercanosAMarkerPositionNoEncontrados() {
        vista.showToastBusquedaSitiosCercanosNoEncontrados();
    }

    @Override
    public void busquedaSitiosCercanosAMarkerPositionFallo(DatabaseError error) {
        vista.showToastBusquedaSitiosCercanosFallo(error);
    }

    @Override
    public void getSitiosCercanosAUsuarioPositionConExito() {

        // Obtenemos las listas de objetos InfoLugar y LataLong
        LatLng latLngUsuario = modelo.getUbicacionLatLngUsuario();
        List<InfoLugar> infoLugarList = modelo.getListInfoLugarDeSitiosCercanos();
        List<LatLng> latLngLugarList = modelo.getListLatLongDeDeSitiosCercanos();
        if (infoLugarList != null && latLngLugarList != null) {
            if (infoLugarList.size() != 0 && latLngLugarList.size() != 0) {
                if (latLngUsuario != null) {
                    vista.addMarkersSitiosCercanos(latLngUsuario, infoLugarList, latLngLugarList);
                }
            }
        }

    }

    @Override
    public void getSitiosCercanosConAUsuarioPositionFalla() {

    }

    @Override
    public void getSitiosCercanosAMarkerPositionConExito() {
        // Obtenemos las listas de objetos InfoLugar y LataLong
        LatLng latLngUsuario = modelo.getUbicacionLatLngMarker();
        List<InfoLugar> infoLugarList = modelo.getListInfoLugarDeSitiosCercanos();
        List<LatLng> latLngLugarList = modelo.getListLatLongDeDeSitiosCercanos();
        if (infoLugarList != null && latLngLugarList != null) {
            if (infoLugarList.size() != 0 && latLngLugarList.size() != 0) {
                if (latLngUsuario != null) {
                    vista.addMarkersSitiosCercanos(latLngUsuario, infoLugarList, latLngLugarList);
                }
            }
        }

    }

    @Override
    public void getSitiosCercanosAMarkerPositionConFalla() {

    }

    @Override
    public InfoLugar getInfoLugarSeleccionadoEnMapa(String tituloMarcador) {
        return modelo.getInfoLugarSeleccionado(tituloMarcador);
    }

    @Override
    public void agregarListaDeMarcadoresEnMapa(List<Marker> marcadoresEnMapa) {
        modelo.setListMarcadoresEnMapa(marcadoresEnMapa);
    }

    @Override
    public void getUltimasConfiguraciones() {
        modelo.getConfiguraciones();
    }

    @Override
    public boolean getModoBusquedaGPSActualizado() {
        modelo.getConfiguraciones();
        return modelo.getModoBusquedaGPS();
    }

    @Override
    public boolean getModoBusquedaMarkerActualizado() {
        modelo.getConfiguraciones();
        return modelo.getModoBusquedaMarker();
    }

    @Override
    public double getRangoDeBusquedaKmActualizado() {
        modelo.getConfiguraciones();
        return modelo.getRangoBusquedaKm();
    }

    @Override
    public double getRangoDeBusquedaEnMActualizado() {
        double rangoBusquedaKm = modelo.getRangoBusquedaKm();
        double rangoBusquedaM = rangoBusquedaKm * 1000;
        return rangoBusquedaM;
    }

    @Override
    public int getZoomMapaSegunRangoBusquedaKm() {
        return modelo.getZoomMapaSegunRangoBusquedaKm();
    }

    @Override
    public void cargarUbicacionGPS() {
        modelo.addLocationUpdateCallback(new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location ubicacionActual = modelo.getUbicacionActual();
                LatLng ubicacionActualLatLong = new LatLng(
                        ubicacionActual.getLatitude(),
                        ubicacionActual.getLongitude()
                );
                vista.addMarkerUbicacionGPS(ubicacionActualLatLong);
            }
        });
    }


}
