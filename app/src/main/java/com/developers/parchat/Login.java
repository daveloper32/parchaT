package com.developers.parchat;

// Links Importantes:
// Repositorio Github: https://github.com/Yezid-Polania/aplicaciones-moviles
// Diseño Adobe: https://xd.adobe.com/view/e6e63823-ecf3-4b1e-93c1-c94003a230d1-b3b5/
// Paleta de colores:
// Color azul #233140
// Naranja #FF5328
// El círculo que se ve es el color naranja con opacidad 37%
// Fuente temporal: sans-serif-smallcaps

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

        // Hacemos la conexion con los objetos de la vista con findViewById
        ConexionObjetosConVista();
        // Asignamos los Listeners a los objetos de interaccion del Login
        ListenersLogin();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences validarInicioSesionUsuario = getSharedPreferences("inicio_sesion",
                Context.MODE_PRIVATE);
        if (validarInicioSesionUsuario != null) {
            Boolean saltarLogin = validarInicioSesionUsuario.getBoolean("saltarLogin", false);
            if (saltarLogin == true){
                //Nos saltamos el Login y vamos directamente a Selecconar Actividad
                IrASeleccionarActividad();

            }
        }
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
        ValidarIngresoCorreo();
        //progressBar_Login.setVisibility(View.GONE);

    }

    private void OlvidarPassword() {
        //progressBar_Login.setVisibility(View.GONE);
        // Creamos un objeto de la clase Intent para que al presionar el boton vayamos al Activity RecuperarPassword
        Intent deLoginAPasswordRecovery = new Intent(Login.this, RecuperarPassword.class);
        // Iniciamos el Activity RecuperarPassword
        startActivity(deLoginAPasswordRecovery);
        // Terminamos el activity Login
        Login.this.finish();
    }

    private void RegistrarNuevoUsuario() {
        //progressBar_Login.setVisibility(View.GONE);
        // Creamos un objeto de la clase Intent para que al presionar el boton vayamos al Activity Registro
        Intent deLoginARegistro = new Intent(Login.this, Registro.class);
        // Iniciamos el Activity Registro
        startActivity(deLoginARegistro);
        // Terminamos el activity Login
        Login.this.finish();
    }

    private void IniciarSesionFacebook() {
    }

    private void IniciarSesionGoogle() {
    }

    private void ValidarIngresoCorreo() {
        // Creamos un par de variables tipo String para obtener lo que se escriba en los EditText
        String email, password;
        // Obtenemos lo que hay en cada EditText
        email = eT_login_email.getText().toString();
        password = eT_login_password.getText().toString();

        // Validacion de que se escribio algo en los campos
        // Email
        if (email.isEmpty()) {
            eT_login_email.setError(getText(R.string.eT_Error_login_1));
            eT_login_email.requestFocus();
        } else {
            // Validación extra email -> Se verifica si tiene la estructura de un email
            // por ejemplo que tenga .com o @
            Boolean validacionEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches();
            if (!validacionEmail) {
                eT_login_email.setError(getText(R.string.eT_Error_login_2));
                eT_login_email.requestFocus();
                return;
            }
            else {
                // Contraseña
                if (password.isEmpty()) {
                    eT_login_password.setError(getText(R.string.eT_Error_login_3));
                    eT_login_password.requestFocus();
                }
                else {
                    // Validacion extra contraseña una mayuscula? cuantos caracteres minimo?
                    // un simbolo como minimo

                    // Creamos un objeto Shared preferences para guardar datos de usuario
                    SharedPreferences datosUsuarioActual = getSharedPreferences(email,
                            Context.MODE_PRIVATE);

                    String buscarEmail, buscarPassword;
                    buscarEmail = datosUsuarioActual.getString("email", "");
                    buscarPassword = datosUsuarioActual.getString("password", "");

                    if (buscarEmail.isEmpty() && buscarPassword.isEmpty()) {
                        Toast.makeText(this, R.string.msgToast_login_1, Toast.LENGTH_LONG).show();
                    } else if (!buscarEmail.equals(email) || !buscarPassword.equals(password)) {
                        Toast.makeText(this, R.string.msgToast_login_2, Toast.LENGTH_LONG).show();
                    }else {
                        // Creamos un objeto Shared preferences para guardar el inicio de sesion
                        SharedPreferences inicioSesionUsuario = getSharedPreferences("inicio_sesion",
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = inicioSesionUsuario.edit();
                        // Guardamos 2 datos
                        editor.putString("email", email);
                        editor.putBoolean("saltarLogin", true);
                        // HAcemos el commit
                        editor.commit();
                        // Vamos a seleccionar actividad
                        IrASeleccionarActividad();
                    }
                }
            }
        }
    }

    private void IrASeleccionarActividad() {
        // Creamos un objeto de la clase Intent para que al presionar el boton vayamos al Activity SeleccionarActividad
        Intent deLoginASeleccionarActividad= new Intent(Login.this, SeleccionarActividad.class);
        // Iniciamos el Activity SeleccionarActividad
        startActivity(deLoginASeleccionarActividad);
        // Terminamos el activity Login
        Login.this.finish();
    }

    private void ConexionObjetosConVista() {
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
    }

    private void ListenersLogin() {
        // Listeners de Button
        b_login_iniSesion.setOnClickListener(this);
        // Listeners de TextView
        tV_login_olvPassw.setOnClickListener(this);
        tV_login_registrarse.setOnClickListener(this);
        // Listeners de ImageView
        imgB_login_facebook.setOnClickListener(this);
        imgB_login_google.setOnClickListener(this);
    }
}