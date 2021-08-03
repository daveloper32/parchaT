package com.developers.parchat.view.perfil_usuario;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.developers.parchat.model.entity.Usuario;
import com.developers.parchat.view.main.MainActivity;
import com.developers.parchat.R;


public class PerfilUsuario extends AppCompatActivity implements PerfilUsuarioMVP.View, View.OnClickListener {

    // Variables modelo MVP
    PerfilUsuarioMVP.Presenter presentador;

    // Declaramos objetos EditText, TextView e Imageview circular e ImageButton
    private ImageButton imgB_perfUsuario_volver, imgB_perfUsuario_editar, imgB_perfUsuario_guardar;
    private de.hdodenhof.circleimageview.CircleImageView imgV_perfUsuario_fotoUsuario;
    private TextView tV_perfUsuario_titulo, tV_perfUsuario_cambiarFoto;
    private EditText eT_perfUsuario_nombreCom, eT_perfUsuario_email, eT_perfUsuario_numero;
    private ProgressBar pB_registro;

    private String nombreUsuario, email, numero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        // Iniciamos los objetos de la vista
        IniciarVista();
        // Cargamos los datos del usuario en los EditText
        presentador.CargarDatosUsuario();
    }

    private void IniciarVista() {
        // Inicializamos elpresentador y le pasamos la vista -> this
        presentador = new PerfilUsuarioPresenter(this);

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
        // ProgressBar
        pB_registro = findViewById(R.id.pB_perfilUsuario);

        // Listeners
        imgB_perfUsuario_volver.setOnClickListener(this);
        imgB_perfUsuario_editar.setOnClickListener(this);
        imgB_perfUsuario_guardar.setOnClickListener(this);
        tV_perfUsuario_cambiarFoto.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // Obtengo el id del objeto que esta siendo presionado
        int idPresionado = v.getId();
        switch (idPresionado) {
            case (R.id.imgB_perfUsuario_volver):
                if (isEdicionActivada()){
                    MensajeEmergente(this);
                } else {
                    irAlActivityMain(MainActivity.class);
                }
                break;
            case (R.id.imgB_perfUsuario_editar):
                EditarDatos();
                break;
            case (R.id.imgB_perfUsuario_guardar):
                presentador.GuardarDatos();
                break;
            case (R.id.tV_perfUsuario_cambiarFoto):
                presentador.CambiarFoto();
                break;

        }
    }

    @Override
    public Usuario getNuevosDatosUsuario() {

        String nombreChanged, emailNotChanged, numeroChanged;
        nombreChanged = eT_perfUsuario_nombreCom.getText().toString().trim();
        emailNotChanged = eT_perfUsuario_email.getText().toString().trim();
        numeroChanged = eT_perfUsuario_numero.getText().toString().trim();
        // El email no lo cambiamos
        Usuario usuarioNuevosDatos = new Usuario(nombreChanged, emailNotChanged,
                numeroChanged);
        return usuarioNuevosDatos;

    }

    @Override
    public boolean isEdicionActivada() {
        boolean et_nom_activado = eT_perfUsuario_nombreCom.isEnabled();
        boolean et_num_activado = eT_perfUsuario_numero.isEnabled();
        if (et_nom_activado && et_num_activado) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void CargamosDatosUsuario(Usuario usuarioActivo) {

        // Validamos que el usuario no este vacio
        if (usuarioActivo != null) {
            // Cargamos los datos del usuario en los edit text
            tV_perfUsuario_titulo.setText(getText(R.string.tV_perfUsuario_titulo) + " " + usuarioActivo.getSoloNombre());
            eT_perfUsuario_nombreCom.setText(usuarioActivo.getNombreCompleto());
            eT_perfUsuario_email.setText(usuarioActivo.getEmail());
            eT_perfUsuario_numero.setText(usuarioActivo.getNumeroCel());
        }
    }

    @Override
    public void EditarDatos() {
        // Hacemos que se puedan editar los EditText
        eT_perfUsuario_nombreCom.setEnabled(true);
        //eT_perfUsuario_email.setEnabled(true);
        eT_perfUsuario_numero.setEnabled(true);
    }

    @Override
    public void showEmptyNombreCompletoError() {
        // Mostramos el error en el TextInputLayout
        eT_perfUsuario_nombreCom.setError(getText(R.string.eT_Error_perfUsuario_1));
        eT_perfUsuario_nombreCom.requestFocus();
        return;
    }

    @Override
    public void showEmptyNumeroError() {
        // Mostramos el error en el TextInputLayout
        eT_perfUsuario_numero.setError(getText(R.string.eT_Error_perfUsuario_2));
        eT_perfUsuario_numero.requestFocus();
        return;
    }

    @Override
    public void MensajeEmergente(Context context) {

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
                        irAlActivityMain(MainActivity.class);
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

    @Override
    public void showToastErrorCargarDatos() {
        // Le decimos al usuario que sus datos no han sido cargados con exito
        Toast.makeText(this, R.string.msgToast_perfUsuario_1, Toast.LENGTH_LONG).show();

    }

    @Override
    public void showToastDatosGuardadosConExito() {
        // Le decimos al usuario que sus datos han sido guardados con exito
        Toast.makeText(this, R.string.msgToast_perfUsuario_2, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showToastErrorDatosGuardados() {
        // Le decimos al usuario que sus datos han sido guardados con exito
        Toast.makeText(this, R.string.msgToast_perfUsuario_4, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showToastErrorActivarEdicionDatos() {
        // Le decimos al usuario que para guardar de presionar el lapiz para editar y guardar
        Toast.makeText(this, R.string.msgToast_perfUsuario_3, Toast.LENGTH_LONG).show();
    }

    @Override
    public void irAlActivityMain(Class<? extends AppCompatActivity> ir_a_ActivityMain) {
        // Creamos un objeto de la clase Intent para que al presionar el boton vayamos al MainActivity o de Pagina Principal de la App
        Intent dePerfilUsuarioAMainActivity = new Intent(PerfilUsuario.this, ir_a_ActivityMain);
        // Iniciamos el MainActivity o de Pagina Principal de la App
        startActivity(dePerfilUsuarioAMainActivity);
        // Terminamos el activity PerfilUsuario
        PerfilUsuario.this.finish();
    }

    @Override
    public Context getContext() {
        return PerfilUsuario.this;
    }

    @Override
    public void showProgressBar() {
        pB_registro.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        pB_registro.setVisibility(View.GONE);
    }

}