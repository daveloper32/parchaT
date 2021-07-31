package com.developers.parchat.view.main;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.developers.parchat.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

public class BottomSheetDialog_InfoLugar extends BottomSheetDialogFragment {

    private TextView tV_bsd_main_activity_nombreLugar, tV_bsd_main_activity_direccion;
    private String nombreLugar, direccion;

    public BottomSheetDialog_InfoLugar(String nombreLugar, String direccion) {
        this.nombreLugar = nombreLugar;
        this.direccion = direccion;
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
        tV_bsd_main_activity_nombreLugar.setText(nombreLugar);
        tV_bsd_main_activity_direccion.setText(direccion);
        return view;

    }
}
