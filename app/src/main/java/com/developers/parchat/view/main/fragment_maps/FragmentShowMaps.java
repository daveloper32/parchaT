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
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.developers.parchat.model.entity.BusquedaSeleccionarActividad;
import com.developers.parchat.model.entity.InfoLugar;
import com.developers.parchat.view.configuraciones.Configuraciones;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentShowMaps extends Fragment implements FragmentShowMapsMVP.View,
        ActivityCompat.OnRequestPermissionsResultCallback {

    // Variables modelo MVP
    FragmentShowMapsMVP.Presenter presentador;
    FragmentShowMapsMVP.Presenter presentador_restaurantes,
            presentador_areasverdes, presentador_arte, presentador_musica,
            presentador_cine, presentador_sorprendeme;
    //MainActivityMVP.View vistaMainActivity;

    private SupportMapFragment mapFragment;

    // Creamos un objeto LatLng y le pasamos las coordenadas de Bogota
    LatLng bogota = new LatLng(4.60971, -74.08175);

    private GoogleMap mMap;

    private boolean modoBusquedaGPS, modoBusquedaMarcador, rangoBusquedaVisible;

    private Marker marcadorGPS, marcadorPosicion;
    private LatLng latLngUbicacionUsuario, latLngUbicacionMarker;
    private Circle rangoMapa;

    private List<Marker> marcadoresMapa;

    private static final int PERMISSION_REQUEST = 12345;

    private View viewFragmentMaps;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private String nombreUsuarioLogueado;

    private boolean areasVerdesSelected, arteSelected, cineSelected, musicaSelected,
            restaurantesSelected, sorprendemeSelected;

    private List<Boolean> busquedaEncontrada;


    private InfoLugar infoLugarSeleccionado_areasverdes,
            infoLugarSeleccionado_arte,
            infoLugarSeleccionado_cine,
            infoLugarSeleccionado_musica,
            infoLugarSeleccionado_restaurantes,
            infoLugarSeleccionado_sorprendeme;

    private BusquedaSeleccionarActividad datosBusquedaActividad;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Conectamos con el archivo de vista en el cual se desplegara el Fragmento
        View v = inflater.inflate(R.layout.fragment_show_maps_activity_main, container, false);

        // Hacemos la conexion con el objeto Fragment en el archivo de vista cotenido_fragmet_activity_main
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapView_ActivityMain);
        IniciarVista(v);
        viewFragmentMaps = v;
        return v;
    }

    @Override
    public void onStart() {
        presentador = new FragmentShowMapsPresenter(this, "");
        presentador_areasverdes = new FragmentShowMapsPresenter(this, "AreasVerdes");
        presentador_arte = new FragmentShowMapsPresenter(this, "Arte");
        presentador_cine = new FragmentShowMapsPresenter(this, "Cine");
        presentador_musica = new FragmentShowMapsPresenter(this, "Musica");
        presentador_restaurantes = new FragmentShowMapsPresenter(this, "Restaurantes");
        presentador_sorprendeme = new FragmentShowMapsPresenter(this, "Sorprendeme");
        //presentador_areasverdes.ObtenerSnapshotTodaRuta();
        //presentador_arte.ObtenerSnapshotTodaRuta();
        //presentador_cine.ObtenerSnapshotTodaRuta();
        //presentador_musica.ObtenerSnapshotTodaRuta();
        //presentador_restaurantes.ObtenerSnapshotTodaRuta();
        //presentador_sorprendeme.ObtenerSnapshotTodaRuta();
        modoBusquedaGPS = presentador.getModoBusquedaGPSActualizado();
        modoBusquedaMarcador = presentador.getModoBusquedaMarkerActualizado();
        rangoBusquedaVisible = presentador.getRangoBusquedaVisibleActualizado();
        datosBusquedaActividad = presentador.getDatosFromBusquedaSeleccionada();
        if (datosBusquedaActividad != null) {
            areasVerdesSelected = datosBusquedaActividad.isAreasVerdesSelected();
            arteSelected = datosBusquedaActividad.isArteSelected();
            cineSelected = datosBusquedaActividad.isCineSelected();
            musicaSelected = datosBusquedaActividad.isMusicaSelected();
            restaurantesSelected = datosBusquedaActividad.isRestaurantesSelected();
            sorprendemeSelected = datosBusquedaActividad.isSorprendemeSelected();
        }
        busquedaEncontrada = new ArrayList<>();
        super.onStart();
    }

    @Override
    public void onResume() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (getContext() != null && getActivity() != null) {
                if (ContextCompat.checkSelfPermission(
                        getContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
                    // Le solicitamos al LocationManager que verifique el tome el servicio de localizacion
                    final LocationManager manager = (LocationManager) getContext().getSystemService(
                            Context.LOCATION_SERVICE);
                    // Modo GPS ACtivo
                    if (modoBusquedaGPS && !modoBusquedaMarcador) {
                        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            CargarMiUbicacion();
                        } else {
                            VerificarPermisos(getViewFragmentMaps());
                        }
                    }
                }else {
                    VerificarPermisos(getViewFragmentMaps());
                }
            }
        }

        super.onResume();
    }

    private void IniciarVista(View view) {

        rangoMapa = null;
        latLngUbicacionMarker = null;
        marcadoresMapa = new ArrayList<>();
        nombreUsuarioLogueado = "";
        //cargaUbicacionInicial = new LatLng(4.657160262597251, -74.05605940861399);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            nombreUsuarioLogueado = bundle
                    .getString("nombreUsuario", "");
        }

        if (getContext() != null) {
            fusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(getContext());
        }
        // Que pasa cuando se carga el mapa en la app
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            // Cuando ya esta listo para funcionar

            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                // Renombramos googleMap por mMap -> literal es como el obejto del mapa de Maps
                mMap = googleMap;
                // Activamos parametros de intereccion con el mapa
                // Zoom in & Zoom out
                mMap.getUiSettings().setZoomControlsEnabled(true);

                if (!modoBusquedaGPS && modoBusquedaMarcador) {
                    //mMap.setMyLocationEnabled(false);
                    // Marcador Inicial
                    latLngUbicacionMarker = presentador.getUltimaUbicacionUsuario();
                    if (latLngUbicacionMarker != null) {
                        CargarMarcadorUbicacionInicial(latLngUbicacionMarker);
                        // Si se presiona un marcador -> Mostramos informacion del sitio en un Bottom Sheet Dialog
                        AbrirBSDdeInformacionSitio();
                        // Hacemos una nueva busqueda y despliegue de marcadores donde sea que la persona agregue un marcador
                        NuevaBusquedaAlDarClickSobreElMapa();
                    }
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
                .title(nombreUsuarioLogueado)
                .visible(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .zIndex(1.0f));
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
            presentador.setUltimaUbicacionUsuario(latLngMiUbicacion);
            latLngUbicacionUsuario = latLngMiUbicacion;
            addMarkerMiUbicacion(latLngMiUbicacion);
        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            if (location != null) {
                actualizarMiUbicacion(location);
                AbrirBSDdeInformacionSitio();
                NuevaBusquedaEnMiUbicacion();
            }
        }
        @Override
        public void onProviderEnabled(@NonNull String provider) {
            if (getContext() != null) {
                Toast.makeText(getContext(),
                        R.string.msgToast_MainActivity_4,
                        Toast.LENGTH_LONG).show();
            }
            CargarMiUbicacion();

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            //verificarGPSEncendido();
            if (getContext() != null) {
                Toast.makeText(getContext(),
                        R.string.msgToast_MainActivity_5,
                        Toast.LENGTH_LONG).show();
            }
            verificarGPSEncendido();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //verificarGPSEncendido();
            if (getContext() != null) {
            }
        }
    };

    @SuppressLint("MissingPermission")
    private void CargarMiUbicacion() {
        if (getContext() != null) {
            LocationManager locationManager = (LocationManager) getContext()
                    .getSystemService(Context.LOCATION_SERVICE);
            // Verificamos si esta encendido o no
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

                fusedLocationProviderClient.getLastLocation()
                        .addOnCompleteListener(new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                // Obtenemos el objeto Location
                                Location ubicacionActual = task.getResult();
                                if (ubicacionActual != null) {
                                    actualizarMiUbicacion(ubicacionActual);
                                    AbrirBSDdeInformacionSitio();
                                    NuevaBusquedaEnMiUbicacion();
                                } else {
                                    LocationRequest locationRequest = new LocationRequest()
                                            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                            .setInterval(10000)
                                            .setFastestInterval(1000)
                                            .setNumUpdates(1);

                                    LocationCallback locationCallback = new LocationCallback() {
                                        @Override
                                        public void onLocationResult(@NonNull LocationResult locationResult) {
                                            Location ubicacionActual = locationResult.getLastLocation();
                                            if (ubicacionActual != null) {
                                                actualizarMiUbicacion(ubicacionActual);
                                                AbrirBSDdeInformacionSitio();
                                                NuevaBusquedaEnMiUbicacion();
                                            }
                                        }
                                    };
                                    fusedLocationProviderClient
                                            .requestLocationUpdates(
                                                    locationRequest,
                                                    locationCallback,
                                                    Looper.myLooper());
                                }
                            }
                        });
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        15000, 10, locationListener);
            } else {
                verificarGPSEncendido();
            }
        }
    }

    private void VerificarPermisos(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (getContext() != null && getActivity() != null) {
                if (ContextCompat.checkSelfPermission(
                        getContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
                    // You can use the API that requires the permission.
                    if (getContext() != null) {
                        // Le solicitamos al LocationManager que verifique el tome el servicio de localizacion
                        final LocationManager manager = (LocationManager) getContext().getSystemService(
                                Context.LOCATION_SERVICE);
                        // Verificamos si esta encendido o no
                        // Si no esta encendido lanzamos en pantalla un AlertDialog
                        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            CargarMiUbicacion();
                        } else {
                            verificarGPSEncendido();
                        }
                    }


                } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION) ) {
                    // In an educational UI, explain to the user why your app requires this
                    // permission for a specific feature to behave as expected. In this UI,
                    // include a "cancel" or "no thanks" button that allows the user to
                    // continue using your app without granting the permission.
                    Snackbar.make(view,
                            R.string.msgSnackbar_MainActivity_2,
                            Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.msgSnackbar_MainActivity_2_okButton, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    requestPermissionLauncher.launch(
                                            Manifest.permission.ACCESS_FINE_LOCATION);
                                }
                            }).show();
                } else {
                    // You can directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.
                    requestPermissionLauncher.launch(
                            Manifest.permission.ACCESS_FINE_LOCATION);
                }
            }

        }

    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    if (getContext() != null) {
                        Toast.makeText(getContext(),
                                R.string.msgToast_MainActivity_2,
                                Toast.LENGTH_SHORT).show();
                        verificarGPSEncendido();
                        CargarMiUbicacion();
                    }
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                    if (getContext() != null) {
                        Toast.makeText(getContext(),
                                R.string.msgToast_MainActivity_3,
                                Toast.LENGTH_SHORT).show();
                        irAlActivitySeleccionarActividad(SeleccionarActividad.class);
                    }
                }
            });


    private void verificarGPSEncendido() {
        if (getContext() != null) {
            // Le solicitamos al LocationManager que verifique el tome el servicio de localizacion
            final LocationManager manager = (LocationManager) getContext().getSystemService(
                    Context.LOCATION_SERVICE);
            // Verificamos si esta encendido o no
            // Si no esta encendido lanzamos en pantalla un AlertDialog
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setMessage(R.string.msgAlertDiag_MainActivity_1)
                        .setPositiveButton(R.string.msgAlertDiag_MainActivity_1_positive,
                                (dialog, id) -> {
                                        startActivity(
                                        new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        })
                        .setNegativeButton(R.string.msgAlertDiag_MainActivity_1_negative,
                                (dialog, id) -> {
                                    irAlActivitySeleccionarActividad(SeleccionarActividad.class);
                                    dialog.cancel();
                                })
                        .setCancelable(false);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                VerificarPermisos(getViewFragmentMaps());
            }
        }
    }

    private View getViewFragmentMaps() {
        return viewFragmentMaps;
    }

    private void CargarMarcadorUbicacionInicial(LatLng cargaUbicacionInicial) {
        // Marcador inicial
        marcadorPosicion = mMap.addMarker(new MarkerOptions()
                .position(latLngUbicacionMarker)
                .title(nombreUsuarioLogueado)
                .visible(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .zIndex(1.0f));

        //
        //latLngUbicacionMarker = cargaUbicacionInicial;
        if (rangoBusquedaVisible) {
            drawRangoMapa(latLngUbicacionMarker);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cargaUbicacionInicial,
                presentador.getZoomMapaSegunRangoBusquedaKm()));
        // Le decimos al presentador que busque Sitios Cercanos al Marcador
        if (areasVerdesSelected) {
            presentador_areasverdes.busquedaSitiosCercanosAMarcador();
        }
        if (arteSelected) {
            presentador_arte.busquedaSitiosCercanosAMarcador();
        }
        if (cineSelected) {
            presentador_cine.busquedaSitiosCercanosAMarcador();
        }
        if (musicaSelected) {
            presentador_musica.busquedaSitiosCercanosAMarcador();
        }
        if (restaurantesSelected) {
            presentador_restaurantes.busquedaSitiosCercanosAMarcador();
        }
        if (sorprendemeSelected) {
            presentador_sorprendeme.busquedaSitiosCercanosAMarcador();
        }
        if (!areasVerdesSelected) {
            presentador_areasverdes.busquedaSitiosCercanosAMarkerPositionNoEncontrados();
        }
        if (!arteSelected) {
            presentador_arte.busquedaSitiosCercanosAMarkerPositionNoEncontrados();
        }
        if (!cineSelected) {
            presentador_cine.busquedaSitiosCercanosAMarkerPositionNoEncontrados();
        }
        if (!musicaSelected) {
            presentador_musica.busquedaSitiosCercanosAMarkerPositionNoEncontrados();
        }
        if (!restaurantesSelected) {
            presentador_restaurantes.busquedaSitiosCercanosAMarkerPositionNoEncontrados();
        }
        if (!sorprendemeSelected) {
            presentador_sorprendeme.busquedaSitiosCercanosAMarkerPositionNoEncontrados();
        }
    }

    private void drawRangoMapa(LatLng latLngUbicacionMarkerOUsuario) {
        rangoMapa = mMap.addCircle(new CircleOptions()
                .center(latLngUbicacionMarkerOUsuario)
                .radius(presentador.getRangoDeBusquedaEnMActualizado())
                .strokeColor(getContext().getResources().getColor(R.color.orange))
                .fillColor(getContext().getResources().getColor(R.color.orange_dif)));
    }

    private void NullInfoLugaresObjects() {
        infoLugarSeleccionado_areasverdes = null;
        infoLugarSeleccionado_arte = null;
        infoLugarSeleccionado_cine = null;
        infoLugarSeleccionado_musica = null;
        infoLugarSeleccionado_restaurantes = null;
        infoLugarSeleccionado_sorprendeme = null;
    }

    private void AbrirBSDdeInformacionSitio() {
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull @NotNull Marker marker) {
                String tituloMarcador = marker.getTitle();

                if (!tituloMarcador.equals(nombreUsuarioLogueado) && !tituloMarcador.equals("")) {

                    NullInfoLugaresObjects();

                    infoLugarSeleccionado_areasverdes = presentador_areasverdes.getInfoLugarSeleccionadoEnMapa(tituloMarcador);
                    infoLugarSeleccionado_arte = presentador_arte.getInfoLugarSeleccionadoEnMapa(tituloMarcador);
                    infoLugarSeleccionado_cine = presentador_cine.getInfoLugarSeleccionadoEnMapa(tituloMarcador);
                    infoLugarSeleccionado_musica = presentador_musica.getInfoLugarSeleccionadoEnMapa(tituloMarcador);
                    infoLugarSeleccionado_restaurantes = presentador_restaurantes.getInfoLugarSeleccionadoEnMapa(tituloMarcador);
                    infoLugarSeleccionado_sorprendeme = presentador_sorprendeme.getInfoLugarSeleccionadoEnMapa(tituloMarcador);

                    if (infoLugarSeleccionado_areasverdes != null) {
                        ((MainActivity) getActivity())
                                .iniciarBottomSheetDialog(
                                        infoLugarSeleccionado_areasverdes
                                );
                    } else if (infoLugarSeleccionado_arte != null) {
                        ((MainActivity) getActivity())
                                .iniciarBottomSheetDialog(
                                        infoLugarSeleccionado_arte
                                );
                    } else if (infoLugarSeleccionado_cine != null) {
                        ((MainActivity) getActivity())
                                .iniciarBottomSheetDialog(
                                        infoLugarSeleccionado_cine
                                );
                    } else if (infoLugarSeleccionado_musica != null) {
                        ((MainActivity) getActivity())
                                .iniciarBottomSheetDialog(
                                        infoLugarSeleccionado_musica
                                );
                    } else if (infoLugarSeleccionado_restaurantes != null) {
                        ((MainActivity) getActivity())
                                .iniciarBottomSheetDialog(
                                        infoLugarSeleccionado_restaurantes
                                );
                    } else if (infoLugarSeleccionado_sorprendeme != null) {
                        ((MainActivity) getActivity())
                                .iniciarBottomSheetDialog(
                                        infoLugarSeleccionado_sorprendeme
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
            public void onMapLongClick(@NonNull @NotNull LatLng latLngMarker) {
                if (marcadorPosicion != null) {
                    marcadorPosicion.remove();
                }
                marcadorPosicion = mMap.addMarker(new MarkerOptions()
                        .position(latLngMarker)
                        .title(nombreUsuarioLogueado)
                        .visible(true)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        .zIndex(1.0f));
                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(new CameraPosition.Builder()
                                .target(latLngMarker)
                                .zoom(presentador.getZoomMapaSegunRangoBusquedaKm())
                                .build()
                        )
                );
                BorrarMarcadores();
                marcadoresMapa.clear();
                // Hacemos la busqueda de los sitios cercanos en un radio x de kilometros
                //
                latLngUbicacionMarker = latLngMarker;
                presentador.setUltimaUbicacionUsuario(latLngUbicacionMarker);

                busquedaEncontrada.clear();

                if (areasVerdesSelected) {
                    presentador_areasverdes.busquedaSitiosCercanosAMarcador();
                }
                if (arteSelected) {
                    presentador_arte.busquedaSitiosCercanosAMarcador();
                }
                if (cineSelected) {
                    presentador_cine.busquedaSitiosCercanosAMarcador();
                }
                if (musicaSelected) {
                    presentador_musica.busquedaSitiosCercanosAMarcador();
                }
                if (restaurantesSelected) {
                    presentador_restaurantes.busquedaSitiosCercanosAMarcador();
                }
                if (sorprendemeSelected) {
                    presentador_sorprendeme.busquedaSitiosCercanosAMarcador();
                }
                if (!areasVerdesSelected) {
                    presentador_areasverdes.busquedaSitiosCercanosAMarkerPositionNoEncontrados();
                }
                if (!arteSelected) {
                    presentador_arte.busquedaSitiosCercanosAMarkerPositionNoEncontrados();
                }
                if (!cineSelected) {
                    presentador_cine.busquedaSitiosCercanosAMarkerPositionNoEncontrados();
                }
                if (!musicaSelected) {
                    presentador_musica.busquedaSitiosCercanosAMarkerPositionNoEncontrados();
                }
                if (!restaurantesSelected) {
                    presentador_restaurantes.busquedaSitiosCercanosAMarkerPositionNoEncontrados();
                }
                if (!sorprendemeSelected) {
                    presentador_sorprendeme.busquedaSitiosCercanosAMarkerPositionNoEncontrados();
                }
            }
        });
    }

    private void  NuevaBusquedaEnMiUbicacion() {

        BorrarMarcadores();
        marcadoresMapa.clear();
        if (busquedaEncontrada.size() != 0) {
            busquedaEncontrada.clear();
        }


        if (areasVerdesSelected) {
            presentador_areasverdes.busquedaSitiosCercanosAUsuario();
        }
        if (arteSelected) {
            presentador_arte.busquedaSitiosCercanosAUsuario();
        }
        if (cineSelected) {
            presentador_cine.busquedaSitiosCercanosAUsuario();
        }
        if (musicaSelected) {
            presentador_musica.busquedaSitiosCercanosAUsuario();
        }
        if (restaurantesSelected) {
            presentador_restaurantes.busquedaSitiosCercanosAUsuario();
        }
        if (sorprendemeSelected) {
            presentador_sorprendeme.busquedaSitiosCercanosAUsuario();
        }

        if (!areasVerdesSelected) {
            presentador_areasverdes.busquedaSitiosCercanosAUsuarioPositionNoEncontrados();
        }
        if (!arteSelected) {
            presentador_arte.busquedaSitiosCercanosAUsuarioPositionNoEncontrados();
        }
        if (!cineSelected) {
            presentador_cine.busquedaSitiosCercanosAUsuarioPositionNoEncontrados();
        }
        if (!musicaSelected) {
            presentador_musica.busquedaSitiosCercanosAUsuarioPositionNoEncontrados();
        }
        if (!restaurantesSelected) {
            presentador_restaurantes.busquedaSitiosCercanosAUsuarioPositionNoEncontrados();
        }
        if (!sorprendemeSelected) {
            presentador_sorprendeme.busquedaSitiosCercanosAUsuarioPositionNoEncontrados();
        }
    }

    private void BorrarMarcadores () {
        if (marcadoresMapa.size() != 0) {
            for (int x = 0; x < marcadoresMapa.size(); x++) {
                Marker m = marcadoresMapa.get(x);
                m.remove();
            }
        }
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
                String tipoLugar = lugar.getTipositio();
                switch (tipoLugar) {
                    case ("areasverdes"):
                        // Añadimos un marcador al mapa en las coordenadas de x restaurante
                        marcadorSitioCercano = mMap.addMarker(new MarkerOptions()
                                .position(latLngLugarList.get(x))
                                .title(lugar.getNombre())
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapa_areasverdes))
                        );
                        // Añadimos el marcador a la lista de marcadores para luego borrarlos
                        marcadoresMapa.add(marcadorSitioCercano);
                        break;
                    case ("arte"):
                        // Añadimos un marcador al mapa en las coordenadas de x restaurante
                        marcadorSitioCercano = mMap.addMarker(new MarkerOptions()
                                .position(latLngLugarList.get(x))
                                .title(lugar.getNombre())
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapa_arte))
                        );
                        // Añadimos el marcador a la lista de marcadores para luego borrarlos
                        marcadoresMapa.add(marcadorSitioCercano);
                        break;
                    case ("cine"):
                        // Añadimos un marcador al mapa en las coordenadas de x restaurante
                        marcadorSitioCercano = mMap.addMarker(new MarkerOptions()
                                .position(latLngLugarList.get(x))
                                .title(lugar.getNombre())
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapa_cine))
                        );
                        // Añadimos el marcador a la lista de marcadores para luego borrarlos
                        marcadoresMapa.add(marcadorSitioCercano);
                        break;
                    case ("musica"):
                        // Añadimos un marcador al mapa en las coordenadas de x restaurante
                        marcadorSitioCercano = mMap.addMarker(new MarkerOptions()
                                .position(latLngLugarList.get(x))
                                .title(lugar.getNombre())
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapa_musica))
                        );
                        // Añadimos el marcador a la lista de marcadores para luego borrarlos
                        marcadoresMapa.add(marcadorSitioCercano);
                        break;
                    case ("restaurante"):
                        // Añadimos un marcador al mapa en las coordenadas de x restaurante
                        marcadorSitioCercano = mMap.addMarker(new MarkerOptions()
                                .position(latLngLugarList.get(x))
                                .title(lugar.getNombre())
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapa_restaurante))
                        );
                        // Añadimos el marcador a la lista de marcadores para luego borrarlos
                        marcadoresMapa.add(marcadorSitioCercano);
                        break;
                    case ("sorprendeme"):
                        // Añadimos un marcador al mapa en las coordenadas de x restaurante
                        marcadorSitioCercano = mMap.addMarker(new MarkerOptions()
                                .position(latLngLugarList.get(x))
                                .title(lugar.getNombre())
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapa_sorpresa))
                        );
                        // Añadimos el marcador a la lista de marcadores para luego borrarlos
                        marcadoresMapa.add(marcadorSitioCercano);
                        break;
                }




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
                if (rangoBusquedaVisible) {
                    drawRangoMapa(latLngUsuario);
                }
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
    public void showSnackbarBusquedaSitiosCercanosNoEncontrados() {
        // Mostramos en pantalla que no se encontraron sitios cercanos a la posicion
        if (getContext() != null) {
            Snackbar.make(getViewFragmentMaps(),
                    R.string.msgSnackbar_MainActivity_1,
                    Snackbar.LENGTH_LONG)
                    .setAction(R.string.msgSnackbar_MainActivity_1_irButton
                            , new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            irAlActivityConfiguraciones(Configuraciones.class);
                        }
                    }).show();
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
                 .title(nombreUsuarioLogueado)
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

    @Override
    public void irAlActivityConfiguraciones(Class<? extends AppCompatActivity> ir_a_Configuraciones) {
        //progressBar_Login.setVisibility(View.GONE);
        // Creamos un objeto de la clase Intent para que al presionar el boton vayamos al Activity SeleccionarActividad
        Intent deMainActivityAConfiguraciones = new Intent(getActivity(), ir_a_Configuraciones);
        // Iniciamos el Activity SeleccionarActividad
        startActivity(deMainActivityAConfiguraciones);
    }

    @Override
    public void saveBusquedaEncontrada (boolean unaBusqueda) {
        busquedaEncontrada.add(unaBusqueda);
    }
    @Override
    public boolean verificarBusquedaEncontrada() {
        if (busquedaEncontrada.size() != 0) {
            if (busquedaEncontrada.size() == 6) {
                if (!busquedaEncontrada.get(0)
                        && !busquedaEncontrada.get(1)
                        && !busquedaEncontrada.get(2)
                        && !busquedaEncontrada.get(3)
                        && !busquedaEncontrada.get(4)
                        && !busquedaEncontrada.get(5)
                ) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
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