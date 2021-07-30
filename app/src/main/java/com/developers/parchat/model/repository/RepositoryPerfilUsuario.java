package com.developers.parchat.model.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.developers.parchat.model.entity.Usuario;
import com.developers.parchat.view.perfil_usuario.PerfilUsuarioDatos;
import com.developers.parchat.view.perfil_usuario.PerfilUsuarioMVP;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


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
    public String getEmailSaltarInicioSesion() {
        // Creamos un objeto Shared preferences para guardar el inicio de sesion
        inicioSesionUsuario = contextPerfilUsuario.getSharedPreferences("inicio_sesion",
                Context.MODE_PRIVATE);
        // Validamos que no este vacio
        if (inicioSesionUsuario != null) {
            // Obtenemos el email del usuario
            String email = inicioSesionUsuario.getString("email", "");
            // Validamos que la variable String email no este vacia
            if (!email.isEmpty()) {
                return email;
            }
            else {
                return "";
            }
        } else {
            return "";
        }
    }

    @Override
    public PerfilUsuarioDatos getDatosPerfilUsuario(String emailUsuario) {
        // Creamos un objeto Shared preferences para buscar los datos de usuario
        datosUsuarioActual = contextPerfilUsuario.getSharedPreferences(emailUsuario,
                Context.MODE_PRIVATE);
        // Verificamos que no este vacio
        if (datosUsuarioActual != null) {
            // Creamos variables para recibir los datos del usuario
            String nombreUsuario, numero;
            // Obtenemos el nombre
            nombreUsuario = datosUsuarioActual.getString("nombre", "");
            // Obtenemos el numero
            numero = datosUsuarioActual.getString("numero", "");
            // Creamos un objeto tipo Usuario
            PerfilUsuarioDatos usuario = new PerfilUsuarioDatos(nombreUsuario, emailUsuario, numero);
            // Retornamos el obtejo tipo Usuario
            return usuario;

        } else {
            return null;
        }
    }

    @Override
    public boolean editarDatosUsuario(PerfilUsuarioDatos usuario_a_editar) {
        // Creamos un objeto Shared preferences para buscar los datos de usuario
        datosUsuarioActual = contextPerfilUsuario.getSharedPreferences(usuario_a_editar.getEmail(),
                Context.MODE_PRIVATE);
        if (datosUsuarioActual != null) {
            editor = datosUsuarioActual.edit();
            // Guardamos 2 datos
            editor.putString("nombre", usuario_a_editar.getNombreCompleto());
            editor.putString("numero", usuario_a_editar.getNumeroCel());
            // HAcemos el commit
            editor.commit();
            return true;
        } else {
            return false;
        }
    }
}
