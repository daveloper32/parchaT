package com.developers.parchat.model.repository;

import android.content.Context;

import androidx.annotation.NonNull;

import com.developers.parchat.model.entity.Usuario;
import com.developers.parchat.view.main.MainActivityMVP;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class RepositoryMainActivity implements MainActivityMVP.Model {

    // Declaramos un objeto de la Clase FirebaseAuth
    private FirebaseAuth mAuth;
    // Declaramos un objeto de la Clase FirebaseUser
    private FirebaseUser usuarioActual;
    // Declaramos un objeto de la Clase DatabaseReference
    private DatabaseReference referenciaUsuario;
    // Declaramos una variable de tipo String para recibir el id del usuario en la base de datos -> User UID
    private String IdUsuario;
    private Usuario datosUsuario;

    // Variables modelo MVP Login
    private MainActivityMVP.Presenter presentadorMain;
    private Context contextMain;

    public RepositoryMainActivity() {
        // Inicializamos la instancia FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        // Buscamos el usuario que este logueado con la clase FirebaseAuth y lo guardamos en un objeto FirebaseUser
        usuarioActual = FirebaseAuth.getInstance().getCurrentUser();
        // Inicializamos la instancia FirebaseDatabase
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
    }
}
