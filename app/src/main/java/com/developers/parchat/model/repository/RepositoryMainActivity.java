package com.developers.parchat.model.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.developers.parchat.R;
import com.developers.parchat.model.entity.Usuario;
import com.developers.parchat.view.main.MainActivityMVP;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

public class RepositoryMainActivity implements MainActivityMVP.Model {

    // Declaramos un objeto de la Clase FirebaseAuth
    private FirebaseAuth mAuth;
    // Declaramos un objeto de la Clase FirebaseUser
    private FirebaseUser usuarioActual;
    // Declaramos un objeto de la Clase DatabaseReference
    private DatabaseReference referenciaUsuario;
    // Declaramos un Objeto de la clase GoogleSignInClient
    private GoogleSignInClient mGoogleSignInClient;
    // Declaramos una variable de tipo String para recibir el id del usuario en la base de datos -> User UID
    private String IdUsuario;
    private Usuario datosUsuario;

    // Creamos un objeto SharedPreferences para guardar datos iniciales o por defecto de COnfiguracion del Usuario
    private SharedPreferences datosConfiguracion;
    private SharedPreferences.Editor editor;

    // Variables modelo MVP Login
    private MainActivityMVP.Presenter presentadorMain;
    private Context contextMain;

    public RepositoryMainActivity() {
        // Inicializamos la instancia FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        // Buscamos el usuario que este logueado con la clase FirebaseAuth y lo guardamos en un objeto FirebaseUser
        usuarioActual = FirebaseAuth.getInstance().getCurrentUser();
        // Inicializamos la referencia con FirebaseDatabase
        referenciaUsuario = FirebaseDatabase.getInstance().getReference("Usuarios");
        // Obtenemos el Id del usuario
        IdUsuario = usuarioActual.getUid();
    }

    @Override
    public void setPresentadorMain(MainActivityMVP.Presenter presentadorMain, Context contextoMain) {
        this.presentadorMain = presentadorMain;
        this.contextMain= contextoMain;
    }

    @Override
    public void getDatosUsuarioLogueadoFromDB() {
        // Le pasamos el id del usuario a la referencia en la base de datos para obtener los datos del usuario
        referenciaUsuario.child(IdUsuario)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        // Creamos un objeto de la clase Usuario para recibir el objeto con los datos
                        // del usuario en la base de datos
                        datosUsuario = snapshot.getValue(Usuario.class);
                        // Verificamos que lo haya encontrado -> Si el objeto Usuario no esta vacio
                        if (datosUsuario != null) {
                            presentadorMain.obtenerDatosUsuarioLogeadoConExito();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        presentadorMain.obtenerDatosUsuarioLogeadoConFalla();
                    }
                });
    }

    @Override
    public Usuario getUsuarioLogueado() {
        return datosUsuario;
    }

    @Override
    public void cerrarSesionFirebase() {
        // Usamos el metodo de FirebaseAuth para cerrar sesión
        mAuth.signOut();
        // Cerramos sesion con Google
        mGoogleSignInClient.signOut();
    }

    @Override
    public void crearConfiguracionesIniciales() {
        // Creamos un objeto Shared preferences para guardar las configuraciones de usuario
        // EL key sera configuracionesUsuario
        datosConfiguracion = contextMain.getSharedPreferences("configuracionesUsuario",
                Context.MODE_PRIVATE);
        editor = datosConfiguracion.edit();
        if (!datosConfiguracion.contains("modoBusquedaGPS")) {
            // Guardamos 3 datos
            editor.putBoolean("modoBusquedaGPS", true);
            editor.putBoolean("modoBusquedaMarker", false);
            editor.putBoolean("rangoBusquedaVisible", true);
            editor.putString("rangoBusquedaKm", "0.5");
            editor.putInt("valorSBRangoBusquedaKm", 2);
            // Hacemos el commit
            editor.commit();
        }

    }


    @Override
    public void performGoogleLogin() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(contextMain.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(contextMain, gso);
    }
}
