package com.developers.parchat.view.main.fragment_maps;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import java.util.List;
import com.developers.parchat.R;

public class FragmentShowMaps extends Fragment {

    private GoogleMap mMap;

    // Pizza Candelaria 4.596205, -74.069449
    // Nativa Arte y Comida Natural 4.596744, -74.070033
    // Restaurante Rosita 4.597280, -74.069721
    // Natsuki Sushi Bar 4.596704, -74.069411
    // Creamos e inicializamos un arreglo de 2 dimensiones tipo double para guardar latitudes y longitudes
    private double coordenadas[][] = {{4.596205, 4.596744, 4.597280, 4.596704},
            {-74.069449, -74.070033, -74.069721, -74.069411}};
    // Creamos e inicializamos un arreglo tipo String con los nombres de los restaurantes
    private String nomRestaurantes[] = {"Crepes & Waffles Calle 73", "Restaurante Peruano - El Indio de Machu Picchu",
            "Restaurante 69 Gauchos",
            "Harry Sasson"};
    private String dirRestaurantes[] = {"Carrera 9, Cl. 73 # 73 - 33, Bogotá",
            "Carrera 10 # 72 - 32, Localidad de Chapinero, Bogotá",
            "Calle 69a #10 - 16, Bogotá, Cundinamarca",
            "Carrera 9 # 75 - 70, Bogotá"};
    // Creamos un arreglo de objetos LatLong de 4 posiciones
    private LatLng restaurantes[] = new LatLng[4];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Conectamos con el archivo de vista en el cual se desplegara el Fragmento
        View v = inflater.inflate(R.layout.fragment_show_maps_activity_main, container, false);

        // Hacemos la conexion con el objeto Fragment en el archivo de vista cotenido_fragmet_activity_main
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapView_ActivityMain);

        // Que pasa cuando se carga el mapa en la app
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            // Cuando ya esta listo para funcionar
            @Override
            public void onMapReady(@NonNull  GoogleMap googleMap) {
                // Renombramos googleMap por mMap -> literal es como el obejto del mapa de Maps
                mMap = googleMap;

                // Creamos un objeto LatLng y le pasamos las coordenadas de Bogota
                LatLng bogota = new LatLng(4.60971, -74.08175);

                // Recorremos el arreglo de objetos LatLng para ubicar y nombrar marcadores con los restauantes en el mapa
                for (int x = 0; x < restaurantes.length; x++) {
                    // Creamos un objeto LatLng y le pasamos las coordenadas de x restaurante
                    //restaurantes[x] = new LatLng(coordenadas[0][x], coordenadas[1][x]);
                    restaurantes[x] = obtenerLatLongDeDireccion(dirRestaurantes[x]);
                    // Solo si el metodo obtenerLatLongDeDireccion nos devuelve las coordenadas
                    // vamos a dibujar el marcador
                    if (restaurantes[x] != null) {
                        // Añadimos un marcador al mapa en las coordenadas de x restaurante
                        mMap.addMarker(new MarkerOptions().
                                position(restaurantes[x])
                                .title(nomRestaurantes[x])
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
                if (restaurantes[0] != null) {
                    // Ubicamos la camara de google maps en el primer restaurante
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurantes[0], 15));
                // SI no
                } else {
                    // Ubicamos la camara de google maps en la ciudad en que viva
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bogota, 11));
                }

            }
        });
        return v;
    }

    public LatLng obtenerLatLongDeDireccion(String direccion) {
        // https://developer.android.com/reference/android/location/Geocoder
        // Creamos un objeto de la clase Geocoder y le apsamos el contexto
        Geocoder geocodificador = new Geocoder(getContext());
        // Creamos una lista de objetos tipo Address
        List<Address> direccionesEncontradas;
        // Inicializamos un objeto LatLong nulo
        LatLng latLng_Direccion = null;

        try {
            // Intentamos obtener la latitud y longitud de la direccion ingresada en base a 5 busquedas
            direccionesEncontradas = geocodificador.getFromLocationName(direccion, 5);
            if (!direccionesEncontradas.isEmpty()) {
                //
                Address direccionEncontrada = direccionesEncontradas.get(0);
                // EScribimos el objeto tipo LatLng
                latLng_Direccion = new LatLng(direccionEncontrada.getLatitude(),
                        direccionEncontrada.getLongitude());
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return latLng_Direccion;
    }
}