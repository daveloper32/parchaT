package com.developers.parchat.model.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.developers.parchat.model.entity.Usuario;
import com.developers.parchat.view.login.LoginMVP;
import com.developers.parchat.view.registro.RegistroMVP;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

public class RepositoryRegistroGuardarUsuario implements RegistroMVP.Model {

    // Declaramos un objeto de la Clase FirebaseAuth
    private FirebaseAuth mAuth;

    // Declaramos un objeto de la Clase DatabaseReference
    private DatabaseReference mDatabase;

    // Variables modelo MVP Registro
    private RegistroMVP.Presenter presentadorRegistro;
    private Context contextRegistro;

    // Creamos un objeto SharedPreferences para buscar los datos del Usuario que quiere iniciar sesion
    private SharedPreferences datosUsuarioActual;
    private SharedPreferences inicioSesionUsuario;
    private SharedPreferences.Editor editor;

    public RepositoryRegistroGuardarUsuario() {
        // Inicializamos la instancia FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        // Inicializamos la instancia FirebaseDatabase
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void setPresentadorRegistro(RegistroMVP.Presenter presentadorRegistro, Context contextRegistro) {
        this.presentadorRegistro = presentadorRegistro;
        this.contextRegistro = contextRegistro;
    }

    @Override
    public void autenticarUsuarioNuevo(Usuario usuario) {
        // Creamos un usuario en FireBase con email y contraseña
        mAuth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            presentadorRegistro.AuthUsuarioExitosa();
                        } else {
                            presentadorRegistro.AuthUsuarioFallo();
                        }
                    }
                });
    }
    @Override
    public void guardarUsuarioNuevoEnBaseDatos(Usuario usuario) {
        // Ahora vamos a conectar con la base de Datos de Firebase y
        // Le pasamos el objeto de la clase Usuario
        mDatabase.child("Usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                // Volvemos a comprobar, esta vez si guardo el nuevo usuario en la base de datos -> Realtime Database
                if(task.isSuccessful()) {
                    presentadorRegistro.SaveUsuarioInDBExitosa();
                } else {
                    presentadorRegistro.SaveUsuarioInDBFallo();
                }
            }
        });

    }



}
