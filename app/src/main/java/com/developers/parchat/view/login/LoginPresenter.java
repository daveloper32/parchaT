package com.developers.parchat.view.login;

// Va a llevar toda la logica de la vista Login

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Patterns;

import com.developers.parchat.model.entity.Usuario;
import com.developers.parchat.model.repository.RepositoryLogin;
import com.developers.parchat.view.recuperar_password.RecuperarPassword;
import com.developers.parchat.view.registro.Registro;
import com.developers.parchat.view.seleccionar_actividad.SeleccionarActividad;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;


public class LoginPresenter implements LoginMVP.Presenter {

    // Variables modelo MVP
    private final LoginMVP.View vista;
    private final LoginMVP.Model modelo;

    // EL constructor va a recibir la vista
    public LoginPresenter(LoginMVP.View vista) {
        this.vista = vista;
        this.modelo = new RepositoryLogin();
        this.modelo.setPresentadorLogin(this, vista.getContext());
    }

    @Override
    public void IniciarSesionCorreo() {
        // Solicito a la vista la informacion del login del usuario y lo almaceno
        // en un objeto tipo LoginCredentialsUsuario
        Usuario credentialsUsuario =  vista.getLoginCredentialsUsuario();

        // Procedemos a hacer la validacion de datos

        // Validacion de que se escribio algo en los campos
        // Email

        if (credentialsUsuario.getEmail() == null || credentialsUsuario.getEmail().isEmpty()) {
            vista.showEmptyEmailError();
            return;
        }
        // Validación extra email -> Se verifica si tiene la estructura de un email
        // por ejemplo que tenga .com o @
        Boolean validacionEmail = Patterns.EMAIL_ADDRESS.matcher(credentialsUsuario.getEmail()).matches();
        if (!validacionEmail) {
            vista.showInvalidEmailError();
            return;
        }
        // Contraseña
        if (credentialsUsuario.getPassword() == null || credentialsUsuario.getPassword().isEmpty()) {
            vista.showEmptyPasswordError();
            return;
        }
        // Validacion extra contraseña una mayuscula? cuantos caracteres minimo?
        // un simbolo como minimo
        if (credentialsUsuario.getPassword().length() < 6 ) {
            vista.showLengthPasswordError();
            return;
        }

        // Verificamos en el modelo si usuario y contraseña existen y/o son validos
        modelo.validarConEmailYPasswordUsuario(credentialsUsuario.getEmail(), credentialsUsuario.getPassword());

        // Mostramos el ProgressBar
        vista.showProgressBar();
    }

    @Override
    public void OlvidarPassword() {
        // Vamos al activity RecuperarPassword
        vista.irAlActivityRecuperarPassword(RecuperarPassword.class);
    }

    @Override
    public void RegistrarNuevoUsuario() {
        // Vamos al activity RecuperarPassword
        vista.irAlActivityRegistro(Registro.class);
    }

    @Override
    public void IniciarSesionFacebook(Login login) {
        vista.showProgressBar();
        firebaseAuthWithFacebook(login);
    }



    @Override
    public void IniciarSesionGoogle() {
        modelo.performGoogleLogin();
        vista.showProgressBar();
        vista.signInGoogle();
    }

    @Override
    public void ValidarSaltoDeLogin() {
        if(modelo.validarSaltarLogin()){
            vista.irAlActivitySeleccionarActividad(SeleccionarActividad.class);
        }
    }

    @Override
    public void InicioSesionExitoso() {
        // Vamos al activity SeleccionarACtividad
        vista.irAlActivitySeleccionarActividad(SeleccionarActividad.class);
        // Escondemos el ProgressBar
        vista.hideProgressBar();
    }

    @Override
    public void InicioSesionFallido() {
        vista.showToastPasswordError();
        // Escondemos el ProgressBar
        vista.hideProgressBar();
    }

    @Override
    public GoogleSignInClient getGoogleSignInClientFromRepo() {
        return modelo.getGoogleSignInClient();
    }

    @Override
    public void runGoogleIntent(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = task.getResult(ApiException.class);
            //Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
            setFirebaseAuthWithGoogle(account.getIdToken());
            vista.hideProgressBar();
        } catch (ApiException e) {
            // Google Sign In failed, update UI appropriately
            //Log.w(TAG, "Google sign in failed", e);
            vista.hideProgressBar();
        }
    }

    @Override
    public void setFirebaseAuthWithGoogle(String idToken) {
        modelo.firebaseAuthWithGoogle(idToken);
    }

    @Override
    public void firebaseAuthWithGoogleFalla() {
        vista.showToastFirebaseAuthWithGoogleError();
        vista.hideProgressBar();
    }

    @Override
    public void SaveUsuarioInDBWithGoogleExitosa() {
        vista.hideProgressBar();
        vista.irAlActivitySeleccionarActividad(SeleccionarActividad.class);
    }

    @Override
    public void SaveUsuarioInDBWithGoogleFallo() {
        vista.showToastFirebaseAuthWithGoogleError();
        vista.hideProgressBar();
    }

    @Override
    public void firebaseAuthWithFacebook(Login login) {
        modelo.firebaseAuthWithFacebook(login);
    }

    @Override
    public void firebaseAuthWithFacebookFalla() {
        vista.showToastFirebaseAuthWithFacebookError();
        vista.hideProgressBar();
    }

    @Override
    public void SaveUsuarioInDBWithFacebookExitosa() {
        vista.hideProgressBar();
        vista.irAlActivitySeleccionarActividad(SeleccionarActividad.class);
    }

    @Override
    public void SaveUsuarioInDBWithFacebookFallo() {
        vista.showToastFirebaseAuthWithFacebookError();
        vista.hideProgressBar();
    }

    @Override
    public CallbackManager getCallbackManager() {
        return modelo.getCallbackManager();
    }



}
