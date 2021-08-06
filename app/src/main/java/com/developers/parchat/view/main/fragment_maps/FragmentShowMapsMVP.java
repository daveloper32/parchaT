package com.developers.parchat.view.main.fragment_maps;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.developers.parchat.model.entity.InfoLugar;
import com.developers.parchat.model.repository.RepositoryFragmentShowMaps;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DatabaseError;

import java.util.List;


public interface FragmentShowMapsMVP {
    interface Model {
        @SuppressLint("MissingPermission")
        void getUltimaUbicacion();

        @SuppressLint("MissingPermission")
        void addLocationUpdateCallback(LocationCallback callback);

        Location getUbicacionActual();

        // enviamos el presentador y el contexto de la vista
        void setPresentadorShowMaps(FragmentShowMapsMVP.Presenter presentadorMaps, Context contextoMaps);
        void setQueryOnUsuarioPosition(@NonNull LatLng latLngUsuarioPosition);
        void setQueryOnMarkerPosition(@NonNull LatLng latLngMarkerPosition);
        void buscarSitiosCercanosAUsuarioPosition();
        void buscarSitiosCercanosAMarkerPosition();
        void addASitiosCercanos(InfoLugar infoLugar);
        void borrarMarcadoresMapa();
        LatLng getUbicacionLatLngUsuario();
        LatLng getUbicacionLatLngMarker();
        void setUbicacionLatLngUsuario(LatLng ubicacionUsuario);
        void setListMarcadoresEnMapa(List<Marker> marcadoresEnMapa);
        List<InfoLugar> getListInfoLugarDeSitiosCercanos();
        List<LatLng> getListLatLongDeDeSitiosCercanos();
        List<String> getListlugaresGeoQueryResult();
        List<Marker> getListListMarcadoresEnMapa();
        InfoLugar getInfoLugarSeleccionado(String tituloMarcador);
        void getConfiguraciones();
        boolean getModoBusquedaGPS();
        boolean getModoBusquedaMarker();
        double getRangoBusquedaKm();
        int getZoomMapaSegunRangoBusquedaKm();
    }
    // EL presentador recibe los eventos que ocurriran en la vista
    interface Presenter {
        void busquedaSitiosCercanosAUsuario();
        void busquedaSitiosCercanosAMarcador();

        void busquedaSitiosCercanosAUsuarioPositionNoEncontrados();

        void busquedaSitiosCercanosAUsuarioPositionFallo(DatabaseError error);

        void busquedaSitiosCercanosAMarkerPositionNoEncontrados();

        void busquedaSitiosCercanosAMarkerPositionFallo(DatabaseError error);

        void getSitiosCercanosAUsuarioPositionConExito();

        void getSitiosCercanosConAUsuarioPositionFalla();

        void getSitiosCercanosAMarkerPositionConExito();

        void getSitiosCercanosAMarkerPositionConFalla();

        InfoLugar getInfoLugarSeleccionadoEnMapa(String tituloMarcador);

        void agregarListaDeMarcadoresEnMapa(List<Marker> marcadoresEnMapa);

        void getUltimasConfiguraciones();
        boolean getModoBusquedaGPSActualizado();
        boolean getModoBusquedaMarkerActualizado();
        double getRangoDeBusquedaKmActualizado();
        double getRangoDeBusquedaEnMActualizado();
        int getZoomMapaSegunRangoBusquedaKm();       

        void cargarUbicacionGPS();
    }
    // Se obtienen datos e informacion a la vista
    interface View {
        void addMarkersSitiosCercanos(LatLng latLngUsuario, List<InfoLugar> infoLugarList, List<LatLng> latLngLugarList);


        // Obtenemos el contexto de la vista para poder acceder a los SharedPreferences
        Context getContext();

        LatLng getUsuarioPosition();

        LatLng getMarkerPosition();


        void showToastBusquedaSitiosCercanosNoEncontrados();

        void showToastBusquedaSitiosCercanosFallo(DatabaseError error);

        void addMarkerUbicacionGPS(LatLng ubicacionActualLatLong);

        // Para hacer el intent e ir a el Activity SeleccionarActividad
        void irAlActivitySeleccionarActividad(Class<? extends AppCompatActivity> ir_a_SeleccionarActividad);
    }
}
