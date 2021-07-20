package com.developers.parchat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PasswordRecovery extends AppCompatActivity implements View.OnClickListener {

    // Declaramos objetos EditText y Button
    private EditText eT_passRe_correo;
    private Button b_passRe_recuperar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);
        // Escondemos la barra superior
        getSupportActionBar().hide();

        // Hacemos puente de conexion con la parte grafica
        // EditText
        eT_passRe_correo = findViewById(R.id.eT_passRe_correo);
        // Button
        b_passRe_recuperar = findViewById(R.id.b_passRe_recuperar);

        // Listeners de Button
        b_passRe_recuperar.setOnClickListener(this);
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
        Toast.makeText(this, "¡Revisa tu bandeja de entrada!\r\n\nTe hemos enviado un correo para que puedas restaurar tu contraseña.", Toast.LENGTH_LONG).show();
        // Creamos un objeto de la clase Intent para que al presionar el boton vayamos al Activity Login
        Intent dePasswordRecoveryALogin = new Intent(PasswordRecovery.this, Login.class);
        // Iniciamos el Activity Login
        startActivity(dePasswordRecoveryALogin);
        // Terminamos el activity PasswordRecovery
        PasswordRecovery.this.finish();
    }
}
