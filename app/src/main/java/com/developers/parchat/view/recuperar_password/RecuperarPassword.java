package com.developers.parchat.view.recuperar_password;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.developers.parchat.R;
import com.developers.parchat.view.login.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperarPassword extends AppCompatActivity implements View.OnClickListener{

    // Declaramos objetos EditText y Button
    private EditText eT_passRe_correo;
    private Button b_passRe_recuperar;

    // Declaramos un objeto de la Clase FirebaseAuth
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_password);

        IniciarVista();

    }

    private void IniciarVista() {
        // Inicializamos la instancia FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
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

        // Creamos un par de variables tipo String para obtener lo que se escriba en los EditText
        String email;
        // Obtenemos lo que hay en cada EditText
        email = eT_passRe_correo.getText().toString();
        // Validacion de que se escribio algo en los campos
        // Email
        if (email.isEmpty()) {
            eT_passRe_correo.setError("¡Ingresa tu e-mail por favor!");
            eT_passRe_correo.requestFocus();
            return;
        }
        // Validación extra email -> Se verifica si tiene la estructura de un email
        // por ejemplo que tenga .com o @
        boolean validacionEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches();
        if (!validacionEmail) {
            eT_passRe_correo.setError("¡Ingresa un e-mail valido!");
            eT_passRe_correo.requestFocus();
            return;
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            sendEmailSucess();
                        }
                        else {
                            sendEmailFail();
                        }
                    }
                });


    }

    private void sendEmailSucess() {
        // Mostramos un mensaje emergente que informa que se a envida un correo para cambiar la contraseña
        Toast.makeText(this, R.string.msgToast_passRe_1, Toast.LENGTH_LONG).show();
        // Creamos un objeto de la clase Intent para que al presionar el boton vayamos al Activity Login
        Intent dePasswordRecoveryALogin = new Intent(RecuperarPassword.this, Login.class);
        // Iniciamos el Activity Login
        startActivity(dePasswordRecoveryALogin);
        // Terminamos el activity RecuperarPassword
        RecuperarPassword.this.finish();
    }

    private void sendEmailFail() {
        Toast.makeText(this, R.string.msgToast_passRe_2, Toast.LENGTH_LONG).show();
    }
}