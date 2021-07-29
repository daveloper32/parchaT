package com.developers.parchat.view.registro;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.developers.parchat.model.entity.Usuario;
import com.developers.parchat.view.login.LoginMVP;

public interface RegistroMVP {

    interface Model {
        // enviamos el presentador y el contexto de la vista
        void setPresentadorRegistro(RegistroMVP.Presenter presentador, Context context);
        // Verificar que correo no este registrado
        boolean buscarCorreo(String email);
        // Guardar Usuario
        void guardarUsuarioNuevo(Usuario usuario);
    }
    // EL presentador recibe los eventos que ocurriran en la vista
    interface Presenter {
        // Que pasa si se presiona el boton Registrarse
        void Registrarse();
        // Que pasa si se presiona el texto Inicia Sesión
        void InicioSesion();
        // Que pasa si se presiona el boton de facebook
        void Log_Facebook();
        // Que pasa si se presiona el boton de google
        void Log_Google();
    }
    // Se obtienen datos e informacion a la vista
    interface View {
        // Primero obtengo los datos del usuario a registrar (nombre completo, correo, password)
        RegistroDatosUsuario getRegistroDatosUsuario();
        // Mostramos errores de validación, si aplica
        // TextInputEditText de nombre completo vacio
        void showEmptyNombreCompletoError();
        // TextInputEditText de email vacio
        void showEmptyEmailError();
        // TextInputEditText de email no tiene estructura de correo (ni @ || .)
        void showInvalidEmailError();
        // TextInputEditText de password vacio
        void showEmptyPasswordError();
        // Para hacer el intent e ir a el Activity Login
        void irAlActivityLogin(Class<? extends AppCompatActivity> ir_a_Login);
        // Mostrar un mensaje emergente que confirme el registro
        void showToastRegistroConfirm();
        // Mostrar un mensaje emergente que diga que el correo ingresado ya se encuentra registrado
        void showToastCorreoYaRegistrado();
        // Mostrar un mensaje emergente que diga algo salio mal, intenta registrarte de nuevo
        void showToastErrorRegistrarUsuarioNuevo();
        // Obtenemos el contexto de la vista para poder acceder a los SharedPreferences
        Context getContext();


    }
}
