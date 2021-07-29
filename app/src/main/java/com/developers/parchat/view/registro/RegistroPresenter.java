package com.developers.parchat.view.registro;

import android.util.Patterns;

import com.developers.parchat.model.entity.Usuario;
import com.developers.parchat.model.repository.RepositoryRegistroGuardarUsuario;
import com.developers.parchat.view.login.Login;

public class RegistroPresenter implements RegistroMVP.Presenter {

    // Variables modelo MVP
    private final RegistroMVP.View vista;
    private final RegistroMVP.Model modelo;

    public RegistroPresenter(RegistroMVP.View vista) {
        this.vista = vista;
        this.modelo = new RepositoryRegistroGuardarUsuario();
        this.modelo.setPresentadorRegistro(this, vista.getContext());
    }

    @Override
    public void Registrarse() {
        // Solicito a la vista la informacion del registro del usuario y lo almaceno
        // en un objeto tipo RegistroDatosUsuario
        RegistroDatosUsuario datosUsuario =  vista.getRegistroDatosUsuario();

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
        // Validacion extra contraseña una mayuscula? cuantos caracteres minimo?
        // un simbolo como minimo

        // Creamos un objeto de la clase Usuario y guardamos nombre, email y contraseña
        Usuario usuarioAGuardar = new Usuario(datosUsuario.getNombreCompleto(),
                datosUsuario.getEmail(), datosUsuario.getPassword(), "");

        // SI el objeto usuarioAGuardar no esta vacio
        if (usuarioAGuardar != null) {
            // Llamamos al modelo para guardar los datos del usuario
            // Verificamos que el correo no este registrado
            if (!modelo.buscarCorreo(datosUsuario.getEmail())) {
                // Guardamos el usuario
                modelo.guardarUsuarioNuevo(usuarioAGuardar);
                // Si todo sale bien vamos al Activity Seleccionar Actividad
                vista.irAlActivityLogin(Login.class);
                // Mostramos un mensaje confirmando el registro
                vista.showToastRegistroConfirm();
            // Si esta registrado mostramos mensaje informando
            } else {
                // Mostrar un mensaje emergente que diga que el correo ingresado ya se encuentra registrado
                vista.showToastCorreoYaRegistrado();
            }
        // SI esta vacio mandamos mensaje de error
        } else {
            // Mostrar un mensaje emergente de error
            vista.showToastErrorRegistrarUsuarioNuevo();
        }
    }

    @Override
    public void InicioSesion() {
        // Vamos al activity Login
        vista.irAlActivityLogin(Login.class);
    }

    @Override
    public void Log_Facebook() {

    }

    @Override
    public void Log_Google() {

    }
}
