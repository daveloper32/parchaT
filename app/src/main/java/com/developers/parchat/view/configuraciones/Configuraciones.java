package com.developers.parchat.view.configuraciones;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.developers.parchat.R;
import com.developers.parchat.view.main.MainActivity;
import com.developers.parchat.view.perfil_usuario.PerfilUsuario;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class Configuraciones extends AppCompatActivity implements View.OnClickListener,
        SwitchMaterial.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {

    private ImageButton imgB_configuraciones_volver;
    private SwitchMaterial sW_configuraciones_tipoBusqueda, sW_configuraciones_activarRango;
    private SeekBar sB_configuraciones_rangoBusqueda;
    private TextView tV_configuraciones_rangoBusqueda;

    private boolean modoBusquedaGPS, modoBusquedaMarker, rangoBusquedaVisible;
    private double rangoBusquedaKm;
    private int valorSBRangoBusquedaKm;

    // Creamos un objeto SharedPreferences para guardar los datos de COnfiguracion del Usuario
    private SharedPreferences datosConfiguracion;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuraciones);

        IniciarVista();
    }

    private void IniciarVista() {
        // Valores iniciales de configuracion
        leerConfiguracion();
        // COnexion con objetos de la vista
        imgB_configuraciones_volver = findViewById(R.id.imgB_configuraciones_volver);
        sW_configuraciones_tipoBusqueda = findViewById(R.id.sW_configuraciones_tipoBusqueda);
        sW_configuraciones_activarRango = findViewById(R.id.sW_configuraciones_activarRango);
        sB_configuraciones_rangoBusqueda = findViewById(R.id.sB_configuraciones_rangoBusqueda);
        tV_configuraciones_rangoBusqueda = findViewById(R.id.tV_configuraciones_rangoBusqueda);

        imgB_configuraciones_volver.setOnClickListener(this);
        sW_configuraciones_tipoBusqueda.setOnCheckedChangeListener(this);
        sW_configuraciones_activarRango.setOnCheckedChangeListener(this);
        sB_configuraciones_rangoBusqueda.setOnSeekBarChangeListener(this);

        CargarConfiguracion();
    }

    private void CargarConfiguracion() {
        // Cargamos configuracion previa
        // switch Modo Busqueda
        if (getModoBusquedaGPS() && !getModoBusquedaMarker()) {
            sW_configuraciones_tipoBusqueda.setChecked(false);
        } else if (!getModoBusquedaGPS() && getModoBusquedaMarker()) {
            sW_configuraciones_tipoBusqueda.setChecked(true);
        }
        // Switch Rango BusquedaVisible
        if (getRangoBusquedaVisible()) {
            sW_configuraciones_activarRango.setChecked(true);
        } else {
            sW_configuraciones_activarRango.setChecked(false);
        }
        // Valor del SeekBar
        sB_configuraciones_rangoBusqueda.setProgress(getValorSBRangoBusquedaKm());

        setValorInicialTVRangoBusqueda();
    }

    private void setValorInicialTVRangoBusqueda() {
        switch (getValorSBRangoBusquedaKm()) {
            case 0:
                // Rango de busqueda de 50 M
                tV_configuraciones_rangoBusqueda.setText(R.string.tV_configuraciones_rangoBusqueda50m);
                break;
            case 1:
                // Rango de busqueda de 100 M
                tV_configuraciones_rangoBusqueda.setText(R.string.tV_configuraciones_rangoBusqueda100m);
                break;
            case 2:
                // Rango de busqueda de 500 M
                tV_configuraciones_rangoBusqueda.setText(R.string.tV_configuraciones_rangoBusqueda500m);
                break;
            case 3:
                // Rango de busqueda de 1 KM
                tV_configuraciones_rangoBusqueda.setText(R.string.tV_configuraciones_rangoBusqueda1km);
                break;
            case 4:
                // Rango de busqueda de 5 KM
                tV_configuraciones_rangoBusqueda.setText(R.string.tV_configuraciones_rangoBusqueda5km);
                break;
            case 5:
                // Rango de busqueda de 10 KM
                tV_configuraciones_rangoBusqueda.setText(R.string.tV_configuraciones_rangoBusqueda10km);
                break;
            case 6:
                // Rango de busqueda de 50 KM
                tV_configuraciones_rangoBusqueda.setText(R.string.tV_configuraciones_rangoBusqueda50km);
                break;
            case 7:
                // Rango de busqueda de 100 KM
                tV_configuraciones_rangoBusqueda.setText(R.string.tV_configuraciones_rangoBusqueda100km);
                break;
        }
    }

    private void leerConfiguracion() {
        datosConfiguracion = getSharedPreferences("configuracionesUsuario",
                Context.MODE_PRIVATE);
        editor = datosConfiguracion.edit();
        if (datosConfiguracion != null) {
            modoBusquedaGPS = datosConfiguracion.getBoolean("modoBusquedaGPS", false);
            modoBusquedaMarker = datosConfiguracion.getBoolean("modoBusquedaMarker", false);
            rangoBusquedaVisible = datosConfiguracion.getBoolean("rangoBusquedaVisible", false);
            rangoBusquedaKm = Double.parseDouble(datosConfiguracion.getString("rangoBusquedaKm", ""));
            valorSBRangoBusquedaKm = datosConfiguracion.getInt("valorSBRangoBusquedaKm", 0);
        }
    }

    @Override
    public void onClick(View v) {
        // Obtengo el id del objeto que esta siendo presionado
        int idPresionado = v.getId();
        switch (idPresionado) {
            case (R.id.imgB_configuraciones_volver):
                guardarNuevaConfiguracion();
                irAlActivityMain(MainActivity.class);
                break;
        }
    }

    private void guardarNuevaConfiguracion() {
        // Creamos un objeto Shared preferences para guardar las configuraciones de usuario
        // EL key sera configuracionesUsuario
        datosConfiguracion = getSharedPreferences("configuracionesUsuario",
                Context.MODE_PRIVATE);
        editor = datosConfiguracion.edit();
        // Guardamos 4 datos
        editor.putBoolean("modoBusquedaGPS", getModoBusquedaGPS());
        editor.putBoolean("modoBusquedaMarker", getModoBusquedaMarker());
        editor.putBoolean("rangoBusquedaVisible", getRangoBusquedaVisible());
        editor.putString("rangoBusquedaKm", getRangoBusquedaKm());
        editor.putInt("valorSBRangoBusquedaKm", getValorSBRangoBusquedaKm());
        // Hacemos el commit
        editor.commit();
    }

    private boolean getRangoBusquedaVisible() {
        return rangoBusquedaVisible;
    }

    private int getValorSBRangoBusquedaKm() {
        return valorSBRangoBusquedaKm;
    }

    private String getRangoBusquedaKm() {
        return String.valueOf(rangoBusquedaKm);
    }


    private boolean getModoBusquedaMarker() {
        return modoBusquedaMarker;
    }

    private boolean getModoBusquedaGPS() {
        return modoBusquedaGPS;
    }

    //@Override
    public void irAlActivityMain(Class<? extends AppCompatActivity> ir_a_ActivityMain) {
        // Creamos un objeto de la clase Intent para que al presionar el boton vayamos al MainActivity o de Pagina Principal de la App
        Intent dePerfilUsuarioAMainActivity = new Intent(Configuraciones.this, ir_a_ActivityMain);
        // Iniciamos el MainActivity o de Pagina Principal de la App
        startActivity(dePerfilUsuarioAMainActivity);
        // Terminamos el activity PerfilUsuario
        Configuraciones.this.finish();
    }



    private void tipoDeBusqueda(boolean isCheckedTipoBusqueda) {
        if (!isCheckedTipoBusqueda) {
            // Se activa modo de busqueda por GPS
            modoBusquedaGPS = true;
            modoBusquedaMarker = false;
            //Toast.makeText(this, R.string.msgToast_configuraciones_1, Toast.LENGTH_LONG).show();
        } else {
            modoBusquedaGPS = false;
            modoBusquedaMarker = true;
            // Se activa modo de busqueda por marcador
            //Toast.makeText(this, R.string.msgToast_configuraciones_2, Toast.LENGTH_LONG).show();
        }
    }
    private void rangoBusquedaVisibleActivado(boolean isCheckedRangoBusquedaVisible) {
        if (isCheckedRangoBusquedaVisible) {
            rangoBusquedaVisible = true;
        } else {
            rangoBusquedaVisible = false;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        //tV_configuraciones_rangoBusqueda.setText(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        validarRangoDeBusqueda(seekBar);
    }

    private void validarRangoDeBusqueda(SeekBar seekBar) {
        int valorSeekBar = seekBar.getProgress();
        switch (valorSeekBar) {
            case 0:
                // Rango de busqueda de 50 M
                rangoBusquedaKm = 0.05;
                valorSBRangoBusquedaKm = 0;
                tV_configuraciones_rangoBusqueda.setText(R.string.tV_configuraciones_rangoBusqueda50m);
                break;
            case 1:
                // Rango de busqueda de 100 M
                rangoBusquedaKm = 0.1;
                valorSBRangoBusquedaKm = 1;
                tV_configuraciones_rangoBusqueda.setText(R.string.tV_configuraciones_rangoBusqueda100m);
                break;
            case 2:
                // Rango de busqueda de 500 M
                rangoBusquedaKm = 0.5;
                valorSBRangoBusquedaKm = 2;
                tV_configuraciones_rangoBusqueda.setText(R.string.tV_configuraciones_rangoBusqueda500m);
                break;
            case 3:
                // Rango de busqueda de 1 KM
                rangoBusquedaKm = 1;
                valorSBRangoBusquedaKm = 3;
                tV_configuraciones_rangoBusqueda.setText(R.string.tV_configuraciones_rangoBusqueda1km);
                break;
            case 4:
                // Rango de busqueda de 5 KM
                rangoBusquedaKm = 5;
                valorSBRangoBusquedaKm = 4;
                tV_configuraciones_rangoBusqueda.setText(R.string.tV_configuraciones_rangoBusqueda5km);
                break;
            case 5:
                // Rango de busqueda de 10 KM
                rangoBusquedaKm = 10;
                valorSBRangoBusquedaKm = 5;
                tV_configuraciones_rangoBusqueda.setText(R.string.tV_configuraciones_rangoBusqueda10km);
                break;
            case 6:
                // Rango de busqueda de 50 KM
                rangoBusquedaKm = 50;
                valorSBRangoBusquedaKm = 6;
                tV_configuraciones_rangoBusqueda.setText(R.string.tV_configuraciones_rangoBusqueda50km);
                break;
            case 7:
                // Rango de busqueda de 100 KM
                rangoBusquedaKm = 100;
                valorSBRangoBusquedaKm = 7;
                tV_configuraciones_rangoBusqueda.setText(R.string.tV_configuraciones_rangoBusqueda100km);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.equals(sW_configuraciones_activarRango)) {
            rangoBusquedaVisibleActivado(isChecked);
            if (isChecked) {
                //Rango de Busqueda Visible
                Toast.makeText(this, R.string.msgToast_configuraciones_3, Toast.LENGTH_SHORT).show();
            } else {
                // Rango de Busqueda Invisible
                Toast.makeText(this, R.string.msgToast_configuraciones_4, Toast.LENGTH_SHORT).show();
            }
        } else if (buttonView.equals(sW_configuraciones_tipoBusqueda)){
            tipoDeBusqueda(isChecked);
            if (!isChecked) {
                //GPS Mode
                Toast.makeText(this, R.string.msgToast_configuraciones_1, Toast.LENGTH_SHORT).show();
            } else {
                // Marker Mode
                Toast.makeText(this, R.string.msgToast_configuraciones_2, Toast.LENGTH_SHORT).show();
            }
        }
    }


}