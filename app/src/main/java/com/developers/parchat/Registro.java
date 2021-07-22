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

    private EditText nomYap;
    private EditText email;
    private EditText password;
    private TextView Registrate;
    private TextView ini_sesion;
    private Button register;
    private ImageButton im_face;
    private ImageButton im_goo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        // Escondemos la barra superior
        getSupportActionBar().hide();

        nomYap = findViewById(R.id.Usuario);
        email = findViewById(R.id.Email);
        password = findViewById(R.id.contrasena);
        Registrate =findViewById(R.id.Registro);
        ini_sesion = findViewById(R.id.sesion);
        register = findViewById(R.id.bRegis);
        im_face = findViewById(R.id.facebook);
        im_goo = findViewById(R.id.google);

        register.setOnClickListener(this);
        ini_sesion.setOnClickListener(this);
        im_face.setOnClickListener(this);
        im_goo.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){
        int ob_click = v.getId();

        switch (ob_click) {
            case (R.id.bRegis):
                Registrarse();
                break;

            case (R.id.sesion):
                InicioSesion();
                break;

            case (R.id.facebook):
                Log_Facebook();
                break;

            case (R.id.google):
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