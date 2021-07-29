package com.developers.parchat.view.login;

// Va a llevar toda la logica de la vista Login

import android.util.Patterns;

import com.developers.parchat.model.repository.RepositoryLoginBuscarUsuario;
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
        this.modelo = new RepositoryLoginBuscarUsuario();
        this.modelo.setPresentadorLogin(this, vista.getContext());



    }

    @Override
    public void IniciarSesionCorreo() {
        // Solicito a la vista la informacion del login del usuario y lo almaceno
        // en un objeto tipo LoginCredentialsUsuario
        LoginCredentialsUsuario credentialsUsuario =  vista.getLoginCredentialsUsuario();

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

        // Verificamos en el modelo si usuario y contraseña existen y/o son validos


        // Primero verificamos que el correo este registrado
        if (modelo.validarEmailUsuario(credentialsUsuario.getEmail())) {
        // Si esta registrado validamos la contraseña
            if (modelo.validarPasswordUsuario(credentialsUsuario.getEmail(),
                    credentialsUsuario.getPassword())){
                // Guardardamos el inicio de sesion
                modelo.guardarSaltarLogin(credentialsUsuario.getEmail());
                // SI la contraseña esta bien, significa que tanto correo como contraseña estan correctas
                // por tanto vamos al Activity Seleccionar Actividad
                vista.irAlActivitySeleccionarActividad(SeleccionarActividad.class);
                // SI la contraseña no fue ingresada correctamente desplegamos un mensaje emergente
                // diciendo que la contraseña es incorrecta
            } else {
                vista.showToastPasswordError();
            }
        // SI no esta registrado desplegamos un mensaje emergente diciendo que el correo ingresado no esta registrado
        } else {
            vista.showToastEmailNotFound();
        }
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
    public boolean ValidarSaltoDeLogin() {
        return modelo.validarSaltarLogin();
    }
}
