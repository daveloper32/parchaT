package com.developers.parchat.view.login;

// Las interfaces me permiten tener un archivo para definir solo metodos

// vista hace referencia al archivo .java que esta linkeado a el .xml

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;



public interface LoginMVP {

    interface Model {
        //
        void setPresentadorLogin(Presenter presentador, Context context);
        // Validacion de las credenciales
        void validarConEmailYPasswordUsuario(String email, String password);
        // Verificamos si el usuario ya habia iniciado sesion previamente en la app
        boolean validarSaltarLogin();
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
        void IniciarSesionFacebook();
        // Que pasa si se presiona el boton de google
        void IniciarSesionGoogle();
        // Validamos si alguien ya inicio sesion
        void ValidarSaltoDeLogin();

        void InicioSesionExitoso();

        void InicioSesionFallido();
    }

    // Se obtienen datos e informacion a la vista
    interface View {
        // Primero obtengo las credenciales (correo, password)
        LoginCredentialsUsuario getLoginCredentialsUsuario();

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

        Context getContext();
    }
}
