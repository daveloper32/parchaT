package com.developers.parchat.view.registro;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.developers.parchat.model.entity.Usuario;
import com.developers.parchat.view.login.Login;
import com.developers.parchat.view.login.LoginMVP;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseUser;

public interface RegistroMVP {

    interface Model {
        // enviamos el presentador y el contexto de la vista
        void setPresentadorRegistro(RegistroMVP.Presenter presentador, Context context);
        // Autenticar y Guardar Usuario
        void autenticarUsuarioNuevo(Usuario usuario);
        void guardarUsuarioNuevoEnBaseDatos(Usuario usuario);

        void performGoogleLogin();

        void firebaseAuthWithGoogle(String idToken);
        void firebaseAuthWithGoogleExito(FirebaseUser usuarioActual);

        void guardarUsuarioNuevoWithGoogleEnBaseDatos(Usuario usuarioNuevoGoogle);

        GoogleSignInClient getGoogleSignInClient();

        void firebaseAuthWithFacebook(Registro registro);
        void firebaseAuthWithFacebookExito(AccessToken token);
        void getInfoUsuarioNuevoAGuardarWithFacebookEnBaseDatos(FirebaseUser usuarioActual);

        void guardarUsuarioNuevoWithFacebookEnBaseDatos(Usuario usuarioNuevoFacebook);

        CallbackManager getCallbackManager();

        void verificarDatosUsuarioLogueadoFromDBGoogle(FirebaseUser firebaseUser);
        void verificarDatosUsuarioLogueadoFromDBFacebook(FirebaseUser firebaseUser);

    }
    // EL presentador recibe los eventos que ocurriran en la vista
    interface Presenter {
        // Que pasa si se presiona el boton Registrarse
        void Registrarse();
        // Que pasa si se presiona el texto Inicia Sesión
        void InicioSesion();
        // Que pasa si se presiona el boton de facebook
        void Log_Facebook(Registro registro);
        // Que pasa si se presiona el boton de google
        void Log_Google();
        ///
        void AuthUsuarioExitosa();
        void AuthUsuarioFallo();
        ///
        void SaveUsuarioInDBExitosa();
        void SaveUsuarioInDBFallo();

        GoogleSignInClient getGoogleSignInClientFromRepo();
        void runGoogleIntent(Intent data);

        void setFirebaseAuthWithGoogle(String idToken);
        void firebaseAuthWithGoogleFalla();

        void SaveUsuarioInDBWithGoogleExitosa();
        void SaveUsuarioInDBWithGoogleFallo();

        void firebaseAuthWithFacebook(Registro registro);
        void firebaseAuthWithFacebookFalla();

        void SaveUsuarioInDBWithFacebookExitosa();
        void SaveUsuarioInDBWithFacebookFallo();

        CallbackManager getCallbackManager();
    }
    // Se obtienen datos e informacion a la vista
    interface View {
        // Primero obtengo los datos del usuario a registrar (nombre completo, correo, password)
        Usuario getRegistroDatosUsuario();
        // Mostramos errores de validación, si aplica
        // TextInputEditText de nombre completo vacio
        void showEmptyNombreCompletoError();
        // TextInputEditText de email vacio
        void showEmptyEmailError();
        // TextInputEditText de email no tiene estructura de correo (ni @ || .)
        void showInvalidEmailError();
        // TextInputEditText de password vacio
        void showEmptyPasswordError();
        // TextInputEditText de password ayor a 6 caracteres
        void showLengthPasswordError();
        void showInvalidPasswordError();
        //
        void showProgressBar();
        void hideProgressBar();
        //
        // Para hacer el intent e ir a el Activity Login
        void irAlActivityLogin(Class<? extends AppCompatActivity> ir_a_Login);
        // Mostrar un mensaje emergente que confirme el registro
        void showToastRegistroConfirm();
        // Mostrar un mensaje emergente que diga que el correo ingresado ya se encuentra registrado
        void showToastCorreoYaRegistrado();
        // Mostrar un mensaje emergente que diga algo salio mal, intenta registrarte de nuevo
        void showToastErrorRegistrarUsuarioNuevo();
        void showToasEnDesarrollo();
        void showToastFirebaseAuthWithGoogleError();

        void showToastFirebaseAuthWithFacebookError();
        // Obtenemos el contexto de la vista para poder acceder a los SharedPreferences
        Context getContext();


        void signInGoogle();
    }
}
