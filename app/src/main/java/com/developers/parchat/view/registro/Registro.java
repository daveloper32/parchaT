package com.developers.parchat.view.registro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.developers.parchat.R;
import com.developers.parchat.view.login.Login;
import com.developers.parchat.view.login.LoginPresenter;
import com.google.android.material.textfield.TextInputEditText;

public class Registro extends AppCompatActivity implements RegistroMVP.View, View.OnClickListener{

    // Variables modelo MVP
    RegistroMVP.Presenter presentador;

    // Declaramos objetos TextInputEditText, TextView, Button y ProgressBar
    private TextInputEditText eT_usuario_nomCom;
    private TextInputEditText eT_usuario_email;
    private TextInputEditText eT_usuario_contrasena;
    private TextView tV_registro_iniSesion;
    private Button b_usuario_regis;
    private ImageButton imgB_registro_facebook;
    private ImageButton imgB_registro_google;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
    // Iniciamos los objetos de la vista
        IniciarVista();

    }

    private void IniciarVista() {
        // Inicializamos elpresentador y le pasamos la vista -> this
        presentador = new RegistroPresenter(this);
        // Hacemos puente de conexion con la parte grafica
        // TextInputEditText
        eT_usuario_nomCom = findViewById(R.id.eT_usuario_nomCom);
        eT_usuario_email = findViewById(R.id.eT_usuario_email);
        eT_usuario_contrasena = findViewById(R.id.eT_usuario_contrasena);
        //TextView
        tV_registro_iniSesion = findViewById(R.id.tV_registro_iniSesion);
        // Button
        b_usuario_regis = findViewById(R.id.b_usuario_regis);
        // ImageButton
        imgB_registro_facebook = findViewById(R.id.imgB_registro_facebook);
        imgB_registro_google = findViewById(R.id.imgB_registro_google);
        //Listeners
        /// Button
        b_usuario_regis.setOnClickListener(this);
        // TextView
        tV_registro_iniSesion.setOnClickListener(this);
        // ImageButton
        imgB_registro_facebook.setOnClickListener(this);
        imgB_registro_google.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // Obtengo el id del objeto que esta siendo presionado
        int ob_click = v.getId();
        switch (ob_click) {
            // Si se presiona el boton Registrarse
            case (R.id.b_usuario_regis):
                // EL presentador llama al metodo Registrarse
                presentador.Registrarse();
                break;
            // Si se presiona el text Inicia Sesión
            case (R.id.tV_registro_iniSesion):
                // EL presentador llama al metodo InicioSesion
                presentador.InicioSesion();
                break;
            // Si se presiona el boton de facebook
            case (R.id.imgB_registro_facebook):
                // EL presentador llama al metodo Log_Facebook
                presentador.Log_Facebook();
                break;
            // Si se presiona el boton de google
            case (R.id.imgB_registro_google):
                // EL presentador llama al metodo Log_Google
                presentador.Log_Google();
                break;
        }
    }

    @Override
    public RegistroDatosUsuario getRegistroDatosUsuario() {
        // Creamos un par de variables tipo String para obtener lo que se escriba en los TextInputLayout
        String nombreComp, email, password;
        // Obtenemos lo que hay en cada TextInputLayout
        nombreComp = eT_usuario_nomCom.getText().toString().trim();
        email = eT_usuario_email.getText().toString().trim();
        password = eT_usuario_contrasena.getText().toString().trim();
        // Guardamos el nombreComp, email y contraseña en un objeto LoginInfoUsuario
        RegistroDatosUsuario datosUsuario = new RegistroDatosUsuario(nombreComp, email, password);
        // Retornamos el objeto RegistroDatosUsuario
        return datosUsuario;
    }

    @Override
    public void showEmptyNombreCompletoError() {
        // Mostramos el error en el TextInputLayout
        eT_usuario_nomCom.setError(getText(R.string.eT_Error_registro_1));
        eT_usuario_nomCom.requestFocus();
        return;
    }

    @Override
    public void showEmptyEmailError() {
        // Mostramos el error en el TextInputLayout
        eT_usuario_email.setError(getText(R.string.eT_Error_registro_2));
        eT_usuario_email.requestFocus();
        return;
    }

    @Override
    public void showInvalidEmailError() {
        // Mostramos el error en el TextInputLayout
        eT_usuario_email.setError(getText(R.string.eT_Error_registro_3));
        eT_usuario_email.requestFocus();
        return;
    }

    @Override
    public void showEmptyPasswordError() {
        // Mostramos el error en el TextInputLayout
        eT_usuario_contrasena.setError(getText(R.string.eT_Error_registro_4));
        eT_usuario_contrasena.requestFocus();
        return;
    }

    @Override
    public void irAlActivityLogin(Class<? extends AppCompatActivity> ir_a_Login) {
        // Limpiamos los TextInputLayout
        LimpiarCampos();
        // Creamos un objeto de la clase Intent para que al presionar el boton vayamos al Activity Login
        Intent IniALogin = new Intent(Registro.this, ir_a_Login);
        // Iniciamos el Activity Login
        startActivity(IniALogin);
        // Terminamos el activity Registro
        Registro.this.finish();
    }

    @Override
    public void showToastRegistroConfirm() {
        // Enviamos un mensaje emergente confirmando el registro
        Toast.makeText(this, R.string.msgToast_registro_1, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showToastCorreoYaRegistrado() {
        // Enviamos un mensaje emergente que diga que el correo ingresado ya se encuentra registrado
        Toast.makeText(this, R.string.msgToast_registro_2, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showToastErrorRegistrarUsuarioNuevo() {
        // Enviamos un mensaje emergente que diga que diga algo salio mal, intenta registrarte de nuevo
        Toast.makeText(this, R.string.msgToast_registro_3, Toast.LENGTH_LONG).show();
    }

    @Override
    public Context getContext() {
        return Registro.this;
    }

    private void LimpiarCampos() {
        eT_usuario_nomCom.setText("");
        eT_usuario_email.setText("");
        eT_usuario_contrasena.setText("");
    }
}