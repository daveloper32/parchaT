package com.developers.parchat.view.main.bt_st_dlg_ifo_lugar;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.developers.parchat.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class BottomSheetDialog_InfoLugar extends BottomSheetDialogFragment {

    private TextView tV_bsd_main_activity_nombreLugar, tV_bsd_main_activity_direccion;
    private String nombreLugar, direccion, sitioWeb, urlImagen;
    private ImageView imgV_main_activity_lugar;

    public BottomSheetDialog_InfoLugar(String nombreLugar, String direccion, String sitioWeb, String urlImagen) {
        this.nombreLugar = nombreLugar;
        this.direccion = direccion;
        this.sitioWeb = sitioWeb;
        this.urlImagen = urlImagen;
    }



    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.bottom_sheet_dialog_info_lugar,
                null, false);
        tV_bsd_main_activity_nombreLugar = view.findViewById(R.id.tV_bsd_main_activity_nomLugar);
        tV_bsd_main_activity_direccion = view.findViewById(R.id.tV_bsd_main_activity_direccion);
        imgV_main_activity_lugar = view.findViewById(R.id.imgV_main_activity_lugar);
        tV_bsd_main_activity_nombreLugar.setText(nombreLugar);
        tV_bsd_main_activity_direccion.setText(direccion);
        Picasso.get()
                .load(urlImagen)
                .error(R.mipmap.ic_mapa_restaurante)
                .into(imgV_main_activity_lugar);
        return view;

    }
}
