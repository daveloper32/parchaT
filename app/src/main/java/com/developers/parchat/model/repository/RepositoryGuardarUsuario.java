package com.developers.parchat.model.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.developers.parchat.model.entity.Usuario;
import com.developers.parchat.view.login.LoginMVP;
import com.developers.parchat.view.registro.RegistroMVP;

public class RepositoryGuardarUsuario implements RegistroMVP.Model {

    // Variables modelo MVP
    private RegistroMVP.Presenter presentadorRegistro;
    private Context context;

    // Creamos un objeto SharedPreferences para buscar los datos del Usuario que quiere iniciar sesion
    private SharedPreferences datosUsuarioActual;
    private SharedPreferences inicioSesionUsuario;
    private SharedPreferences.Editor editor;

    @Override
    public void setPresentadorRegistro(RegistroMVP.Presenter presentadorRegistro, Context context) {
        this.presentadorRegistro = presentadorRegistro;
        this.context = context;
    }

    @Override
    public boolean buscarCorreo(String email) {
        datosUsuarioActual = context.getSharedPreferences(email,
                Context.MODE_PRIVATE);
        String buscarEmail;
        buscarEmail = datosUsuarioActual.getString("email", "");

        if (buscarEmail.equals(email)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void guardarUsuarioNuevo(Usuario usuario) {
        // Creamos un objeto Shared preferences para guardar datos de usuario
        // EL key sera el correo reistrado
        datosUsuarioActual = context.getSharedPreferences(usuario.getEmail(),
                Context.MODE_PRIVATE);
        editor = datosUsuarioActual.edit();
        // Guardamos 4 datos
        editor.putString("nombre", usuario.getNombreCompleto());
        editor.putString("email", usuario.getEmail());
        editor.putString("password", usuario.getPassword());
        editor.putString("numero", usuario.getNumeroCel());
        // Hacemos el commit
        editor.commit();
    }

}
