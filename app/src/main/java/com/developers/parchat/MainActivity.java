package com.developers.parchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Creramos los objetos necesarios para conectar con la parte grafica del menu desplegable y la barra
    // de herramientas personalizada
    private DrawerLayout dL_MainActivity;
    private ActionBarDrawerToggle bAB_MainActivity;
    private Toolbar tB_MainActivity;
    private NavigationView nV_MainActivity;

    //FragmentManager fragmentManager;
    // FragmentTransaction fragmentTransaction;

    // Declaramos Textviews e ImageView circular que aparecen en el header del menu desplegable
    private TextView tV_ActivityMain_nomUsuario, tV_ActivityMain_emailUsuario;
    private de.hdodenhof.circleimageview.CircleImageView imgV_ActivityMain_fotoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hacemos la conexion con la vista de los diferentes objetos de los layouts del Navigation View
        // Toolbar
        tB_MainActivity = findViewById(R.id.tB_MainActivity);

        dL_MainActivity = findViewById(R.id.dL_MainActivity);
        // NavigationView
        nV_MainActivity = findViewById(R.id.nV_MainActivity_menuLateral);

        // Hacemos la conexion de Textviews y ImageView circular del Header
        tV_ActivityMain_nomUsuario = findViewById(R.id.tV_ActivityMain_nomUsuario);
        tV_ActivityMain_emailUsuario = findViewById(R.id.tV_ActivityMain_emailUsuario);
        imgV_ActivityMain_fotoUsuario = findViewById(R.id.imgV_ActivityMain_fotoUsuario);

        // Le pasamos la barra que se creo en toolbar_activity_main
        setSupportActionBar(tB_MainActivity);
        // Le quitamos el titulo que viene por defecto y lo dejamos vacio
        getSupportActionBar().setTitle("");

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

        // Cargamos el fragmento
        /*fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fL_MainActivity_Contenedor, new FragmentShowMaps());
        fragmentTransaction.commit();*/

        // CArgamos el fragmento con el mapa en el Frame Layout de contenido_fragment_activity_main
        Fragment fragment = new FragmentShowMaps();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fL_MainActivity_Contenedor, fragment)
                .commit();

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

        //Listener del Navigation View
        nV_MainActivity.setNavigationItemSelectedListener(this);


        //Dejamos preparado los sets para poner el nombre del usuario e imagen
        //tV_ActivityMain_nomUsuario.setText("");
        //tV_ActivityMain_emailUsuario.setText("");


    }

    // Que ocurre cuando se selecciona alguna opcion del Navigation View
    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
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
        // Creamos un objeto de la clase Intent para que al presionar el boton vayamos al Activity
        //Intent deMainActivityABusquedaSitios = new Intent(MainActivity.this, .class);
        // Iniciamos el Activity
        //startActivity(deMainActivityABusquedaSitios);
        // Terminamos el activity MainActivity
        //MainActivity.this.finish();
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
        // Creamos un objeto de la clase Intent para que al presionar el boton vayamos al Activity Login
        Intent deMainActivityALogin = new Intent(MainActivity.this, Login.class);
        // Iniciamos el Activity Login
        startActivity(deMainActivityALogin);
        // Terminamos el activity MainActivity
        MainActivity.this.finish();
    }
}