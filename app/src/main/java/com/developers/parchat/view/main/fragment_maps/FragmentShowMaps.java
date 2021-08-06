package com.developers.parchat.view.main.fragment_maps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.developers.parchat.model.entity.InfoLugar;
import com.developers.parchat.view.main.bt_st_dlg_ifo_lugar.BottomSheetDialog_InfoLugar;
import com.developers.parchat.view.main.MainActivity;
import com.developers.parchat.view.main.MainActivityMVP;
import com.developers.parchat.view.main.MainActivityPresenter;
import com.developers.parchat.view.registro.RegistroMVP;
import com.developers.parchat.view.seleccionar_actividad.SeleccionarActividad;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import com.developers.parchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentShowMaps extends Fragment implements FragmentShowMapsMVP.View {

    // Variables modelo MVP
    FragmentShowMapsMVP.Presenter presentador;
    MainActivityMVP.View vistaMainActivity;

    // Creamos un objeto LatLng y le pasamos las coordenadas de Bogota
    LatLng bogota = new LatLng(4.60971, -74.08175);

    private GoogleMap mMap;

    private boolean modoBusquedaGPS, modoBusquedaMarcador;

    private Marker marcadorGPS, marcadorPosicion;
    private LatLng latLngUbicacionUsuario, latLngUbicacionMarker;
    private Circle rangoMapa;

    private List<Marker> marcadoresMapa;

    private static final int PERMISSION_REQUEST = 12345;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Conectamos con el archivo de vista en el cual se desplegara el Fragmento
        View v = inflater.inflate(R.layout.fragment_show_maps_activity_main, container, false);

        // Hacemos la conexion con el objeto Fragment en el archivo de vista cotenido_fragmet_activity_main
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapView_ActivityMain);

        IniciarVista(mapFragment);

        return v;
    }

    private void IniciarVista(SupportMapFragment mapFragment) {

        vistaMainActivity = new MainActivity();
        // Inicializamos el presentador
        rangoMapa = null;
        presentador = new FragmentShowMapsPresenter(this, "Restaurantes");
        latLngUbicacionMarker = null;
        marcadoresMapa = new ArrayList<>();

        // Que pasa cuando se carga el mapa en la app
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            // Cuando ya esta listo para funcionar
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                // Renombramos googleMap por mMap -> literal es como el obejto del mapa de Maps
                mMap = googleMap;
                // Activamos parametros de intereccion con el mapa
                // Zoom in & Zoom out
                mMap.getUiSettings().setZoomControlsEnabled(true);

                modoBusquedaGPS = presentador.getModoBusquedaGPSActualizado();
                modoBusquedaMarcador = presentador.getModoBusquedaMarkerActualizado();



                // Modo GPS ACtivo
                if (modoBusquedaGPS && !modoBusquedaMarcador) {
                    //mMap.setMyLocationEnabled(true);
                    verificarGPSEncendido();
                    CargarMiUbicacion();
                    // Si se presiona un marcador -> Mostramos informacion del sitio en un Bottom Sheet Dialog
                    AbrirBSDdeInformacionSitio();
                    NuevaBusquedaEnMiUbicacion();


                }
                // Modo Marker Activo
                if (!modoBusquedaGPS && modoBusquedaMarcador) {
                    //mMap.setMyLocationEnabled(false);
                    // Marcador Inicial
                    LatLng cargaUbicacionInicial = new LatLng(4.657160262597251, -74.05605940861399);
                    CargarMarcadorUbicacionInicial(cargaUbicacionInicial);
                    // Si se presiona un marcador -> Mostramos informacion del sitio en un Bottom Sheet Dialog
                    AbrirBSDdeInformacionSitio();
                    // Hacemos una nueva busqueda y despliegue de marcadores donde sea que la persona agregue un marcador
                    NuevaBusquedaAlDarClickSobreElMapa();
                }
            }
        });
    }

    private void addMarkerMiUbicacion(LatLng miUbicacionGPS) {
        if (marcadorGPS != null) {
            marcadorGPS.remove();
        }
        marcadorGPS = mMap.addMarker(new MarkerOptions()
                .position(miUbicacionGPS)
                .title("¡Mi Ubicación!")
                .visible(true));
        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(new CameraPosition.Builder()
                        .target(miUbicacionGPS)
                        .zoom(presentador.getZoomMapaSegunRangoBusquedaKm())
                        .build()
                )
        );
    }

    private void actualizarMiUbicacion(Location location) {
        if (location != null) {
            LatLng latLngMiUbicacion = new LatLng(location.getLatitude(),
                    location.getLongitude());
            latLngUbicacionUsuario = latLngMiUbicacion;
            addMarkerMiUbicacion(latLngMiUbicacion);
        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            actualizarMiUbicacion(location);
            NuevaBusquedaEnMiUbicacion();
        }
    };

    private void CargarMiUbicacion() {

        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_WIFI_STATE
            }, PERMISSION_REQUEST);
            return;
        }
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        actualizarMiUbicacion(location);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                15000, 0, locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext()
                            ,R.string.msgToast_MainActivity_3
                            , Toast.LENGTH_LONG)
                            .show();
                    irAlActivitySeleccionarActividad(SeleccionarActividad.class);
                }
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void verificarGPSEncendido() {
        // Le solicitamos al LocationManager que verifique el tome el servicio de localizacion
        final LocationManager manager = (LocationManager) getContext().getSystemService(
                Context.LOCATION_SERVICE);
        // Verificamos si esta encendido o no
        // Si no esta encendido lanzamos en pantalla un AlertDialog
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            builder.setMessage(R.string.msgAlertDiag_MainActivity_1)
                    .setPositiveButton(R.string.msgAlertDiag_MainActivity_1_positive,
                            (dialog, id) -> startActivity(
                                    new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            ))
                    .setNegativeButton(R.string.msgAlertDiag_MainActivity_1_negative,
                            (dialog, id) -> {
                                irAlActivitySeleccionarActividad(SeleccionarActividad.class);
                                dialog.cancel();
                            })
                    .setCancelable(false);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }




    private void CargarMarcadorUbicacionInicial(LatLng cargaUbicacionInicial) {
        // Marcador inicial
        marcadorPosicion = mMap.addMarker(new MarkerOptions()
                .position(cargaUbicacionInicial)
                .title("¡Estas aquí!")
                .visible(true));
        //
        latLngUbicacionMarker = cargaUbicacionInicial;
        rangoMapa = mMap.addCircle(new CircleOptions()
                .center(latLngUbicacionMarker)
                .radius(presentador.getRangoDeBusquedaEnMActualizado())
                .strokeColor(R.color.orange)
                .fillColor(R.color.orange_dif));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cargaUbicacionInicial, 15));
        // Le decimos al presentador que busque Sitios Cercanos al Marcador
        presentador.busquedaSitiosCercanosAMarcador();
    }

    private void AbrirBSDdeInformacionSitio() {
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull @NotNull Marker marker) {
                String tituloMarcador = marker.getTitle();

                if (!tituloMarcador.equals("¡Estas aquí!") && !tituloMarcador.equals("¡Mi Ubicación!")) {
                    String direccion = null;

                    InfoLugar infoLugarSeleccionado = presentador.getInfoLugarSeleccionadoEnMapa(tituloMarcador);
                    if (infoLugarSeleccionado != null) {
                        ((MainActivity) getActivity())
                                .iniciarBottomSheetDialog(
                                        infoLugarSeleccionado
                                );
                    } else {
                        ((MainActivity) getActivity())
                                .iniciarBottomSheetDialog(null);
                    }
                }
                return false;
            }
        });
    }

    private void NuevaBusquedaAlDarClickSobreElMapa() {
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull @NotNull LatLng latLngUsuario) {
                if (marcadorPosicion != null) {
                    marcadorPosicion.remove();
                }
                marcadorPosicion = mMap.addMarker(new MarkerOptions()
                        .position(latLngUsuario)
                        .title("¡Estas aquí!")
                        .visible(true));
                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(new CameraPosition.Builder()
                                .target(latLngUsuario)
                                .zoom(presentador.getZoomMapaSegunRangoBusquedaKm())
                                .build()
                        )
                );
                // Hacemos la busqueda de los sitios cercanos en un radio x de kilometros
                //
                latLngUbicacionMarker = latLngUsuario;
                presentador.busquedaSitiosCercanosAMarcador();
            }
        });
    }

    private void  NuevaBusquedaEnMiUbicacion() {
        presentador.busquedaSitiosCercanosAUsuario();
    }

    @Override
    public void addMarkersSitiosCercanos(LatLng latLngUsuario, List<InfoLugar> infoLugarList, List<LatLng> latLngLugarList) {

        // Escondemos el circle del rango anterior
        if (rangoMapa != null) {
            rangoMapa.setVisible(false);
        }
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
                if (getContext() != null) {
                    // Enviamos un mensaje emergente diciendo que la conexion fallo
                    Toast.makeText(getContext(),
                            R.string.msgToast_MainActivity_1,
                            Toast.LENGTH_LONG).show();
                }
            }
        }
        presentador.agregarListaDeMarcadoresEnMapa(marcadoresMapa);
        // Si el objeto latlong en la posicion 0 del arreglo restaurantes no esta vacio
        if (latLngUsuario != null) {
            if (getContext() != null) {
                rangoMapa = mMap.addCircle(new CircleOptions()
                        .center(latLngUsuario)
                        .radius(presentador.getRangoDeBusquedaEnMActualizado())
                        .strokeColor(getContext().getResources().getColor(R.color.orange))
                        .fillColor(getContext().getResources().getColor(R.color.orange_dif)));
                // Ubicamos la camara de google maps en el primer restaurante
                //mMap.moveCame#FF5328ra(CameraUpdateFactory.newLatLngZoom(latLngUsuario, 15));
                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(new CameraPosition.Builder()
                                .target(latLngUsuario)
                                .zoom(presentador.getZoomMapaSegunRangoBusquedaKm())
                                .build()
                        )
                );
            }
            // SI no
        } else {
            // Ubicamos la camara de google maps en la ciudad en que viva
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bogota, 11));
        }
    }

    @Override
    public LatLng getUsuarioPosition() {
        return latLngUbicacionUsuario;
    }

    @Override
    public LatLng getMarkerPosition() {
        return latLngUbicacionMarker;
    }

    @Override
    public void showToastBusquedaSitiosCercanosNoEncontrados() {
        // Mostramos en pantalla que no se encontraron sitios cercanos a la posicion
        if (getContext() != null) {
            Toast.makeText(getContext(),
                    R.string.msgToast_MainActivity_2,
                    Toast.LENGTH_LONG).show();
        }
        if (rangoMapa != null) {
            rangoMapa.setVisible(false);
        }
    }

    @Override
    public void showToastBusquedaSitiosCercanosFallo(DatabaseError error) {
        if (getContext() != null) {
            Toast.makeText(getContext(),
                    "Error: " + error,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void addMarkerUbicacionGPS(LatLng ubicacionActualLatLong) {

         marcadorGPS = mMap.addMarker(new MarkerOptions()
                 .position(ubicacionActualLatLong)
                 .title("¡GPS!")
                 .visible(true));

    }

    @Override
    public void irAlActivitySeleccionarActividad(Class<? extends AppCompatActivity> ir_a_SeleccionarActividad) {
        //progressBar_Login.setVisibility(View.GONE);
        // Creamos un objeto de la clase Intent para que al presionar el boton vayamos al Activity SeleccionarActividad
        Intent deMainActivityASeleccionarActividad = new Intent(getActivity(), ir_a_SeleccionarActividad);
        // Iniciamos el Activity SeleccionarActividad
        startActivity(deMainActivityASeleccionarActividad);

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