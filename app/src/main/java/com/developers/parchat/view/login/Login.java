package com.developers.parchat.view.login;

// Links Importantes:
// Repositorio Github: https://github.com/Yezid-Polania/aplicaciones-moviles
// Diseño Adobe: https://xd.adobe.com/view/e6e63823-ecf3-4b1e-93c1-c94003a230d1-b3b5/
// Paleta de colores:
// Color azul #233140
// Naranja #FF5328
// El círculo que se ve es el color naranja con opacidad 37%
// Fuente temporal: sans-serif-smallcaps

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.developers.parchat.R;

import com.developers.parchat.model.entity.Usuario;
import com.developers.parchat.view.seleccionar_actividad.SeleccionarActividad;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;

// EN model
// -> entity clases y archivos para guardar informacion -> EN general informacion
// -> repository -> conexion a bases de datos locales o en linea, archivos locales etc

/// SOLO VA A OBTENER Y MOSTRAR INFORMACION

public class Login extends AppCompatActivity implements LoginMVP.View, View.OnClickListener {

    // Variables modelo MVP
    private LoginMVP.Presenter presentador;
    private LoginMVP.Model modelo;

    // Declaramos objetos TextInputEditText, TextView, Button y ProgressBar
    private TextInputEditText eT_login_email, eT_login_password;
    private TextView tV_login_olvPassw, tV_login_registrarse;
    private Button b_login_iniSesion;
    private ImageButton imgB_login_facebook, imgB_login_google;
    private ProgressBar pB_login;

    //Google Login
    private GoogleSignInClient googleSignInClient;
    // Constantes
    //private int RC_SIGN_IN = 112;
    //private String TAG = "GoogleSignInLoginActivity";
    // End Google Login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Iniciamos los objetos de la vista
        IniciarVista();
    }

    private void IniciarVista() {
        // Inicializamos elpresentador y le pasamos la vista -> this
        presentador = new LoginPresenter(this);
        // Hacemos puente de conexion con la parte grafica
        // TextInputEditText
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
        pB_login = findViewById(R.id.pB_login);

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
                // EL presentador llama al metodo IniciarSesionCorreo
                presentador.IniciarSesionCorreo();
                break;
            // Si se presiona el texto ¿Olvidaste la contraseña?
            case (R.id.tV_login_olvPassw):
                // EL presentador llama al metodo OlvidarPassword
                presentador.OlvidarPassword();
                break;
            // Si se presiona el texto Registrarse
            case (R.id.tV_login_registrarse):
                // EL presentador llama al metodo RegistrarNuevoUsuario
                presentador.RegistrarNuevoUsuario();
                break;
            // Si se presiona el boton de Facebook
            case (R.id.imgB_login_facebook):
                // EL presentador llama al metodo IniciarSesionFacebook
                presentador.IniciarSesionFacebook(this);
                break;
            // Si se presiona el boton de Google
            case (R.id.imgB_login_google):
                // EL presentador llama al metodo IniciarSesionGoogle
                presentador.IniciarSesionGoogle();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        presentador.ValidarSaltoDeLogin();
    }

    @Override
    public Usuario getLoginCredentialsUsuario() {
        // Creamos un par de variables tipo String para obtener lo que se escriba en los EditText
        String email, password;
        // Obtenemos lo que hay en cada EditText
        email = eT_login_email.getText().toString().trim();
        password = eT_login_password.getText().toString().trim();
        // Guardamos el email y contraseña en un objeto LoginInfoUsuario
        Usuario credentialsUsuario = new Usuario(email, password);
        // Retornamos el objeto LoginCredentialsoUsuario
        return credentialsUsuario;
    }

    @Override
    public void signInGoogle() {
        googleSignInClient = presentador.getGoogleSignInClientFromRepo();
        Intent signInIntent = googleSignInClient.getSignInIntent();
        googleLauncher.launch(signInIntent);
        //startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    ActivityResultLauncher <Intent> googleLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                presentador.runGoogleIntent(data);
            }
    );


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        CallbackManager callbackManager = presentador.getCallbackManager();
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void showEmptyEmailError() {
        // Mostramos el error en el TextInputLayout
        eT_login_email.setError(getText(R.string.eT_Error_login_1));
        eT_login_email.requestFocus();
        return;
    }

    @Override
    public void showInvalidEmailError() {
        // Mostramos el error en el TextInputLayout
        eT_login_email.setError(getText(R.string.eT_Error_login_2));
        eT_login_email.requestFocus();
        return;
    }

    @Override
    public void showEmptyPasswordError() {
        // Mostramos el error en el TextInputLayout
        eT_login_password.setError(getText(R.string.eT_Error_login_3));
        eT_login_password.requestFocus();
        return;
    }

    @Override
    public void irAlActivitySeleccionarActividad(Class<? extends AppCompatActivity> ir_a_SeleccionarActividad) {
        // Creamos un objeto de la clase Intent para que al presionar el boton vayamos al Activity SeleccionarActividad
        Intent deLoginASeleccionarActividad= new Intent(Login.this, ir_a_SeleccionarActividad);
        // Iniciamos el Activity SeleccionarActividad
        startActivity(deLoginASeleccionarActividad);
        // Terminamos el activity Login
        Login.this.finish();
    }

    @Override
    public void showToastEmailNotFound() {
        Toast.makeText(this, R.string.msgToast_login_1, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showToastPasswordError() {
        Toast.makeText(this, R.string.msgToast_login_2, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showToastFirebaseAuthWithGoogleError() {
        Toast.makeText(this, R.string.msgToast_login_3, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showToastFirebaseAuthWithFacebookError() {
        Toast.makeText(this, R.string.msgToast_login_3, Toast.LENGTH_LONG).show();
    }

    @Override
    public Context getContext() {
        return Login.this;
    }


    @Override
    public void showLengthPasswordError() {
        Toast.makeText(this, R.string.eT_Error_login_4, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgressBar() {
        pB_login.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        pB_login.setVisibility(View.GONE);
    }

    @Override
    public void irAlActivityRecuperarPassword(Class<? extends AppCompatActivity> ir_a_RecuperarPassword) {
        //progressBar_Login.setVisibility(View.GONE);
        // Creamos un objeto de la clase Intent para que al presionar el boton vayamos al Activity RecuperarPassword
        Intent deLoginAPasswordRecovery = new Intent(Login.this, ir_a_RecuperarPassword);
        // Iniciamos el Activity RecuperarPassword
        startActivity(deLoginAPasswordRecovery);
        // Terminamos el activity Login
        Login.this.finish();
    }

    @Override
    public void irAlActivityRegistro(Class<? extends AppCompatActivity> ir_a_Registro) {
        //progressBar_Login.setVisibility(View.GONE);
        // Creamos un objeto de la clase Intent para que al presionar el boton vayamos al Activity Registro
        Intent deLoginARegistro = new Intent(Login.this, ir_a_Registro);
        // Iniciamos el Activity Registro
        startActivity(deLoginARegistro);
        // Terminamos el activity Login
        Login.this.finish();
    }
}