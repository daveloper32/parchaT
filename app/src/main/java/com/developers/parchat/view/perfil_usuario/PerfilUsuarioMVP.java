package com.developers.parchat.view.perfil_usuario;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

public interface PerfilUsuarioMVP {

    interface Model {
        // Enviamos el presentador y el contexto de la vista
        void setPresentadorPerfilUsuario(PerfilUsuarioMVP.Presenter presentador, Context context);
        // Par obtener el email que tiene la sesion inciada
        void getDatosUsuarioLogueadoFromDB();
        // Para obtener los datos del correo que inicio sesion
        PerfilUsuarioDatos getDatosPerfilUsuario();
        // Para editar datos del usuario que tiene sesion inciada
        void editarDatosUsuario(PerfilUsuarioDatos usuario_a_editar);
        void actualizarDatosUsuarioLogeadoConExito(Map<String,Object> childUpdates);        
        
    }
    // EL presentador recibe los eventos que ocurriran en la vista
    interface Presenter {
        // Buscar el usuario para cargar Datos
        void CargarDatosUsuario();
        // Que pasa si se presiona el boton GuardarDatos
        void GuardarDatos();
        // Que pasa si se presiona el text Cambiar foto
        void CambiarFoto();

        void obtenerDatosUsuarioLogeadoConExito();

        void obtenerDatosUsuarioLogeadoConFalla();

        void actualizarDatosUsuarioLogeadoConFalla();

        void actualizarDatosUsuarioLogeadoConExito();
    }
    // Se obtienen datos e informacion a la vista
    interface View {
        // Se obtinen los cambios en los datos del usuario
        PerfilUsuarioDatos getNuevosDatosUsuario();
        // Para verificar si se presiono el boton edicion o lapiz
        boolean isEdicionActivada();
        // Para cargar los datos de usuario que inicio sesion en los edittext
        void CargamosDatosUsuario(PerfilUsuarioDatos usuarioActivo);
        // Que pasa si se presiona el boton Lapiz o editar
        void EditarDatos();
        // Mostramos errores de validación, si aplica
        // TextInputEditText de nombre completo vacio
        void showEmptyNombreCompletoError();
        // TextInputEditText de numero vacio
        void showEmptyNumeroError();
        // Mostrar mensaje de advertencia con dos opciones cancelar o si -> para confirmaciond e no guardar datos
        void MensajeEmergente(Context context);
        // Mostrar un mensaje emergente Le decimos al usuario que sus datos no han sido guardados con exito
        void showToastErrorCargarDatos();
        // Mostrar un mensaje emergente Le decimos al usuario que sus datos han sido guardados con exito
        void showToastDatosGuardadosConExito();
        // Mostrar un mensaje emergente Le decimos al usuario que sus datos no han sido guardados con exito
        void showToastErrorDatosGuardados();
        // Mostrar un mensaje emergente que diga Debes presionar el lapiz para editar y guardar tus datos
        void showToastErrorActivarEdicionDatos();
        // Para hacer el intent e ir a el Activity ActivityMain
        void irAlActivityMain(Class<? extends AppCompatActivity> ir_a_ActivityMain);
        // Obtenemos el contexto de la vista para poder acceder a los SharedPreferences
        Context getContext();
    }
}
