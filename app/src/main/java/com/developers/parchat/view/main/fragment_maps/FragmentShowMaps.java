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

    private GoogleMap mMap;


    private Marker marcadorPosicion;

    // Declaramos un objeto de la Clase DatabaseReference
    private DatabaseReference mDatabase, geoFireDatabase;
    private GeoFire geoFire;

    private static final String TAG = "CargarLugares";

    private List<InfoLugar> lugares;
    private List<LatLng> latLngsLugares;

    private List<String> lugaresGeoQueryResult;


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
                getReference();
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

                lugares = new ArrayList<>();
                latLngsLugares = new ArrayList<>();
                lugaresGeoQueryResult = new ArrayList<>();


                // Creamos un objeto LatLng y le pasamos las coordenadas de Bogota
                LatLng bogota = new LatLng(4.60971, -74.08175);

                // 4.657160262597251, -74.05605940861399

                marcadorPosicion = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(4.657160262597251, -74.05605940861399))
                        .title(4.657 + ", " + -74.056)
                        .visible(true));

                ///
                mDatabase.child("SitiosParchaT")
                        .child("Bogota")
                        .child("InfoSitios")
                        .child("Restaurantes")
                        .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e(TAG, "Error recibiendo datos", task.getException());
                        } else {

                            for (DataSnapshot snapshot : task.getResult().getChildren()) {
                                // Recibo los datos de un lugar en un objeto de la clase InfoLugar
                                InfoLugar lugar = snapshot.getValue(InfoLugar.class);
                                // Añado el lugar a una lista de lugares tipo InfoLugar
                                lugares.add(lugar);
                                // Añado latitud y longitud del lugar que se recibio a lista tipo LatLong
                                LatLng latLngLugar = lugar.getLatLong();
                                if (latLngLugar != null) {
                                    latLngsLugares.add(latLngLugar);
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
                                }
                            }
                            // Recorremos el arreglo de objetos LatLng para ubicar y nombrar marcadores con los restauantes en el mapa
                            for (int x = 0; x < latLngsLugares.size(); x++) {
                                // Creamos un objeto LatLng y le pasamos las coordenadas de x restaurante
                                //restaurantes[x] = new LatLng(coordenadas[0][x], coordenadas[1][x]);
                                //restaurantes[x] = obtenerLatLongDeDireccion(dirRestaurantes[x]);
                                // Solo si el metodo obtenerLatLongDeDireccion nos devuelve las coordenadas
                                // vamos a dibujar el marcador
                                if (latLngsLugares.get(x) != null) {
                                    InfoLugar lugar = lugares.get(x);
                                    // Añadimos un marcador al mapa en las coordenadas de x restaurante
                                    mMap.addMarker(new MarkerOptions().
                                            position(latLngsLugares.get(x))
                                            .title(lugar.getNombre())
                                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapa_restaurante))
                                            .anchor(0.0f, 0.0f));
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
                            if (latLngsLugares.get(0) != null) {
                                // Ubicamos la camara de google maps en el primer restaurante
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngsLugares.get(0), 15));
                                // SI no
                            } else {
                                // Ubicamos la camara de google maps en la ciudad en que viva
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bogota, 11));
                            }
                        }

                    }
                });

                ///


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
                    public void onMapLongClick(@NonNull @NotNull LatLng latLng) {

                        lugaresGeoQueryResult.clear();

                        if (marcadorPosicion != null) {
                            marcadorPosicion.remove();
                        }
                        marcadorPosicion = mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(latLng.latitude + ", " + latLng.longitude)
                                .visible(true));

                        // creates a new query around [37.7832, -122.4056] with a radius of 0.6 kilometers
                        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), 0.5);
                        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                            @Override
                            public void onKeyEntered(String key, GeoLocation location) {
                                Toast.makeText(getContext(), "Bien Geofire, encontramos a " + key + " en un rango de 0.5km", Toast.LENGTH_SHORT).show();

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
                                    for (int x = 0; x < lugaresGeoQueryResult.size(); x++) {
                                        Log.d("Lugar Encontrado", "#" + (x + 1) + " -> " + lugaresGeoQueryResult.get(x));
                                    }
                                }



                            }

                            @Override
                            public void onGeoQueryError(DatabaseError error) {
                                Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_LONG).show();

                            }
                        });


                    }
                });
            }
        });
        return v;
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