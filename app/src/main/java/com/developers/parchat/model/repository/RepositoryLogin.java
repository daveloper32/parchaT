package com.developers.parchat.model.repository;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.developers.parchat.R;
import com.developers.parchat.model.entity.Usuario;
import com.developers.parchat.view.login.LoginMVP;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class RepositoryLogin implements LoginMVP.Model {

    // Declaramos un objeto de la Clase FirebaseAuth
    private FirebaseAuth mAuth;
    // Declaramos un objeto de la Clase DatabaseReference
    private DatabaseReference mDatabase;

    // Variables modelo MVP Login
    private LoginMVP.Presenter presentadorLogin;
    private Context contextLogin;

    /// REGISTRO GOOGLE
    // CLiente de inicio de sesion con Google
    private GoogleSignInClient mGoogleSignInClient;
    /// FIN REGISTRO GOOGLE

    // FACEBOOK THINGS
    private CallbackManager callbackManager;
    // FIN FACEBOOK THINGS


    public RepositoryLogin() {
        // Inicializamos la instancia FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        // Inicializamos la instancia FirebaseDatabase
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //performGoogleLogin();
        callbackManager = CallbackManager.Factory.create();
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

    @Override
    public void performGoogleLogin() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(contextLogin.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(contextLogin, gso);
    }



    @Override
    public void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Obtenemos el usaro autenticado
                            FirebaseUser usuarioActual = mAuth.getCurrentUser();
                            if (usuarioActual != null) {
                                firebaseAuthWithGoogleExito(usuarioActual);
                            } else {
                                presentadorLogin.firebaseAuthWithGoogleFalla();
                            }
                        }
                        else {
                            presentadorLogin.firebaseAuthWithGoogleFalla();
                        }
                    }
                });
    }

    @Override
    public void firebaseAuthWithGoogleExito(FirebaseUser usuarioActual) {
        String nomCompleto = usuarioActual.getDisplayName();
        String email = usuarioActual.getEmail();
        String numero = usuarioActual.getPhoneNumber();

        // Creamos un objeto de la clase usuario
        Usuario usuarioNuevoGoogle = new Usuario(nomCompleto,
                email, numero);

        if (usuarioNuevoGoogle != null) {
            guardarUsuarioNuevoWithGoogleEnBaseDatos(usuarioNuevoGoogle);
        } else {
            presentadorLogin.firebaseAuthWithGoogleFalla();
        }
    }


    @Override
    public void guardarUsuarioNuevoWithGoogleEnBaseDatos(Usuario usuarioNuevoGoogle) {
        mDatabase.child("Usuarios")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(usuarioNuevoGoogle)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            presentadorLogin.SaveUsuarioInDBWithGoogleExitosa();
                        } else {
                            presentadorLogin.SaveUsuarioInDBWithGoogleFallo();
                        }
                    }
                });
    }

    @Override
    public GoogleSignInClient getGoogleSignInClient() {
        return mGoogleSignInClient;
    }

    @Override
    public void firebaseAuthWithFacebook() {

        LoginManager.getInstance()
                .registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                if (loginResult != null) {
                                    firebaseAuthWithFacebookExito(loginResult.getAccessToken());
                                } else {
                                    presentadorLogin.firebaseAuthWithFacebookFalla();
                                }
                            }

                            @Override
                            public void onCancel() {

                            }

                            @Override
                            public void onError(FacebookException error) {
                                presentadorLogin.firebaseAuthWithFacebookFalla();
                            }
                        });
    }

    @Override
    public void firebaseAuthWithFacebookExito(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider
                .getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser usuarioActual = mAuth.getCurrentUser();
                            String nomCompleto = usuarioActual.getDisplayName();
                            String email = usuarioActual.getEmail();
                            String numero = usuarioActual.getPhoneNumber();

                            // Creamos un objeto de la clase usuario
                            Usuario usuarioNuevoFacebook = new Usuario(nomCompleto,
                                    email, numero);

                            if (usuarioNuevoFacebook != null) {
                                guardarUsuarioNuevoWithFacebookEnBaseDatos(usuarioNuevoFacebook);
                            } else {
                                presentadorLogin.firebaseAuthWithFacebookFalla();
                            }
                        } else {
                            presentadorLogin.firebaseAuthWithFacebookFalla();
                        }
                    }
                });

    }

    @Override
    public void guardarUsuarioNuevoWithFacebookEnBaseDatos(Usuario usuarioNuevoFacebook) {
        mDatabase.child("Usuarios")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(usuarioNuevoFacebook)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            presentadorLogin.SaveUsuarioInDBWithFacebookExitosa();
                        } else {
                            presentadorLogin.SaveUsuarioInDBWithFacebookFallo();
                        }
                    }
                });
    }

    @Override
    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

}
