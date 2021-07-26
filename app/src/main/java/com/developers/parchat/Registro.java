package com.developers.parchat;

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

        // Hacemos la conexion con los objetos de la vista con findViewById
        ConexionObjetosConVista();
        // Asignamos los Listeners a los objetos de interaccion del Registro
        ListenersRegistro();
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
        ValidacionCampos();

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

    private void ValidacionCampos() {
        // Creamos un par de variables tipo String para obtener lo que se escriba en los EditText
        String nombreComp, email, password;
        // Obtenemos lo que hay en cada EditText
        nombreComp = eT_usuario_nomCom.getText().toString();
        email = eT_usuario_email.getText().toString();
        password = eT_usuario_contrasena.getText().toString();

        // Validacion de que se escribio algo en los campos
        // Nombre
        if (nombreComp.isEmpty()) {
            eT_usuario_nomCom.setError(getText(R.string.eT_Error_registro_1));
            eT_usuario_nomCom.requestFocus();
            return;
        }
        else {
            // Email
            if (email.isEmpty()) {
                eT_usuario_email.setError(getText(R.string.eT_Error_registro_2));
                eT_usuario_email.requestFocus();
                return;
            }
            else {
                // Validación extra email -> Se verifica si tiene la estructura de un email
                // por ejemplo que tenga .com o @
                Boolean validacionEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches();
                if (validacionEmail == false) {
                    eT_usuario_email.setError(getText(R.string.eT_Error_registro_3));
                    eT_usuario_email.requestFocus();
                    return;
                }
                else {
                    // Contraseña
                    if (password.isEmpty()) {
                        eT_usuario_contrasena.setError(getText(R.string.eT_Error_registro_4));
                        eT_usuario_contrasena.requestFocus();
                        return;
                    }
                    // Validacion extra contraseña una mayuscula? cuantos caracteres minimo?
                    // un simbolo como minimo
                    else {
                        // Si los edit text no estan vacios
                        if (!nombreComp.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                            // Creamos un objeto Shared preferences para guardar datos de usuario
                            // EL key sera el correo reistrado
                            SharedPreferences datosUsuarioActual = getSharedPreferences(email,
                                    Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = datosUsuarioActual.edit();
                            // Guardamos 4 datos
                            editor.putString("nombre", nombreComp);
                            editor.putString("email", email);
                            editor.putString("password", password);
                            editor.putString("numero", "");
                            // HAcemos el commit
                            editor.commit();
                            // Limpiarmos los editText
                            LimpiarCampos();
                            // Enviamos un mensaje emergente confirmando el registro
                            Toast.makeText(this, R.string.msgToast_registro_1, Toast.LENGTH_LONG).show();
                            // VOlvemos a login automaticamente
                            Intent RegisALogin = new Intent(Registro.this, Login.class);
                            // Iniciamos el Activity Login
                            startActivity(RegisALogin);
                            // Finalizamos Activity Registro
                            Registro.this.finish();
                        }
                    }
                }
            }
        }
    }

    private void LimpiarCampos() {
        eT_usuario_nomCom.setText("");
        eT_usuario_email.setText("");
        eT_usuario_contrasena.setText("");
    }

    private void ConexionObjetosConVista() {
        // Hacemos puente de conexion con la parte grafica
        // EditText
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
    }

    private void ListenersRegistro() {
        //Listeners
        /// Button
        b_usuario_regis.setOnClickListener(this);
        // TextView
        tV_registro_iniSesion.setOnClickListener(this);
        // ImageButton
        imgB_registro_facebook.setOnClickListener(this);
        imgB_registro_google.setOnClickListener(this);
    }


}