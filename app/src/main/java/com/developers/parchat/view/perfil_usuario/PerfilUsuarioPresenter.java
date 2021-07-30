package com.developers.parchat.view.perfil_usuario;


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
    public void CargarDatosUsuario() {
        modelo.getDatosUsuarioLogueadoFromDB();
    }

    @Override
    public void GuardarDatos() {
        // Primero verifico que se haya activado el boton para editar
        if (vista.isEdicionActivada()){
            // Solicito a la vista la informacion de los cambios del usuario y lo almaceno
            // en un objeto tipo Usuario
            PerfilUsuarioDatos usuarioNuevosDatos = vista.getNuevosDatosUsuario();
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
            modelo.editarDatosUsuario(usuarioNuevosDatos);

        } else {
            vista.showToastErrorActivarEdicionDatos();
        }


    }

    @Override
    public void CambiarFoto() {

    }

    @Override
    public void obtenerDatosUsuarioLogeadoConExito() {
        // Le solicitamos al presentador que nos entregue los datos de usuario
        PerfilUsuarioDatos usuarioActivo = modelo.getDatosPerfilUsuario();
        vista.CargamosDatosUsuario(usuarioActivo);
    }

    @Override
    public void obtenerDatosUsuarioLogeadoConFalla() {
        vista.showToastErrorCargarDatos();
    }

    @Override
    public void actualizarDatosUsuarioLogeadoConFalla() {
        vista.showToastErrorDatosGuardados();

    }

    @Override
    public void actualizarDatosUsuarioLogeadoConExito() {
        vista.showToastDatosGuardadosConExito();
        vista.irAlActivityMain(MainActivity.class);
    }
}
