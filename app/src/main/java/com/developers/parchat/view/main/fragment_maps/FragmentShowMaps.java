package com.developers.parchat.view.main.fragment_maps;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.developers.parchat.model.entity.InfoLugar;
import com.developers.parchat.view.main.bt_st_dlg_ifo_lugar.BottomSheetDialog_InfoLugar;
import com.developers.parchat.view.main.MainActivity;
import com.developers.parchat.view.main.MainActivityMVP;
import com.developers.parchat.view.main.MainActivityPresenter;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.developers.parchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentShowMaps extends Fragment {

    // Creamos un objeto LatLng y le pasamos las coordenadas de Bogota
    LatLng bogota = new LatLng(4.60971, -74.08175);

    private GoogleMap mMap;


    private Marker marcadorPosicion;

    // Declaramos un objeto de la Clase DatabaseReference
    private DatabaseReference mDatabase, geoFireDatabase;
    private GeoFire geoFire;

    private static final String TAG = "CargarLugares";

    private List<InfoLugar> lugares;
    private List<LatLng> latLngsLugares;

    private List<String> lugaresGeoQueryResult;

    private List<Marker> marcadoresMapa;
    private LatLng latLngUbicacionUsuario;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Conectamos con el archivo de vista en el cual se desplegara el Fragmento
        View v = inflater.inflate(R.layout.fragment_show_maps_activity_main, container, false);

        // Hacemos la conexion con el objeto Fragment en el archivo de vista cotenido_fragmet_activity_main
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapView_ActivityMain);

        // Inicializamos la instancia FirebaseDatabase
        mDatabase = FirebaseDatabase.getInstance().
                getReference("SitiosParchaT/Bogota/InfoSitios/Restaurantes");
        geoFireDatabase = FirebaseDatabase.getInstance().
                getReference("SitiosParchaT/Bogota/GeoFire/Restaurantes");
        geoFire = new GeoFire(geoFireDatabase);

        //4.653848659303366, -74.05495626444139




        // Que pasa cuando se carga el mapa en la app
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            // Cuando ya esta listo para funcionar
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                // Renombramos googleMap por mMap -> literal es como el obejto del mapa de Maps
                mMap = googleMap;
                mMap.getUiSettings().setZoomControlsEnabled(true);

                lugares = new ArrayList<>();
                latLngsLugares = new ArrayList<>();
                lugaresGeoQueryResult = new ArrayList<>();
                marcadoresMapa = new ArrayList<>();
                latLngUbicacionUsuario = null;

                LatLng cargaInicial = new LatLng(4.657160262597251, -74.05605940861399);

                // Marcador inicial
                marcadorPosicion = mMap.addMarker(new MarkerOptions()
                        .position(cargaInicial)
                        .title(4.657 + ", " + -74.056)
                        .visible(true));
                latLngUbicacionUsuario = cargaInicial;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cargaInicial, 15));
                setQueryOnUsuario(cargaInicial
                        ,0.5);


                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull @NotNull Marker marker) {
                        String tituloMarcador = marker.getTitle();
                        String direccion = null;

                        InfoLugar infoLugarSeleccionado = getInfoLugarSeleccionado(tituloMarcador);
                        if (infoLugarSeleccionado != null) {
                            //direccion = obtenerDireccionDeLatLong(marker.getPosition());
                            direccion = infoLugarSeleccionado.getDireccion();
                            ((MainActivity) getActivity()).iniciarBottomSheetDialog(tituloMarcador, direccion
                                    , infoLugarSeleccionado.getSitioweb(), infoLugarSeleccionado.getUrlimagen());
                        } else {
                            ((MainActivity) getActivity()).iniciarBottomSheetDialog(tituloMarcador, "No disponible en este momento.", "", "");
                        }
                        return false;
                    }
                });

                mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(@NonNull @NotNull LatLng latLngUsuario) {

                        lugaresGeoQueryResult.clear();

                        if (marcadorPosicion != null) {
                            marcadorPosicion.remove();
                        }
                        marcadorPosicion = mMap.addMarker(new MarkerOptions()
                                .position(latLngUsuario)
                                .title(latLngUsuario.latitude + ", " + latLngUsuario.longitude)
                                .visible(true));
                        // Hacemos la busqueda de los sitios cercanos en un radio x de kilometros
                        latLngUbicacionUsuario = latLngUsuario;
                        setQueryOnUsuario(latLngUsuario, 0.4);
                    }
                });
            }
        });
        return v;
    }

    private void borrarMarcadoresMapa() {
        // Borramos todos los marcadores que esten en el mapa
        for (int x = 0; x < marcadoresMapa.size(); x++) {
            Marker marker = marcadoresMapa.get(x);
            marker.remove();
        }
        // Desocupamos la lista de marcadores
        marcadoresMapa.clear();
    }

    private void setQueryOnUsuario(@NonNull LatLng latLngUsuario, double radioEnKm) {

        // creates a new query around [37.7832, -122.4056] with a radius of 0.6 kilometers
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(latLngUsuario.latitude,
                latLngUsuario.longitude), radioEnKm);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {

                lugaresGeoQueryResult.add(key);

            }

            @Override
            public void onKeyExited(String key) {
                //Toast.makeText(getContext(), "Bien Geofire, onKeyExited", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                //Toast.makeText(getContext(), "All initial data has been loaded and events have been fired!", Toast.LENGTH_LONG).show();
                if (lugaresGeoQueryResult.size() != 0) {
                    buscarSitiosCercanos(lugaresGeoQueryResult);
                    for (int x = 0; x < lugaresGeoQueryResult.size(); x++) {
                        Log.d("Lugar Encontrado", "#" + (x + 1) + " -> " + lugaresGeoQueryResult.get(x));
                    }
                } else {
                    borrarMarcadoresMapa();
                    lugares.clear();
                    latLngsLugares.clear();
                    Toast.makeText(getContext(), "No encontramos ningun sitio cerca a ti :c, intenta aumentar el rango de busqueda.", Toast.LENGTH_SHORT).show();
                }
                // Se buscan los sitios cercanos en la base de datos


            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addMarkersSitiosCercanos(LatLng latLngUsuario, List<InfoLugar> infoLugarList, List<LatLng> latLngLugarList) {

        // Recorremos el arreglo de objetos LatLng para ubicar y nombrar marcadores con los restauantes en el mapa
        for (int x = 0; x < latLngLugarList.size(); x++) {
            // Creamos un objeto LatLng y le pasamos las coordenadas de x restaurante
            //restaurantes[x] = new LatLng(coordenadas[0][x], coordenadas[1][x]);
            //restaurantes[x] = obtenerLatLongDeDireccion(dirRestaurantes[x]);
            // Solo si el metodo obtenerLatLongDeDireccion nos devuelve las coordenadas
            // vamos a dibujar el marcador
            if (latLngLugarList.get(x) != null) {
                // Creamos un objeto tipo Marker
                Marker marcadorSitioCercano;
                InfoLugar lugar = infoLugarList.get(x);
                // Añadimos un marcador al mapa en las coordenadas de x restaurante
                marcadorSitioCercano = mMap.addMarker(new MarkerOptions()
                        .position(latLngLugarList.get(x))
                        .title(lugar.getNombre())
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapa_restaurante))
                        .anchor(0.0f, 0.0f));
                // Añadimos el marcador a la lista de marcadores para luego borrarlos
                marcadoresMapa.add(marcadorSitioCercano);
            }
            // De lo contrario
            else {
                // Enviamos un mensaje emergente diciendo que la conexion fallo
                Toast.makeText(getContext(),
                        R.string.msgToast_MainActivity_1,
                        Toast.LENGTH_LONG).show();
            }
        }
        // Si el objeto latlong en la posicion 0 del arreglo restaurantes no esta vacio
        if (latLngUsuario != null) {
            // Ubicamos la camara de google maps en el primer restaurante
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngUsuario, 15));
            // SI no
        } else {
            // Ubicamos la camara de google maps en la ciudad en que viva
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bogota, 11));
        }
    }

    private void buscarSitiosCercanos(List<String> lugaresQueryGeoFire) {

        borrarMarcadoresMapa();
        lugares.clear();
        latLngsLugares.clear();

        // Obtenemos la informacion de los lugares que arrojo el Query
        // de la base de datos

            mDatabase.get()
                    .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                                    InfoLugar infoLugar = snapshot.getValue(InfoLugar.class);
                                    if (infoLugar != null) {
                                        for (int x = 0; x < lugaresQueryGeoFire.size(); x++) {
                                            String nomlugarQueryGeoFire = lugaresQueryGeoFire.get(x);
                                            String nomlugarDB = infoLugar.getNombre();
                                            if (nomlugarQueryGeoFire.equals(nomlugarDB)) {
                                                addASitiosCercanos(infoLugar);
                                            }
                                        }
                                    }
                                }
                                if ((lugares.size()) == lugaresQueryGeoFire.size()) {
                                    getSitiosCercanosConExito();
                                }
                            } else {
                                getSitiosCercanosConFalla();
                            }
                        }
                    });


    }

    private void getSitiosCercanosConFalla() {

    }

    private void getSitiosCercanosConExito() {
        // Obtenemos las listas de objetos InfoLugar y LataLong
        LatLng latLngUsuario = getLatLongUsuario();
        List<InfoLugar> infoLugarList = getListInfoLugarDeSitiosCercanos();
        List<LatLng> latLngLugarList = getListLatLongDeDeSitiosCercanos();
        if (infoLugarList != null && latLngLugarList != null) {
            if (infoLugarList.size() != 0 && latLngLugarList.size() != 0) {
                if (latLngUsuario != null) {
                    addMarkersSitiosCercanos(latLngUsuario, infoLugarList, latLngLugarList);
                }
            }
        }
    }

    private void addASitiosCercanos(InfoLugar infoLugar) {
        // Añado el lugar a una lista de lugares tipo InfoLugar
        lugares.add(infoLugar);
        // Añado latitud y longitud del lugar que se recibio a lista tipo LatLong
        LatLng latLngLugar = infoLugar.getLatLong();
        if (latLngLugar != null) {
            latLngsLugares.add(latLngLugar);
        }
    }

    private LatLng getLatLongUsuario() {
        return latLngUbicacionUsuario;
    }

    private List<InfoLugar> getListInfoLugarDeSitiosCercanos(){
        return lugares;
    }

    private List<LatLng> getListLatLongDeDeSitiosCercanos(){
        return latLngsLugares;
    }


    private InfoLugar getInfoLugarSeleccionado(String tituloMarcador) {
        InfoLugar lugar = null;
        for (int x = 0; x < lugares.size(); x++) {
            lugar = lugares.get(x);
            if (lugar.getNombre().equals(tituloMarcador)) {
                break;
            }
        }
        return lugar;
    }
}

/*geoFire.setLocation(lugar.getNombre(),
new GeoLocation(latLngLugar.latitude, latLngLugar.longitude),
new GeoFire.CompletionListener() {
    @Override
    public void onComplete(String key, DatabaseError error) {
        if (error == null) {
            Toast.makeText(getContext(), "Bien Geofire", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Mal Geofire", Toast.LENGTH_LONG).show();
        }
    }
});*/