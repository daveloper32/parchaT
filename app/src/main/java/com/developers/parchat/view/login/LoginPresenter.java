package com.developers.parchat.view.login;

// Va a llevar toda la logica de la vista Login

import android.util.Patterns;

import com.developers.parchat.model.entity.Usuario;
import com.developers.parchat.model.repository.RepositoryLogin;
import com.developers.parchat.view.recuperar_password.RecuperarPassword;
import com.developers.parchat.view.registro.Registro;
import com.developers.parchat.view.seleccionar_actividad.SeleccionarActividad;


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
    public void IniciarSesionFacebook() {

    }

    @Override
    public void IniciarSesionGoogle() {


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
    }


}
