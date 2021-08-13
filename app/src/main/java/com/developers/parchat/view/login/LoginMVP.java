package com.developers.parchat.view.login;

// Las interfaces me permiten tener un archivo para definir solo metodos

// vista hace referencia al archivo .java que esta linkeado a el .xml

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.developers.parchat.model.entity.Usuario;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseUser;


public interface LoginMVP {

    interface Model {
        //
        void setPresentadorLogin(Presenter presentador, Context context);
        // Validacion de las credenciales
        void validarConEmailYPasswordUsuario(String email, String password);
        // Verificamos si el usuario ya habia iniciado sesion previamente en la app
        boolean validarSaltarLogin();

        void performGoogleLogin();

        void firebaseAuthWithGoogle(String idToken);
        void firebaseAuthWithGoogleExito(FirebaseUser usuarioActual);

        void guardarUsuarioNuevoWithGoogleEnBaseDatos(Usuario usuarioNuevoGoogle);

        GoogleSignInClient getGoogleSignInClient();

        void firebaseAuthWithFacebook(Login login);
        void firebaseAuthWithFacebookExito(AccessToken token);
        void getInfoUsuarioNuevoAGuardarWithFacebookEnBaseDatos(FirebaseUser usuarioActual);

        void guardarUsuarioNuevoWithFacebookEnBaseDatos(Usuario usuarioNuevoFacebook);

        CallbackManager getCallbackManager();

        void verificarDatosUsuarioLogueadoFromDBGoogle(FirebaseUser firebaseUser);
        void verificarDatosUsuarioLogueadoFromDBFacebook(FirebaseUser firebaseUser);
    }

    // EL presentador recibe los eventos que ocurriran en la vista
    interface Presenter {
        // Que pasa si se presiona el boton Iniciar Sesion
        void IniciarSesionCorreo();
        // Que pasa si se presiona el texto ¿Olvidaste la contraseña?
        void OlvidarPassword();
        // Que pasa si se presiona el texto Registrate
        void RegistrarNuevoUsuario();
        // Que pasa si se presiona el boton de facebook
        void IniciarSesionFacebook(Login login);
        //void IniciarSesionFacebook();
        // Que pasa si se presiona el boton de google
        void IniciarSesionGoogle();
        // Validamos si alguien ya inicio sesion
        void ValidarSaltoDeLogin();

        void InicioSesionExitoso();

        void InicioSesionFallido();

        GoogleSignInClient getGoogleSignInClientFromRepo();
        void runGoogleIntent(Intent data);

        void setFirebaseAuthWithGoogle(String idToken);
        void firebaseAuthWithGoogleFalla();

        void SaveUsuarioInDBWithGoogleExitosa();
        void SaveUsuarioInDBWithGoogleFallo();

        void firebaseAuthWithFacebook(Login login);
        void firebaseAuthWithFacebookFalla();

        void SaveUsuarioInDBWithFacebookExitosa();
        void SaveUsuarioInDBWithFacebookFallo();

        CallbackManager getCallbackManager();



    }

    // Se obtienen datos e informacion a la vista
    interface View {
        // Primero obtengo las credenciales (correo, password)
        Usuario getLoginCredentialsUsuario();

        void signInGoogle();

        // Mostramos errores de validación, si aplica
        // TextInputEditText de email vacio
        void showEmptyEmailError();
        // TextInputEditText de email no tiene estructura de correo (ni @ || .)
        void showInvalidEmailError();
        // TextInputEditText de password vacio
        void showEmptyPasswordError();
        // TextInputEditText de password minimo 6 caracteres
        void showLengthPasswordError();
        ////
        void showProgressBar();
        void hideProgressBar();
        ////
        // Para hacer el intent e ir a el Activity Registro
        void irAlActivityRegistro(Class<? extends AppCompatActivity> ir_a_Registro);
        // Para hacer el intent e ir a el Activity RecuperarPassword
        void irAlActivityRecuperarPassword(Class<? extends AppCompatActivity> ir_a_RecuperarPassword);
        // Para hacer el intent e ir a el Activity SeleccionarActividad
        void irAlActivitySeleccionarActividad(Class<? extends AppCompatActivity> ir_a_SeleccionarActividad);
        // Mostrar un mensaje emergente que diga que el correo ingresado no esta registrado
        void showToastEmailNotFound();
        // Mostrar un mensaje emergente que diga que la contraseña es incorrecta
        void showToastPasswordError();

        void showToastFirebaseAuthWithGoogleError();

        void showToastFirebaseAuthWithFacebookError();

        Context getContext();
    }
}
