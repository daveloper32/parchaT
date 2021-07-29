package com.developers.parchat.view.recuperar_password;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.developers.parchat.R;
import com.developers.parchat.view.login.Login;

public class RecuperarPassword extends AppCompatActivity implements View.OnClickListener{

    // Declaramos objetos EditText y Button
    private EditText eT_passRe_correo;
    private Button b_passRe_recuperar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_password);

        // Hacemos la conexion con los objetos de la vista con findViewById
        ConexionObjetosConVista();
        // Asignamos los Listeners a los objetos de interaccion del RecuperarPassword
        ListenersRecuperarPassword();
    }

    @Override
    public void onClick(View v) {
        // Obtengo el id del objeto que esta siendo presionado
        int idPresionado = v.getId();
        switch (idPresionado) {
            // Si se presiona el boton EnviarCorreo
            case (R.id.b_passRe_recuperar):
                EnviarCorreo();
                break;
        }
    }

    private void EnviarCorreo() {

        // Mostramos un mensaje emergente que informa que se a envida un correo para cambiar la contraseña
        Toast.makeText(this, R.string.msgToast_passRe_1, Toast.LENGTH_LONG).show();
        // Creamos un objeto de la clase Intent para que al presionar el boton vayamos al Activity Login
        Intent dePasswordRecoveryALogin = new Intent(RecuperarPassword.this, Login.class);
        // Iniciamos el Activity Login
        startActivity(dePasswordRecoveryALogin);
        // Terminamos el activity RecuperarPassword
        RecuperarPassword.this.finish();
    }

    private void ConexionObjetosConVista() {
        // Hacemos puente de conexion con la parte grafica
        // EditText
        eT_passRe_correo = findViewById(R.id.eT_passRe_correo);
        // Button
        b_passRe_recuperar = findViewById(R.id.b_passRe_recuperar);
    }

    private void ListenersRecuperarPassword() {
        // Listeners de Button
        b_passRe_recuperar.setOnClickListener(this);
    }
}