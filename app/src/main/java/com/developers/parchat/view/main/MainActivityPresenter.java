package com.developers.parchat.view.main;

import com.developers.parchat.model.entity.Usuario;
import com.developers.parchat.model.repository.RepositoryMainActivity;
import com.developers.parchat.view.login.Login;
import com.developers.parchat.view.perfil_usuario.PerfilUsuario;
import com.developers.parchat.view.seleccionar_actividad.SeleccionarActividad;

public class MainActivityPresenter implements MainActivityMVP.Presenter {

    // Variables modelo MVP
    private final MainActivityMVP.View vista;
    private final  MainActivityMVP.Model modelo;

    public MainActivityPresenter(MainActivityMVP.View vista) {
        this.vista = vista;
        this.modelo = new RepositoryMainActivity();
        this.modelo.setPresentadorMain(this, vista.getContext());
    }

    @Override
    public void VerPerfilUsuario() {
        vista.irAlActivityPerfilUsuario(PerfilUsuario.class);
    }

    @Override
    public void CambiarBusqueda() {
        vista.irAlActivitySeleccionarActividad(SeleccionarActividad.class);
    }

    @Override
    public void VerConfiguraciones() {

    }

    @Override
    public void CerrarSesion() {
        modelo.cerrarSesionFirebase();
        vista.irAlActivityLogin(Login.class);
    }

    @Override
    public void obtenerDatosUsuarioLogeadoConExito() {
        Usuario usuarioActual = modelo.getUsuarioLogueado();
        vista.setDatosEnHeader(usuarioActual);
    }

    @Override
    public void obtenerDatosUsuarioLogeadoConFalla() {

    }

    @Override
    public void cargarDatosEnHeader() {
        modelo.getDatosUsuarioLogueadoFromDB();
    }

}
