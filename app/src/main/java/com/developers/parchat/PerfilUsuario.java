package com.developers.parchat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class PerfilUsuario extends AppCompatActivity implements View.OnClickListener {

    // Declaramos objetos EditText, TextView e Imageview circular e ImageButton
    private ImageButton imgB_perfUsuario_volver, imgB_perfUsuario_editar, imgB_perfUsuario_guardar;
    private de.hdodenhof.circleimageview.CircleImageView imgV_perfUsuario_fotoUsuario;
    private TextView tV_perfUsuario_titulo, tV_perfUsuario_cambiarFoto;
    private EditText eT_perfUsuario_nombreCom, eT_perfUsuario_email, eT_perfUsuario_numero;

    private String nombreUsuario, email, numero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        // Hacemos la conexion con los objetos de la vista con findViewById
        ConexionObjetosConVista();
        // Cargamos los datos del usuario en los EditText
        CargamosDatosusuario();
        // Asignamos los Listeners a los objetos de interaccion del PerfilUsuario
        ListenersPerfilUsuario();
    }

    @Override
    public void onClick(View v) {
        // Obtengo el id del objeto que esta siendo presionado
        int idPresionado = v.getId();
        switch (idPresionado) {
            case (R.id.imgB_perfUsuario_volver):
                if (ComprobarEdit() == true){
                    MensajeEmergente();
                } else if (ComprobarEdit() == false) {
                    VolverAMain();
                }
                break;
            case (R.id.imgB_perfUsuario_editar):
                EditarDatos();
                break;
            case (R.id.imgB_perfUsuario_guardar):
                GuardarDatos();
                break;
            case (R.id.tV_perfUsuario_cambiarFoto):
                CambiarFoto();
                break;

        }
    }

    private Boolean ComprobarEdit() {
        if (eT_perfUsuario_nombreCom.isEnabled()  && eT_perfUsuario_numero.isEnabled()) {
            return true;
        } else {
            return false;
        }
    }

    private void VolverAMain() {
        //progressBar_Login.setVisibility(View.GONE);
        // Creamos un objeto de la clase Intent para que al presionar el boton vayamos al MainActivity o de Pagina Principal de la App
        Intent dePerfilUsuarioAMainActivity = new Intent(PerfilUsuario.this, MainActivity.class);
        // Iniciamos el MainActivity o de Pagina Principal de la App
        startActivity(dePerfilUsuarioAMainActivity);
        // Terminamos el activity PerfilUsuario
        PerfilUsuario.this.finish();
    }

    private void EditarDatos() {
        // Hacemos que se puedan editar los EditText
        eT_perfUsuario_nombreCom.setEnabled(true);
        //eT_perfUsuario_email.setEnabled(true);
        eT_perfUsuario_numero.setEnabled(true);
    }

    private void GuardarDatos() {
        // Si todos los edit text estan activos podemos guardar
        if (eT_perfUsuario_nombreCom.isEnabled() && eT_perfUsuario_numero.isEnabled()) {
            String nombreChanged, numeroChanged;
            nombreChanged = eT_perfUsuario_nombreCom.getText().toString();
            numeroChanged = eT_perfUsuario_numero.getText().toString();
            // EL key sera el correo reistrado
            SharedPreferences datosUsuarioActualChanged = getSharedPreferences(email,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = datosUsuarioActualChanged.edit();
            // Guardamos 3 datos
            editor.putString("nombre", nombreChanged);
            //editor.putString("email", email);
            editor.putString("numero", numeroChanged);
            // HAcemos el commit
            editor.commit();
            // Le decimos al usuario que sus datos han sido guardados con exito
            Toast.makeText(this, R.string.msgToast_perfUsuario_1, Toast.LENGTH_LONG).show();
            // Volvemos a main
            VolverAMain();
        } else {
            Toast.makeText(this, R.string.msgToast_perfUsuario_2, Toast.LENGTH_LONG).show();
        }
    }

    private void CambiarFoto() {
        //Creamos un intent para dar la opcion de recorrer la galeria de imagenes
        //Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //intent.setType("image/");
        //startActivityForResult(intent.createchooser(intent, "Selecciona la Aplicación"), 10);
    }

    public void MensajeEmergente() {
        // Creamos un objeto de la clase AlertDialog, para poner en pantalla un mensaje emergente
        AlertDialog.Builder msg_alertaA4 = new AlertDialog.Builder(PerfilUsuario.this);
        // COnfiguramos un mensaje para el  AlertDialog
        msg_alertaA4.setMessage(R.string.msgAlertDiag_perfUsuario_1)
                .setNegativeButton(R.string.msgAlertDiag_perfUsuario_1_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                // Le damos un nombre al boton positivo del AlertDialog y hacemos un listener para programar la accion del boton
                .setPositiveButton(R.string.msgAlertDiag_perfUsuario_1_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Creamos un objeto de la clase Intent para que al presionar el boton vayamos al Activity Login
                        VolverAMain();
                    }
                })
                ;
        // Creamos un titulo para el AlertDialog
        AlertDialog titulo = msg_alertaA4.create();
        // Configuramos el mensaje para el titulo
        titulo.setTitle(R.string.msgAlertDiag_perfUsuario_1_title);
        // Mostramos en pantalla el AlertDialog
        titulo.show();
    }

    private void ConexionObjetosConVista() {
        // Hacemos puente de conexion con la parte grafica
        // Image Buttons
        imgB_perfUsuario_volver = findViewById(R.id.imgB_perfUsuario_volver);
        imgB_perfUsuario_editar = findViewById(R.id.imgB_perfUsuario_editar);
        imgB_perfUsuario_guardar = findViewById(R.id.imgB_perfUsuario_guardar);
        // ImageView circular
        imgV_perfUsuario_fotoUsuario = findViewById(R.id.imgV_perfUsuario_fotoUsuario);
        // TextView
        tV_perfUsuario_titulo = findViewById(R.id.tV_perfUsuario_titulo);
        tV_perfUsuario_cambiarFoto = findViewById(R.id.tV_perfUsuario_cambiarFoto);
        // EditText
        eT_perfUsuario_nombreCom = findViewById(R.id.eT_perfUsuario_nombreCom);
        eT_perfUsuario_email = findViewById(R.id.eT_perfUsuario_email);
        eT_perfUsuario_numero = findViewById(R.id.eT_perfUsuario_numero);
    }

    private void CargamosDatosusuario() {
        // Creamos un objeto SharedPreferences para buscar los datos del usuario que inicio sesion
        SharedPreferences inicioSesionUsuario = getSharedPreferences("inicio_sesion",
                Context.MODE_PRIVATE);
        // SI el objeto no esta vacio
        if (inicioSesionUsuario != null) {
            // Obtenemos el email del usuario
            email = inicioSesionUsuario.getString("email", "");
            // Si la variable String email no esta vacia
            if (!email.isEmpty()) {
                // Creamos un objeto Shared preferences para buscar los datos de usuario
                SharedPreferences datosUsuarioActual = getSharedPreferences(email,
                        Context.MODE_PRIVATE);
                // Obtenemos el nombre
                nombreUsuario = datosUsuarioActual.getString("nombre", "");
                // Obtenemos el numero
                numero = datosUsuarioActual.getString("numero", "");
                // Cargamos los datos del usuario en los edit text
                tV_perfUsuario_titulo.setText(getText(R.string.tV_perfUsuario_titulo) + " " + nombreUsuario);
                eT_perfUsuario_nombreCom.setText(nombreUsuario);
                eT_perfUsuario_email.setText(email);
                eT_perfUsuario_numero.setText(numero);
            }
        }
    }

    private void ListenersPerfilUsuario() {
        // Listeners
        imgB_perfUsuario_volver.setOnClickListener(this);
        imgB_perfUsuario_editar.setOnClickListener(this);
        imgB_perfUsuario_guardar.setOnClickListener(this);
        tV_perfUsuario_cambiarFoto.setOnClickListener(this);
    }
}