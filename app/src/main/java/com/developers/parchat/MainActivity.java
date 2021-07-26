// Si quiero llamar a un objeto de otro archivo de vista .xml
// LinearLayout vistaSecundaria;
// vistaSecundaria = findViewById(R.id.id_include);
// Objeto objeto = vistaSecundaria.findViewById(R.id.id_objeto);

package com.developers.parchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    // Creamos los objetos necesarios para conectar con la parte grafica del menu desplegable y la barra
    // de herramientas personalizada
    private DrawerLayout dL_MainActivity;
    private ActionBarDrawerToggle bAB_MainActivity;
    private Toolbar tB_MainActivity;
    private NavigationView nV_MainActivity;
    // Creamos un objeto tipo View para acceder alos objetos del header del main activity
    private View header_main_activity;
    // Declaramos Textviews e ImageView circular que aparecen en el header del menu desplegable
    private TextView tV_ActivityMain_nomUsuario, tV_ActivityMain_emailUsuario;
    private de.hdodenhof.circleimageview.CircleImageView imgV_ActivityMain_fotoUsuario;
    // Declaramos 2 variables de tipo String para recibir el nombre y email del usuario que inicio sesion
    private String emailUsuario, nombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hacemos la conexion con los objetos de la vista con findViewById
        ConexionObjetosConVista();
        // Le pasamos la barra que se creo en toolbar_activity_main
        setSupportActionBar(tB_MainActivity);
        // Le quitamos el titulo que viene por defecto y lo dejamos vacio
        getSupportActionBar().setTitle("");
        // Cargamos el nombre y el correo del usuario que inicio sesion
        DatosUsuarioEnHeader();
        // Cargamos el Fragment de Google Maps
        CargarFragmentGoogleMaps();
        // Cargamos la configuracion del NavigationDrawer
        CargarNavigationDrawer();
        // Asignamos los Listeners a los objetos de interaccion del MainActivity
        ListenersActivityMain();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {
        // Obtenemos el id de la opcion selecionada
        int idItem = item.getItemId();
        switch (idItem) {
            // Si se presiona ver perfil
            case (R.id.dM_MainActivity_verPerfil):
                VerPerfilUsuario();
                break;
            // Si se presiona Cambiar busqueda
            case (R.id.dM_MainActivity_cambiarBusqueda):
                CambiarBusqueda();
                break;
            // Si se presiona ver configuraciones
            case (R.id.dM_MainActivity_config):
                VerConfiguraciones();
                break;
            // Si se presiona Cerrar Sesión
            case (R.id.dM_MainActivity_cerrarSesion):
                CerrarSesion();
                break;
        }
        return false;
    }

    private void VerPerfilUsuario() {
        //progressBar_Login.setVisibility(View.GONE);
        // Creamos un objeto de la clase Intent para que al presionar el boton vayamos al Activity
        Intent deMainActivityAPerfilUsuario = new Intent(MainActivity.this, PerfilUsuario.class);
        // Iniciamos el Activity
        startActivity(deMainActivityAPerfilUsuario);
        // Terminamos el activity MainActivity
        MainActivity.this.finish();
    }

    private void CambiarBusqueda() {
        //progressBar_Login.setVisibility(View.GONE);
        // Creamos un objeto de la clase Intent para que al presionar el boton vayamos al Activity SeleccionarActividad
        Intent deMainActivityASeleccionarActividad = new Intent(MainActivity.this, SeleccionarActividad.class);
        // Iniciamos el Activity SeleccionarActividad
        startActivity(deMainActivityASeleccionarActividad);
        // Terminamos el activity MainActivity
        MainActivity.this.finish();
    }

    private void VerConfiguraciones() {
        //progressBar_Login.setVisibility(View.GONE);
        // Creamos un objeto de la clase Intent para que al presionar el boton vayamos al Activity
        //Intent deMainActivityAConfiguraciones = new Intent(MainActivity.this, .class);
        // Iniciamos el Activity
        //startActivity(deMainActivityAConfiguraciones);
        // Terminamos el activity MainActivity
        //MainActivity.this.finish();
    }

    private void CerrarSesion() {
        //progressBar_Login.setVisibility(View.GONE);
        // Creamos un objeto Shared preferences para guardar el inicio de sesion
        SharedPreferences inicioSesionUsuario = getSharedPreferences("inicio_sesion",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = inicioSesionUsuario.edit();
        // Guardamos 3 datos
        editor.putString("email", "");
        editor.putBoolean("saltarLogin", false);
        // HAcemos el commit
        editor.commit();
        // Creamos un objeto de la clase Intent para que al presionar el boton vayamos al Activity Login
        Intent deMainActivityALogin = new Intent(MainActivity.this, Login.class);
        // Iniciamos el Activity Login
        startActivity(deMainActivityALogin);
        // Terminamos el activity MainActivity
        MainActivity.this.finish();
    }

    private void ConexionObjetosConVista() {
        // Hacemos la conexion con la vista de los diferentes objetos de los layouts del Navigation View
        // Toolbar
        tB_MainActivity = findViewById(R.id.tB_MainActivity);
        // Drawer Layout
        dL_MainActivity = findViewById(R.id.dL_MainActivity);
        // NavigationView
        nV_MainActivity = findViewById(R.id.nV_MainActivity_menuLateral);
    }

    private void DatosUsuarioEnHeader() {
        // Le decimos al objeto View header_main_activity que vaya al navigationViewer
        // y obtenga la vista del header
        header_main_activity = nV_MainActivity.getHeaderView(0);

        // Hacemos la conexion de Textviews y ImageView circular del Header
        tV_ActivityMain_nomUsuario = header_main_activity.findViewById(R.id.tV_ActivityMain_nomUsuario);
        tV_ActivityMain_emailUsuario = header_main_activity.findViewById(R.id.tV_ActivityMain_emailUsuario);
        imgV_ActivityMain_fotoUsuario = header_main_activity.findViewById(R.id.imgV_ActivityMain_fotoUsuario);

        // Creamos objeto SharedPreferences para buscar el email del usuario que ingreso
        SharedPreferences inicioSesionUsuario = getSharedPreferences("inicio_sesion",
                Context.MODE_PRIVATE);

        // Si el objeto no es nulo o esta vacio procedemos a cargar los datos en el header
        if (inicioSesionUsuario != null) {
            // Obtenemos el email del usuario
            emailUsuario = inicioSesionUsuario.getString("email", "");
            // COnfirmamos que nos obtenga el email del usuario
            if (!emailUsuario.isEmpty()) {
                // Creamos un objeto Shared preferences para ver los datos del usuario
                SharedPreferences datosUsuarioActual = getSharedPreferences(emailUsuario,
                        Context.MODE_PRIVATE);
                // Obtenemos el nombre del usuario
                nombreUsuario = datosUsuarioActual.getString("nombre", "");
                // Hacemos sets a los textView para poner el nombre del usuario e imagen
                tV_ActivityMain_nomUsuario.setText(nombreUsuario);
                tV_ActivityMain_emailUsuario.setText(emailUsuario);
            }
        }

    }

    private void CargarNavigationDrawer() {

        // Creamos el botón que nos ayuda a abrir el Navigation View -> deslizable
        bAB_MainActivity = new ActionBarDrawerToggle(MainActivity.this,
                dL_MainActivity, tB_MainActivity,
                R.string.nV_Abrir_MainActivity,
                R.string.nV_Cerrar_MainActivity);

        // Por defecto el Navigation View se ubica a la izquierda de la pantalla -> con esta linea
        // la pasamos a la derecha de la pantalla
        ViewCompat.setLayoutDirection(tB_MainActivity, ViewCompat.LAYOUT_DIRECTION_RTL);

        dL_MainActivity.addDrawerListener(bAB_MainActivity);
        bAB_MainActivity.setDrawerIndicatorEnabled(true);
        bAB_MainActivity.syncState();

        // Al igual que el Navigation View esta ubicado por defecto a la izquierda de la pantalla el boton
        // que generamos para abrirlo sale a la izquierda por lo que se pasa  la derecha
        tB_MainActivity.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dL_MainActivity.isDrawerOpen(Gravity.RIGHT)) {
                    dL_MainActivity.closeDrawer(Gravity.RIGHT);
                } else {
                    dL_MainActivity.openDrawer(Gravity.RIGHT);
                }
            }
        });

    }

    private void CargarFragmentGoogleMaps() {
        // CArgamos el fragmento con el mapa en el Frame Layout de contenido_fragment_activity_main
        Fragment fragment = new FragmentShowMaps();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fL_MainActivity_Contenedor, fragment)
                .commit();
    }

    private void ListenersActivityMain() {
        //Listener del Navigation View
        nV_MainActivity.setNavigationItemSelectedListener(this);
    }
}
