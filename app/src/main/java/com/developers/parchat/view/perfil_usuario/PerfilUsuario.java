package com.developers.parchat.view.perfil_usuario;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.developers.parchat.model.entity.Usuario;
import com.developers.parchat.view.main.MainActivity;
import com.developers.parchat.R;

import java.io.File;
import java.io.InputStream;
import java.net.URL;


@RequiresApi(api = Build.VERSION_CODES.O)
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

    // Ruta almacenamiento de fotos tomadas
    private String RUTA_FOTO = "parchat";
    private String ruta;
    private Uri fotoUri;
    private String linkFotoUsuarioSubida;


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
        linkFotoUsuarioSubida = "";

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
                //cambiarFotoPronto();
                verificarPermisosCamara();
                //presentador.CambiarFoto();
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
                numeroChanged, linkFotoUsuarioSubida);
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
            String imgUsuario = usuarioActivo.getUrlImagenPerfil();
            if (!imgUsuario.equals("") && imgUsuario != null) {
                Uri uriImgUsuario = Uri.parse(imgUsuario);
                cargarImagenUsuario(uriImgUsuario);
            }
        }
    }

    @Override
    public void EditarDatos() {
        // Hacemos que se puedan editar los EditText
        eT_perfUsuario_nombreCom.setEnabled(true);
        //eT_perfUsuario_email.setEnabled(true);
        eT_perfUsuario_numero.setEnabled(true);
        //
        //tV_perfUsuario_cambiarFoto.setEnabled(true);
    }

    public void verificarPermisosCamara() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED)
                {
            // SI tiene el permiso de camara
            //presentador.CambiarFoto();
            //cambiarFotoPronto();
            cambiarFoto();

        } else {
            final String[] PERMISOS_CAMARA = {
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            };
            // Si no tiene el permiso de camara
            //cambiarFotoPronto();
            requestPermissionCamara.launch(PERMISOS_CAMARA);
        }
    }

    private ActivityResultLauncher <String[]> requestPermissionCamara =
    registerForActivityResult(
                    new ActivityResultContracts.RequestMultiplePermissions(),
    isGranted -> {
        if (!isGranted.containsValue(false)) {
            //presentador.CambiarFoto();
            //cambiarFotoPronto();
            cambiarFoto();
        } else {
            // Si los permisos no se otorgaron
            showToastPermisosNoOtorgados();
        }
    });

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("UseCompatLoadingForDrawables")
    private void cambiarFoto() {
        String tomarFoto = this.getResources().getString(R.string.msgAlertDiag_perfUsuario_cambiarFoto_1);
        String elegirDeGaleria = this.getResources().getString(R.string.msgAlertDiag_perfUsuario_cambiarFoto_2);
        String cancelar = this.getResources().getString(R.string.msgAlertDiag_perfUsuario_cambiarFoto_3);
        String [] opcionesCambiarFoto = {
                tomarFoto,
                elegirDeGaleria,
                cancelar
        };
        // Hacemos un alert Dialog

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));

        // Le ponemos las opciones
        builder.setItems(opcionesCambiarFoto,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (opcionesCambiarFoto[which] == tomarFoto) {
                            //cambiarFotoPronto();
                            TomarFoto();
                        } else if (opcionesCambiarFoto[which] == elegirDeGaleria) {
                            ElegirFotoDeGaleria();
                        } else if (opcionesCambiarFoto[which] == cancelar) {
                            // Cerramos el alert dialog
                            dialog.dismiss();
                        }

                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle(this.getResources().getString(R.string.msgAlertDiag_perfUsuario_cambiarFoto_titulo));
        alertDialog.setIcon(this.getResources().getDrawable(R.drawable.ic_foto));
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(this.getResources().getDrawable(R.drawable.style_alert_dialog));

    }

    private void TomarFoto() {
        Intent iniCamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        tomarFotoRequest.launch(iniCamara);
    }
    private ActivityResultLauncher <Intent> tomarFotoRequest =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult()
                    , new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                Intent data = result.getData();
                                if (data.getExtras() != null) {
                                    Bundle extras = data.getExtras();
                                    Bitmap imgBitmap = (Bitmap) extras.get("data");
                                    imgV_perfUsuario_fotoUsuario.setImageBitmap(imgBitmap);
                                    imgV_perfUsuario_fotoUsuario.setDrawingCacheEnabled(true);
                                    imgV_perfUsuario_fotoUsuario.buildDrawingCache();
                                    presentador.uploadFotoUsuarioFromImageView(imgBitmap);
                                    showProgressBar();
                                } else {
                                    showToastUploadImagenPerfilFallo();
                                }
                            }
                        }
                    }
                    );


    private void ElegirFotoDeGaleria() {
        Intent iniGaleria = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        selectFotoFromGaleriaRequest.launch(iniGaleria);
    }
    private ActivityResultLauncher <Intent> selectFotoFromGaleriaRequest =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult()
                    , new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                Intent data = result.getData();
                                fotoUri = data.getData();
                                imgV_perfUsuario_fotoUsuario.setImageURI(fotoUri);
                                imgV_perfUsuario_fotoUsuario.setDrawingCacheEnabled(true);
                                imgV_perfUsuario_fotoUsuario.buildDrawingCache();
                                Bitmap bitmap1 = ((BitmapDrawable) imgV_perfUsuario_fotoUsuario.getDrawable()).getBitmap();
                                presentador.uploadFotoUsuarioFromImageView(bitmap1);
                                showProgressBar();

                            }
                        }
                    }
            );


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

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void MensajeEmergente(Context context) {

        // Creamos un objeto de la clase AlertDialog, para poner en pantalla un mensaje emergente
        AlertDialog.Builder msg_alertaA4 = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
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
        titulo.getWindow().setBackgroundDrawable(this.getResources().getDrawable(R.drawable.style_alert_dialog));

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
    public void showToastPermisosNoOtorgados() {
        Toast.makeText(this, R.string.msgToast_perfUsuario_5, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showToastUploadImagenPerfilFallo() {
        Toast.makeText(this, R.string.msgToast_perfUsuario_6, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showToastActualizarURLFotoUsuarioLogueadoToDBExito() {
        Toast.makeText(this, R.string.msgToast_perfUsuario_8, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showToastActualizarURLFotoUsuarioLogueadoToDBFallo() {
        Toast.makeText(this, R.string.msgToast_perfUsuario_9, Toast.LENGTH_LONG).show();
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

    @Override
    public void cargarImagenUsuario(Uri linkFotoUsuario) {
        // Cargamos la foto
        if (linkFotoUsuario != null) {
            // Download directly from StorageReference using Glide
            // (See MyAppGlideModule for Loader registration)
            Glide.with(this /* context */)
                    .load(linkFotoUsuario)
                    .into(imgV_perfUsuario_fotoUsuario);
        }
    }

    @Override
    public void getURLImagenUsuarioFromStorage(Uri linkFotoUsuario) {
        linkFotoUsuarioSubida = String.valueOf(linkFotoUsuario);
        hideProgressBar();
    }
}