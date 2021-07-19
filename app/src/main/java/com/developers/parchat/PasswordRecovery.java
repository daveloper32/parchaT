package com.developers.parchat;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class PasswordRecovery extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);
        // Escondemos la barra superior
        getSupportActionBar().hide();
    }

}
