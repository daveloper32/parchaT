package com.developers.parchat.view.seleccionar_actividad;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.developers.parchat.R;

import com.developers.parchat.view.main.MainActivity;

public class SeleccionarActividad extends AppCompatActivity implements View.OnClickListener {

    // Declaramos objetos CardView, Button y ProgressBar
    private androidx.cardview.widget.CardView cV_SeleccionarActividad_restaurantes, cV_SeleccionarActividad_musica,
            cV_SeleccionarActividad_cine, cV_SeleccionarActividad_areasVerdes,
            cV_SeleccionarActividad_arte, cV_SeleccionarActividad_sorpresa;
    private LinearLayout LL_SeleccionarActividad_restaurantes, LL_SeleccionarActividad_musica,
            LL_SeleccionarActividad_cine, LL_SeleccionarActividad_areasVerdes,
            LL_SeleccionarActividad_arte, LL_SeleccionarActividad_sorpresa;
    private Button b_SeleccionarActividad_salir, b_SeleccionarActividad_continuar;

    private boolean restaurantesSelected, musicaSelected,
            cineSelected, areasVerdesSelected,
            arteSelected, sorprendemeSelected;

    // Creamos un objeto SharedPreferences para guardar datos las actividades que el usuario selecciono
    private SharedPreferences datosActividadesSelect;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_actividad);

        IniciarVista();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        // Obtengo el id del objeto que esta siendo presionado
        int idPresionado = v.getId();
        switch (idPresionado) {
            // Cardviews
            case (R.id.cV_SeleccionarActividad_restaurantes):
                isRestaurantesSelected();
                break;
            case (R.id.cV_SeleccionarActividad_musica):
                isMusicaSelected();
                break;
            case (R.id.cV_SeleccionarActividad_cine):
                isCineSelected();
                break;
            case (R.id.cV_SeleccionarActividad_areasVerdes):
                isAreasVerdesSelected();
                break;
            case (R.id.cV_SeleccionarActividad_arte):
                isArteSelected();
                break;
            case (R.id.cV_SeleccionarActividad_sorpresa):
                isSorprendemeSelected();
                break;
            // Si se da click al boton salir
            case (R.id.b_SeleccionarActividad_salir):
                SalirDeApp();
                break;
            // Si se da click al boton continuar
            case (R.id.b_SeleccionarActividad_continuar):
                ContinuarAMain();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("UseCompatLoadingForDrawables")
    private void isRestaurantesSelected() {
        if (restaurantesSelected) {
            restaurantesSelected = false;
            cV_SeleccionarActividad_restaurantes
                    .setCardBackgroundColor(this.getResources().getColor(R.color.white));
            LL_SeleccionarActividad_restaurantes.setForeground(null);
        } else {
            restaurantesSelected = true;
            cV_SeleccionarActividad_restaurantes
                    .setCardBackgroundColor(this.getResources().getColor(R.color.cardview_color));
            LL_SeleccionarActividad_restaurantes.setForeground(this.getResources().getDrawable(R.drawable.ic_tick_light_grey));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("UseCompatLoadingForDrawables")
    private void isMusicaSelected() {
        if (musicaSelected) {
            musicaSelected = false;
            cV_SeleccionarActividad_musica
                    .setCardBackgroundColor(this.getResources().getColor(R.color.white));
            LL_SeleccionarActividad_musica.setForeground(null);
        } else {
            musicaSelected = true;
            cV_SeleccionarActividad_musica
                    .setCardBackgroundColor(this.getResources().getColor(R.color.cardview_color));
            LL_SeleccionarActividad_musica.setForeground(this.getResources().getDrawable(R.drawable.ic_tick_light_grey));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("UseCompatLoadingForDrawables")
    private void isCineSelected() {
        if (cineSelected) {
            cineSelected = false;
            cV_SeleccionarActividad_cine
                    .setCardBackgroundColor(this.getResources().getColor(R.color.white));
            LL_SeleccionarActividad_cine.setForeground(null);
        } else {
            cineSelected = true;
            cV_SeleccionarActividad_cine
                    .setCardBackgroundColor(this.getResources().getColor(R.color.cardview_color));
            LL_SeleccionarActividad_cine.setForeground(this.getResources().getDrawable(R.drawable.ic_tick_light_grey));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("UseCompatLoadingForDrawables")
    private void isAreasVerdesSelected() {
        if (areasVerdesSelected) {
            areasVerdesSelected = false;
            cV_SeleccionarActividad_areasVerdes
                    .setCardBackgroundColor(this.getResources().getColor(R.color.white));
            LL_SeleccionarActividad_areasVerdes.setForeground(null);
        } else {
            areasVerdesSelected = true;
            cV_SeleccionarActividad_areasVerdes
                    .setCardBackgroundColor(this.getResources().getColor(R.color.cardview_color));
            LL_SeleccionarActividad_areasVerdes.setForeground(this.getResources().getDrawable(R.drawable.ic_tick_light_grey));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("UseCompatLoadingForDrawables")
    private void isArteSelected() {
        if (arteSelected) {
            arteSelected = false;
            cV_SeleccionarActividad_arte
                    .setCardBackgroundColor(this.getResources().getColor(R.color.white));
            LL_SeleccionarActividad_arte.setForeground(null);
        } else {
            arteSelected = true;
            cV_SeleccionarActividad_arte
                    .setCardBackgroundColor(this.getResources().getColor(R.color.cardview_color));
            LL_SeleccionarActividad_arte.setForeground(this.getResources().getDrawable(R.drawable.ic_tick_light_grey));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("UseCompatLoadingForDrawables")
    private void isSorprendemeSelected() {
        if (sorprendemeSelected) {
            sorprendemeSelected = false;
            cV_SeleccionarActividad_sorpresa
                    .setCardBackgroundColor(this.getResources().getColor(R.color.white));
            LL_SeleccionarActividad_sorpresa.setForeground(null);
        } else {
            sorprendemeSelected = true;
            cV_SeleccionarActividad_sorpresa
                    .setCardBackgroundColor(this.getResources().getColor(R.color.cardview_color));
            LL_SeleccionarActividad_sorpresa.setForeground(this.getResources().getDrawable(R.drawable.ic_tick_light_grey));
        }
    }

    private void IniciarVista() {

        restaurantesSelected = false;
        musicaSelected = false;
        cineSelected = false;
        areasVerdesSelected = false;
        arteSelected = false;
        sorprendemeSelected = false;
        // Hacemos puente de conexion con la parte grafica
        // CardView
        cV_SeleccionarActividad_restaurantes = findViewById(R.id.cV_SeleccionarActividad_restaurantes);
        cV_SeleccionarActividad_musica = findViewById(R.id.cV_SeleccionarActividad_musica);
        cV_SeleccionarActividad_cine = findViewById(R.id.cV_SeleccionarActividad_cine);
        cV_SeleccionarActividad_areasVerdes = findViewById(R.id.cV_SeleccionarActividad_areasVerdes);
        cV_SeleccionarActividad_arte = findViewById(R.id.cV_SeleccionarActividad_arte);
        cV_SeleccionarActividad_sorpresa = findViewById(R.id.cV_SeleccionarActividad_sorpresa);
        //LinearLayout
        LL_SeleccionarActividad_restaurantes = findViewById(R.id.LL_SeleccionarActividad_restaurantes);
        LL_SeleccionarActividad_musica = findViewById(R.id.LL_SeleccionarActividad_musica);
        LL_SeleccionarActividad_cine = findViewById(R.id.LL_SeleccionarActividad_cine);
        LL_SeleccionarActividad_areasVerdes = findViewById(R.id.LL_SeleccionarActividad_areasVerdes);
        LL_SeleccionarActividad_arte = findViewById(R.id.LL_SeleccionarActividad_arte);
        LL_SeleccionarActividad_sorpresa = findViewById(R.id.LL_SeleccionarActividad_sorpresa);
        // Button
        b_SeleccionarActividad_salir = findViewById(R.id.b_SeleccionarActividad_salir);
        b_SeleccionarActividad_continuar = findViewById(R.id.b_SeleccionarActividad_continuar);

        // Listeners
        cV_SeleccionarActividad_restaurantes.setOnClickListener(this);
        cV_SeleccionarActividad_musica.setOnClickListener(this);
        cV_SeleccionarActividad_cine.setOnClickListener(this);
        cV_SeleccionarActividad_areasVerdes.setOnClickListener(this);
        cV_SeleccionarActividad_arte.setOnClickListener(this);
        cV_SeleccionarActividad_sorpresa.setOnClickListener(this);
        // Listeners de Button
        b_SeleccionarActividad_salir.setOnClickListener(this);
        b_SeleccionarActividad_continuar.setOnClickListener(this);


    }

    private void SalirDeApp() {
        SeleccionarActividad.this.finish();
        finish();
    }

    public void guardarActividadesSeleccionadas() {
        // Creamos un objeto Shared preferences para guardar las configuraciones de usuario
        // EL key sera configuracionesUsuario
        datosActividadesSelect = this.getSharedPreferences("actividadesSeleccionadasUsuario",
                Context.MODE_PRIVATE);
        editor = datosActividadesSelect.edit();
        editor.putBoolean("areasVerdesSelected", areasVerdesSelected);
        editor.putBoolean("arteSelected", arteSelected);
        editor.putBoolean("cineSelected", cineSelected);
        editor.putBoolean("musicaSelected", musicaSelected);
        editor.putBoolean("restaurantesSelected", restaurantesSelected);
        editor.putBoolean("sorprendemeSelected", sorprendemeSelected);
        // Hacemos el commit
        editor.commit();

    }

    private void ContinuarAMain() {

        if (!restaurantesSelected
                && !musicaSelected
                && !cineSelected
                && !areasVerdesSelected
                && !arteSelected
                && !sorprendemeSelected) {
            Toast.makeText(this, "¡Debes seleccionar al menos una categoria!", Toast.LENGTH_LONG).show();
        } else {
            guardarActividadesSeleccionadas();
            // Creamos un objeto de la clase Intent para que al presionar el boton vayamos al MainActivity o de Pagina Principal de la App
            Intent deSeleccionarActividadAMainActivity = new Intent(SeleccionarActividad.this, MainActivity.class);
            // Iniciamos el MainActivity o de Pagina Principal de la App
            startActivity(deSeleccionarActividadAMainActivity);
            // Terminamos el activity SeleccionarActividad
            SeleccionarActividad.this.finish();
        }
    }



}