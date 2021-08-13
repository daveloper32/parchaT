package com.developers.parchat.view.main.bt_st_dlg_ifo_lugar;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.developers.parchat.view.info_extra_sitio.InformacionExtraSitio;
import com.developers.parchat.R;
import com.developers.parchat.model.entity.InfoLugar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class BottomSheetDialog_InfoLugar extends BottomSheetDialogFragment {

    private TextView tV_bsd_main_activity_nombreLugar, tV_bsd_main_activity_direccion, tV_bsd_main_activity_moreInfo;
    private String nombreLugar, direccion, sitioWeb, urlImagen;
    private ImageView imgV_main_activity_lugar;
    private InfoLugar infoLugar;

    public BottomSheetDialog_InfoLugar(InfoLugar infoLugar) {
        this.infoLugar = infoLugar;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new BottomSheetDialog(getContext(), R.style.TransparentBottomSheetDialog);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.bottom_sheet_dialog_info_lugar,
                null);
        tV_bsd_main_activity_nombreLugar = view.findViewById(R.id.tV_bsd_main_activity_nomLugar);
        tV_bsd_main_activity_direccion = view.findViewById(R.id.tV_bsd_main_activity_direccion);
        imgV_main_activity_lugar = view.findViewById(R.id.imgV_main_activity_lugar);
        tV_bsd_main_activity_moreInfo = view.findViewById(R.id.tV_bsd_main_activity_moreInfo);
        if (infoLugar != null) {
            nombreLugar = infoLugar.getNombre();
            direccion = infoLugar.getDireccion();
            sitioWeb = infoLugar.getSitioweb();
            urlImagen = infoLugar.getUrlimagen();
        } else {
            nombreLugar = "No disponible";
            direccion = "No disponible en este momento";
            sitioWeb = "";
            urlImagen = "";
        }

        tV_bsd_main_activity_nombreLugar.setText(nombreLugar);
        tV_bsd_main_activity_direccion.setText(direccion);
        Picasso.get()
                .load(urlImagen)
                .error(R.drawable.ic_actividades_comida)
                .into(imgV_main_activity_lugar);

        tV_bsd_main_activity_moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos un objeto de la clase Intent para que al presionar el boton vayamos al Activity SeleccionarActividad
                Intent deMainActivityAInformacionExtraSitio = new Intent(getActivity(), InformacionExtraSitio.class);
                deMainActivityAInformacionExtraSitio.putExtra("infolugar", infoLugar);
                // Iniciamos el Activity SeleccionarActividad
                startActivity(deMainActivityAInformacionExtraSitio);
            }
        });

        return view;

    }
}
