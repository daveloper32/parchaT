package com.developers.parchat.view.main;

// Las interfaces me permiten tener un archivo para definir solo metodos

import android.content.Context;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import com.developers.parchat.model.entity.InfoLugar;
import com.developers.parchat.model.entity.Usuario;
import com.google.firebase.database.DatabaseError;

public interface MainActivityMVP {

    interface Model {

        // enviamos el presentador y el contexto de la vista
        void setPresentadorMain(Presenter presentadorMain, Context contextoMain);
        // Para obtener los datos del correo que inicio sesion
        void getDatosUsuarioLogueadoFromDB();
        Usuario getUsuarioLogueado();
        // Cerrar sesion
        void cerrarSesionFirebase();
        // Crear COnfiguracion Inicial si no se tiene
        void crearConfiguracionesIniciales();


        void performGoogleLogin();


    }
    // EL presentador recibe los eventos que ocurriran en la vista
    interface Presenter {
        // Que pasa si se presiona en Ver Perfil
        void VerPerfilUsuario();
        // Que pasa si se presiona en Cambiar Busqueda
        void CambiarBusqueda();
        // Que pasa si se presiona en Configuracion
        void VerConfiguraciones();
        // Que pasa si se presiona en Cerrar Sesion
        void CerrarSesion();

        void obtenerDatosUsuarioLogeadoConExito();

        void obtenerDatosUsuarioLogeadoConFalla();

        void cargarDatosEnHeader();

        void configuracionesIniciales();

    }
    // Se obtienen datos e informacion a la vista
    interface View {


        void setDatosEnHeader(Usuario datosUsuario);

        // Para hacer el intent e ir a el Activity PerfilUsuario
        void irAlActivityPerfilUsuario(Class<? extends AppCompatActivity> ir_a_PerfilUsuario);
        // Para hacer el intent e ir a el Activity SeleccionarActividad
        void irAlActivitySeleccionarActividad(Class<? extends AppCompatActivity> ir_a_SeleccionarActividad);
        // Para hacer el intent e ir a el Activity Login
        void irAlActivityLogin(Class<? extends AppCompatActivity> ir_a_Login);
        // Para hacer el intent e ir a el Activity Configuraciones
        void irAlActivityConfiguraciones(Class<? extends AppCompatActivity> ir_a_Configuraciones);
        // Obtenemos el contexto de la vista para poder acceder a los SharedPreferences
        Context getContext();

        void cargarImagenUsuario(Uri linkFotoUsuario);
    }
}
