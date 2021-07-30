package com.developers.parchat.model.repository;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.developers.parchat.model.entity.Usuario;
import com.developers.parchat.view.perfil_usuario.PerfilUsuarioDatos;
import com.developers.parchat.view.perfil_usuario.PerfilUsuarioMVP;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;


public class RepositoryPerfilUsuario implements PerfilUsuarioMVP.Model {

    // Declaramos un objeto de la Clase FirebaseAuth
    private FirebaseAuth mAuth;
    // Declaramos un objeto de la Clase FirebaseUser
    private FirebaseUser usuarioActual;
    // Declaramos un objeto de la Clase DatabaseReference
    private DatabaseReference referenciaUsuario;
    // Declaramos una variable de tipo String para recibir el id del usuario en la base de datos -> User UID
    private String IdUsuario;
    private Usuario datosUsuario;

    // Variables modelo MVP PerfilUsuario
    private PerfilUsuarioMVP.Presenter presentadorPerfilUsuario;
    private Context contextPerfilUsuario;

    // Creamos un objeto SharedPreferences para buscar los datos del Usuario que quiere iniciar sesion
    private SharedPreferences datosUsuarioActual;
    private SharedPreferences inicioSesionUsuario;
    private SharedPreferences.Editor editor;

    public RepositoryPerfilUsuario() {
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
    public void setPresentadorPerfilUsuario(PerfilUsuarioMVP.Presenter presentadorPerfilUsuario, Context contextPerfilUsuario) {
        this.presentadorPerfilUsuario = presentadorPerfilUsuario;
        this.contextPerfilUsuario = contextPerfilUsuario;
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
                            presentadorPerfilUsuario.obtenerDatosUsuarioLogeadoConExito();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        presentadorPerfilUsuario.obtenerDatosUsuarioLogeadoConFalla();
                    }
                });
    }

    @Override
    public PerfilUsuarioDatos getDatosPerfilUsuario() {
        if (datosUsuario != null) {
            PerfilUsuarioDatos datosUsuarioActual = new PerfilUsuarioDatos(
                    datosUsuario.getNombreCompleto(), datosUsuario.getEmail(),
                    datosUsuario.getNumeroCel());
            return datosUsuarioActual;
        } else {
            return null;
        }
    }

    @Override
    public void editarDatosUsuario(PerfilUsuarioDatos usuario_a_editar) {
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
                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put(IdUsuario + "/nombreCompleto/",usuario_a_editar.getNombreCompleto());
                            childUpdates.put(IdUsuario + "/email/",usuario_a_editar.getEmail());
                            childUpdates.put(IdUsuario + "/numeroCel/",usuario_a_editar.getNumeroCel());

                            actualizarDatosUsuarioLogeadoConExito(childUpdates);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        presentadorPerfilUsuario.actualizarDatosUsuarioLogeadoConFalla();
                    }
                });
    }

    @Override
    public void actualizarDatosUsuarioLogeadoConExito(Map<String, Object> childUpdates) {
        referenciaUsuario.updateChildren(childUpdates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        presentadorPerfilUsuario.actualizarDatosUsuarioLogeadoConExito();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                presentadorPerfilUsuario.actualizarDatosUsuarioLogeadoConFalla();
            }
        });
    }

}
