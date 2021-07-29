package com.developers.parchat.view.perfil_usuario;


import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.developers.parchat.model.entity.Usuario;
import com.developers.parchat.model.repository.RepositoryPerfilUsuario;
import com.developers.parchat.view.main.MainActivity;

public class PerfilUsuarioPresenter implements PerfilUsuarioMVP.Presenter {

    // Variables modelo MVP
    private final PerfilUsuarioMVP.View vista;
    private final PerfilUsuarioMVP.Model modelo;

    public PerfilUsuarioPresenter(PerfilUsuarioMVP.View vista) {
        this.vista = vista;
        this.modelo = new RepositoryPerfilUsuario();
        this.modelo.setPresentadorPerfilUsuario(this, vista.getContext());
    }

    @Override
    public Usuario BuscarDatosUsuario() {
        // Solicitamos al modelo el correo del usuario que inicio sesion
        String emailUsuarioActivo = modelo.getEmailSaltarInicioSesion();
        // Validamos que no nos devuelva un String vacio
        if (!emailUsuarioActivo.isEmpty()) {
            // Solicitamos al modelo los datos del usuario con el email
            Usuario usuarioActivo = modelo.getDatosPerfilUsuario(emailUsuarioActivo);
            return usuarioActivo;
        }
        else {
            return null;
        }
    }



    @Override
    public void GuardarDatos() {
        // Primero verifico que se haya activado el boton para editar
        if (vista.isEdicionActivada()){
            // Solicito a la vista la informacion de los cambios del usuario y lo almaceno
            // en un objeto tipo Usuario
            Usuario usuarioNuevosDatos = vista.getNuevosDatosUsuario();
            // Validar Campos
            // Validacion de que se escribio algo en los campos
            // Nombre Completo
            if (usuarioNuevosDatos.getNombreCompleto() == null || usuarioNuevosDatos.getNombreCompleto().isEmpty()) {
                vista.showEmptyNombreCompletoError();
                return;
            }
            // Numero
            if (usuarioNuevosDatos.getNumeroCel() == null || usuarioNuevosDatos.getNumeroCel().isEmpty()) {
                vista.showEmptyNumeroError();
                return;
            }

            // Guardo y confirmo si se guardo
            boolean confirmoSaveDatos = modelo.editarDatosUsuario(usuarioNuevosDatos);
            if (confirmoSaveDatos) {
                vista.showToastDatosGuardadosConExito();
                vista.irAlActivityMain(MainActivity.class);
            } else {
                vista.showToastErrorDatosGuardados();
            }
        } else {
            vista.showToastErrorActivarEdicionDatos();
        }


    }

    @Override
    public void CambiarFoto() {

    }
}
