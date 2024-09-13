package com.example.checklist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;

public class ChooseRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_specific); // Verifique se este é o nome correto do layout

        // Configurando o botão de cadastro de Médico
        RelativeLayout registerMedicoButton = findViewById(R.id.registerMedicoButton);
        registerMedicoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseRegisterActivity.this, RegisterMedicoActivity.class);
                startActivity(intent);
            }
        });

        // Configurando o botão de cadastro de Paciente
        RelativeLayout registerPacienteButton = findViewById(R.id.registerPacienteButton);
        registerPacienteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseRegisterActivity.this, RegisterPacienteActivity.class);
                startActivity(intent);
            }
        });
    }
}
