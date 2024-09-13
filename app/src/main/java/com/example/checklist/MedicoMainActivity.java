package com.example.checklist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MedicoMainActivity extends AppCompatActivity {

    private static final String TAG = "MedicoMainActivity";
    private ImageView iconHome, iconPerfil, imageBottom;
    private ListView listViewExames;
    private ExameAdapter adapter;
    private List<Exame> exames;
    private TextView emptyTextView;
    private ExameDAO exameDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medico_main);

        // Referências aos ícones e componentes da interface
        iconHome = findViewById(R.id.iconHome);
        iconPerfil = findViewById(R.id.iconPerfil);
        imageBottom = findViewById(R.id.imageBottom);
        listViewExames = findViewById(R.id.listViewExames);
        emptyTextView = findViewById(R.id.emptyTextView);

        exameDAO = new ExameDAO(this);

        // Lógica para o ícone de Home
        iconHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadHomeContent();
            }
        });

        // Lógica para o ícone de Perfil
        iconPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Se você ainda deseja implementar o redirecionamento para uma página de perfil, ajuste a implementação aqui
                Log.i(TAG, "Perfil icon clicked, but PerfilActivity is not implemented.");
            }
        });

        // Lógica para o ícone de adicionar exame
        imageBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MedicoMainActivity.this, AdicionarExameActivity.class);
                startActivity(intent);
            }
        });

        // Obtendo o e-mail do médico
        Intent intent = getIntent();
        String medicoEmail = intent.getStringExtra("medico_email");

        Log.d(TAG, "E-mail do médico: " + medicoEmail);

        if (medicoEmail != null) {
            loadExams(medicoEmail);
        } else {
            Log.e(TAG, "No medico email found in Intent");
            listViewExames.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
        }
    }

    private void loadExams(String medicoEmail) {
        Log.d(TAG, "Loading exams for medico email: " + medicoEmail);

        exames = exameDAO.obterExamesDoMedico(medicoEmail);

        if (exames != null) {
            if (!exames.isEmpty()) {
                Log.d(TAG, "Number of exams retrieved: " + exames.size());
                for (Exame exame : exames) {
                    Log.d(TAG, "Exame: " + exame.getNomeExame() + ", Data: " + exame.getData());
                }
                adapter = new ExameAdapter(this, exames);
                listViewExames.setAdapter(adapter);
                listViewExames.setVisibility(View.VISIBLE);
                emptyTextView.setVisibility(View.GONE);
            } else {
                Log.d(TAG, "Nenhum exame encontrado para o médico: " + medicoEmail);
                listViewExames.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.VISIBLE);
            }
        } else {
            Log.d(TAG, "Falha ao obter exames para o médico: " + medicoEmail);
            listViewExames.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
        }
    }

    private void reloadHomeContent() {
        // Recarrega o conteúdo da página inicial
        Intent intent = getIntent();
        String medicoEmail = intent.getStringExtra("medico_email");
        if (medicoEmail != null) {
            loadExams(medicoEmail);
        }
    }
}
