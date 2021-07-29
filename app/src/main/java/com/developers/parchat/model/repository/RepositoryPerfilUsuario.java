package com.developers.parchat.model.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.developers.parchat.model.entity.Usuario;
import com.developers.parchat.view.perfil_usuario.PerfilUsuarioMVP;


public class RepositoryPerfilUsuario implements PerfilUsuarioMVP.Model {

    // Variables modelo MVP
    private PerfilUsuarioMVP.Presenter presentadorPerfilUsuario;
    private Context context;

    // Creamos un objeto SharedPreferences para buscar los datos del Usuario que quiere iniciar sesion
    private SharedPreferences datosUsuarioActual;
    private SharedPreferences inicioSesionUsuario;
    private SharedPreferences.Editor editor;

    @Override
    public void setPresentadorPerfilUsuario(PerfilUsuarioMVP.Presenter presentadorPerfilUsuario, Context context) {
        this.presentadorPerfilUsuario = presentadorPerfilUsuario;
        this.context = context;
    }

    @Override
    public String getEmailSaltarInicioSesion() {
        // Creamos un objeto Shared preferences para guardar el inicio de sesion
        inicioSesionUsuario = context.getSharedPreferences("inicio_sesion",
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
    public Usuario getDatosPerfilUsuario(String emailUsuario) {
        // Creamos un objeto Shared preferences para buscar los datos de usuario
        datosUsuarioActual = context.getSharedPreferences(emailUsuario,
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
            Usuario usuario = new Usuario(nombreUsuario, emailUsuario, numero);
            // Retornamos el obtejo tipo Usuario
            return usuario;

        } else {
            return null;
        }
    }

    @Override
    public boolean editarDatosUsuario(Usuario usuario_a_editar) {
        // Creamos un objeto Shared preferences para buscar los datos de usuario
        datosUsuarioActual = context.getSharedPreferences(usuario_a_editar.getEmail(),
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
