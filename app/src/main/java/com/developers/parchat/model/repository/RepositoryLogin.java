package com.developers.parchat.model.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.developers.parchat.model.entity.Usuario;
import com.developers.parchat.view.login.LoginMVP;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import java.util.List;

public class RepositoryLogin implements LoginMVP.Model {

    // Declaramos un objeto de la Clase FirebaseAuth
    private FirebaseAuth mAuth;
    // Declaramos un objeto de la Clase DatabaseReference
    private DatabaseReference mDatabase;

    // Variables modelo MVP Login
    private LoginMVP.Presenter presentadorLogin;
    private Context contextLogin;


    public RepositoryLogin() {
        // Inicializamos la instancia FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        // Inicializamos la instancia FirebaseDatabase
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void setPresentadorLogin(LoginMVP.Presenter presentadorLogin, Context contextLogin) {
        this.presentadorLogin = presentadorLogin;
        this.contextLogin = contextLogin;
    }

    @Override
    public void validarConEmailYPasswordUsuario(String email, String password) {
        // Usamos el metodo de FirebaseAuth para ingresar con email y contraseña
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                // Verificamos si logro ingresar
                if (task.isSuccessful()) {
                    presentadorLogin.InicioSesionExitoso();
                } else {
                    presentadorLogin.InicioSesionFallido();
                }
            }
        });
    }

    @Override
    public boolean validarSaltarLogin() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            return true;
        } else {
            return false;
        }
    }
}
