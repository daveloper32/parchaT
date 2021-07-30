package com.developers.parchat.model.repository;

import android.content.Context;

import com.developers.parchat.view.login.LoginMVP;
import com.developers.parchat.view.main.MainActivityDatos;
import com.developers.parchat.view.main.MainActivityMVP;
import com.google.firebase.auth.FirebaseAuth;

public class RepositoryMainActivity implements MainActivityMVP.Model {

    // Declaramos un objeto de la Clase FirebaseAuth
    private FirebaseAuth mAuth;

    // Variables modelo MVP Login
    private MainActivityMVP.Presenter presentadorLogin;
    private Context contextLogin;

    public RepositoryMainActivity() {
        // Inicializamos la instancia FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void setPresentadorMain(MainActivityMVP.Presenter presentadorMain, Context contextoMain) {
        this.presentadorLogin = presentadorMain;
        this.contextLogin = contextoMain;
    }


    @Override
    public MainActivityDatos getDatosPerfilUsuario(String emailUsuario) {
        return null;
    }

    @Override
    public void cerrarSesionFirebase() {
        // Usamos el metodo de FirebaseAuth para cerrar sesión
        mAuth.signOut();
    }
}
