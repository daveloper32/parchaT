package com.developers.parchat.view.login;

// Las interfaces me permiten tener un archivo para definir solo metodos

// vista hace referencia al archivo .java que esta linkeado a el .xml

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;



public interface LoginMVP {

    interface Model {
        // Validacion del email
        boolean validarEmailUsuario(String email);
        // Validacion de la contraseña
        boolean validarPasswordUsuario(String email, String password);
        // enviamos el presentador y el contexto de la vista
        void setPresentadorLogin(Presenter presentador, Context context);
        // Guardamos el estado de si se salta o no el login
        void guardarSaltarLogin(String email);
        // Validamos si se inicio sesion previamente para pasar al login
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

        //
        boolean ValidarSaltoDeLogin();
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
