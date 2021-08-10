package com.developers.parchat.view.main.fragment_maps;

import android.location.Location;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.developers.parchat.model.entity.BusquedaSeleccionarActividad;
import com.developers.parchat.model.entity.InfoLugar;
import com.developers.parchat.model.repository.RepositoryFragmentShowMaps;
import com.developers.parchat.model.repository.RepositoryMainActivity;
import com.developers.parchat.view.main.MainActivityMVP;
import com.developers.parchat.view.registro.RegistroMVP;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class FragmentShowMapsPresenter implements FragmentShowMapsMVP.Presenter {

    // Variables modelo MVP
    private final FragmentShowMapsMVP.View vista;
    private final FragmentShowMapsMVP.Model modelo;
    private final MainActivityMVP.Model modeloMain;

    private String keyBusqueda;

    public FragmentShowMapsPresenter(FragmentShowMapsMVP.View vista, String keyBusqueda) {
        this.vista = vista;
        this.keyBusqueda = keyBusqueda;
        this.modelo = new RepositoryFragmentShowMaps(keyBusqueda);
        this.modelo.setPresentadorShowMaps(this, vista.getContext());
        this.modeloMain = new RepositoryMainActivity();
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
        vista.saveBusquedaEncontrada(false);
        if (vista.verificarBusquedaEncontrada()) {
            vista.showSnackbarBusquedaSitiosCercanosNoEncontrados();
        }
    }

    @Override
    public void busquedaSitiosCercanosAUsuarioPositionFallo(DatabaseError error) {
        vista.showToastBusquedaSitiosCercanosFallo(error);
    }

    @Override
    public void busquedaSitiosCercanosAMarkerPositionNoEncontrados() {
        vista.saveBusquedaEncontrada(false);
        if (vista.verificarBusquedaEncontrada()) {
            vista.showSnackbarBusquedaSitiosCercanosNoEncontrados();
        }
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
                    vista.saveBusquedaEncontrada(true);
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
                    vista.saveBusquedaEncontrada(true);
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
    public boolean getRangoBusquedaVisibleActualizado() {
        modelo.getConfiguraciones();
        return modelo.getRangoBusquedaVisible();
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
    public void setUltimaUbicacionUsuario(LatLng ultimaUbicacion) {
        if (ultimaUbicacion != null) {
            modelo.guardarUltimaUbicacionUsuario(ultimaUbicacion);
        }
    }

    @Override
    public LatLng getUltimaUbicacionUsuario() {
        return modelo.getUltimaUbicacionUsuario();
    }

    @Override
    public BusquedaSeleccionarActividad getDatosFromBusquedaSeleccionada() {
        return modelo.getDatosBusquedaSeleccionada();
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

    @Override
    public void CargarDatosGeofire(InfoLugar lugar) {
        LatLng latLngLugar = lugar.getLatLong();
        GeoFire geoFire = modelo.getGeoFire();
        geoFire.setLocation(lugar.getNombre(),
                new GeoLocation(latLngLugar.latitude, latLngLugar.longitude));
    }

    @Override
    public void ObtenerSnapshotTodaRuta() {
        DatabaseReference mDatabase = modelo.getDataBaseReference();
        mDatabase.get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DataSnapshot snapshot : task.getResult().getChildren()) {
                                // COnvertimos el snapshot a un objeto tipo InfoLugar
                                InfoLugar infoLugar = snapshot.getValue(InfoLugar.class);
                                // Verificamos que el objeto InfoLugar no este vacio
                                if (infoLugar != null) {
                                    CargarDatosGeofire(infoLugar);
                                }
                            }
                        }

                    }
                });
    }


}
