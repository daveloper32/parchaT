package com.developers.parchat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class PerfilUsuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        // Escondemos la barra superior
        getSupportActionBar().hide();
    }
}