package com.developers.parchat.view.seleccionar_actividad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.developers.parchat.R;

import com.developers.parchat.view.main.MainActivity;

public class SeleccionarActividad extends AppCompatActivity implements View.OnClickListener {

    // Declaramos objetos CheckBox, Button y ProgressBar
    private CheckBox cB_SeleccionarActividad_restaurantes, cB_SeleccionarActividad_musica,
            cB_SeleccionarActividad_cine, cB_SeleccionarActividad_areasVerdes,
            cB_SeleccionarActividad_arte, cB_SeleccionarActividad_farmacia;
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

        // Hacemos la conexion con los objetos de la vista con findViewById
        ConexionObjetosConVista();
        // Asignamos los Listeners a los objetos de interaccion del SeleccionarActividad
        ListenersSeleccionarActividad();
    }

    @Override
    public void onClick(View v) {
        // Obtengo el id del objeto que esta siendo presionado
        int idPresionado = v.getId();
        switch (idPresionado) {
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
        if (cB_SeleccionarActividad_restaurantes.isChecked()) {
            restaurantesSelected = true;
        } else {
            restaurantesSelected = false;
        }

        if (cB_SeleccionarActividad_musica.isChecked()) {
            musicaSelected = true;
        } else {
            musicaSelected = false;
        }

        if (cB_SeleccionarActividad_cine.isChecked()) {
            cineSelected = true;
        } else {
            cineSelected = false;
        }

        if (cB_SeleccionarActividad_areasVerdes.isChecked()) {
            areasVerdesSelected = true;
        } else {
            areasVerdesSelected = false;
        }

        if (cB_SeleccionarActividad_arte.isChecked()) {
            arteSelected = true;
        } else {
            arteSelected = false;
        }

        if (cB_SeleccionarActividad_farmacia.isChecked()) {
            sorprendemeSelected = true;
        } else {
            sorprendemeSelected = false;
        }

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

    private void ConexionObjetosConVista() {
        // Hacemos puente de conexion con la parte grafica
        // Checkbox
        cB_SeleccionarActividad_restaurantes = findViewById(R.id.cB_SeleccionarActividad_restaurantes);
        cB_SeleccionarActividad_musica = findViewById(R.id.cB_SeleccionarActividad_musica);
        cB_SeleccionarActividad_cine = findViewById(R.id.cB_SeleccionarActividad_cine);
        cB_SeleccionarActividad_areasVerdes = findViewById(R.id.cB_SeleccionarActividad_areasVerdes);
        cB_SeleccionarActividad_arte = findViewById(R.id.cB_SeleccionarActividad_arte);
        cB_SeleccionarActividad_farmacia = findViewById(R.id.cB_SeleccionarActividad_farmacia);
        // Button
        b_SeleccionarActividad_salir = findViewById(R.id.b_SeleccionarActividad_salir);
        b_SeleccionarActividad_continuar = findViewById(R.id.b_SeleccionarActividad_continuar);


    }

    private void ListenersSeleccionarActividad() {
        // Listeners de Button
        b_SeleccionarActividad_salir.setOnClickListener(this);
        b_SeleccionarActividad_continuar.setOnClickListener(this);
    }
}