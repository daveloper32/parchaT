package com.developers.parchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Registro extends AppCompatActivity implements View.OnClickListener{

    // Declaramos objetos EditText, TextView, Button y ProgressBar
    private EditText eT_usuario_nomCom;
    private EditText eT_usuario_email;
    private EditText eT_usuario_contrasena;
    private TextView tV_registro_iniSesion;
    private Button b_usuario_regis;
    private ImageButton imgB_registro_facebook;
    private ImageButton imgB_registro_google;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        // Escondemos la barra superior
        getSupportActionBar().hide();

        eT_usuario_nomCom = findViewById(R.id.eT_usuario_nomCom);
        eT_usuario_email = findViewById(R.id.eT_usuario_email);
        eT_usuario_contrasena = findViewById(R.id.eT_usuario_contrasena);
        tV_registro_iniSesion = findViewById(R.id.tV_registro_iniSesion);
        b_usuario_regis = findViewById(R.id.b_usuario_regis);
        imgB_registro_facebook = findViewById(R.id.imgB_registro_facebook);
        imgB_registro_google = findViewById(R.id.imgB_registro_google);

        b_usuario_regis.setOnClickListener(this);
        tV_registro_iniSesion .setOnClickListener(this);
        imgB_registro_facebook.setOnClickListener(this);
        imgB_registro_google.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // Obtengo el id del objeto que esta siendo presionado
        int ob_click = v.getId();
        switch (ob_click) {
            case (R.id.b_usuario_regis):
                Registrarse();
                break;
            case (R.id.tV_registro_iniSesion):
                InicioSesion();
                break;
            case (R.id.imgB_registro_facebook):
                Log_Facebook();
                break;
            case (R.id.imgB_registro_google):
                Log_Google();
                break;
        }
    }

    private void Registrarse(){
        Toast.makeText(this, "Hemos enviado un correo para que confirmes tu registro.", Toast.LENGTH_LONG).show();
        Intent RegisALogin = new Intent(Registro.this, Login.class);
        startActivity(RegisALogin);
        Registro.this.finish();
    }

    private void InicioSesion(){
        Intent IniALogin = new Intent(Registro.this, Login.class);
        startActivity(IniALogin);
        Registro.this.finish();
    }

    private void Log_Facebook(){

    }

    private void Log_Google(){

    }
}