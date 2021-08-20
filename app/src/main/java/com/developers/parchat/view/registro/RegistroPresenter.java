package com.developers.parchat.view.registro;

import android.content.Intent;
import android.util.Patterns;

import com.developers.parchat.model.entity.Usuario;
import com.developers.parchat.model.repository.RepositoryRegistro;
import com.developers.parchat.view.login.Login;
import com.developers.parchat.view.seleccionar_actividad.SeleccionarActividad;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class RegistroPresenter implements RegistroMVP.Presenter {

    // Variables modelo MVP
    private final RegistroMVP.View vista;
    private final RegistroMVP.Model modelo;

    public RegistroPresenter(RegistroMVP.View vista) {
        this.vista = vista;
        this.modelo = new RepositoryRegistro();
        this.modelo.setPresentadorRegistro(this, vista.getContext());
    }

    @Override
    public void Registrarse() {
        // Solicito a la vista la informacion del registro del usuario y lo almaceno
        // en un objeto tipo RegistroDatosUsuario
        Usuario datosUsuario =  vista.getRegistroDatosUsuario();

        // Procedemos a hacer la validacion de datos

        // Validacion de que se escribio algo en los campos
        // Nombre Completo
        if (datosUsuario.getNombreCompleto() == null || datosUsuario.getNombreCompleto().isEmpty()) {
            vista.showEmptyNombreCompletoError();
            return;
        }
        // Email

        if (datosUsuario.getEmail() == null || datosUsuario.getEmail().isEmpty()) {
            vista.showEmptyEmailError();
            return;
        }
        // Validación extra email -> Se verifica si tiene la estructura de un email
        // por ejemplo que tenga .com o @
        Boolean validacionEmail = Patterns.EMAIL_ADDRESS.matcher(datosUsuario.getEmail()).matches();
        if (!validacionEmail) {
            vista.showInvalidEmailError();
            return;
        }
        // Contraseña
        if (datosUsuario.getPassword() == null || datosUsuario.getPassword().isEmpty()) {
            vista.showEmptyPasswordError();
            return;
        }
        if (datosUsuario.getPassword().length() < 10 ) {
            vista.showLengthPasswordError();
            return;
        }
        // Validacion extra contraseña una mayuscula? cuantos caracteres minimo?
        // un simbolo como minimo
        // debe tener numeros
        if (!datosUsuario.getPassword().matches(".*\\d.*")) {
            vista.showInvalidPasswordError();
            return;
        }
        // Debe tener letras minusculas
        if (!datosUsuario.getPassword().matches(".*[a-z].*")) {
            vista.showInvalidPasswordError();
            return;
        }

        // Creamos un objeto de la clase Usuario y guardamos nombre, email y contraseña
        Usuario usuarioAGuardar = new Usuario(datosUsuario.getNombreCompleto(),
                datosUsuario.getEmail(), datosUsuario.getPassword(), "", "");

        // SI el objeto usuarioAGuardar no esta vacio
        if (usuarioAGuardar != null) {
            // Llamamos al modelo para guardar los datos del usuario
            // Guardamos el usuario
            modelo.autenticarUsuarioNuevo(usuarioAGuardar);
            // Mostramos ProgressBar
            vista.showProgressBar();
        }
    }

    @Override
    public void InicioSesion() {
        // Vamos al activity Login
        vista.irAlActivityLogin(Login.class);
    }

    @Override
    public void Log_Facebook(Registro registro) {
        vista.showProgressBar();
        firebaseAuthWithFacebook(registro);

    }

    @Override
    public void Log_Google() {
        modelo.performGoogleLogin();
        vista.showProgressBar();
        vista.signInGoogle();

    }

    @Override
    public void AuthUsuarioExitosa() {
        // Obtenemos datos
        Usuario datosUsuario =  vista.getRegistroDatosUsuario();
        // COnvertimos a Usuario
        Usuario usuario = new Usuario(datosUsuario.getNombreCompleto(),
                datosUsuario.getEmail(), datosUsuario.getPassword(), "");
        // Guardarmos en RealTimeDatabase
        modelo.guardarUsuarioNuevoEnBaseDatos(usuario);
    }

    @Override
    public void AuthUsuarioFallo() {
        vista.showToastErrorRegistrarUsuarioNuevo();
        // Ocultamos ProgressBar
        vista.hideProgressBar();
    }

    @Override
    public void SaveUsuarioInDBExitosa() {
        // Si todo sale bien vamos al Activity Seleccionar Actividad
        vista.irAlActivityLogin(Login.class);
        // Ocultamos ProgressBar
        vista.hideProgressBar();
        // Mostramos un mensaje confirmando el registro
        vista.showToastRegistroConfirm();
    }

    @Override
    public void SaveUsuarioInDBFallo() {
        vista.showToastErrorRegistrarUsuarioNuevo();
        // Ocultamos ProgressBar
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
        vista.irAlActivityLogin(Login.class);
    }

    @Override
    public void SaveUsuarioInDBWithGoogleFallo() {
        vista.showToastFirebaseAuthWithGoogleError();
        vista.hideProgressBar();
    }

    @Override
    public void firebaseAuthWithFacebook(Registro registro) {
        modelo.firebaseAuthWithFacebook(registro);
    }

    @Override
    public void firebaseAuthWithFacebookFalla() {
        vista.showToastFirebaseAuthWithFacebookError();
        vista.hideProgressBar();
    }

    @Override
    public void SaveUsuarioInDBWithFacebookExitosa() {
        vista.hideProgressBar();
        vista.irAlActivityLogin(Login.class);
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
