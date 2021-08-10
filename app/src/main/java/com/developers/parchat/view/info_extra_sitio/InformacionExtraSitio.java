package com.developers.parchat.view.info_extra_sitio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.developers.parchat.R;
import com.developers.parchat.model.entity.InfoLugar;
import com.developers.parchat.view.main.MainActivity;
import com.developers.parchat.view.perfil_usuario.PerfilUsuario;
import com.developers.parchat.view.show_webpage_sitio.ShowWebPageSitio;


public class InformacionExtraSitio extends AppCompatActivity implements View.OnClickListener {

    private InfoLugar infoLugar;

    private ImageButton imgB_InformacionExtraSitio_atras;
    private ImageView imgV_InformacionExtraSitio_fotoSitio;
    private TextView tV_InformacionExtraSitio_nomSitio,
            tV_InformacionExtraSitio_tipoLugar,
            tV_InformacionExtraSitio_direccionSitio,
            tV_InformacionExtraSitio_cityCountry,
            tV_InformacionExtraSitio_numeroSitio,
            tV_InformacionExtraSitio_urlSitio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_extra_sitio);
        IniciarVista();
    }

    private void IniciarVista() {
        // COnectamos objetos de la vista
        // ImageButton
        imgB_InformacionExtraSitio_atras = findViewById(R.id.imgB_InformacionExtraSitio_atras);
        // ImageView
        imgV_InformacionExtraSitio_fotoSitio = findViewById(R.id.imgV_InformacionExtraSitio_fotoSitio);
        // TextView
        tV_InformacionExtraSitio_nomSitio = findViewById(R.id.tV_InformacionExtraSitio_nomSitio);
        tV_InformacionExtraSitio_tipoLugar = findViewById(R.id.tV_InformacionExtraSitio_tipoLugar);
        tV_InformacionExtraSitio_direccionSitio = findViewById(R.id.tV_InformacionExtraSitio_direccionSitio);
        tV_InformacionExtraSitio_cityCountry = findViewById(R.id.tV_InformacionExtraSitio_cityCountry);
        tV_InformacionExtraSitio_numeroSitio = findViewById(R.id.tV_InformacionExtraSitio_numeroSitio);
        tV_InformacionExtraSitio_urlSitio = findViewById(R.id.tV_InformacionExtraSitio_urlSitio);

        // Obtenemos un objeto tipo InfoLugar con toda la informacion disponible del lugar
        infoLugar = getIntent().getParcelableExtra("infolugar");
        if (infoLugar != null) {
            // SetTexts & Image
            // Cargamos la foto
            if (infoLugar.getUrlimagen() != null) {
                // Download directly from StorageReference using Glide
                // (See MyAppGlideModule for Loader registration)
                Glide.with(this /* context */)
                        .load(infoLugar.getUrlimagen())
                        .into(imgV_InformacionExtraSitio_fotoSitio);
            }
            // SetText
            // Nombre
            if (!infoLugar.getNombre().equals("")) {
                String nomSitio = infoLugar.getNombre();
                tV_InformacionExtraSitio_nomSitio.setText(nomSitio);
            } else {
                String nomSitio = this.getResources().getString(R.string.txt_InformacionExtraSitio_noDisponible);
                tV_InformacionExtraSitio_nomSitio.setText(nomSitio);
            }
            // Tipo Lugar
            if (!infoLugar.getTipositio().equals("")) {
                String tipoSitio = this.getResources().getString(R.string.tV_InformacionExtraSitio_tipoLugar)
                        + " " + infoLugar.getTipositio();
                tV_InformacionExtraSitio_tipoLugar.setText(tipoSitio);
            }
            else {
                String tipoSitio = this.getResources().getString(R.string.txt_InformacionExtraSitio_noDisponible);
                tV_InformacionExtraSitio_tipoLugar.setText(tipoSitio);
            }
            // Direccion Sitio
            if (!infoLugar.getDireccion().equals("")) {
                String direccionSitio = infoLugar.getDireccion();
                tV_InformacionExtraSitio_direccionSitio.setText(direccionSitio);
            } else {
                String direccionSitio = this.getResources().getString(R.string.txt_InformacionExtraSitio_noDisponible);
                tV_InformacionExtraSitio_direccionSitio.setText(direccionSitio);
            }

            // City COuntry
            if (!infoLugar.getCiudad().equals("") && !infoLugar.getPais().equals("")) {
                String cityCountry = infoLugar.getCiudad() + ", " + infoLugar.getPais();
                tV_InformacionExtraSitio_cityCountry.setText(cityCountry);
            } else {
                String cityCountry = this.getResources().getString(R.string.txt_InformacionExtraSitio_noDisponible);
                tV_InformacionExtraSitio_cityCountry.setText(cityCountry);
            }
            // Numero Sitio
            if (!infoLugar.getNumero().equals("")) {
                String numeroSitio = infoLugar.getNumero();
                tV_InformacionExtraSitio_numeroSitio.setText(numeroSitio);
            } else {
                String numeroSitio = this.getResources().getString(R.string.txt_InformacionExtraSitio_noDisponible);
                tV_InformacionExtraSitio_numeroSitio.setText(numeroSitio);
            }
            // Url Sitio
            if (!infoLugar.getSitioweb().equals("")) {
                String urlSitio = infoLugar.getSitioweb();
                tV_InformacionExtraSitio_urlSitio.setText(urlSitio);
                tV_InformacionExtraSitio_urlSitio.setOnClickListener(this);
            } else {
                String urlSitio = this.getResources().getString(R.string.txt_InformacionExtraSitio_noDisponible);
                tV_InformacionExtraSitio_urlSitio.setText(urlSitio);
            }
            // Listeners
            imgB_InformacionExtraSitio_atras.setOnClickListener(this);


        }
    }

    @Override
    public void onClick(View v) {
        // Obtengo el id del objeto que esta siendo presionado
        int idPresionado = v.getId();
        switch (idPresionado) {
            case (R.id.imgB_InformacionExtraSitio_atras):
                irAlActivityMain(MainActivity.class);
                break;
            case (R.id.tV_InformacionExtraSitio_urlSitio):
                irAlShowWebPageSitio(ShowWebPageSitio.class);
                break;
        }
    }

    public void irAlShowWebPageSitio(Class<? extends AppCompatActivity> ir_a_ShowWebPageSitio) {
        // Creamos un objeto de la clase Intent para que al presionar el boton vayamos al MainActivity o de Pagina Principal de la App
        Intent deInformacionExtraSitioAShowWebPageSitio = new Intent(InformacionExtraSitio.this, ir_a_ShowWebPageSitio);
        if (!infoLugar.getSitioweb().equals("")) {
            deInformacionExtraSitioAShowWebPageSitio.putExtra("wPsitio", infoLugar.getSitioweb());
            deInformacionExtraSitioAShowWebPageSitio.putExtra("infolugar", infoLugar);
        }
        // Iniciamos el MainActivity o de Pagina Principal de la App
        startActivity(deInformacionExtraSitioAShowWebPageSitio);
    }

    public void irAlActivityMain(Class<? extends AppCompatActivity> ir_a_ActivityMain) {
        // Creamos un objeto de la clase Intent para que al presionar el boton vayamos al MainActivity o de Pagina Principal de la App
        Intent deInformacionExtraSitioAMainActivity = new Intent(InformacionExtraSitio.this, ir_a_ActivityMain);
        // Iniciamos el MainActivity o de Pagina Principal de la App
        startActivity(deInformacionExtraSitioAMainActivity);
        // Terminamos el activity PerfilUsuario
        InformacionExtraSitio.this.finish();
    }
}