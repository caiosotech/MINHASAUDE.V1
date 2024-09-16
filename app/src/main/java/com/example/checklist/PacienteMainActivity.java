package com.example.checklist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class PacienteMainActivity extends AppCompatActivity {

    private static final String TAG = "PacienteMainActivity";
    private ImageView iconHome;
    private ListView listViewExames;
    private ExameAdapter adapter;
    private List<Exame> exames;
    private TextView emptyTextView;
    private EditText searchEditText;
    private String pacienteCPF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente_main);

        // Referências aos ícones e componentes da interface
        iconHome = findViewById(R.id.iconHome);
        listViewExames = findViewById(R.id.listViewExames);
        emptyTextView = findViewById(R.id.emptyTextView);
        searchEditText = findViewById(R.id.searchEditText);

        // Suponha que o CPF do paciente seja passado via Intent
        pacienteCPF = getIntent().getStringExtra("paciente_cpf");

        Log.d(TAG, "CPF do paciente: " + pacienteCPF);

        // Lógica para o ícone de Home
        iconHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Código para lidar com o clique no botão Home
                Log.d(TAG, "Home icon clicked");
                reloadHomeContent();
            }
        });



        // Configurar a pesquisa
        searchEditText.setOnEditorActionListener((textView, i, keyEvent) -> {
            // Lógica para pesquisar exames
            String query = searchEditText.getText().toString();
            searchExams(query);
            return true;
        });

        // Carregar exames
        loadExams();
    }

    private void loadExams() {
        if (pacienteCPF == null || pacienteCPF.isEmpty()) {
            Log.e(TAG, "Paciente CPF is not available.");
            return;
        }

        ExameDAO exameDAO = new ExameDAO(this);
        exames = exameDAO.obterExamesDoPaciente(pacienteCPF);

        if (exames != null) {
            if (!exames.isEmpty()) {
                Log.d(TAG, "Exames encontrados para o paciente: " + pacienteCPF);
                for (Exame exame : exames) {
                }
                adapter = new ExameAdapter(this, exames);
                listViewExames.setAdapter(adapter);
                listViewExames.setVisibility(View.VISIBLE);
                emptyTextView.setVisibility(View.GONE);
            } else {
                Log.d(TAG, "Nenhum exame encontrado para o paciente: " + pacienteCPF);
                listViewExames.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.VISIBLE);
            }
        } else {
            Log.e(TAG, "Falha ao obter exames para o paciente: " + pacienteCPF);
            listViewExames.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
        }
    }

    private void searchExams(String query) {
        ExameDAO exameDAO = new ExameDAO(this);
        exames = exameDAO.buscarExamesPorConsulta(query);

        if (exames != null) {
            if (!exames.isEmpty()) {
                Log.d(TAG, "Exames encontrados com a consulta: " + query);
                for (Exame exame : exames) {
                }
                adapter = new ExameAdapter(this, exames);
                listViewExames.setAdapter(adapter);
                listViewExames.setVisibility(View.VISIBLE);
                emptyTextView.setVisibility(View.GONE);
            } else {
                Log.d(TAG, "Nenhum exame encontrado para a consulta: " + query);
                listViewExames.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.VISIBLE);
            }
        } else {
            Log.e(TAG, "Falha ao buscar exames para a consulta: " + query);
            listViewExames.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
        }
    }

    private void reloadHomeContent() {
        loadExams();
    }
}