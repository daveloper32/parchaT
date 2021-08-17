package com.developers.parchat.view.show_webpage_sitio;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.developers.parchat.R;
import com.developers.parchat.model.entity.InfoLugar;
import com.developers.parchat.view.info_extra_sitio.InformacionExtraSitio;

public class ShowWebPageSitio extends AppCompatActivity {

    private ImageButton imgB_ShowWebPageSitio_atras;
    private WebView wB_ShowWebPageSitio;
    private ProgressBar pB_ShowWebPageSitio;

    private String paginaWeb;

    private InfoLugar infoLugar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_web_page_sitio);
        IniciarVista();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void IniciarVista() {
        imgB_ShowWebPageSitio_atras = findViewById(R.id.imgB_ShowWebPageSitio_atras);
        wB_ShowWebPageSitio = findViewById(R.id.wB_ShowWebPageSitio);
        pB_ShowWebPageSitio = findViewById(R.id.pB_ShowWebPageSitio);

        pB_ShowWebPageSitio.setVisibility(View.VISIBLE);

        infoLugar = getIntent().getParcelableExtra("infolugar");

        paginaWeb = getIntent().getStringExtra("wPsitio");
        if (!paginaWeb.equals("")) {
            wB_ShowWebPageSitio.setWebViewClient(new WebViewClient());
            //webViewMio.getSettings().setLoadsImagesAutomatically(true);
            wB_ShowWebPageSitio.getSettings().setJavaScriptEnabled(true);
            //wB_ShowWebPageSitio.loadUrl(paginaWeb);
            revisarPaginaCargada();
        }

        imgB_ShowWebPageSitio_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irAlInformacionExtraSitio(InformacionExtraSitio.class);
            }
        });
    }

    private void revisarPaginaCargada() {

        wB_ShowWebPageSitio.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(wB_ShowWebPageSitio, url);
                pB_ShowWebPageSitio.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                irAlInformacionExtraSitio(InformacionExtraSitio.class);
            }
        });
        wB_ShowWebPageSitio.loadUrl(paginaWeb);
    }


    public void irAlInformacionExtraSitio(Class<? extends AppCompatActivity> ir_a_InformacionExtraSitio) {
        // Creamos un objeto de la clase Intent para que al presionar el boton vayamos al MainActivity o de Pagina Principal de la App
        Intent deShowWebPageSitioAInformacionExtra = new Intent(ShowWebPageSitio.this, ir_a_InformacionExtraSitio);
        deShowWebPageSitioAInformacionExtra.putExtra("infolugar", infoLugar);
        // Iniciamos el MainActivity o de Pagina Principal de la App
        startActivity(deShowWebPageSitioAInformacionExtra);
        // Terminamos el activity PerfilUsuario
        ShowWebPageSitio.this.finish();
    }
}