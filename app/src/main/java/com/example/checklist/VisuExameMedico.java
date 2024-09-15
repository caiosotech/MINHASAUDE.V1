package com.example.checklist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class VisuExameMedico extends AppCompatActivity {

    private TextView textViewNomePaciente, textViewCpfPaciente, textViewDescricaoExame, textViewNomeHospital;
    private TextView textViewNomeMedico, textViewData, textViewHora, textViewAnexo;
    private Button buttonVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visu_exame_medico);

        // Referências aos TextViews e Botão
        textViewNomePaciente = findViewById(R.id.textViewNomePaciente);
        textViewCpfPaciente = findViewById(R.id.textViewCpfPaciente);
        textViewDescricaoExame = findViewById(R.id.textViewDescricaoExame);
        textViewNomeHospital = findViewById(R.id.textViewNomeHospital);
        textViewNomeMedico = findViewById(R.id.textViewNomeMedico);
        textViewData = findViewById(R.id.textViewData);
        textViewHora = findViewById(R.id.textViewHora);
        textViewAnexo = findViewById(R.id.textViewAnexo);
        buttonVoltar = findViewById(R.id.buttonVoltar);

        // Recebendo dados da Intent
        Intent intent = getIntent();
        String nomePaciente = intent.getStringExtra("nomePaciente");
        String cpfPaciente = intent.getStringExtra("cpfPaciente");
        String descricaoExame = intent.getStringExtra("descricaoExame");
        String nomeHospital = intent.getStringExtra("nomeHospital");
        String nomeMedico = intent.getStringExtra("nomeMedico");
        String data = intent.getStringExtra("data");
        String hora = intent.getStringExtra("hora");
        String anexo = intent.getStringExtra("anexo");

        // Definindo valores nos TextViews
        textViewNomePaciente.setText(nomePaciente);
        textViewCpfPaciente.setText(cpfPaciente);
        textViewDescricaoExame.setText(descricaoExame);
        textViewNomeHospital.setText(nomeHospital);
        textViewNomeMedico.setText(nomeMedico);
        textViewData.setText(data);
        textViewHora.setText(hora);
        textViewAnexo.setText(anexo);

        // Lógica do botão Voltar
        buttonVoltar.setOnClickListener(v -> finish());
    }
}
