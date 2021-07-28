package com.developers.parchat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FragmentShowMaps extends Fragment {

    private GoogleMap mMap;

    // Pizza Candelaria 4.596216, -74.069437
    // Nativa Arte y Comida Natural 4.596735, -74.070038
    // Restaurante Rosita 4.597280, -74.069721
    // Natsuki Sushi Bar 4.596704, -74.069411
    // Creamos e inicializamos un arreglo de 2 dimensiones tipo double para guardar latitudes y longitudes
    private double coordenadas[][] = {{4.596216, 4.596735, 4.597280, 4.596704},
            {-74.069437, -74.070038, -74.069721, -74.069411}};
    // Creamos e inicializamos un arreglo tipo String con los nombres de los restaurantes
    private String nomRestaurantes[] = {"Pizza Candelaria", "Nativa Arte y Comida Natural",
            "Restaurante Rosita", "Natsuki Sushi Bar"};
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

                // Recorremos el arreglo de objetos LatLng para ubicar y nombrar marcadores con los restauantes en el mapa
                for (int x = 0; x < restaurantes.length; x++) {
                    // Creamos un objeto LatLng y le pasamos las coordenadas de x restaurante
                    restaurantes[x] = new LatLng(coordenadas[0][x], coordenadas[1][x]);
                    // Añadimos un marcador al mapa en las coordenadas de x restaurante
                    mMap.addMarker(new MarkerOptions().
                            position(restaurantes[x])
                            .title(nomRestaurantes[x])
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapa_restaurante))
                            .anchor(0.0f, 0.0f));
                }
                // Ubicamos la camara de google maps en el primer restaurante
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurantes[0], 18));
            }
        });
        return v;
    }
}

//
/*googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
@Override
public void onMapClick(@NonNull  LatLng latLng) {
        // Esto es para que la persona pueda clickar y que salga un marcador con una leyenda con latitud y longitud
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(latLng.latitude+" : "+latLng.longitude);
        googleMap.clear();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
        latLng, 11
        ));
        }
        });*/
