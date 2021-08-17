package com.developers.parchat.model.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.developers.parchat.R;
import com.developers.parchat.model.entity.BusquedaSeleccionarActividad;
import com.developers.parchat.model.entity.InfoLugar;
import com.developers.parchat.view.main.fragment_maps.FragmentShowMapsMVP;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class RepositoryFragmentShowMaps implements FragmentShowMapsMVP.Model {

    // Variables modelo MVP Registro
    private FragmentShowMapsMVP.Presenter presentadorMaps;
    private Context contextMaps;

    // Declaramos dos objeto de la Clase DatabaseReference y uno Geofire
    private DatabaseReference mDatabase, geoFireDatabase;
    private GeoFire geoFire;
    private String keyBusqueda;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private static Location ubicacionActual;

    final private List<InfoLugar> listInfoLugarDeSitiosCercanos;
    private List<LatLng> listLatLngsLugares;
    private List<String> listLugaresGeoQueryResult;
    private List<Marker> listMarcadoresMapa;
    private LatLng latLngUbicacionUsuario;
    private LatLng latLngUbicacionMarker;
    private boolean modoBusquedaGPS, modoBusquedaMarker, rangoBusquedaVisible;
    private double radioEnKm;
    private LatLng ultimaUbicacionUsuario;
    private BusquedaSeleccionarActividad datosBusquedaSeleccionarActividad;

    // Creamos un objeto SharedPreferences para buscar datos de COnfiguracion del Usuario
    private SharedPreferences datosConfiguracion, datoUltimaUbicacionUsuario, datosBusqueda;
    private SharedPreferences.Editor editor;

    public RepositoryFragmentShowMaps(String keyBusqueda) {
        this.keyBusqueda = keyBusqueda;
        // Inicializamos la instancia FirebaseDatabase
        mDatabase = FirebaseDatabase.getInstance().
                getReference("SitiosParchaT/InfoSitios/"+keyBusqueda);
        geoFireDatabase = FirebaseDatabase.getInstance().
                getReference("SitiosParchaT/GeoFire/"+keyBusqueda);
        geoFire = new GeoFire(geoFireDatabase);

        // Inicializamos las listas
        listInfoLugarDeSitiosCercanos = new ArrayList<>();
        listLatLngsLugares = new ArrayList<>();
        listLugaresGeoQueryResult = new ArrayList<>();
        listMarcadoresMapa = new ArrayList<>();
        // Inicializamos el objeto LatLng
        latLngUbicacionUsuario = null;
        latLngUbicacionMarker = null;
        datosBusquedaSeleccionarActividad = null;

        //
        //fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient();
        //getUltimaUbicacion();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void getUltimaUbicacion() {
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Verificamos que la ubicacion no sea nula
                        if (location != null) {
                            // Le pasamos la ubicacion actual a nuestra variable privada
                            ubicacionActual = location;
                        }
                    }
                });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void addLocationUpdateCallback(LocationCallback callback) {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Cada 10 segundos
        locationRequest.setInterval(10 * 1000);

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, callback, null);
    }

    @Override
    public Location getUbicacionActual() {
        return ubicacionActual;
    }



    @Override
    public void setPresentadorShowMaps(FragmentShowMapsMVP.Presenter presentadorMaps, Context contextMaps) {
        this.presentadorMaps = presentadorMaps;
        this.contextMaps = contextMaps;
    }

    @Override
    public void setQueryOnUsuarioPosition(@NonNull LatLng latLngUsuarioPosition) {
        // Siempre empiezo obteniendo datos de configuracion para actualizar
        getConfiguraciones();
        latLngUbicacionUsuario = latLngUsuarioPosition;
        listLugaresGeoQueryResult.clear();
        // creates a new query around latLngUsuario with a radius of radioEnKm kilometers
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(latLngUsuarioPosition.latitude,
                latLngUsuarioPosition.longitude), getRangoBusquedaKm());
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                // Añadioms los nombres de lugares encontrados
                listLugaresGeoQueryResult.add(key);
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
                if (listLugaresGeoQueryResult.size() != 0) {
                    buscarSitiosCercanosAUsuarioPosition();
                    /*for (int x = 0; x < lugaresGeoQueryResult.size(); x++) {
                        Log.d("Lugar Encontrado", "#" + (x + 1) + " -> " + lugaresGeoQueryResult.get(x));
                    }*/
                } else {
                    borrarMarcadoresMapa();
                    listInfoLugarDeSitiosCercanos.clear();
                    listLatLngsLugares.clear();
                    presentadorMaps.busquedaSitiosCercanosAUsuarioPositionNoEncontrados();
                }
            }
            @Override
            public void onGeoQueryError(DatabaseError error) {
                //
                presentadorMaps.busquedaSitiosCercanosAUsuarioPositionFallo(error);
            }
        });
    }

    @Override
    public void setQueryOnMarkerPosition(@NonNull LatLng latLngMarkerPosition) {
        // Siempre empiezo obteniendo datos de configuracion para actualizar
        getConfiguraciones();
        latLngUbicacionMarker = latLngMarkerPosition;
        listLugaresGeoQueryResult.clear();
        // creates a new query around latLngUsuario with a radius of radioEnKm kilometers
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(latLngMarkerPosition.latitude,
                latLngMarkerPosition.longitude), getRangoBusquedaKm());
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                // Añadioms los nombres de lugares encontrados
                listLugaresGeoQueryResult.add(key);
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
                if (listLugaresGeoQueryResult.size() != 0) {
                    buscarSitiosCercanosAMarkerPosition();
                    /*for (int x = 0; x < lugaresGeoQueryResult.size(); x++) {
                        Log.d("Lugar Encontrado", "#" + (x + 1) + " -> " + lugaresGeoQueryResult.get(x));
                    }*/
                } else {
                    borrarMarcadoresMapa();
                    listInfoLugarDeSitiosCercanos.clear();
                    listLatLngsLugares.clear();
                    presentadorMaps.busquedaSitiosCercanosAMarkerPositionNoEncontrados();
                }
            }
            @Override
            public void onGeoQueryError(DatabaseError error) {
                //
                presentadorMaps.busquedaSitiosCercanosAMarkerPositionFallo(error);
            }
        });
    }

    @Override
    public void buscarSitiosCercanosAUsuarioPosition() {
        borrarMarcadoresMapa();
        listInfoLugarDeSitiosCercanos.clear();
        listLatLngsLugares.clear();
        // Obtenemos la informacion de los lugares que arrojo el Query
        // de la base de datos
        mDatabase.get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        // Si se encontraron los datos
                        if (task.isSuccessful()) {
                            // Obtenemos dato a dato -> obtenido por la busqueda
                            for (DataSnapshot snapshot : task.getResult().getChildren()) {
                                // COnvertimos el snapshot a un objeto tipo InfoLugar
                                InfoLugar infoLugar = snapshot.getValue(InfoLugar.class);
                                // Verificamos que el objeto InfoLugar no este vacio
                                if (infoLugar != null) {
                                    // Comparamos los lugares obtenidos por la busqueda Query cercaa al usuario
                                    // con la busqueda
                                    for (int x = 0; x < listLugaresGeoQueryResult.size(); x++) {
                                        // Vamos obteniendo nombre por nombre del lugar obtenido por el Query
                                        String nomlugarQueryGeoFire = listLugaresGeoQueryResult.get(x);
                                        // Obtenemos el nombre del lugar actual obtenido del snapshot
                                        String nomlugarDB = infoLugar.getNombre();
                                        // Si los nombres coinciden
                                        if (nomlugarQueryGeoFire.equals(nomlugarDB)) {
                                            // Añadimos el snapshot (InfoLugar) a la lista de lugares cercanos
                                            addASitiosCercanos(infoLugar);
                                        }
                                    }
                                }
                            }
                            // Verificamos que no nos haya arrojado lugares extra
                            // Comparando el tamaño de la lista de lugares cercanos con la lista de lugares del
                            //Query
                            if ((listInfoLugarDeSitiosCercanos.size()) == listLugaresGeoQueryResult.size()) {
                                // Cuando acabe llamamos al presentador para decirle que ya se tiene
                                // la lista de lugares a recomendar cercanos al usuario
                                presentadorMaps.getSitiosCercanosAUsuarioPositionConExito();
                            }
                        } else {
                            // Si la no se encontraron datos le decimos al presentador que lanze un mensaje
                            presentadorMaps.getSitiosCercanosConAUsuarioPositionFalla();
                        }
                    }
                });
    }

    @Override
    public void buscarSitiosCercanosAMarkerPosition() {
        borrarMarcadoresMapa();
        listInfoLugarDeSitiosCercanos.clear();
        listLatLngsLugares.clear();
        // Obtenemos la informacion de los lugares que arrojo el Query
        // de la base de datos
        mDatabase.get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        // Si se encontraron los datos
                        if (task.isSuccessful()) {
                            // Obtenemos dato a dato -> obtenido por la busqueda
                            for (DataSnapshot snapshot : task.getResult().getChildren()) {
                                // COnvertimos el snapshot a un objeto tipo InfoLugar
                                InfoLugar infoLugar = snapshot.getValue(InfoLugar.class);
                                // Verificamos que el objeto InfoLugar no este vacio
                                if (infoLugar != null) {
                                    // Comparamos los lugares obtenidos por la busqueda Query cercaa al usuario
                                    // con la busqueda
                                    for (int x = 0; x < listLugaresGeoQueryResult.size(); x++) {
                                        // Vamos obteniendo nombre por nombre del lugar obtenido por el Query
                                        String nomlugarQueryGeoFire = listLugaresGeoQueryResult.get(x);
                                        // Obtenemos el nombre del lugar actual obtenido del snapshot
                                        String nomlugarDB = infoLugar.getNombre();
                                        // Si los nombres coinciden
                                        if (nomlugarQueryGeoFire.equals(nomlugarDB)) {
                                            // Añadimos el snapshot (InfoLugar) a la lista de lugares cercanos
                                            addASitiosCercanos(infoLugar);
                                        }
                                    }
                                }
                            }
                            // Verificamos que no nos haya arrojado lugares extra
                            // Comparando el tamaño de la lista de lugares cercanos con la lista de lugares del
                            //Query
                            if ((listInfoLugarDeSitiosCercanos.size()) == listLugaresGeoQueryResult.size()) {
                                // Cuando acabe llamamos al presentador para decirle que ya se tiene
                                // la lista de lugares a recomendar cercanos al usuario
                                presentadorMaps.getSitiosCercanosAMarkerPositionConExito();
                            }
                        } else {
                            // Si la no se encontraron datos le decimos al presentador que lanze un mensaje
                            presentadorMaps.getSitiosCercanosAMarkerPositionConFalla();
                        }
                    }
                });
    }

    @Override
    public void addASitiosCercanos(InfoLugar infoLugar) {
        // Añado el lugar a una lista de lugares tipo InfoLugar
        listInfoLugarDeSitiosCercanos.add(infoLugar);
        // Añado latitud y longitud del lugar que se recibio a lista tipo LatLong
        LatLng latLngLugar = infoLugar.getLatLong();
        if (latLngLugar != null) {
            listLatLngsLugares.add(latLngLugar);        }

    }

    @Override
    public void borrarMarcadoresMapa() {
        // Borramos todos los marcadores que esten en el mapa
        for (int x = 0; x < listMarcadoresMapa.size(); x++) {
            Marker marker = listMarcadoresMapa.get(x);
            marker.remove();
        }
        // Desocupamos la lista de marcadores
        listMarcadoresMapa.clear();
    }

    @Override
    public LatLng getUbicacionLatLngUsuario() {
        return latLngUbicacionUsuario;
    }

    @Override
    public LatLng getUbicacionLatLngMarker() {
        return latLngUbicacionMarker;
    }

    @Override
    public void setUbicacionLatLngUsuario(LatLng ubicacionUsuario) {
        if (ubicacionUsuario != null) {
            latLngUbicacionUsuario = ubicacionUsuario;
        }
    }

    @Override
    public void setListMarcadoresEnMapa(List<Marker> marcadoresEnMapa) {
        if (marcadoresEnMapa != null) {
            listMarcadoresMapa = marcadoresEnMapa;
        }
    }

    @Override
    public List<InfoLugar> getListInfoLugarDeSitiosCercanos() {
        return listInfoLugarDeSitiosCercanos;
    }

    @Override
    public List<LatLng> getListLatLongDeDeSitiosCercanos() {
        return listLatLngsLugares;
    }

    @Override
    public List<String> getListlugaresGeoQueryResult() {
        return listLugaresGeoQueryResult;
    }

    @Override
    public List<Marker> getListListMarcadoresEnMapa() {
        return listMarcadoresMapa;
    }

    @Override
    public InfoLugar getInfoLugarSeleccionado(String tituloMarcador) {
        InfoLugar lugar = null;
        boolean seEncontro = false;
        for (int x = 0; x < listInfoLugarDeSitiosCercanos.size(); x++) {
            lugar = listInfoLugarDeSitiosCercanos.get(x);
            if (lugar.getNombre().equals(tituloMarcador)) {
                seEncontro = true;
                break;
            }
        }
        if (seEncontro) {
            return lugar;
        } else {
            return null;
        }
    }

    @Override
    public void getConfiguraciones() {
        datosConfiguracion = contextMaps.getSharedPreferences("configuracionesUsuario",
                Context.MODE_PRIVATE);
        editor = datosConfiguracion.edit();
        if (datosConfiguracion != null) {
            modoBusquedaGPS = datosConfiguracion.getBoolean("modoBusquedaGPS", false);
            modoBusquedaMarker = datosConfiguracion.getBoolean("modoBusquedaMarker", false);
            rangoBusquedaVisible = datosConfiguracion.getBoolean("rangoBusquedaVisible", false);
            radioEnKm = Double.parseDouble(datosConfiguracion.getString("rangoBusquedaKm", ""));
        }
    }

    @Override
    public boolean getModoBusquedaGPS() {
        return modoBusquedaGPS;
    }

    @Override
    public boolean getModoBusquedaMarker() {
        return modoBusquedaMarker;
    }

    @Override
    public boolean getRangoBusquedaVisible() {
        return rangoBusquedaVisible;
    }

    @Override
    public double getRangoBusquedaKm() {
        return radioEnKm;
    }

    @Override
    public int getZoomMapaSegunRangoBusquedaKm() {
        double rangoBusqueda = getRangoBusquedaKm();
        if (rangoBusqueda == 0.05) { return 18;}
        else if (rangoBusqueda == 0.1) { return 17;}
        else if (rangoBusqueda == 0.5) {return 15;}
        else if (rangoBusqueda == 1) {return 14;}
        else if (rangoBusqueda == 5) {return 12;}
        else if (rangoBusqueda == 10) {return 11;}
        else if (rangoBusqueda == 50) {return 9;}
        else if (rangoBusqueda == 100) {return 8;}
        else {return 12;}
    }

    @Override
    public void guardarUltimaUbicacionUsuario(LatLng ultimaUbicacion) {
        datoUltimaUbicacionUsuario = contextMaps.getSharedPreferences("datoUltimaUbicacionUsuario",
                Context.MODE_PRIVATE);
        editor = datoUltimaUbicacionUsuario.edit();
        editor.putString("lat_ultimaUbicacion", String.valueOf(ultimaUbicacion.latitude));
        editor.putString("lng_ultimaUbicacion", String.valueOf(ultimaUbicacion.longitude));
        // Hacemos el commit
        editor.commit();
    }

    @Override
    public LatLng getUltimaUbicacionUsuario() {
        datoUltimaUbicacionUsuario = contextMaps.getSharedPreferences("datoUltimaUbicacionUsuario",
                Context.MODE_PRIVATE);
        editor = datoUltimaUbicacionUsuario.edit();
        if (datoUltimaUbicacionUsuario != null) {
            ultimaUbicacionUsuario = new LatLng(
                    Double.parseDouble(datoUltimaUbicacionUsuario
                            .getString("lat_ultimaUbicacion", "")),
                    Double.parseDouble(datoUltimaUbicacionUsuario
                            .getString("lng_ultimaUbicacion", "")));
            return ultimaUbicacionUsuario;
        } else {
            return null;
        }
    }

    @Override
    public BusquedaSeleccionarActividad getDatosBusquedaSeleccionada() {
        datosBusqueda = contextMaps.getSharedPreferences("actividadesSeleccionadasUsuario",
                Context.MODE_PRIVATE);
        editor = datosBusqueda.edit();
        if (datosBusqueda != null) {
            datosBusquedaSeleccionarActividad = new BusquedaSeleccionarActividad(
                    datosBusqueda.getBoolean("areasVerdesSelected", false),
                    datosBusqueda.getBoolean("arteSelected", false),
                    datosBusqueda.getBoolean("cineSelected", false),
                    datosBusqueda.getBoolean("musicaSelected", false),
                    datosBusqueda.getBoolean("restaurantesSelected", false),
                    datosBusqueda.getBoolean("sorprendemeSelected", false)
            );
            return datosBusquedaSeleccionarActividad;
        } else {
            return null;
        }
    }

    @Override
    public GeoFire getGeoFire() {
        return geoFire;
    }

    @Override
    public DatabaseReference getDataBaseReference() {
        return mDatabase;
    }


}
