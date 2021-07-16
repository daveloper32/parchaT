package com.developers.parchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Login extends AppCompatActivity implements View.OnClickListener {

    // Declaramos objetos EditText, TextView, Button y ProgressBar
    private EditText eT_login_email, eT_login_password;
    private TextView tV_login_olvPassw, tV_login_registrarse;
    private Button b_login_iniSesion;
    private ImageButton imgB_login_facebook, imgB_login_google;
    //private ProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Escondemos la barra superior
        getSupportActionBar().hide();

        // Hacemos puente de conexion con la parte grafica
        // EditText
        eT_login_email = findViewById(R.id.eT_login_email);
        eT_login_password = findViewById(R.id.eT_login_password);
        // TextView
        tV_login_olvPassw = findViewById(R.id.tV_login_olvPassw);
        tV_login_registrarse = findViewById(R.id.tV_login_registrarse);
        // Button
        b_login_iniSesion = findViewById(R.id.b_login_iniSesion);
        // ImageButton
        imgB_login_facebook = findViewById(R.id.imgB_login_facebook);
        imgB_login_google = findViewById(R.id.imgB_login_google);
        // ProgressBar
        // = findViewById(R.id.);

        // Listeners de Button
        b_login_iniSesion.setOnClickListener(this);
        // Listeners de TextView
        tV_login_olvPassw.setOnClickListener(this);
        tV_login_registrarse.setOnClickListener(this);
        // Listeners de ImageView
        imgB_login_facebook.setOnClickListener(this);
        imgB_login_google.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // Obtengo el id del objeto que esta siendo presionado
        int idPresionado = v.getId();

        switch (idPresionado) {
            // Si se presiona el boton Iniciar Sesion
            case (R.id.b_login_iniSesion):
                IniciarSesionCorreo();
                break;
            // Si se presiona el texto ¿Olvidaste la contraseña?
            case (R.id.tV_login_olvPassw):
                OlvidarPassword();
                break;
            // Si se presiona el texto Registrarse
            case (R.id.tV_login_registrarse):
                RegistrarNuevoUsuario();
                break;
            // Si se presiona el boton de Facebook
            case (R.id.imgB_login_facebook):
                IniciarSesionFacebook();
                break;
            // Si se presiona el boton de Google
            case (R.id.imgB_login_google):
                IniciarSesionGoogle();
                break;
        }

    }

    private void IniciarSesionCorreo() {
        //progressBar_Login.setVisibility(View.GONE);
        // Creamos un objeto de la clase Intent para que al presionar el boton vayamos al MainActivity o de Pagina Principal de la App
        Intent deLoginAMainActivity = new Intent(Login.this, MainActivity.class);
        // Iniciamos el MainActivity o de Pagina Principal de la App
        startActivity(deLoginAMainActivity);
        // Terminamos el activity Login
        Login.this.finish();
    }

    private void OlvidarPassword() {
        //progressBar_Login.setVisibility(View.GONE);
        // Creamos un objeto de la clase Intent para que al presionar el boton vayamos al Activity
        //Intent deLoginAOlvidePassword = new Intent(Login.this, .class);
        // Iniciamos el Activity
        //startActivity(deLoginAOlvidePassword);
        // Terminamos el activity Login
        //Login.this.finish();
    }

    private void RegistrarNuevoUsuario() {
        //progressBar_Login.setVisibility(View.GONE);
        // Creamos un objeto de la clase Intent para que al presionar el boton vayamos al Activity Registro
        //Intent deLoginARegistro = new Intent(Login.this, Registro.class);
        // Iniciamos el Activity Registro
        //startActivity(deLoginARegistro);
        // Terminamos el activity Login
        //Login.this.finish();
    }

    private void IniciarSesionFacebook() {
    }

    private void IniciarSesionGoogle() {
    }
}