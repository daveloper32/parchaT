package com.developers.parchat.model.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.developers.parchat.model.entity.Usuario;
import com.developers.parchat.view.login.LoginMVP;

import java.util.List;

public class RepositoryBuscarUsuario implements LoginMVP.Model {

    // Variables modelo MVP
    private LoginMVP.Presenter presentadorLogin;
    private Context context;

    // Creamos un objeto SharedPreferences para buscar los datos del Usuario que quiere iniciar sesion
    private SharedPreferences datosUsuarioActual;
    private SharedPreferences inicioSesionUsuario;
    private SharedPreferences.Editor editor;

    @Override
    public void setPresentadorLogin(LoginMVP.Presenter presentadorLogin, Context context) {
        this.presentadorLogin = presentadorLogin;
        this.context = context;
    }

    @Override
    public void guardarSaltarLogin(String email) {
        // Creamos un objeto Shared preferences para guardar el inicio de sesion
        inicioSesionUsuario = context.getSharedPreferences("inicio_sesion",
                Context.MODE_PRIVATE);
        editor = inicioSesionUsuario.edit();
        // Guardamos 2 datos
        editor.putString("email", email);
        editor.putBoolean("saltarLogin", true);
        // HAcemos el commit
        editor.commit();
    }

    @Override
    public boolean validarSaltarLogin() {
        // Creamos un objeto Shared preferences para guardar el inicio de sesion
        inicioSesionUsuario = context.getSharedPreferences("inicio_sesion",
                Context.MODE_PRIVATE);
        if (inicioSesionUsuario != null) {
            boolean saltarLogin = inicioSesionUsuario.getBoolean("saltarLogin", false);
            if (saltarLogin) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean validarEmailUsuario(String email) {
        datosUsuarioActual = context.getSharedPreferences(email,
                Context.MODE_PRIVATE);
        String buscarEmail;
        buscarEmail = datosUsuarioActual.getString("email", "");

        if (buscarEmail.isEmpty()) {
            return false;
        } else {
            return true;
        }

    }

    @Override
    public boolean validarPasswordUsuario(String email, String password) {
        datosUsuarioActual = context.getSharedPreferences(email,
                Context.MODE_PRIVATE);
        String buscarEmail, buscarPassword;
        buscarEmail = datosUsuarioActual.getString("email", "");
        buscarPassword = datosUsuarioActual.getString("password", "");

        if (buscarEmail.equals(email) && buscarPassword.equals(password)) {
            return true;
        } else {
            return false;
        }
    }
}
