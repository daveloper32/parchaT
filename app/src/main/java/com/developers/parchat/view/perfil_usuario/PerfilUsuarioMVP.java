package com.developers.parchat.view.perfil_usuario;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.developers.parchat.model.entity.Usuario;

public interface PerfilUsuarioMVP {

    interface Model {
        // Enviamos el presentador y el contexto de la vista
        void setPresentadorPerfilUsuario(PerfilUsuarioMVP.Presenter presentador, Context context);
        //
        String getEmailSaltarInicioSesion();
        //
        Usuario getDatosPerfilUsuario(String emailUsuario);
        //
        boolean editarDatosUsuario(Usuario usuario_a_editar);

    }
    // EL presentador recibe los eventos que ocurriran en la vista
    interface Presenter {

        Usuario BuscarDatosUsuario();



        void GuardarDatos();

        void CambiarFoto();


    }
    // Se obtienen datos e informacion a la vista
    interface View {

        // Se obtinen los cambios en los datos del usuario
        Usuario getNuevosDatosUsuario();

        boolean isEdicionActivada();

        void CargamosDatosUsuario();

        void EditarDatos();

        // Mostramos errores de validación, si aplica
        // TextInputEditText de nombre completo vacio
        void showEmptyNombreCompletoError();
        // TextInputEditText de numero vacio
        void showEmptyNumeroError();

        boolean ComprobarEdit();

        void MensajeEmergente(Context context);
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
