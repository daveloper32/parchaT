package com.developers.parchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

public class PerfilUsuario extends AppCompatActivity implements View.OnClickListener {

    //Toolbar tB_PerfilUsuario;
    // Declaramos objetos EditText, TextView e Imageview circular
    private de.hdodenhof.circleimageview.CircleImageView imgV_perfUsuario_fotoUsuario;
    private TextView tV_perfUsuario_cambiarFoto;
    private EditText eT_perfUsuario_nombreCom, eT_perfUsuario_email, eT_perfUsuario_numero;
    //private MenuItem b_perfUsuario_guardar = findViewById(R.id.b_perfUsuario_guardar);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        //tB_PerfilUsuario = findViewById(R.id.tB_PerfilUsuario);
        //setSupportActionBar(tB_PerfilUsuario);

        // Le quitamos el titulo que viene por defecto y lo dejamos vacio
        getSupportActionBar().setTitle("");

        // Hacemos puente de conexion con la parte grafica
        // ImageView circular
        imgV_perfUsuario_fotoUsuario = findViewById(R.id.imgV_perfUsuario_fotoUsuario);
        // TextView
        tV_perfUsuario_cambiarFoto = findViewById(R.id.tV_perfUsuario_cambiarFoto);
        // EditText
        eT_perfUsuario_nombreCom = findViewById(R.id.eT_perfUsuario_nombreCom);
        eT_perfUsuario_email = findViewById(R.id.eT_perfUsuario_email);
        eT_perfUsuario_numero = findViewById(R.id.eT_perfUsuario_numero);
    }

    @Override
    public void onClick(View v) {
        // Obtengo el id del objeto que esta siendo presionado
        int idPresionado = v.getId();

        switch (idPresionado) {
            case (R.id.tV_perfUsuario_cambiarFoto):
                break;
            case (R.id.eT_perfUsuario_nombreCom):
                break;
            case (R.id.eT_perfUsuario_email):
                break;
            case (R.id.eT_perfUsuario_numero):
                break;
        }

    }

    // Para mostrar los botones de accion en el toolbar
    public boolean onCreateOptionsMenu (Menu menu) {
        // Llamamos al menu que creamos en res/menu
        getMenuInflater().inflate(R.menu.menu_perfil_usuario, menu);

        return true;
    }

    // Para agregar las acciones a los botones
    public boolean onOptionsItemSelected (MenuItem menuItem) {
        // Obtenemos el id del boton que se haya seleccionado
        int id = menuItem.getItemId();

        switch (id) {
            case (R.id.b_perfUsuario_atras):
                VolverAMain();
                break;
            case (R.id.b_perfUsuario_editar):
                PermitirEdicionDatos();
                break;
            case (R.id.b_perfUsuario_guardar):
                GuardarCambios();
                break;
        }
        return true;
    }

    private void PermitirEdicionDatos() {
        // Hacemos que se puedan editar los EditText
        eT_perfUsuario_nombreCom.setEnabled(true);
        eT_perfUsuario_email.setEnabled(true);
        eT_perfUsuario_numero.setEnabled(true);
    }

    private void GuardarCambios() {
        // Creamos 3 varibales booleanas
        //Boolean nomComActivo, emailActivo, numeroActivo;
        // Verificamos que los edit text se puedan editar
        //nomComActivo = eT_perfUsuario_nombreCom.isEnabled();
        //emailActivo = eT_perfUsuario_email.isEnabled();
        //numeroActivo = eT_perfUsuario_numero.isEnabled();
        // Si se dejan editar podemos activar el boton
        //b_perfUsuario_guardar.setVisible(true);

    }

    private void VolverAMain() {
        // Creamos un objeto de la clase Intent para que al presionar el boton vayamos al MainActivity o de Pagina Principal de la App
        Intent dePerfilUsuarioAMainActivity = new Intent(PerfilUsuario.this, MainActivity.class);
        // Iniciamos el MainActivity o de Pagina Principal de la App
        startActivity(dePerfilUsuarioAMainActivity);
        // Terminamos el activity PerfilUsuario
        PerfilUsuario.this.finish();
    }


}